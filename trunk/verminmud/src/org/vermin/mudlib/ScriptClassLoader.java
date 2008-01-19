package org.vermin.mudlib;

import java.security.*;
import java.util.Enumeration;
import java.net.URL;
import java.security.cert.Certificate;

/**
 * Loads a single class from a byte array.
 */
public class ScriptClassLoader extends SecureClassLoader {

	public Class clazz;

	public ScriptClassLoader(String name, byte[] bytes) {
		clazz = defineClass(name, bytes, 0, bytes.length,
								  new ProtectionDomain(SCRIPT_CODE_SOURCE, 
															  SCRIPT_PERMISSIONS, this, 
															  new Principal[0]));
	}
	
	private static PermissionCollection SCRIPT_PERMISSIONS = new ScriptPermissionCollection();
	private static CodeSource SCRIPT_CODE_SOURCE = new CodeSource((URL) null, new Certificate[0]);

	public static class ScriptPermissionCollection extends PermissionCollection {
		public void add(Permission permission) {
		}
		public Enumeration elements() {
			return null;
		}
		public boolean	implies(Permission permission) {
			return false;
		}
	}

}
