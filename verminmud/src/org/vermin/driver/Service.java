/**
 * Service.java
 * 21.9.2003 Tatu Tarvainen
 *
 * Interface for startable/stoppable services.
 */
package org.vermin.driver;

public interface Service {
	public void startService() throws ServiceException;
	public void stopService() throws ServiceException;
	public String getName();
	public boolean isActive();
}
