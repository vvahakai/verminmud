/*
 * Created on 20.2.2005
 *
 */
package org.vermin.mudlib;

import java.io.PrintStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author tadex
 *
 */
public class OutputCapture implements InvocationHandler {

    private PrintStream to;
    private Living delegate;
    
    public interface CapturerProxy {
        public void close();
    }
    
    private OutputCapture(Living delegate, PrintStream to) {
        this.to = to;
        this.delegate = delegate;
    }
    
    /**
     * Wraps the given <code>Living</code> in a <code>Proxy</code> that
     * forwards all notices to the given stream instead of processing
     * them normally.
     * All other calls are delegated to the proxied object.
     * 
     * @param who the Living to proxy
     * @param to the stream to send the notices to
     * @return the Proxy wrapper
     */
    public static Living withOutputTo(Living who, PrintStream to) {
		World.log(who.getClass().getName());
        Class[] interfaces = who.getClass().getInterfaces();
        Class[] cls = new Class[interfaces.length+1];
        for(int i=0; i<interfaces.length; i++) {
            cls[i] = interfaces[i];
			World.log(interfaces[i].getName());
        }
        cls[interfaces.length] = CapturerProxy.class;
        
        return (Living) Proxy.newProxyInstance(who.getClass().getClassLoader(),
                cls,  new OutputCapture(who, to));
    }
    
    private void notice(String what) {
        to.println(what);
    }
    
    private void close() {
        to.flush();
        to.close();
        to = null;
    }
        
    
    /* (non-Javadoc)
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    public Object invoke(Object target, Method method, Object[] args) throws Throwable {
        if(to == null)
            throw new IllegalStateException("OutputCapture proxy has already been closed.");
        
        if(method.getName().equals("notice") && method.getParameterTypes().length == 1) {
            notice(args[0] != null ? args[0].toString() : "");
        } else if(method.getDeclaringClass() == CapturerProxy.class) {
            close();
        } else
            return method.invoke(delegate, args);
        return null;
    }
}
