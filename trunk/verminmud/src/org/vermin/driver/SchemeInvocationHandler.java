package org.vermin.driver;

import java.io.StringReader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import jscheme.JScheme;
import jscheme.SchemeException;
import jscheme.SchemeProcedure;

public class SchemeInvocationHandler implements InvocationHandler {

	private JScheme jscheme = new JScheme();
	
	public SchemeInvocationHandler(String code) {
		jscheme.load(new StringReader(code));
	}
	
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
	
		SchemeProcedure p = null;
		try {
			p = jscheme.getGlobalSchemeProcedure(method.getName());
		} catch(SchemeException se) {
			return null;
		}
		
		return p.apply(args);
	}

}
