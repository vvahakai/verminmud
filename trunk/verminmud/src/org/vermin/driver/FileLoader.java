/* FileLoader.java
 * 8.3.2003 Tatu Tarvainen
 *
 * Loads objects.
 */
package org.vermin.driver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.vermin.io.SExpObjectInput;

public class FileLoader implements Loader {
	
	public static Logger log = Logger.getLogger(FileLoader.class.getName());
	
	public static class Directory {
		
		File base;
		String loaderBase;
		
		Directory(String loaderBase, File base) {
			this.loaderBase = loaderBase;
			this.base = base;
		}
		
		public String[] getFiles() {
			String[] files = base.list();
			ArrayList<String> al = new ArrayList(files.length);
			for(String f : files)
				if(!f.endsWith(".backup"))
					al.add(f);
			
			return al.toArray(new String[al.size()]);
		}
		
		public String[] getContainedIDs() {
			String[] files = getFiles();
			String[] ids = new String[files.length];
			for(int i=0; i<files.length; i++)
				ids[i] = loaderBase+"/"+files[i];
			return ids;
		}
		
		public File getBase() {
			return base;
		}
	}
	                               
	/* The base directory from which to load from */
	private File base;

	// empty constructor for scripting purposes
	public FileLoader() {}

	public FileLoader(File base) {
		if(!base.isDirectory()) {
			throw new IllegalArgumentException("Base must be a directory!");
		}

		this.base = base;
	}

	public FileLoader(String base) {
		
		this.base = new File(base);

		if(!this.base.isDirectory()) {
			throw new IllegalArgumentException("Base must be a directory!");
		}

	}

	public void setBase(String base) {
		this.base = new File(base);
		if(!this.base.isDirectory())
			throw new IllegalArgumentException("Base must be a directory!");
	}

	/**
	 * Read an object from the given file.
	 * Path access and security checks have been made before
	 * this method is called.
	 *
	 * This method can be customized to provide different
	 * loading methods.
	 *
	 * @param in the file to read the object from
	 * @return the prototype read from the file
	 * @throws LoadException, if loading failed
	 */
	protected Prototype readObject(File in) throws Exception {
		try {
			
		
			LoaderObjectInput input;
			input = new LoaderObjectInput(this, in);
		
			Object obj = input.deserialize();

            //System.out.println("FileLoader HAS LOADED: "+obj);
			if(obj instanceof Prototype) {
				return (Prototype) obj;
			} else if(obj instanceof Object[] && ((Object[]) obj)[0].toString().equals("INFO")) {
				Object[] info = (Object[]) obj;
				return new SExpPrototype( in, ((Boolean) info[2]).booleanValue(),
												  info[1].toString(), input.loadSExpression());
			
			} else {
				return new PrototypeObjectWrapper(null, obj);
			}
		} catch(SExpObjectInput.ExecutionException see) {
			
			Throwable t = see;
			while(t.getCause() != null)
				t = t.getCause();
			
			throw new LoadException(see.getFailure() + "; " + t.getMessage());
		} catch(java.io.IOException ioe) {
			throw new LoadException(ioe.getMessage());
		} 
	}

	/**
	 * Check if this file name is acceptable.
	 * This method can be overridden to provide, for example, loaders 
	 * which only operate files of a specific suffix.
	 *
	 * @param file the name of the file
	 * @return true if this file is acceptable, false otherwise
	 */
	protected boolean isFileAccepted(String file) {
		return true;
	}

	public Prototype load(String file) throws Exception {

		try {
            file = file.replace('\\', '/');
			File in = new File(base, file);
			
			/**
			 * If file does not exist, try appending extension ".vof".
			 * (Because Eclipse sucks).
			 */
			if(!in.exists()) 
				in = new File(base, file+".vof");
			
			/* If file still does not exist, do the original behaviour and return null. */
			if(!in.exists()) {
				throw new LoadException("File does not exist: "+in.getAbsolutePath());
				// return null;
			}

			if(!isPathAllowed(in)) throw new LoadException("Path is not allowed.");
			if(!in.canRead()) throw new LoadException("File is not readable.");

			String key = in.getCanonicalPath();
			
			if(in.isDirectory()) {
				return new PrototypeObjectWrapper(null, new Directory(file, in));
			} else {
				return readObject(in);
			}
		} catch(IOException io) {
			throw new LoadException("Unable to load, IOException: "+io.getMessage());
		}
	}
	
	public Object get(String path) throws Exception {
		return load(path).get();
	}

	private boolean isPathAllowed(File path) {

		try {
			String pathString = path.getCanonicalPath();
			
			// Simple access check to ensure that the path
			// is under the base.
			// This prevents "../../../../../../etc/passwd" type of hacks.
			return pathString.startsWith(base.getCanonicalPath());

		} catch(IOException io) {
			return false;
		}
	}
	
	public boolean isLoaded(String p) {
		return false;
	}

	public void unload(String path) {}
}
