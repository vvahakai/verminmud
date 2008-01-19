package org.vermin.mudlib;

import java.io.StringReader;

import jscheme.JScheme;

public class SchemeCommand implements Command {

    private transient JScheme js;
    private String code;

    public SchemeCommand() {}
    
    private void init() {
    	if(js != null) return;
    	
    	js = new JScheme();
    	js.load(new StringReader(code));
    }
    
	
    public synchronized boolean action(Living who, String params) {
    	init();
    	
    	Object o = js.call(js.getGlobalSchemeProcedure("action"), who, params);
    	if(o instanceof Boolean)
    		return ((Boolean)o).booleanValue();
    	else
    		return false;
    }

}
