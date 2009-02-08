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
	private String lineMargin = null;
	private Player who;

	public static interface Listener {
		public void done(String txt);
		public void canceled();
	};

	public LineEditor(Player who, Listener l, int maxLines) {
		this(who,l,maxLines,null);
	}
	
	public LineEditor(Player who, Listener l, int maxLines, String lineMargin) {
		this.who = who;
        this.maxLines = maxLines;
        this.listener = l;
        this.lineMargin = lineMargin;
	}
	
	public void activate() {
		who.handleAll(this);
        who.setPrompt(this);
	}
	
    public String prompt(Player who) {
    	StringBuilder sb = new StringBuilder();
    	if (lines.size() < 1) {
    		who.notice("\nVerminEditor v1.0: Type '.' to quit.");
        	who.notice("====================================");
    	}
        sb.append("[");
        sb.append(lines.size()+1);
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
			if (this.lineMargin != null) {
				lines.add(lineMargin + command);
			} else {
				lines.add(command);
			}
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
		for(String s : lines) {
			sb.append(s);
			sb.append("\n");
		}
        listener.done(sb.toString());
	}
}
