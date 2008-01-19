package org.vermin.mudlib;

import org.vermin.driver.Persistent;
import org.vermin.driver.Utility;

public class DefaultPersistentImpl implements Persistent {

	private String id;
	private transient boolean anonymous = true;
	
	public void start() {}

	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}

	public boolean isAnonymous() {
		return anonymous;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void save() {
		if(!isAnonymous())
			Utility.save(this, id);
	}

}
