package org.vermin.driver;

/**
 * A loader that can first load a loader from a file
 * and use that as a subloader
 */
public class SubLoader implements Loader {

	public Prototype load(String path) throws Exception {
		int idx = path.indexOf('|');
		
		if(idx != -1) {
			String loaderPath = path.substring(0, idx);
			//String subPath = path.substring(idx+1);
			//System.out.println("SubLoader :: Trying to load subloader: "+loaderPath);
			
			// give the full path for the subloader also
			Loader l = (Loader) Driver.getInstance().getLoader().get(loaderPath);
			if(l != null) {
				//System.out.println("SubLoader :: trying to load: "+path);
				return l.load(path);
			}
			
			if(l == null)
				System.out.println("Unable to load subloader: "+path);
		}
		
		
		return null;
	}

	public Object get(String path) throws Exception {
		return load(path).get();
	}

	public boolean isLoaded(String path) {
		throw new UnsupportedOperationException("SubLoader does not support unload");
	}

	public void unload(String path) {
		throw new UnsupportedOperationException("SubLoader does not support unload");
	}
}
