package org.vermin.mudlib.minion;

import org.vermin.mudlib.Living;

public interface Minion extends Living {

	public abstract void setFollowing(boolean f);

	public abstract void setNickname(String nickname);

	public abstract String getNickname();

	public abstract Living getMaster();

	public abstract boolean isFollowing();

	public abstract void addCommand(String command);

	public abstract boolean performCommand(String command, String params);
	
	public abstract void moveToFront();
	
	public abstract void listCommands();

}