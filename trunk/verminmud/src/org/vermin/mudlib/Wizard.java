/* Wizard.java
	16.2.2002
	
	An extended Player for Wizards.
	Added ability to set values and reflectively
	change almost anything runtime.
	
	18.11.2005: Moved all commands to their own classes and refactored
*/

package org.vermin.mudlib;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import org.vermin.driver.ExceptionHandler;
import org.vermin.wicca.ClientOutput;

public class Wizard extends DefaultPlayerImpl implements ExceptionHandler {
	
	protected transient Object editing; /* Object we are editing */
	
	protected String homeDirectory;

	
	public Wizard() {
		super();
	}

	
	public Object getEditing() {
		return editing;
	}


	public void setEditing(Object editing) {
		this.editing = editing;
	}
	
	@Override
	protected Command getCommand(String cmd) {
		return Commander.getWizardInstance().get(cmd);
	}
	 
	
	public void handleException(Exception e, Map<String, Object> context) {
		ClientOutput co = getClientOutput();
		if(co == null) return;
		
		StringWriter strw = new StringWriter();
		PrintWriter pw = new PrintWriter(strw);
		pw.println("--- EXCEPTION ---");
		e.printStackTrace(pw);
		if(context != null && !context.isEmpty()) {
			pw.println("CONTEXT ITEMS:");
			for(String key : context.keySet()) {
				pw.println(key+": "+context.get(key));
			}
		}
		co.put(strw.toString());
	}

	
	
	/* Wizards do not die, so override all subHp methods */

	public boolean subHp(int amount) {
		hp = (hp - amount < 0 ? hp : hp-amount);
		return false;
	}
	public boolean subHp(Damage amount) {
		hp = (hp - amount.damage < 0 ? hp : hp-amount.damage);
		return false;
	}
	public boolean subHp(Damage amount, int hitloc) {
		hp = (hp - amount.damage < 0 ? hp : hp-amount.damage);
		return false;
	}
	public boolean subHp(Damage amount, Living attacker) {
		hp = (hp - amount.damage < 0 ? hp : hp-amount.damage);
		return false;
	}
	public boolean subHp(Damage amount, int hitloc, Living attacker) {
		hp = (hp - amount.damage < 0 ? hp : hp-amount.damage);
		return false;
	}

}
