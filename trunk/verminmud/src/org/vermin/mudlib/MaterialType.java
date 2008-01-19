package org.vermin.mudlib;

public enum MaterialType {

	NONE	 ("none", "none"),
	WOOD     ("wood", "carpentry"),
	MINERAL  ("mineral", "mineralogy"),
	METAL    ("metal", "metallurgy"),
	ORGANIC  ("organic", "none"),
	GEM      ("gem", "gemcutting");
	
	private final String name;
	private final String masterySkill;
	
	public String getName() {
		return name;
	}
	
	public String getMasterySkill() {
		return masterySkill;
	}
	
    MaterialType(String name, String masterySkill) {
		this.name = name;
		this.masterySkill = masterySkill;
	}
}
