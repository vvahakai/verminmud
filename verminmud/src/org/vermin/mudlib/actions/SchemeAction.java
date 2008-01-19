package org.vermin.mudlib.actions;

import jscheme.JS;
import jscheme.SchemeProcedure;

import org.vermin.mudlib.ScriptAction;

public class SchemeAction implements ScriptAction {

	// Shared? per monster?
	private static JS js = new JS();
	static {
		JS.eval("(import \"org.vermin.mudlib.*\")");
		JS.eval("(define-macro (action args . code)"+
				  "  `(lambda (this ,args)"+
				  "     ,@code))");
	}

	private SchemeProcedure proc;
	private boolean initialized = false;
	private String code;

	public SchemeAction() {}
	
	public SchemeAction(SchemeProcedure proc) {
		this.proc = proc;
		initialized = true;
	}
	
	public Object run(Object self, Object ... args) {
		initialize();
		
		return JS.call(proc, self, args);
	}

	private void initialize() {
		if(initialized) return;
		
		proc = (SchemeProcedure) JS.eval(code);
		
		initialized = true;
	}

}
