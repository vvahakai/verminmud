package org.vermin.driver;

import java.util.Map;

public interface ExceptionHandler {
	/**
	 * Handle the exception in some way.
	 * 
	 * @param e the exception that occured
	 * @param context a map of related objects, may be null
	 */
	public void handleException(Exception e, Map<String,Object> context);
}
