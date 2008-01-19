/**
 * Automatic.java
 *
 * Notification for trying to use automatic skills.
 */

package org.vermin.mudlib.skills;
import org.vermin.mudlib.*;

public class Automatic extends BaseSkill {

	private static SkillType AUTOMATIC =
		new SkillTypeAdapter() {
			public boolean tryUse(SkillUsageContext suc) {
				suc.getActor().notice("This skill is automatically in use.");
				return false;
			}};
			
	
	private static SkillType[] types = new SkillType[] { AUTOMATIC };

	public SkillType[] getTypes() {
		return types;
	}

	public String getName() {
		return "";
	}
	public int getTickCount() {
		return 0;
	}

	public void use(SkillUsageContext suc) {}

}
