/**
 * LineEditor.java
 * 21.9.2003 Tatu Tarvainen
 *
 * A line based text editor for simple in game text editing (like setting plan).
 */
package org.vermin.mudlib;

import java.util.*;

public class LineEditor implements ActionHandler<MObject>, Prompt {
	
	private boolean editDone = false;
	private List<String> lines = new ArrayList<String>();
	private Listener listener;
	private int maxLines = -1;
	private Player who;

	public static interface Listener {
		public void done(String txt);
		public void canceled();
	};

	public LineEditor(Player who, Listener l, int maxLines) {
		this.who = who;
        this.maxLines = maxLines;
        this.listener = l;
		who.handleAll(this);
        who.setPrompt(this);
	}

    public String prompt(Player who) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(lines.size());
        if(maxLines != -1) {
            sb.append("/");
            sb.append(maxLines);
        }
        sb.append("]: ");
        return sb.toString();
    }
	public boolean action(MObject actor, String command) {
		if(editDone)
			return false;

		if(command.equals(".")) {
			done();
		} else {
			lines.add(command);
			if(maxLines != -1 && lines.size() == maxLines)
				done();
		}
		return true;
	}

	private void done() {
		who.unhandleAll();
        who.setPrompt(null);
		editDone = true;
		StringBuilder sb = new StringBuilder();
		for(String s : lines) sb.append(s);
        listener.done(sb.toString());
	}
}
