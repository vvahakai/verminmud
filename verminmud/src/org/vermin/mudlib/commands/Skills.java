package org.vermin.mudlib.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.vermin.mudlib.DefaultPlayerImpl;
import org.vermin.mudlib.RegexCommand;
import org.vermin.mudlib.SkillObject.SkillEntry;
import org.vermin.util.Table;

public class Skills extends RegexCommand {

	public String[] getDispatchConfiguration() {
		return new String[] {
				"skills => listSkills(actor)"
		};
	}
	
	public boolean listSkills(DefaultPlayerImpl p) {
		
		boolean nextRow = true;
		
		Table t = new Table();
		t.addHeader("Skill", 29, Table.ALIGN_LEFT);
		t.addHeader("%", 3, Table.ALIGN_MIDDLE);
		t.addHeader("Skill", 29, Table.ALIGN_LEFT);
		t.addHeader("%", 3, Table.ALIGN_MIDDLE);
		
		
		Map skills = p.getSkillObject().getSkills();
		
		ArrayList<String> al = new ArrayList(skills.keySet());
		Collections.sort(al, String.CASE_INSENSITIVE_ORDER);
		Iterator it = al.iterator();
		while(it.hasNext()) {
			String name = (String) it.next();
			SkillEntry se = (SkillEntry) skills.get(name);
			if(nextRow == true) {
				t.addRow();
				nextRow = false;
			}
			else {
				nextRow = true;
			}
			t.addColumn(name, 29, Table.ALIGN_LEFT);
			t.addColumn(Integer.toString(se.percent), 3, Table.ALIGN_RIGHT);
		}
		
		p.notice(t.render());
		return true;
	}
}
