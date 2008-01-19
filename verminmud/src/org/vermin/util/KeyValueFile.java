/*
 * Created on Jun 18, 2004
 */
package org.vermin.util;

import java.util.Hashtable;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;


/**
 * @author Jaakko Pohjamo
 */
public class KeyValueFile extends Hashtable {

	public KeyValueFile(String name) throws java.io.FileNotFoundException, java.io.IOException {
		super();
		
		BufferedReader in = new BufferedReader(new FileReader(new File(name)));
		String line = in.readLine();
		
		while(line != null) {
			String[] matches = line.split("=");
			if(matches.length == 2) {
				put(matches[0], matches[1]);
			}
			line = in.readLine();
		}
		in.close();
	}
}
