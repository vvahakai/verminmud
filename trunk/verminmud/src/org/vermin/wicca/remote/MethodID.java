package org.vermin.wicca.remote;

import java.lang.annotation.Retention;


@Retention(java.lang.annotation.RetentionPolicy.RUNTIME) public @interface MethodID {
	byte id();
}
