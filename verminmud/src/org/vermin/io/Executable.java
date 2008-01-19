package org.vermin.io;

import java.util.List;
import java.lang.reflect.Type;

public abstract class Executable {

	public static class Parent {
		public Object object;
		public Parent next;
	};

	public static class Dynvars {
		public Type genericType; // generic type for child elements
		public Class type;  // type for child elements 

		public Parent parent; // the containing (object ...) value
	};

	private boolean special; // if true, arguments are not evaluated before the call
	private String name;

	public String getName() {
		return name;
	}

	public Executable(String name, boolean special) {
		this.name = name;
		this.special = special;
	}

	public Executable(String name) {
		this.name = name;
		special = false;
	}

	public boolean isSpecial() {
		return special;
	}
	public abstract Object execute(List args, Dynvars dynvars);

	protected Object eval(Object o, Dynvars d) throws Exception {
		if(o instanceof SExpObjectInput.SExp)
			return ((SExpObjectInput.SExp) o).execute(d);
		else
			return o;
	}
}

