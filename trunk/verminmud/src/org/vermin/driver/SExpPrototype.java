/* SExpPrototype.java
 * 6.3.2003 Tatu Tarvainen / Council 4
 *
 */
package org.vermin.driver;

import java.io.File;

import org.vermin.io.Executable;
import org.vermin.io.SExpObjectInput;

public class SExpPrototype extends AbstractPrototype {

	/* The S-Expression that creates the object */
	private SExpObjectInput.SExp sexp;

	protected SExpPrototype() {}

	public SExpPrototype(File source, boolean unique, String name, SExpObjectInput.SExp sexp) {
		
		super(source, unique, name);

		this.sexp = sexp;
		
	}
	
	public Object makeObject() {
        try {
            Object value = sexp.execute(new Executable.Dynvars());
            if(value instanceof Persistent)
                ((Persistent)value).setId(this.id);
            return value;
        } catch(SExpObjectInput.ExecutionException see) {
        	Throwable t = see;
        	while(t.getCause() != null)
        		t = see.getCause();
        	
            System.out.println("SExp execution failed for prototype: "+getSource().getAbsolutePath()+" ("+t.getMessage()+")");
            return null;
        } catch(Exception e) {
        	System.out.println("Unknown SExp execution exception: "+e.getMessage());
        	return null;
        }
	}

}
