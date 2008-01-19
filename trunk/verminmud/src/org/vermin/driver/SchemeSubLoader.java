package org.vermin.driver;

import java.io.File;

import jscheme.JScheme;
import jscheme.SchemeProcedure;

public class SchemeSubLoader extends SubLoader {

	private JScheme js = null;
	private String code;
	
	public SchemeSubLoader() {}
	
	private synchronized JScheme getJS() { 
		if(js == null) {
			js = new JScheme();
			System.out.println("LOAD CODE: "+code);
			js.load(code);
		}
		return js;
	}

	private static Object NO_SUCH_FUNCTION = new Object();
	
	private Object call(String name, Object ... args) {
		JScheme j = getJS();
		SchemeProcedure sp = j.getGlobalSchemeProcedure(name);
		if(sp == null) // PENDING: the above throws an exception if the procedure does not exist
			return NO_SUCH_FUNCTION;
		return j.apply(sp, args);
	}
	
	@Override
	public Object get(String path) throws Exception {
		return call("get" ,path);
	}

	@Override
	public boolean isLoaded(String path) {
		throw new UnsupportedOperationException("isLoaded should be handled by default loader");
	}

	@Override
	public Prototype load(final String path) throws Exception {
		return new Prototype() {
			public Object create(String name) {
				return get();
			}
			public Object create() {
				return get();
			}
			public Object get(String name) {
				return get();
			}
			public Object get() {
				return call("make-object", path);
			}
			public String getName() {
				return toString();
			}
			public boolean isUnique() {
				if(call("unique?") == Boolean.TRUE)
					return true;
				return false;
			}
			public File getSource() {
				return null;
			}
			public void setId(String id) {
			}
		};
	}

	@Override
	public void unload(String path) {
		super.unload(path);
	}

}
