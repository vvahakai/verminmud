package org.vermin.mudlib.commands;

import org.vermin.mudlib.*;
import org.vermin.util.Print;
import org.vermin.world.commands.EqCommand;

import java.util.*;

public class Look extends RegexCommand {

	public String[] getDispatchConfiguration() {
		return new String[] {
			"look => look(actor)",
			"look (at )?(.*) => look2(actor, 2)"
		};
	}

	/* Look around (not moving) */
	public void look(Living who) {
		look(who, false);
	}
	


	/* Look around */
	public void look(Living who, boolean moved) {

		Room currentRoom = who.getRoom();
		
		boolean verbose = true;
		if(who instanceof Player)
			verbose = ((Player)who).isVerbose();		
		
		if(tryLook(who)) {
			currentRoom.explore(who);

			StringBuffer sb = new StringBuffer();
			
			String loc = null;
			if(currentRoom.getArea() != null) {
				loc = currentRoom.getArea().getName();
			}
			
			if(loc != null) {
				sb.append("&B2;"+loc+"&;");
				sb.append(verbose ? "\n" : ". ");
			}

			if(moved && !verbose) {
				sb.append(currentRoom.getDescription()+" ");
			} else {
				sb.append(currentRoom.getLongDescription()+"\n");
			}
		
			Iterator en = currentRoom.findByType(Types.EXIT);
		
			boolean first = true;
			while(en.hasNext()) {
				if(first) {
					if(moved && !verbose) sb.append("(");
					else sb.append("The obvious exits are: ");
					first = false;
				} else {
					if(en.hasNext() || (moved && !verbose)) {
						sb.append(", ");
					} else {
						sb.append(" and ");
					}
				}
			
				Exit e = (Exit) en.next();
				sb.append("&B2;"+e.getDirection(currentRoom.getId())+"&21;");
			}
			if(!first) {
				if(moved && !verbose)
					sb.append(")\n");
				else
					sb.append(".\n");
			}
			
			
			// if there is a map, output a small map columnized with the desc
			Area area = currentRoom.getArea();
			if(area != null) {
				Mapper mapper = area.getMapper();
				if(mapper != null) {
					String[] map = mapper.getSmallMap(currentRoom.getId()).split("\n");
					String[] desc = sb.toString().split("\n");
					sb = new StringBuffer();
					for(String line : Print.columnize(map, 6, desc, 63))
						who.notice(line);
				}
			}
		
			en = currentRoom.findByType(Types.ITEM);
		
			first = true;
			if(!currentRoom.getMoney().isEmpty()) {
                sb.append(currentRoom.getMoney().getDescription());
                first = false;
            }
			while(en.hasNext()) {
				Item i = (Item) en.next();
				if(!first) {
					if(en.hasNext()) {
						sb.append(", ");
					} else {
						sb.append(" and ");
					}
				}
				if(i instanceof Wearable) {
					sb.append("&B5;");
				} else if(i instanceof Wieldable) {
					sb.append("&5;");
				} else {
					sb.append("&7;");
				}
				sb.append(i.getDescription()+"&21;");
				first = false;
			}
			if(!first) sb.append(".\n");
		
			en = currentRoom.findByType(Types.LIVING);
			BattleGroup bg = who.getBattleGroup();
			while(en.hasNext()) {
				Living l = (Living) en.next();
				if(l != who) { // don't see yourself =)
					if(l instanceof org.vermin.mudlib.Player) {
						org.vermin.mudlib.Player p = (org.vermin.mudlib.Player) l;
						String surnameSeparator = "";
						if(p.getSurname().length() != 0) surnameSeparator = " ";
						sb.append("&6;"+p.getName()+surnameSeparator+p.getSurname()+" the "+p.getRace().getName()+" "+p.getTitle()+"\n");
					} else if (l.getDescription() != null && l.getDescription().length() > 0) {
						if(bg.isOpponent(l)) {
							sb.append("&B3;");
						} else {
							sb.append("&3;");
						}
						sb.append(l.getDescription()+"\n");
					} 
				}
			}
		
			who.notice(sb.toString());
		}
		else {
			String illuminated = illuminatedExits(currentRoom, who);
			if(illuminated != null) {
				who.notice("There is light glowing in from: " +illuminated);
			}
		}
	}
	
	protected String illuminatedExits(Room currentRoom, Living who) {
		Iterator en = currentRoom.findByType(Types.EXIT);
		
		StringBuffer sb = new StringBuffer();
		
		boolean first = true;
		while(en.hasNext()) {
			
			Exit e = (Exit) en.next();
			Object otherside = World.get(e.getTarget(who.getRoom().getId()));
			if(otherside instanceof Room && tryLookIn(who, (Room) otherside)) {
				if(first) {
					first = false;
				} else {
					sb.append(", ");
				}

				sb.append("&B2;"+e.getDirection(currentRoom.getId())+"&21;");	
			}
		}
		if(!first)
			return sb.toString();
		else
			return null;
	}
	
	/* Look at something in particular */
	public void look2(Living who, String obj) {
		MObject item = who.findByName(obj);
		if(item == null)
			item = who.getRoom().findByName(obj);
		
		if(tryLook(who)) {
			if(item != null) {
		
				if(item instanceof Living) {
					lookAt(who, (Living) item);
				} else if(item instanceof Item) {
					lookAt(who, (Item) item);
					if(item instanceof Wieldable) {
						
						if(who instanceof Wizard) {
							who.notice("You would attack about "+who.getBattleStyle().calculateWeaponSpeed((Wieldable) item)+" times per round with it.");
						}
					}
				} else if(item instanceof Exit) {
					lookAt(who, (Exit) item);
				} else {
					who.notice("FIXME! Object type & name: " + item.getType() + ", " + item.getName());
				}
				return;
			} else {
				lookAt(who, obj);
				return;
			}
		
		}
	}

	
	private void lookAt(Living who, Item obj) {
		who.notice(obj.getLongDescription());
	}
	
	private void lookAt(Living who, Living l) {
		who.notice(l.getLongDescription());
		who.notice(Print.capitalize(l.getName()) + " is " + l.getShape()+".");

		EqCommand eq = (EqCommand) Commander.getInstance().get("eq");
		eq.run(l, who, new Vector());

		if(who.getSkill("consider") > 0) {
			Vector v = calculateDamage(who, l);
			
			for(int i=0; i<v.size(); i++) {
				who.notice( v.get(i).toString() );
			}
		}
		
		
		if(who instanceof Wizard) {
			who.notice("Extended info:");
			who.notice("  Race: "+l.getRace().getName());
			who.notice("  HP: "+l.getHp()+"/"+l.getMaxHp());
			who.notice("  Stats: Str("+l.getPhysicalStrength()+"/"+l.getMentalStrength()+") Con("+l.getPhysicalConstitution()+"/"+
						  l.getMentalConstitution()+") Dex("+l.getPhysicalDexterity()+"/"+l.getMentalDexterity()+")");
			who.notice("  Exp worth: "+l.getExperienceWorth());
		} 
	}
	
	private void lookAt(Living who, Exit e) {
		who.notice("It's an exit leading "+e.getDirection(who.getRoom().getId()));
	}
	
	/* Look at an extended room description. */
	private void lookAt(Living who, String desc) {
		String extendedDesc = who.getRoom().getExtendedDescription(desc);
		if(extendedDesc != null) {
			who.notice(extendedDesc);
		} else {
			who.notice("Look at what?");
		}
	}
		
	private boolean tryLook(Living who) {
		if(!tryLookIn(who, who.getRoom())) {
			who.notice("It's too dark to see anything.");
			return false;
		}
		return true;
	}
	
	private boolean tryLookIn(Living who, Room r) {
		if(r.getIllumination() < who.getRace().getMinimumVisibleIllumination()) {
			// Check race for 'see in dark' ability
			return false;
		}
		return true;
	}

	// consider related method
	private Vector calculateDamage(Living object, Living subject) {
		
		Vector objectMessage = new Vector();

		int subjectPhysicalStatAverage = 0;
		int subjectMentalStatAverage = 0;

		int objectPhysicalStatAverage = 0;
		int objectMentalStatAverage = 0;

		String physicalMessage;
		String mentalMessage;

		int failureAmount = 0;

		/* Calculate success/failure */
		int success = object.checkSkill("consider");
		
		if(success > 0) {
			failureAmount = 0;
		} else {
			failureAmount = Dice.random(-success);
		}
			
		subjectPhysicalStatAverage = (subject.getPhysicalStrength() + subject.getPhysicalConstitution() + subject.getPhysicalDexterity()) / 3;
		subjectMentalStatAverage = (subject.getMentalStrength() + subject.getMentalConstitution() + subject.getMentalDexterity()) / 3;
		
		subjectPhysicalStatAverage -= failureAmount;
		subjectMentalStatAverage -= failureAmount;

		objectPhysicalStatAverage = (object.getPhysicalStrength() + object.getPhysicalConstitution() + object.getPhysicalDexterity()) / 3;
		objectMentalStatAverage = (object.getMentalStrength() + object.getMentalConstitution() + object.getMentalDexterity()) / 3;

		physicalMessage = getConsiderMessage((subjectPhysicalStatAverage * 100) / objectPhysicalStatAverage);
		mentalMessage = getConsiderMessage((subjectMentalStatAverage * 100) / objectMentalStatAverage);

		
		objectMessage.add("You observe "+subject.getName()+".");
		objectMessage.add("Physically "+subject.getPronoun()+" looks "+physicalMessage+" compared to you.");
		objectMessage.add("Mentally "+subject.getPronoun()+" looks "+mentalMessage+" compared to you.");
			
		return objectMessage;
	}

	// consider related method	
	private String getConsiderMessage(int percentage) {
		String message;
		if(percentage < 90)			message	= "slightly weaker";
		else if(percentage < 10)	message	= "extremely weak";
		else if(percentage < 20)	message	= "weak";
		else if(percentage < 50)	message	= "inferior";
		else if(percentage < 110)	message	= "equal";
		else if(percentage < 250)	message	= "strong";
		else if(percentage < 999)	message	= "extremely powerful";
		else						message	= "godlike";

		return message;
	}
}

