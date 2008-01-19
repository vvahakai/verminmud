/* PlayerCreator.java
	16.3.2002	Tatu and Jaakko (drunk)
	23.3.2002	Fixed while sober.
	
	Create a new player.
*/
package org.vermin.mudlib;

import org.vermin.driver.AuthenticationProvider;
import org.vermin.driver.Driver;
import org.vermin.io.TelnetConnection;
import org.vermin.util.Print;
import org.vermin.world.commands.HelpCommand;

public class PlayerCreator { 
	private String name;
	private String password;
	private Race race;
	
	private AuthenticationProvider pa;
	private DefaultPlayerImpl p;

	private TelnetConnection connection;
	private String prompt;
	private int question=0;

	public PlayerCreator(TelnetConnection connection) {
		pa = Driver.getInstance().getAuthenticator();
		this.connection = connection;
	}
	
	
	public Player create() {
		
		connection.println("");
		connection.println("VerminMUD character creation.");
		connection.flush();
		
		// Ask for player name
		String name = "";
		boolean done = false;
		while(!done) {
			
			connection.println("Please choose your name (min of 3 characters, no special characters).");
			connection.flush();
			
			name = Print.capitalize(prompt("Name").trim());
			if(name.length() >= 3 && name.indexOf(' ') == -1 && !isInvalid(name))
				done = true;
			
			if(pa.contains(name)) {
				connection.println("The name '"+name+"' is already in use, please choose another one.");
				done = false;
			}
		}
		
		// Ask twice for a password
		String password1 = "", password2 = "";
		done = false;
		while(!done) {
			
			connection.println("Please choose a password (minimum of 5 characters).");
			password1 = promptHidden("Password");
			password2 = promptHidden("Password (again)");
			if(password1.length() >= 5 && password1.equals(password2))
				done = true;
		}
			
		// Ask for player race
		Race race = null;
		done = false;
		while(!done) {
		
			connection.println("Please choose a race.");
			connection.println("Possible races:");
			connection.println(Print.joinI(World.getPlayableRaceNames(), ", "));
			connection.println("Use 'help race <racename>' to get a description of a race.");
			
			String raceName = prompt("Race");
			String helpOnRace = null;
			if(raceName.indexOf("help race ") != -1) {
				String[] results = raceName.split(" ");
				helpOnRace = results[results.length-1];
			}
			if(helpOnRace != null) {
				connection.println(((HelpCommand) Commander.getInstance().get("help")).dumpFile("race_"+helpOnRace));
			} else {
				race = World.getPlayableRaceByName(raceName);
			}
			
			if(race != null)
				done = true;
		}
		
		return createPlayer(name, password1, race);
	}

	public String prompt(String p) {
		
		connection.print(p+": ");
		connection.goAhead();
		return connection.readLine();
		
	}
	
	private String promptHidden(String p) {
		
		connection.print(p+": ");
		connection.hideInput();
	
		String hidden = connection.readLine();
		connection.showInput();
		return hidden;
	}



	
	private Player createPlayer(String name, String password, Race race) {
		
		
		p = new DefaultPlayerImpl();
		p.setName(name);
		p.setStartingRoom("city:0,0,0");
		p.setId("players/"+p.getName().toLowerCase());
		p.setRace(race);
		p.setExperience(10000);
		
		pa.add(name, password, p.getId());
		
		p.getMoney().add(Money.Coin.GOLD, 10);

        Race r = race;
		p.setPhysicalStrength(
			Math.round(
					(float) r.getMaxPhysicalStrength() / (float) 10.0));
		
		p.setPhysicalDexterity(
			Math.round(
				(float) r.getMaxPhysicalDexterity() / (float) 10.0));
				
		p.setPhysicalConstitution(
			Math.round(
				(float) r.getMaxPhysicalConstitution() / (float) 10.0));
				
		p.setPhysicalCharisma(
			Math.round(
				(float) r.getMaxPhysicalCharisma() / (float) 10.0));
		
		p.setMentalStrength(
			Math.round(
				(float) r.getMaxMentalStrength() / (float) 10.0));
				
		p.setMentalDexterity(
			Math.round(
				(float) r.getMaxMentalDexterity() / (float) 10.0));
				
		p.setMentalConstitution(
			Math.round(
				(float) r.getMaxMentalConstitution() / (float) 10.0));
				
		p.setMentalCharisma(
			Math.round(
				(float) r.getMaxMentalCharisma() / (float) 10.0));
		
		p.start();
		
		p.setHp(p.getMaxHp());
		p.setSp(p.getMaxSp());
		p.setSustenance(10000);
		
		
		p.save();
		
		String id = p.getId();
		return (Player) World.get(id);

	}
	
	private boolean isInvalid(String name) {

		for(int i=0; i<name.length(); i++)
			if(!Character.isLetterOrDigit(name.charAt(i)))
				return true;
		
		return false;
	}

    /* (non-Javadoc)
     * @see org.vermin.driver.ClientInputHandler#startSession()
     */
    public void startSession() {}
}
