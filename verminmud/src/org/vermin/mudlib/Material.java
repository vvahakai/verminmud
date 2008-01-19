package org.vermin.mudlib;

public interface Material {

	public abstract int getWeight();

	public abstract int getDurability();

	public abstract MaterialType getType();
	
	public abstract boolean isType(MaterialType type);

	public abstract long getValue();

	public abstract String getName();

}