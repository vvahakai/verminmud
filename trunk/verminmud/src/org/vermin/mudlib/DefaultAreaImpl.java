/*
 * Created on 19.2.2005
 */
package org.vermin.mudlib;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Jaakko Pohjamo
 */
public class DefaultAreaImpl implements Area {
	private Mapper mapper;
	private Weather weather;
	private String name;
	private List<SpawningRule> spawningRules;
	private Area parent;
	
	/**
	 * Instantiate a new default area.
	 * Using this constructor automatically uses the default weather service.
	 * 
	 * @param mapper
	 * @param name
	 * @param rules
	 */
	public DefaultAreaImpl(Mapper mapper, String name, List<SpawningRule> rules) {
		this(mapper, WeatherService.getInstance(), name, rules);
	}
	
	public DefaultAreaImpl(Mapper mapper, Weather weather, String name, List<SpawningRule> rules) {
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
