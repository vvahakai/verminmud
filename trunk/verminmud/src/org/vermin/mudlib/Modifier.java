
package org.vermin.mudlib;

public interface Modifier {

	/**
	 * Is this modifier still active.
	 * @return true if modifier is active, false otherwise.
	 */
	public boolean isActive();

	/**
	 * Returns the modification value for
	 * the given target. If this modifies
	 * a boolean value then the modification
	 * is a strength (-100 to 100) and if the 
	 * total value of all modifiers is positive
	 * then the resulting boolean will be true.
	 * If the total is negative then the
	 * resulting boolean will be false.
	 * Otherwise the original value stands.
	 *
	 * @param target the object to modify
	 * @return the modification value
	 */
	public int modify(MObject target);

	/**
	 * Returns the extra description (if any)
	 * for this modifier. The extra descriptions
	 * will be included in the modified object's 
	 * long description where applicable.
	 * @return the extra description
	 */
	public String getDescription();

	/**
	 * This method must deactivate this modifier.
	 * Any calls to isActive after this must return false.
	 */
	public void deActivate();

	/**
	 * Returns the type of this modifier which is one
	 * of the enumerated modifier types.
	 */
	public ModifierTypes getType();

	/**
	 * Returns the modifier arguments.
	 * The semantics of arguments is specific
	 * to the modifier type.
	 */ 
	public Object[] getArguments();
}
