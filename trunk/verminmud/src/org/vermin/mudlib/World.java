package org.vermin.mudlib;

import org.vermin.driver.AuthenticationProvider;
import org.vermin.driver.FileLoader;
import org.vermin.driver.Persistent;
import org.vermin.driver.Queue;
import org.vermin.driver.Tickable;
import org.vermin.driver.TickService;
import org.vermin.driver.Prototype;
import org.vermin.driver.Driver;
import org.vermin.driver.LoadException;
import java.text.DateFormat;
import java.util.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.*;

import org.vermin.io.brpc.Client;


import org.vermin.util.Print;
import org.vermin.wicca.Session;
import org.vermin.world.commands.ChannelCommand;

/**
 * Provides an interface to the driver and other
 * game-wide utilities.
 */
public class World {
	
	public static DateFormat dateFormat;
	private static HashSet<String> playableRaces;
    
	private static HashSet<Persistent> saveSet = new HashSet();

	public static String dbDriver = "org.apache.derby.jdbc.EmbeddedDriver";
	public static String dbURL = "jdbc:derby:database;create=true";
	
	static {
		logstream = System.out;
		
		//System.out.println("World STATIC START");
		dateFormat = DateFormat.getTimeInstance(DateFormat.LONG);
		driver = Driver.getInstance();
		//System.out.println("World STATIC END");
        
	}

	public static Driver driver;

	private static java.sql.Connection verminDB;

    private static PrintStream logstream = System.out;
    private static PrintStream exceptionStream = System.err;
    
    /* HashMap of currently logged in players.
     * login name as key, the connection as value.
     */
    private static HashMap players = new HashMap();
    
    private static String objectRoot;
    
    /* The plaque object */
    private static Plaque plaque;
    
    public static java.sql.Connection getDatabaseConnection() {
    	return verminDB;
    }

    private static void loadPlayableRaces() {
    	if(playableRaces != null)
    		return;
    	
        playableRaces = new HashSet();
        // When races are changed into data, load these
        // from savefiles on demand
        
        FileLoader.Directory dir = (FileLoader.Directory) World.get("races");
        for(String f : dir.getFiles())
        	playableRaces.add(f);
        
    }
    
    public static void resetDatabaseConnection() {
        driver = Driver.getInstance();
		try {
			Class c = Class.forName(dbDriver);
			verminDB = DriverManager.getConnection(dbURL);
			
		} catch(Exception e) {
			log("SQL connection error: "+e.getMessage());
			e.printStackTrace();
		}
    }
    
	public static void start() {

		resetDatabaseConnection();
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					log("World shutdown initiated.");
					Iterator it = World.getPlayers();
					while(it.hasNext()) {
						Player p = (Player) it.next();
						p.notice("Mud is going down for reboot NOW.");
						p.save();
					}
					
					for(Persistent p : saveSet)
						p.save();
					
					log("World shutdown complete.");
					driver.shutdown();
				}});
					

	}

	public static int explorableRoomCount;

	public static Prototype load(String id) {
		try {
			return driver.getLoader().load(id);
		}  catch(LoadException le) {
            log("World.load failed with LoadException: "+le.getMessage());
            if(le.getCause() != null) {
                log("  Caused by: "+le.getCause().getMessage());
                le.getCause().printStackTrace(logstream);
            }
            return null;
        } catch(Exception le) {
            log("World.load failed for object: "+id+"\n"+
                    "           exception type: "+le.getClass().getName()+"\n"+
                    "           exception msg: "+le.getMessage());
            le.printStackTrace(logstream);
			return null;
		}
	}

    public static boolean isLoaded(String id) {
        return driver.getLoader().isLoaded(id);
    }
    
	public static Object get(String id) {
	    Prototype p = load(id);
        return p == null ? null : p.get();
	}

	public static Object maybeGet(String id) {
		return isLoaded(id) ? get(id) : null;
	}
	
	public static void unload(String id) {
		driver.getLoader().unload(id);
	}

	public static void stopTick(Tickable t) {
		TickService ts = driver.getTickService();
		ts.removeTickable(t, Tick.BATTLE);
		ts.removeTickable(t, Tick.REGEN);
		ts.removeTickable(t, Tick.GARBAGE);
	}


	public static void shutdown() {
		driver.shutdown();
	}

	/**
	 * Write a log message to the console (also known as the 'matrix').
	 *
	 * This method is meant to be used as an alternative to printing
	 * debug messages via <code>System.out.println</code>.
	 *
	 * @param msg the message to write
	 */
	public static void log(String msg) {
		StringBuilder sb = new StringBuilder("[");
		sb.append(dateFormat.format(new java.util.Date()));
		sb.append("] ");
		sb.append(msg);
		logstream.println(sb.toString());
	}
	
	/**
	 * Write a log message to the Wizards' devel channel.
	 * 
	 * @param msg
	 */
	public static void logDevel(String msg) {
		ChannelCommand cc = (ChannelCommand) Commander.getCommand(":");
		cc.notice("devel", msg);
	}
	
	/**
	 * Write a message to the info channel.
	 *
	 * @param msg the message to write
	 */
	public static void info(String msg) {
		ChannelCommand cc = (ChannelCommand) Commander.getInstance().get(":");
		cc.notice("info", msg);
	}

	private static boolean explorableRoomDBCountAttempted = false;
	public static int getExplorableRoomCount() {
		if(!explorableRoomDBCountAttempted) { // try once to grab this data from the db
			try {
				Connection c = getDatabaseConnection();
				PreparedStatement countExplorableRooms = c.prepareStatement("SELECT COUNT(DISTINCT room) AS roomcount FROM explore");

				ResultSet rs = countExplorableRooms.executeQuery();
				if(rs.next()) {
					int count = rs.getInt(1);
					if(count != 0) // if db is reset for some reason, we don't want the zero here
						explorableRoomCount = count;
				}
			} catch (SQLException sqle) {
				log("SQL exception while counting explorable rooms: "+sqle.getMessage());
				resetDatabaseConnection();
			} finally {
				explorableRoomDBCountAttempted = true;
			}
		}

		return explorableRoomCount;
	}

	public static void setExplorableRoomCount(int erc) {
		explorableRoomCount = erc;
	}

	/**
	 * Check from the database if the given player has
	 * explored the given room and mark the room as explored
	 * by the player.
	 *
	 * This method returns true if this was the first time
	 * the given player explored the room.
	 *
	 * @param who the player to check
	 * @param what the room to check
	 * @return true if this was the first time, false otherwise
	 */
	public static synchronized boolean explore(Player who, Room what) {

		boolean newExplore = checkExplore(who, what.getId());
		if(newExplore) {
			double exp = 50.0 * (1.0 + (double) who.getExploreCount()* 100.0 / (double) getExplorableRoomCount());
			long prevExp = who.getExperience();
			who.addExperience(Math.round(exp));
			who.increaseExploreCount();
			
			if(who.getPreference("showexp").equals("on")) {
				long expDelta = who.getExperience() - prevExp;
				who.notice("Exploring this room gains you "+expDelta+" experience.");
			}
		}
		return newExplore;
	}

	private static boolean checkExplore(Player who, String target) {
		int tries = 2;
		while(tries > 0) {
			try {
		
				PreparedStatement stmt = getDatabaseConnection()
					.prepareStatement("SELECT COUNT(*) FROM explore WHERE player = ? AND room = ?");
				stmt.setString(1, who.getId());
				stmt.setString(2, target);
				ResultSet rs = stmt.executeQuery();
				rs.next();
				boolean newExplore = rs.getInt(1) == 0;
				
				rs.close();
				stmt.close();
				
				if(newExplore) {
					stmt = getDatabaseConnection()
						.prepareStatement("INSERT INTO explore (player,room) VALUES(?,?)");
					stmt.setString(1, who.getId());
					stmt.setString(2, target);
					stmt.executeUpdate();
					stmt.close();
				}
				return newExplore;
				
			} catch(SQLException se) {
				log("SQL exception: "+se.getMessage());
				resetDatabaseConnection();
			}
			tries--;
		}
		return false;
	}
	
	/**
	 * Mark the given quest as completed for the given player.
	 *
	 * @param who the player who completed the quest
	 * @param quest the name of the completed quest
	 */
	public static synchronized void completeQuest(Player who, String quest) {
		checkExplore(who, quest);
	}

	/**
	 * Update player stats to the plaque.
	 *
	 * @param player the player to update
	 */
	public static synchronized void updatePlaque(Player who) {
		if(who instanceof Wizard) { return; }
		plaque.updatePlaque(who);
	}
	
	/**
	 * Allows a player to attempt to claim race leadership.
	 *
	 * @deprecated
	 * @param who
	 * @return
	 */
	public static synchronized boolean claimRaceLeadership(Player who) {
		// This method is unmeaningful, race leadership claims should be
		// done in a manner specific to the race (eg. in a racial shrine, etc)
		return false;
	}

	public static void addGarbageTick(Tickable obj) {
		Driver.getInstance().getTickService().addTick(obj, Tick.GARBAGE);
	}
	
	public static void addBattleTick(Tickable obj) {
		Driver.getInstance().getTickService().addTick(obj, Tick.BATTLE);
	}
	
	public static void addRegenTick(Tickable obj) {
		Driver.getInstance().getTickService().addTick(obj, Tick.REGEN);
	}
	
	public static void withDelay(Queue queue, int origAmount, final Runnable action) {
		final int[] amount = new int[] { origAmount };
		Driver.getInstance().getTickService().addTick(new Tickable() {
			public boolean tick(Queue queue) {
				amount[0]--;
				if(amount[0] == 0)
					action.run();
				return amount[0] > 0;
			}
		}, queue);
		
	}
	
    public static Iterable<String> getPlayableRaceNames() {
    	loadPlayableRaces();
        return playableRaces;
    }
    
    public static Race getPlayableRaceByName(String name) {
    	loadPlayableRaces();
    	if(!playableRaces.contains(name))
    		return null;
        return (Race) get("races/"+name);
    }
    
    public static void initializeTickService(TickService ts) {
        ts.addQueue(Tick.BATTLE);
        ts.addQueue(Tick.REGEN);
        ts.addQueue(Tick.GARBAGE);
        ts.startTickerThread(Tick.BATTLE);
        ts.startTickerThread(Tick.BATTLE);
        ts.startTickerThread(Tick.REGEN);
        ts.startTickerThread(Tick.REGEN);
        ts.startTickerThread(Tick.GARBAGE);
    }
    
    

	private static PreparedStatement deletePlayer = null;
	private static PreparedStatement deleteExplore = null;
    
	/**
     * Deletes all data about this player from the database
     * and removes player savefile.
     * 
     * @param p
     */
    public static synchronized void suicidePlayer(Player p) {
    	if(p instanceof Wizard) {
    		p.notice("Notice from World.suicidePlayer: wizards don't cut themselves.");
    		return;
    	}
		try {
			Connection c = getDatabaseConnection();
			p.notice("Removing player entries from database...");
			if(deletePlayer == null) {
				deletePlayer = c.prepareStatement("DELETE FROM players WHERE id = ?");
			}
			deletePlayer.setString(1, p.getId());
			deletePlayer.execute();
			
			if(deleteExplore == null) {
				deleteExplore = c.prepareStatement("DELETE FROM explore WHERE player = ?");
			}
			deleteExplore.setString(1, p.getId());
			deleteExplore.execute();

			p.getRoom().notice(p, Print.capitalize(p.getName()+" suddenly commits suicide."));

			p.notice("Unloading player object...");
			p.getRoom().remove(p);
			World.unload(p.getId());
			
			p.notice("Deleting player file...");
			File f = new File(getObjectRoot()+"/players/"+p.getName().toLowerCase());
			f.delete();
			
			p.notice("You have successfully committed a suicide. Have a nice day.");
			p.getClientOutput().close();
			
		} catch(SQLException se) {
			log("Unable delete player: "+p.getName()+", message: "+se.getMessage());
			resetDatabaseConnection();
		}
	
    }

    public static synchronized void suicidePlayer(String playerId, String playerName) {
    	if(isLoaded(playerId)) {
    		return;
    	}
		try {
			Connection c = getDatabaseConnection();
			if(deletePlayer == null) {
				deletePlayer = c.prepareStatement("DELETE FROM players WHERE id = ?");
			}
			deletePlayer.setString(1, playerId);
			deletePlayer.execute();
			
			if(deleteExplore == null) {
				deleteExplore = c.prepareStatement("DELETE FROM explore WHERE player = ?");
			}
			deleteExplore.setString(1, playerId);
			deleteExplore.execute();

			File f = new File(getObjectRoot()+"/players/"+playerName.toLowerCase());
			f.delete();
			
			
		} catch(SQLException se) {
			log("Unable delete player: "+playerName+", message: "+se.getMessage());
			resetDatabaseConnection();
		}
    }
    
    public static Iterator getPlayers() {
   
        ArrayList v = new ArrayList();
        
        AuthenticationProvider auth = Driver.getInstance().getAuthenticator();
        
        Iterator it = getPlayerNames();
        while(it.hasNext()) {
            String name = it.next().toString();
            Object p = get( auth.getIdForRecord(name) );
            if(p != null) v.add(p);
        }
        return v.iterator();
        
    }

    public static Iterator getPlayerNames() {
        return players.keySet().iterator();
    }
    public static boolean isLoggedIn(String name) {
        return players.containsKey(Print.capitalize(name));
    }
    public static void register(String name, Session conn) {
    	log("%%%%%%%%%%% SETTING SESSION FOR "+Print.capitalize(name)+": "+conn);
        players.put(Print.capitalize(name), conn);
    }
    
    public static void logout(String name) {
        players.remove(Print.capitalize(name));
    }
    public static Session getSession(String name) {
        Session s = (Session) players.get(Print.capitalize(name));
        if(s == null)
        	log("%%%%%%%%%%%%%% null session for player: "+Print.capitalize(name));
        return s;
    }
    public static void setLogStream(PrintStream out) {
        logstream = out;
    }
    
    public static String getBugTrackerURL() {
        return "http://vermin.game-host.org/bugtracker/";
    }
    
    public static void recordScore(Player p) {
        log("recordScore for "+p.getName());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        
        Living cap = OutputCapture.withOutputTo(p, ps);
        log("output capturing proxy: "+cap);
        Commander.getInstance().get("score").action((Player)cap, "score");
        ((OutputCapture.CapturerProxy)cap).close();
        System.out.println(new String(out.toByteArray()));
    }
	
	private static HashMap<String,Client> clients = new HashMap();
	public synchronized static Client getClient(String host, int port) {
		try {
			Client c = clients.get(host+port);
			if(c == null) {
				c = new Client(new Socket(host, port));
				clients.put(host+port, c);
			}
			return c;
		} catch(Exception e) {
			return null;
		}
	}

	public static void saveOnExit(Persistent p) {
		saveSet.add(p);
	}

	public static void exception(Exception e) {
		exceptionStream.println("---- "+e.getClass()+": "+e.getMessage()+" ---");
		e.printStackTrace(exceptionStream);
		exceptionStream.println();
	}
	
	public static void setExceptionStream(PrintStream ps) {
		exceptionStream = ps;
	}

	public static void setObjectRoot(String string) {
		objectRoot = string;
	}
	public static String getObjectRoot() {
		return objectRoot;
	}
	
	private static PreparedStatement playerIdByName = null;
	
	public static synchronized String getPlayerIdByName(String name) {
		try {
			Connection c = getDatabaseConnection();
			
			if(playerIdByName == null) {
				playerIdByName = c.prepareStatement("SELECT id FROM players WHERE name = ?");
			}
			playerIdByName.setString(1, name);
			ResultSet rs = playerIdByName.executeQuery();
			String v = null;
			if(rs.next()) 
				v = rs.getString(1);
			rs.close();
			return v;
		} catch(SQLException se) {
			log("Unable to fetch player id for name: "+name+", message: "+se.getMessage());
			return null;
		}
	}

	public static Plaque getPlaque() {
		return plaque;
	}

	/**
	 * Set the plaque object, called from game init only.
	 * 
	 * @param plaque
	 */
	public static void setPlaque(Plaque plaque) {
		World.plaque = plaque;
		saveOnExit(World.plaque);
	}
	
	
}
