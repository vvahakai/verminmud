package org.vermin.driver;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import org.vermin.io.Executable;
import org.vermin.io.SExpObjectOutput;

public class ObjectWriter extends SExpObjectOutput {

	public ObjectWriter(OutputStream os) throws IOException {
		super(os);
	}
	public ObjectWriter(PrintWriter pw) throws IOException {
		super(pw);
	}
	
	private Object obj;

	public void serialize(Object obj) {
		this.obj = obj;
		super.serialize(obj);
	}

	protected Executable getAugmentedFunction(Object obj) {

        if(obj instanceof Singleton) {
            final String singletonClass = obj.getClass().getName();
            return new Executable("singleton") {
                public Object execute(List args, Dynvars dynvars) {
                    return "(singleton \""+
                        singletonClass +
                        "\")";
                }};
        } else if(obj != this.obj && obj instanceof Persistent &&
        		!((Persistent) obj).isAnonymous()) {

        	System.out.println("SAVING AS REFERENCE: "+obj);
			final Persistent p = (Persistent) obj;
			return new Executable("ref") {
					public Object execute(List args, Dynvars dynvars) {
						return 
							"(ref \""+
							p.getId()+
							"\")";
					}};
		} else {
			return null;
		}

	}
	
}
