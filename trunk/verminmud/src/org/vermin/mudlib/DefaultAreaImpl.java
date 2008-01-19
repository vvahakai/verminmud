/*
 * Created on 19.2.2005
 */
package org.vermin.mudlib;

import java.util.LinkedList;

/**
 * @author Jaakko Pohjamo
 */
public class DefaultAreaImpl implements Area {
	private Mapper mapper;
	private Weather weather;
	private String name;
	private LinkedList<SpawningRule> spawningRules;
	private Area parent;
	
	public DefaultAreaImpl(Mapper mapper, Weather weather, String name, LinkedList<SpawningRule> rules) {
		this.mapper = mapper;
		this.weather = weather;
		this.name = name;
		
		if(rules != null)
			spawningRules = rules;
		else
			spawningRules = new LinkedList();
	}
	
	public Mapper getMapper() {
		return mapper;
	}
	public void setMapper(Mapper m) {
		mapper = m;
	}
	
	public Weather getWeather() {
		return weather;
	}
	
	public String getName() {
		return name;
	}
	
	public void spawn(Room r) {
		for(SpawningRule rule : spawningRules) {
			rule.spawn(r);
		}
		if(parent != null) {
			parent.spawn(r);
		}
	}
	
	public void addSpawningRule(SpawningRule r) {
		spawningRules.add(r);
	}
	
	public void removed(MObject o) {
		for(SpawningRule rule : spawningRules) {
			rule.unspawn(o);
		}
		if(parent != null) {
			parent.removed(o);
		}
	}
	
	public Area getParent() {
		return parent;
	}
	
	public void setParent(Area a) {
		parent = a;
	}
}
