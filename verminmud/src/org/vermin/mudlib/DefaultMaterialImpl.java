/* DefaultMaterialImpl.java
	2.2.2002	Tatu Tarvainen / Council 4
	
	Models different material types.
*/

package org.vermin.mudlib;

import java.util.HashMap;

public class DefaultMaterialImpl implements Material
{
	/* Material name */
	private String name;
	
	/* Weight measured in grams per cubic centimeter */
	private int weight;
	
	/* Durability of this material. Material's inherent damage resistance. */
	private int durability;
	
	/* Materials type ie. wood, metal, gem */
	private MaterialType type;
	
   /* Intrinsic value (in tin coins) for a cubic centimeter of this
    * material.
    */
	private long value;

	private HashMap properties = new HashMap();

	protected DefaultMaterialImpl(String name, int weight, int durability, long value, MaterialType type) {
		this.setName(name);
		this.setWeight(weight);
		this.setDurability(durability);
		this.setValue(value);
		this.setType(type);
	}

	/* (non-Javadoc)
	 * @see org.vermin.mudlib.Material#setProperty(java.lang.String, int)
	 */
	public void setProperty(String property, int value) {
		properties.put(property, new Integer(value));
	}

	/* (non-Javadoc)
	 * @see org.vermin.mudlib.Material#getProperty(java.lang.String)
	 */
	public int getProperty(String property) throws NoSuchPropertyException {
		Integer i = (Integer) properties.get(property);
		if(i == null)
			throw new NoSuchPropertyException("No property named: "+property);
		else
			return i.intValue();
	}

	/* (non-Javadoc)
	 * @see org.vermin.mudlib.Material#getProperty(java.lang.String, int)
	 */
	public int getProperty(String property, int dfl) {
		Integer i = (Integer) properties.get(property);
		if(i == null)
			return dfl;
		else
			return i.intValue();
	}
		
	/* (non-Javadoc)
	 * @see org.vermin.mudlib.Material#setWeight(int)
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}

	/* (non-Javadoc)
	 * @see org.vermin.mudlib.Material#getWeight()
	 */
	public int getWeight() {
		return weight;
	}

	/* (non-Javadoc)
	 * @see org.vermin.mudlib.Material#setDurability(int)
	 */
	public void setDurability(int durability) {
		this.durability = durability;
	}

	/* (non-Javadoc)
	 * @see org.vermin.mudlib.Material#getDurability()
	 */
	public int getDurability() {
		return durability;
	}

	/* (non-Javadoc)
	 * @see org.vermin.mudlib.Material#setType(org.vermin.mudlib.MaterialType)
	 */
	public void setType(MaterialType type) {
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see org.vermin.mudlib.Material#getType()
	 */
	public MaterialType getType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see org.vermin.mudlib.Material#setValue(long)
	 */
	public void setValue(long value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see org.vermin.mudlib.Material#getValue()
	 */
	public long getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see org.vermin.mudlib.Material#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see org.vermin.mudlib.Material#getName()
	 */
	public String getName() {
		return name;
	}

	public boolean isType(MaterialType type) {
		return type == this.type;
	}
}
