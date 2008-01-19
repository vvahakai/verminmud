
package org.vermin.world.exits;

import org.vermin.mudlib.*;

public class LockableDoor extends DefaultExitImpl {

	public LockableDoor() {}

	/* Is the door locked. */
	protected boolean locked;

	/* This door can only be opened with a key
	 * that has the same key code.
	 */
	protected String keyCode;

	protected static final String defaultLockMessage = "The door is locked.";

	protected String lockMessage = defaultLockMessage;
	
	public LockableDoor(String room1, String dir1, String room2, String dir2,
							  String keyCode, boolean initiallyLocked) {
		super(room1, dir1, room2, dir2);
		this.keyCode = keyCode;
		locked = initiallyLocked;
		lockMessage = defaultLockMessage;
	}

	public boolean tryMove(Living who, String roomId) {
		
		if(locked) {
			who.notice(lockMessage);
			return false;
		} else {
			return super.tryMove(who, roomId);
		}
	}
	
	public String getKeyCode() {
		return keyCode;
	}

	public void setLocked(boolean lock) {
		locked = lock;
	}

	public boolean isLocked() {
		return locked;
	}
}
