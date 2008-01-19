/* LoaderObjectInput.java
 * 8.3.2003 Tatu Tarvainen
 *
 * Augments SExpObjectInput with loader-specific extensions.
 */
package org.vermin.driver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Iterator;
import java.util.List;

import org.vermin.io.SExpObjectInput;
import org.vermin.io.Executable.Dynvars;

public class LoaderObjectInput extends SExpObjectInput {
	
	private Loader loader;

	public LoaderObjectInput(Loader loader, File in) throws FileNotFoundException {
		super(in);
		this.loader = loader;
	}

	public LoaderObjectInput(Loader loader, InputStream in) {
		super(in);
		this.loader = loader;
	}
	protected MethodInfo getAugmentedFunction(String name) {
        if(name.equals("singleton"))
            return singleton;
        else if(name.equals("prototype"))
			return prototype;
		else if(name.equals("create"))
			return create;
		else if(name.equals("ref"))
			return ref;
		else if(name.equals("class-for"))
			return classFor;
        else if(name.equals("factory"))
            return factory;
        else if(name.equals("script"))
        	return script;
		else
			return null;
	}

	private static MethodInfo prototype = null;
	private static MethodInfo create = null; 
	private static MethodInfo ref = null; 
	private static MethodInfo classFor = null;
    private static MethodInfo singleton = null;
    private static MethodInfo factory = null;
    private static MethodInfo script = null;
    
	static {
		try {
			prototype =  new MethodInfo(false, LoaderObjectInput.class.getDeclaredMethod("handlePrototype", List.class, Dynvars.class));
			create = new MethodInfo(false, LoaderObjectInput.class.getDeclaredMethod("handleCreate", List.class, Dynvars.class));
			ref = new MethodInfo(false, LoaderObjectInput.class.getDeclaredMethod("handleRef", List.class, Dynvars.class));
			classFor = new MethodInfo(false, LoaderObjectInput.class.getDeclaredMethod("handleClassFor", List.class, Dynvars.class));
            singleton = new MethodInfo(false, LoaderObjectInput.class.getDeclaredMethod("handleSingleton", List.class, Dynvars.class));
            factory = new MethodInfo(true, LoaderObjectInput.class.getDeclaredMethod("handleFactory", List.class, Dynvars.class));
            script = new MethodInfo(false, LoaderObjectInput.class.getDeclaredMethod("handleScript", List.class, Dynvars.class));
		} catch(NoSuchMethodException nse) {
			System.out.println("Unable to initialize LoaderObjectInput.");
		}
	}

	/* Create a Scheme proxy for an interface */
	public Object handleScript(List args, Dynvars dynvars) {
		String[] interfaces = ((String)args.get(0)).split(",");
		Class[] ifaces = new Class[interfaces.length];
		for(int i=0; i<interfaces.length; i++) {
			try {
				ifaces[i] = Class.forName(interfaces[i].trim());
			} catch(ClassNotFoundException cnfe) {
				throw new IllegalArgumentException("Can't find interface to proxy: "+interfaces[i]);
			}
		}
		
		return Proxy.newProxyInstance(ifaces[0].getClassLoader(), ifaces,
					new org.vermin.driver.SchemeInvocationHandler((String) args.get(1)));
		
		// return new SchemeAction((SchemeProcedure) args.get(0));
		
	}
	
	public Object handlePrototype(List args, Dynvars dynvars) {
		Iterator it = args.listIterator(0);
		return new Object[] { "INFO", 
		        it.next().toString(), 
		        new Boolean(it.next().toString()) };
	}

	public Object handleCreate(List args, Dynvars dynvars) throws Exception {
		String file = args.get(0).toString();
		Prototype p = Driver.getInstance().getLoader().load(file);
		if(p == null)
			return null;
		// FIXME: Should it be possible to create named instances with 2 second argument?
		return p.create();
	}
	
	public Object handleRef(List args, Dynvars dynvars) throws Exception {
		String file = args.get(0).toString();
		String name = args.size()>1 ? args.get(1).toString() : null;
		

		Prototype p = null;
        try {
            p = Driver.getInstance().getLoader().load(file);
        } catch(Exception e) {
            System.out.println("handleREF FAILED: "+e.getMessage());
            if(e.getCause() != null) {
                System.out.println(" CAUSE: "+e.getCause().getMessage());
                e.getCause().printStackTrace(System.out);
            }
        }
		if(p == null)
			return null;
		
        // FIXME: Remove named instances all together
        // the following line causes much lossage
		//return name == null ? p.get() : p.get(name);
        
        return p.get();
	}

	public Object handleClassFor(List args, Dynvars dynvars) {
		return Driver.getInstance().getClassMapping().getClassForConcept(args.get(1).toString());
	}

	public Object handleSingleton(List args, Dynvars dynvars) {
		try {
			Class c = Class.forName(args.get(0).toString());
			Method m = c.getDeclaredMethod("getInstance");
			return m.invoke(null);
		} catch (Exception exc) {
			System.out.println("Unable to instantiate singleton: "+exc.getMessage());
			System.out.println("  Exception: "+exc.getClass().getName());
			return null;
		}
		
	}
    
    public Object handleFactory(List args, Dynvars dynvars) {
        if(args.size() < 1) {
            System.out.println("No factory name given!");
            return null;
        }
           
        
        	String factoryName = args.get(0).toString();
        	args.remove(0);
       	try {
       		Object[] argValues = new Object[args.size()];
       		for(int i=0; i<args.size(); i++) {
        		argValues[i] = args.get(i).toString();
       		}
       		return Driver.getInstance().getFactory(factoryName).create(argValues);
       	} catch(Exception e) {
       		System.out.println("Factory '"+factoryName+"' failed: "+e.getMessage());
       		return null;
       	}
    }
}
