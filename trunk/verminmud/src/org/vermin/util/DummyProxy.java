package org.vermin.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * A dummy proxy that does nothing on invoked method calls.
 */
public class DummyProxy {

	public static Object create(Class cls) {
		return Proxy.newProxyInstance(cls.getClassLoader(), new Class[] { cls }, 
				new InvocationHandler() {
					public Object invoke(Object proxy, Method method, Object[] args)
							throws Throwable {
						return null;
					}
				});
	}
	                              
}
