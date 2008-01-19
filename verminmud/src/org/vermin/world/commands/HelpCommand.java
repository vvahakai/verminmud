/* HelpCommand.java
	22.3.2002	Tatu Tarvainen
	
	The 'help topic' player command.
*/
package org.vermin.world.commands;

import org.vermin.mudlib.*;

import java.util.Vector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class HelpCommand extends TokenizedCommand implements Command
{
	protected String[] path;
	
	public HelpCommand() {}
	public HelpCommand(String path) {
		this.path = path.split(":");
	}
	
	public void run(Living who, Vector params) {
		String topic;
		
		if(params.size() < 2) {
			topic = "topics";
		} else {
			topic = vectorToString(params, 1);
		}
		
		who.notice(dumpFile(topic));
	}
	
	protected String vectorToString(Vector v, int start) {
		StringBuffer sb = new StringBuffer();
		for(int i=start; i<v.size(); i++) {
			sb.append(v.get(i).toString().toLowerCase() + (i==v.size()-1 ? "" : "_"));
		}
		return sb.toString();
	}
	
	public String dumpFile(String topic) {
		StringBuffer sb = new StringBuffer();
		
		for(String path : this.path) {
			File f = new File(path, topic);
			if(!f.exists())
				continue;
			
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(f));
			} catch(FileNotFoundException e) {
				continue;
			}
		
			String line = null;
			try {
				line = in.readLine();
				while(line != null) {
					sb.append(line+"\n");
					line = in.readLine();
				}
			} catch(IOException e) {
				return "Error reading help text, please contact a Wizard.";
			}
			
			return sb.toString();
		}
		
		return "No such help topic: "+topic;

	}

}
