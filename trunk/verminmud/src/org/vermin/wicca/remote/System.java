package org.vermin.wicca.remote;

@ComponentID(id=0)
public interface System {

	/**
	 * SYSTEM methods:
	 * 	
	 * 
	 *	Server methods:
	 * 0,0 : login
	 * 0,1 : put line
	 * 
	 * Client methods:
	 * 2 : show error message
	 * 4 : login failure
	 * 5 : raw eval of javascript code
	 * 6 : login accepted
	 */
	
	/**
	 * Notification of login failure.
	 * 
	 */
	@MethodID(id=4)
	public void loginFailed();
	
	/**
	 * Generic "server error" message.
	 * 
	 * @param message
	 */
	@MethodID(id=2)
	public void showErrorMessage(String message);

	/**
	 * Login accepted.
	 */
	@MethodID(id=6)
	public void loginAccepted();
}
