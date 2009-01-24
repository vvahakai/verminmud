/*
 * Created on 19.2.2005
 *
 */
package org.vermin.mudlib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.LogManager;

import org.vermin.driver.DefaultLoader;
import org.vermin.driver.Driver;
import org.vermin.driver.ExecLoader;
import org.vermin.driver.Factory;
import org.vermin.driver.FileLoader;
import org.vermin.driver.SqlAuthenticationProvider;
import org.vermin.driver.SubLoader;
import org.vermin.driver.TickService;
import org.vermin.io.SExpObjectInput;
import org.vermin.mudlib.outworld.DefaultOutworldRoomFactory;
import org.vermin.mudlib.outworld.OutworldLoader;
import org.vermin.wicca.telnet.ConnectionListener;
import org.vermin.world.rooms.CattlebridgeOutworldRoomFactory;
import org.vermin.world.rooms.CityOutworldRoomFactory;

/**
 * @author tadex
 *
 */
public class Main {

	static boolean testMode = false;
	
    static long startTime;
    
    static HashMap<String,String> configuration;
    
    
    static void preInitialize(Driver d, String[] args) throws Exception {
    	
    	try {
    		configuration = (HashMap<String, String>) new SExpObjectInput(new File(args[0])).deserialize();
    	} catch(Exception e) {
    		System.out.println("UNABLE TO READ CONFIGURATION: "+e.getMessage());
    		e.printStackTrace();
    		System.exit(0);
    	}
    	
        startTime = System.currentTimeMillis();
        
        initializeLogging();
        
        
        if(configuration.containsKey("dbURL"))
        	World.dbURL = configuration.get("dbURL");
        
        World.log("<init> Using database: "+World.dbURL);
        
        World.log("<init> Creating outworld loader");

        String outworldPath = configuration.get("outworld");
        if(outworldPath == null)
        	outworldPath = "objects";
        
        OutworldLoader outworldLoader = 
            new OutworldLoader(
                    // path mapfile rowlength bytesPerEntry stride
                    outworldPath+"/common/outworld", "bigmap", "things", 900, 2, 1,

                    // Id prefix  and  outworld room factory
                    "outworld", new DefaultOutworldRoomFactory(),

                    // Map origo 
                    281, 212);

        DefaultLoader main = new DefaultLoader();
        main.addLoader(new SubLoader());
        
        if(new File("local").isDirectory())
        	main.addLoader(new FileLoader("local")); // Loader for local object files
        main.addLoader(new FileLoader("objects")); // Loader for base Open Source VerminMUD objects
        
        
        		
        World.log("<init> Creating city loader");
        OutworldLoader cityLoader =
            new OutworldLoader(
                    outworldPath+"/common/city", "citymap", "things", 79, 1, 1,
                    "city", new CityOutworldRoomFactory(),
                    26, 25);
        cityLoader.getMapper().setSuppressSpecials(true);
       
        World.log("<init> Creating city2 loader");
        OutworldLoader city2Loader =
            new OutworldLoader(
            		outworldPath+"/common/city2", "city2map", "things", 55, 1, 1,
                    "city2", new CattlebridgeOutworldRoomFactory(),
                    26, 13);
        city2Loader.getMapper().setSuppressSpecials(true);
        
        
        main.addLoader(outworldLoader);
        main.addLoader(cityLoader);
        main.addLoader(city2Loader);
        
        World.setObjectRoot("objects");
        
        d.setLoader(main);

        outworldLoader.loadSpawningRules("common/outworld/spawning-rules");
        cityLoader.loadSpawningRules("common/city/spawning-rules");
        city2Loader.loadSpawningRules("common/city2/spawning-rules");
        
        World.log("<init> Loaders created");
	
        
        World.log("<init> Starting tick service");
        TickService ts = new TickService();
        World.initializeTickService(ts);
        d.setTickService(ts);       
        
        World.log("<init> Creating authenticator");
        
        d.setAuthenticator(new SqlAuthenticationProvider(World.dbURL, "", "", "org.apache.derby.jdbc.EmbeddedDriver"));

        World.log("<init> Creating telnet connection listener");    
        
        int port = 23;
        try {
        	port = Integer.parseInt(configuration.get("port"));
        } catch(Exception e) {}
        
        if(!testMode) {
        	d.addConnectionListener(new ConnectionListener(d, port, 5));
        	d.addConnectionListener(new org.vermin.wicca.web.ConnectionListener(8069));
        }
        loadFactories(d);
        
       
        // Set some game engine objects
        
        SkillObject.skillsProvider = new SkillsProvider();
 
        // load plaque object
        World.log("<init> Loading plaque");
        World.setPlaque((Plaque) World.get("plaque-data"));
        
 
        // Start the world
        World.start();

    }

    private static void loadFactories(Driver d) {
    	Map factories = (Map) World.get("common/factories");
    	
    	if(factories != null)
    		for(Object key : factories.keySet()) {
    			String name = (String) key;
    			d.addFactory(name, (Factory) factories.get(name));
    		}
    	
        d.addFactory("armour", ArmourFactory.getInstance());
        d.addFactory("weapon", WeaponFactory.getInstance());
	
    }
    
	private static void initializeLogging() throws IOException, FileNotFoundException {
		String logprops = "log.properties";
        if(configuration.containsKey("logging"))
        	logprops = configuration.get("logging");
        File logProperties = new File(logprops);
        if(logProperties.exists())
        	LogManager.getLogManager().readConfiguration(new FileInputStream(logProperties));
        else
        	World.log("<init> WARNING: Logging properties file '"+logprops+"' does not exist!");
	}
    
    static void postInitialize(Driver d) {
        
        readTypoMessages(d);
		
        long elapsed = System.currentTimeMillis()-startTime;
        World.log("<init> Full init done in "+(elapsed/1000)+"."+(elapsed%1000)+" seconds.");
    }
    
    static void readTypoMessages(Driver d) {
        World.log("<init> Reading typo messages");
        try {
            BufferedReader in = new BufferedReader(new FileReader("misc/typomessage.txt"));
            String line = in.readLine();
            while(line != null) {
                d.addTypoMessage(line);
                line = in.readLine();
            }
        } catch(IOException ioe) {
            World.log("Unable to read typo messages: "+ioe.getMessage());
        }
    }
    
    public static void main(String[] args) {
        
    	if(args.length > 1 && "test".equalsIgnoreCase(args[1]))
    		testMode = true;
    	
        try {
            Driver d = Driver.getInstance();
            preInitialize(d, args);
            d.run();
            postInitialize(d);
            
            if(testMode) {
            	PlayerTester.runTests();
            }
        } catch(Throwable e) {
            System.out.println("Exception in Driver "+e.getClass().getName()+": "+e.getMessage());
            e.printStackTrace(System.out);
            Throwable cause = e.getCause();
            if(cause != null) {
                System.out.println("  Caused by: "+cause.getClass().getName()+": "+cause.getMessage());
                cause.printStackTrace(System.out);
            }
        }
    }
    
    
}
