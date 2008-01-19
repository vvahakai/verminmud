package org.vermin.driver;

import jscheme.SchemeProcedure;

public class SchemeHook implements Hook {

	private SchemeProcedure hook;

	public SchemeHook(SchemeProcedure h) {
		this.hook = h;
	}

	public void run(Object[] args) {
		hook.apply(args);
	}
}
