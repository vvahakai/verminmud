/* GuildRoom.java
	16.2.2002
	
*/

package org.vermin.world.rooms;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.vermin.mudlib.BattleStyle;
import org.vermin.mudlib.Damage;
import org.vermin.mudlib.DefaultRoomImpl;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.Player;
import org.vermin.mudlib.Types;
import org.vermin.util.Table;
import org.vermin.world.items.GuildItem;

public class GuildRoom extends DefaultRoomImpl {
	
	private static final int freeStatPointsPerLevel = 10;
	protected String guildName;
	
	private String[] guildTitles = { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
    protected HashMap<String,Integer> joinGuildRequirements = new HashMap();

	public static class TrainEntry {
		public String name;
		public byte[] levelMinimum;
		public byte[] levelMaximum;
      public int costClass;
		
      public TrainEntry() {} // for SExpObjectOutput 
		public TrainEntry(String name, byte[] lMin, byte[] lMax, int cost) {
			this.name = name;
			levelMinimum = lMin;
			levelMaximum = lMax;
         costClass = cost;
		}
	}
	
	protected Vector train;
	
   protected int guildLevels;

	public GuildRoom(String guildName) {
		this.guildName = guildName;
		train = new Vector();
      guildLevels = 0;
	}
	
	public BattleStyle[] getBattleStyles() { return new BattleStyle[0]; }
	public byte getPhysicalStrengthBonus(int level) { return (byte) 0; }
	public byte getPhysicalConstitutionBonus(int level) { return (byte) 0; }
	public byte getPhysicalDexterityBonus(int level) { return (byte) 0; }
	public byte getMentalStrengthBonus(int level) { return (byte) 0; }
	public byte getMentalConstitutionBonus(int level) { return (byte) 0; }
	public byte getMentalDexterityBonus(int level) { return (byte) 0; }
	public byte getMagicResistanceBonus(int level) { return (byte) 0; }
	public String getGuildTitle(int level) { return ""; }

   protected TrainEntry getTrainEntry(String name) {
		//System.out.println("getTrainEntry: "+name);
      for(int i=0; i<train.size(); i++) {
         TrainEntry te = (TrainEntry) train.get(i);
         if(te.name.equalsIgnoreCase(name)) {
            return te;
         }
      }
      return null;
   }

	public void addTrainable(String name, byte[] lMin, byte[] lMax, int cost) {
		train.add(new TrainEntry(name, lMin, lMax, cost));
	}
	
	public boolean action(Living who, Vector cmd) {
		if(!(who instanceof org.vermin.mudlib.DefaultPlayerImpl))
			return false;
			
		String command = (String) cmd.get(0);
		
		if(command.equalsIgnoreCase("join")) {
			
			if(tryJoin((org.vermin.mudlib.DefaultPlayerImpl) who)) {
				join((org.vermin.mudlib.DefaultPlayerImpl) who);
				if(tryAdvance((org.vermin.mudlib.DefaultPlayerImpl) who))
					advance((org.vermin.mudlib.DefaultPlayerImpl) who);
			}			
			return true;
		} else if(command.equalsIgnoreCase("advance")) {
			if(tryAdvance((org.vermin.mudlib.DefaultPlayerImpl) who)) {
				advance((org.vermin.mudlib.DefaultPlayerImpl) who);
			}
			return true;
		} else if(command.equalsIgnoreCase("train")) {
         if(cmd.size() < 2) {
            listSkills((org.vermin.mudlib.DefaultPlayerImpl) who);
         } else if(cmd.get(1).toString().equals("level") &&
						 cmd.size() == 2) {
				trainLevelRequirements((org.vermin.mudlib.DefaultPlayerImpl) who);
				return true;
			} else {
				
				StringBuffer sb = new StringBuffer();

				TrainEntry old = null;
				int lastMatch = 0;
				TrainEntry te = null;
				int i = 1;
				while(i < cmd.size()) {
					sb.append(cmd.get(i++));
					te = getTrainEntry(sb.toString());
					if(te != null) {
						old = te;
						lastMatch = i;
					}
					sb.append(" ");
				}

				if(old == null) {
					who.notice("Train what?");
					return true;
				}

				String percent = "";
				if(lastMatch < cmd.size()-1) { // two more words
					String to = cmd.get(lastMatch).toString();
					if(!to.equalsIgnoreCase("to")) {
						usage(who);
						return true;
					}
					percent = cmd.get(lastMatch+1).toString();
				} else if(lastMatch == cmd.size()) {
					percent = Integer.toString(who.getSkill(te.name, false)+1);
				} else {
					usage(who);
					return true;
				}
				
            trainSkill((org.vermin.mudlib.DefaultPlayerImpl) who, old, percent);
         } 
			return true;
		} else if(command.equalsIgnoreCase("list")) {
			if(cmd.size() == 2) {
				int level = 0;
				if(cmd.get(1).toString().equals("all")) {
					level = guildLevels;					
				}
				else {
					try {
						level = Integer.parseInt(cmd.get(1).toString());
					} catch(NumberFormatException nfe) {
						GuildItem git = getGuildItem((org.vermin.mudlib.DefaultPlayerImpl) who);
						if(git != null)
							level = git.getLevel();
					}
				}
				listSkillsForLevel((org.vermin.mudlib.DefaultPlayerImpl) who, level);
			}
			else {
				listSkills((org.vermin.mudlib.DefaultPlayerImpl) who);
			}
			return true;
      } else if(command.equalsIgnoreCase("cost")) {
			int level = 0;
			if(cmd.size() == 2) {
				try {
					level = Integer.parseInt(cmd.get(1).toString());
				} catch(NumberFormatException nfe) {
					if(getGuildItem((org.vermin.mudlib.DefaultPlayerImpl) who) != null)
						level = getGuildItem((org.vermin.mudlib.DefaultPlayerImpl) who).getLevel() + 1;
				}
			}
			else {
				if(getGuildItem((org.vermin.mudlib.DefaultPlayerImpl) who) != null)
					level = getGuildItem((org.vermin.mudlib.DefaultPlayerImpl) who).getLevel() + 1;
			}
			costForLevel((Player) who, level);
			return true;
      } else {
			return false;
		}
	}
	
	protected void usage(Living who) {
		who.notice("Usage: train <skill name> [to <# | level | max>]");
	}

	protected void trainLevelRequirements(org.vermin.mudlib.DefaultPlayerImpl who) {

		GuildItem gi = getGuildItem(who);
		if(gi == null) {
			who.notice("You are not a member of the "+guildName+" guild.");
			return;
		}

		if(gi.getLevel() == guildLevels) {
			who.notice("You are already on the maximum level.");
			return;
		}

		for(int i=0; i<train.size(); i++) {
			TrainEntry te = (TrainEntry) train.get(i);
			trainSkill(who, te, Integer.toString(te.levelMinimum[gi.getLevel()+1]));
		}
	}


	public long getExpForLevel(int level) {
//		return (level * level * 103);
		return (long) ((5.7-Math.log(101-level)) * (level * 123456) / 100);
	}	
	
	public long getExpForTrain(String skill, int percent) {
      TrainEntry te = getTrainEntry(skill);
      if(te == null) {
         return 0;
      } else {
		// 
        // return (percent * percent) * te.costClass / 5 + 13;
		// (5.7-log(100-x)) * (x/100 * 10000)
			long exp = (long) ((5.7-Math.log(101-percent)) * (percent * te.costClass) / 100);
			//System.out.println("getExpForTrain('"+skill+"', "+percent+"): "+exp);
			return exp;
      }
	}
	
	public boolean tryJoin(org.vermin.mudlib.DefaultPlayerImpl who) {
		GuildItem gi = (GuildItem) who.findByNameAndType(guildName+"_guild_object", Types.ITEM);
		
		if(gi != null) {
			who.notice("You are already a member of the "+guildName+" guild.");
			return false;
		}

		if(who.getExperience() <= getExpForLevel(who.getLevel())) {
			who.notice("You are not experienced enough to join this guild.");
			return false;
		}
		
		for(Map.Entry<String, Integer> e : joinGuildRequirements.entrySet()) {
			GuildItem reqItem = (GuildItem) who.findByNameAndType(e.getKey()+"_guild_object", Types.ITEM);
			if(reqItem == null) {
				who.notice("You need to be at least level "+e.getValue()+" in guild "+e.getKey());				
				return false;
			}
			if(reqItem.getLevel() < e.getValue()) {
				who.notice("You need to be at least level "+e.getValue()+" in guild "+e.getKey());
				return false;
			}			
		}

		/* Check prerequisite skills */
		boolean ok = true;
		for(int i=0; i<train.size(); i++) {
			TrainEntry te = (TrainEntry) train.get(i);
			
			if(who.getSkill(te.name, false) < te.levelMinimum[1]) {
				who.notice("You must have "+te.name+" at "+te.levelMinimum[1]+"% before joining. (now at "+who.getSkill(te.name, false)+"%)");
				ok = false;
			}
		}
		
		return ok;
	}
	
	public boolean tryAdvance(org.vermin.mudlib.DefaultPlayerImpl who) {
		GuildItem gi = (GuildItem) who.findByNameAndType(guildName+"_guild_object", Types.ITEM);
		
		if(gi == null) {
			who.notice("You are not a member of the "+guildName+" guild.");
			return false;
		}

      if(gi.getLevel() == guildLevels) {
         who.notice("You are already on the maximum guild level.");
         return false;
      }

	  if(who.getLevel() >= 100) {
         who.notice("You are already on the maximum total level.");
         return false;		
	  }
		
		if(who.getExperience() <= getExpForLevel(who.getLevel())) {
			who.notice("You are not experienced enough. "+(getExpForLevel(who.getLevel()) - who.getExperience())+" more experience needed for next level.");
			return false;
		}
		
		/* Check prerequisite skills */
		boolean ok = true;
		for(int i=0; i<train.size(); i++) {
			TrainEntry te = (TrainEntry) train.get(i);
			
			if(who.getSkill(te.name, false) < te.levelMinimum[gi.getLevel()+1]) {
				who.notice("You must have "+te.name+" at "+te.levelMinimum[gi.getLevel()+1]+"% before advancing. (now at "+who.getSkill(te.name, false)+"%)");
				ok = false;
			}
		}
		
		return ok;
	}
	
	
	public void join(org.vermin.mudlib.DefaultPlayerImpl who) {
		GuildItem gi = createGuildItem();
		who.add(gi);

		// Add guild battle styles to the player
		BattleStyle[] bs = getBattleStyles();
		for(int i=0; i<bs.length; i++)
			who.addAvailableBattleStyle(bs[i]);
	}
	
	public void advance(org.vermin.mudlib.DefaultPlayerImpl who) {
	
		GuildItem gi = (GuildItem) who.findByNameAndType(guildName+"_guild_object", Types.ITEM);
		
		if(gi == null) {
			System.out.println("[GuildRoom] This is impossible!");
			return;
		}
		
		who.useExperience( getExpForLevel(who.getLevel()) );
		
		who.setLevel((who.getLevel() + 1));
		
		gi.setLevel((gi.getLevel() + 1));

		who.setFreeStatPoints(who.getFreeStatPoints()+freeStatPointsPerLevel);
		
		if(getGuildTitle(gi.getLevel()) != null && !getGuildTitle(gi.getLevel()).equals(""))
			who.addAvailableTitle(getGuildTitle(gi.getLevel()));

		who.setPhysicalStrength(who.getPhysicalStrength(false) + getPhysicalStrengthBonus(gi.getLevel()));
		who.setPhysicalConstitution(who.getPhysicalConstitution(false) + getPhysicalConstitutionBonus(gi.getLevel()));
		who.setPhysicalDexterity(who.getPhysicalDexterity(false) + getPhysicalDexterityBonus(gi.getLevel()));	
		who.setMentalStrength(who.getMentalStrength(false) + getMentalStrengthBonus(gi.getLevel()));
		who.setMentalConstitution(who.getMentalConstitution(false) + getMentalConstitutionBonus(gi.getLevel()));	
		who.setMentalDexterity(who.getMentalDexterity(false) + getMentalDexterityBonus(gi.getLevel()));	
		
		who.setResistance(Damage.Type.MAGICAL, who.getResistance(Damage.Type.MAGICAL, false) + getMagicResistanceBonus(gi.getLevel()));
		who.notice("You are now on level "+gi.getLevel()+" of the "+guildName+" guild.");
	}

   private GuildItem getGuildItem(org.vermin.mudlib.DefaultPlayerImpl who) {
		GuildItem gi = (GuildItem) who.findByNameAndType(guildName+"_guild_object", Types.ITEM);
      return gi;
   }
   
   
	protected boolean tryTrain(org.vermin.mudlib.DefaultPlayerImpl who, String name) {
		GuildItem gi = (GuildItem) who.findByNameAndType(guildName+"_guild_object", Types.ITEM);
      if(gi == null) {
         who.notice("You are not a member of the "+guildName+" guild.");
      }

		if(getExpForTrain(name, (who.getSkill(name, false)+1)) > who.getExperience()) {
			who.notice("You don't have enough experience. "+(getExpForTrain(name, (who.getSkill(name,false)+1))-who.getExperience())+" more experience needed.");
			return false;
		}
		
		boolean found=false;
		for(int i=0; i<train.size(); i++) {
			TrainEntry te = (TrainEntry) train.get(i);
			
			if(te.name.equals(name)) {
				found = true;
				
				if(te.levelMaximum[gi.getLevel()] <= who.getSkill(name, false)) {
					who.notice("Skill is already at maximum.");
					return false;
				}
				
				break;
			}
		}
		
		if(!found) {
			who.notice("No such skill.");
			return false;
		}
		
		return true;
	}
	
	protected void trainSkill(org.vermin.mudlib.DefaultPlayerImpl who, TrainEntry te, String to) {
		int oldPercent = who.getSkill(te.name, false);
      int requested = (oldPercent+1);

      GuildItem gi = getGuildItem(who);
		if(gi == null) {
			who.notice("You are not a member of the "+guildName+" guild.");
			return;
		}

		if(to != null) {
         if(to.equals("max")) {
            requested = te.levelMaximum[gi.getLevel()];
         } else if(to.equals("level")) {
            if(gi.getLevel() == guildLevels) {
               requested = 0;
            } else {
               requested = te.levelMinimum[gi.getLevel()+1];
            }
         } else {
            try {
               requested = Integer.parseInt(to);
            } catch(NumberFormatException nfe) {
               who.notice("Train to what?");
               return;
            }
         }
      }
		
      boolean done=false;
      boolean trained=false;
      while(!done) {
         if(who.getSkill(te.name, false) >= requested || !tryTrain(who, te.name)) {
            done = true;
         } else {
            who.useExperience(getExpForTrain(te.name, (oldPercent+1)));
            who.setSkill(te.name, (who.getSkill(te.name,false)+1));
            oldPercent++; // FIX for static train cost, when training many percent
            trained = true;
         }
      }

      if(trained) 
         who.notice("Trained "+te.name+" to "+who.getSkill(te.name,false)+"%");

	}

	public void trainSkill(org.vermin.mudlib.DefaultPlayerImpl who, String name, String to) {

      GuildItem gi = getGuildItem(who);
      TrainEntry te = getTrainEntry(name);

      if(gi == null) {
         who.notice("You are not a member of the "+guildName+" guild.");
         return;
      } else if(te == null) {
         who.notice("No such skill.");
         return;
      }
		
		trainSkill(who,te,to);
	}
	
	/* Override this in other guild rooms for custom guild items
	 * (for example a visible emblem)
	 */	
	public GuildItem createGuildItem() {
		GuildItem gi = instantiateGuildItem();
		gi.setName(guildName+"_guild_object");
		return gi;
	}

	protected GuildItem instantiateGuildItem() {
		return new GuildItem();
	}

	public void listSkills(org.vermin.mudlib.DefaultPlayerImpl who) {
		if(getGuildItem(who) == null) {
			who.notice("You are not a member of the "+guildName+" guild.");
			return;
		}
		listSkillsForLevel(who, getGuildItem(who).getLevel());
	}

	public void listSkillsForLevel(org.vermin.mudlib.DefaultPlayerImpl who, int level) {
		Table t = new Table();
		t.addHeader("Skill", 29, Table.ALIGN_LEFT);
		t.addHeader("At %", 6, Table.ALIGN_MIDDLE);
		t.addHeader("Max %", 7, Table.ALIGN_MIDDLE);
		t.addHeader("Xp cost", 10, Table.ALIGN_MIDDLE);
		if(level >= guildLevels)
			level = guildLevels;
		
		for(int i=0; i<train.size(); i++) {
			TrainEntry te = (TrainEntry) train.get(i);
			
			if(te.levelMaximum[level] > 0) {
				t.addRow();
				t.addColumn(te.name, 29, Table.ALIGN_LEFT);
				t.addColumn(Integer.toString(who.getSkill(te.name,false)), 6, Table.ALIGN_RIGHT);
				t.addColumn(Integer.toString(te.levelMaximum[level]),
						7, Table.ALIGN_RIGHT);
				if(who.getSkill(te.name,false) >= (int) (te.levelMaximum[level])) {
					t.addColumn("N/A", 10, Table.ALIGN_RIGHT);
				}
				else {
					t.addColumn(Long.toString(getExpForTrain(te.name, (who.getSkill(te.name,false)+1))),
							10, Table.ALIGN_RIGHT);
				}
			}
		}
		
		who.notice(t.render());
	}

	public void costForLevel(Player who, int level) {
		if(level <= guildLevels) {
			int guildLvl = 0;
			GuildItem gi = getGuildItem((org.vermin.mudlib.DefaultPlayerImpl) who);
			if(gi != null) guildLvl = gi.getLevel();
			who.notice("Cost for guild level " + level + ": " + getExpForLevel(who.getLevel() + level - guildLvl - 1));
			who.notice("Skill Requirements:");
			for(int i=0; i<train.size(); i++) {
				TrainEntry te = (TrainEntry) train.get(i);
				int requirement = 0;

				for(int j=0; j <= level; j++) {
					if(te.levelMinimum[j] > requirement) {
						requirement = te.levelMinimum[j];
					}
				}
				if(requirement > 0)
					who.notice(te.name +" at "+requirement);
			}
		}
	}
}
