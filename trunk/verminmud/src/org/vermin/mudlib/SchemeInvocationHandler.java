/**
 * SchemeInvocationHandler.java
 * An invocation handler for scripting objects in scheme.
 *
 * 20.7.2003 Tatu Tarvainen / Council 4
 */
package org.vermin.mudlib;

import java.io.InputStreamReader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jscheme.JS;
import jscheme.SchemeProcedure;

@Deprecated
public class SchemeInvocationHandler implements InvocationHandler {

	private JS js = new JS();
	private Object host;

	public SchemeInvocationHandler(Object host, String code) {
		JS.eval("(define-macro (def name arglist . code)"+
				  "  `(define (,(string->symbol {[name]/[(length arglist)]}) ,@arglist) ,@code))");

		ClassLoader cl = getClass().getClassLoader();
		JS.load(new InputStreamReader(cl.getResourceAsStream("mudlib/skills.scm")));
		
		JS.eval(code);
		this.host = host;
	}

	public void setProxy(Object proxy) {
		JS.setGlobalValue("this", proxy);
	}

	public Object invoke(Object proxy, Method method, Object[] args) {

		// Scheme procedures are named as follows: methodName/arity
		// example: leaves/2

		SchemeProcedure sp = (SchemeProcedure) JS.getGlobalValue(method.getName()+"/"+args.length);
		if(sp != null) {
			switch(args.length) {
			  case 0: return JS.call(sp); 
			  case 1: return JS.call(sp, args[0]); 
			  case 2: return JS.call(sp, args[0], args[1]); 
			  case 3: return JS.call(sp, args[0], args[1], args[2]); 
			  case 4: return JS.call(sp, args[0], args[1], args[2], args[3]); 
			  default: throw new RuntimeException("FIXME: over 4 args to method: "+method.getName());
			}
		} else {
			if(host == null)
				throw new RuntimeException("Method "+method.getName()+"/"+
													args.length+" not defined and no host object available.");
			
			try {
				return method.invoke(host, args);
			} catch(IllegalAccessException iae) {
				throw new RuntimeException("IllegalAccessException: "+iae.getMessage());
			} catch(InvocationTargetException ite) {
				throw new RuntimeException("InvocationTargetException: "+ite.getMessage());
			}
		}
	}
}
