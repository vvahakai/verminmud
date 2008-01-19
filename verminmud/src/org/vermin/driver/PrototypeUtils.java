package org.vermin.driver;

import java.lang.reflect.Method;

public class PrototypeUtils {

	public static void startObject(Object o) {
		
		Class cls = o.getClass();
		
		Method[] m = cls.getMethods();
		
		for(int i=0; i<m.length; i++) {
			if(m[i].getName().equals("start") && m[i].getParameterTypes().length == 0) {
				
				try {
					m[i].invoke(o);
				} catch(Exception e) {
					System.out.println("[PrototypeUtils] Exception in object start -method: "+e.getMessage());
					e.getCause().printStackTrace();
				}
				return;
			}
		}

	}
}
