/*
 * Created on 28.5.2005
 */
package org.vermin.world.quest;

import java.util.Vector;

import org.vermin.mudlib.quest.Journal;


public class ReportObjective {

	private Vector prereq;
	
	
	private String requiredItemId;
	private String debriefing;

	public ReportObjective() {
	
	}
	
	public ReportObjective( String requiredItemId, String debriefing) {
		
		prereq = new Vector();
		
		this.requiredItemId = requiredItemId;
		this.debriefing = debriefing;
	}
	
	public String getCode() {
		return "report";
	}

	public String getDescription() {
		return "Report your success to the Master of Mercenaries in Vermin city.";
	}
	
	public String getRequiredItemId() {
		return requiredItemId;
	}
	
	public String getDebriefing() {
		return debriefing;
	}
	
	public boolean isAvailable(Journal journal) {
		return false;
	}

}