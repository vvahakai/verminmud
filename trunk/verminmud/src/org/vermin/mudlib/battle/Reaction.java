package org.vermin.mudlib.battle;

public abstract class Reaction extends Message {

	public String attackerMessage;
	public String targetMessage;
	public String spectatorMessage;

	public Attack counterAttack;

}
