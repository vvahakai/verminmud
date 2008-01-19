package org.vermin.driver;

import java.io.InputStream;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Loads an object by invoking a subprocess and reading the standard
 * output of that process.
 * The output object must be a valid SExp object.
 * 
 * @author tadex
 *
 */
public class ExecLoader implements Loader {

	private String prefix;
	private String command;
	
	private Pattern splitter;
	
	public ExecLoader(String prefix, String command) {
		this(prefix, command, null);
	}
	public ExecLoader(String prefix, String command, String splitter) { 
		this.prefix = prefix+":";
		this.command = command;
		if(splitter != null)
			this.splitter = Pattern.compile(splitter);
		
	}
	
	public Prototype load(String path) throws Exception {
		if(!path.startsWith(prefix))
			return null;
		
		Object obj = execAndLoad(path.substring(prefix.length()));
		if(obj == null)
			return null;
		
		return new PrototypeObjectWrapper(null, obj);
		
	}

	private Object execAndLoad(String path) {
		String[] cmd = null;
		if(splitter != null) {
			String[] args = splitter.split(path);
			cmd = new String[args.length+1];
			args[0] = command;
			for(int i=0; i<args.length; i++) 
				cmd[i+1] = args[i];
		} else {
			cmd = new String[] { 
					command, path
			};
		}
		
		try {
			ProcessBuilder pb = new ProcessBuilder(cmd);
			final Process proc = pb.start();
			InputStream in = proc.getInputStream();
			final boolean[] done = new boolean[] { false };
			new Thread() {
				public void run() {
					try {
						proc.waitFor();
						done[0] = true;
					} catch(InterruptedException ie) {
						System.out.println("ExecLoader interrupted: "+ie.getMessage());
					}
				}	
			}.start();
		
			LoaderObjectInput loi = new LoaderObjectInput(this, in);
			return loi.deserialize();
		} catch(Exception ioe) {
			System.out.println("ExecLoader failed: "+ioe.getMessage());
			return null;
		} 
	}
	
	public Object get(String path) throws Exception {
		Prototype p = load(path);
		if(p != null)
			return p.get();
		return null;
	}

	public boolean isLoaded(String path) {
		return false;
	}

	public void unload(String path) {}

	
	public static void main(String[] args) {
		try {
			System.out.println("ExecLoader test...");
			ExecLoader el = new ExecLoader("test", "/Users/tadex/Documents/workspace/VerminMUD/src/scripts/test_exec_loader.sh");
			Map map = (Map) el.get("test:Hello from ExecLoader!");
			System.out.println("key1: "+map.get("key1"));
			System.out.println("Done.");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
