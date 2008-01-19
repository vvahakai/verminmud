/**
 * ServiceException.java
 * 21.9.2003 Tatu Tarvainen
 *
 * Exception type for Services.
 * Driver will catch these when starting or stopping services.
 */
package org.vermin.driver;

public class ServiceException extends RuntimeException {

	private Service service;
	private Exception nested;

	public ServiceException(Service s, String msg) {
		super(msg);
		service = s;
	}
	
	public ServiceException(Service s, Exception ex, String msg) {
		super(msg);
		service = s;
		nested = ex;
	}

	public Service getService() {
		return service;
	}

	public Exception getNestedException() {
		return nested;
	}

	public String toString() {
		StringBuffer s = new StringBuffer("ServiceException in ");
		s.append(service);
		s.append(": ");
		s.append(getMessage());
		if(nested != null) {
			s.append("\n  Nested exception: ");
			s.append(nested);
		}
		return s.toString();
	}
}
