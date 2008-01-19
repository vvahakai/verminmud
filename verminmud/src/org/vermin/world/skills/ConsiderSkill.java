/* ConsiderSkill.java
	6.7.2002	VV
	
	skill for finding out how tough other players and monsters are
*/
package org.vermin.world.skills;

import org.vermin.mudlib.*;
import org.vermin.mudlib.skills.*;

import java.util.Vector;

public class ConsiderSkill extends BaseSkill {
	
	protected SkillType[] skillTypes = new SkillType[] { SkillTypes.DIVINATION, SkillTypes.LOCAL };

	public SkillType[] getTypes() {
		return skillTypes;
	}

	public String getName() {
		return "consider";
	}
	
	/* Try to use this skill. */
	public boolean tryUse(Living who, MObject target) {
		return hasLivingTarget(who, target);
	}

   public int getTickCount() {
      return 2 + Dice.random(3);
   }

	public void use(SkillUsageContext suc) {
		Living who = suc.getActor();
		MObject target = suc.getTarget();
		int success = suc.getSkillSuccess();

		Vector v = calculateDamage(who, (Living) target, success);
		
		for(int i=0; i<v.size(); i++)
			who.notice( v.get(i).toString() );

	}

	private String getConsiderMessage(int percentage)
	{
		String message;
		if(percentage < 90)			message	= "slightly weaker";
		else if(percentage < 10)	message	= "extremely weak";
		else if(percentage < 20)	message	= "weak";
		else if(percentage < 50)	message	= "inferior";
		else if(percentage < 110)	message	= "equal";
		else if(percentage < 250)	message	= "strong";
		else if(percentage < 999)	message	= "extremely powerful";
		else						message	= "godlike";

		return message;
	}

	private Vector calculateDamage(Living object, Living subject, int success) {
		
		Vector objectMessage = new Vector();

		int subjectPhysicalStatAverage = 0;
		int subjectMentalStatAverage = 0;

		int objectPhysicalStatAverage = 0;
		int objectMentalStatAverage = 0;

		String physicalMessage;
		String mentalMessage;

		int failureAmount = 0;

		/* Calculate success/failure */
		if(success < 0)
		{
			failureAmount = Dice.random(0 - success);
			
		}
		
		subjectPhysicalStatAverage = (subject.getPhysicalStrength() + subject.getPhysicalConstitution() + subject.getPhysicalDexterity()) / 3;
		subjectMentalStatAverage = (subject.getMentalStrength() + subject.getMentalConstitution() + subject.getMentalDexterity()) / 3;
		
		subjectPhysicalStatAverage -= failureAmount;
		subjectMentalStatAverage -= failureAmount;

		objectPhysicalStatAverage = (object.getPhysicalStrength() + object.getPhysicalConstitution() + object.getPhysicalDexterity()) / 3;
		objectMentalStatAverage = (object.getMentalStrength() + object.getMentalConstitution() + object.getMentalDexterity()) / 3;

		physicalMessage = getConsiderMessage((subjectPhysicalStatAverage * 100) / objectPhysicalStatAverage);
		mentalMessage = getConsiderMessage((subjectMentalStatAverage * 100) / objectMentalStatAverage);

		
		objectMessage.add("You observe "+subject.getName()+".");
		objectMessage.add("Physically "+subject.getPronoun()+" looks "+physicalMessage+" compared to you.");
		objectMessage.add("Mentally "+subject.getPronoun()+" looks "+mentalMessage+" compared to you.");
			
		return objectMessage;
	}
}
