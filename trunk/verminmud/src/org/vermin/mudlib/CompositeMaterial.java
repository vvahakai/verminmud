package org.vermin.mudlib;

import java.util.HashSet;

public class CompositeMaterial implements Material {

	private String name;
	
	private HashSet<MaterialPair> materials = new HashSet<MaterialPair>();
	
	public class MaterialPair {
		
		public MaterialPair(float portion, Material mat) {
			this.portion = portion;
			this.material = mat;
		}
		
		Material material;
		float portion;
	}

	public int getWeight() {
		float weight = 0;
		for(MaterialPair mp : materials) {
			weight += mp.material.getWeight() * mp.portion;
		}
		return (int) weight;
	}

	public int getDurability() {
		float durability = 0;
		for(MaterialPair mp : materials) {
			durability += mp.material.getDurability() * mp.portion;
		}
		return (int) durability;
	}

	public MaterialType getType() {
		//XXX: we could do something fancy with this, but for now just return the majority type
		MaterialType majorType = MaterialType.NONE;
		float majorPortion = 0;
		for(MaterialPair mp : materials) {
			if(mp.portion > majorPortion) {
				majorType = mp.material.getType();
			}
		}
		return majorType;
	}

	public long getValue() {
		float value = 0;
		for(MaterialPair mp : materials) {
			value += mp.material.getValue() * mp.portion;
		}
		return (int) value;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public boolean isType(MaterialType type) {
		for(MaterialPair mp : materials) {
			if(mp.material.getType() == type)
				return true;
		}
		return false;
	}

	public void addMaterial(float portion, Material mat) {
		materials.add(new MaterialPair(portion, mat));
	}
}
