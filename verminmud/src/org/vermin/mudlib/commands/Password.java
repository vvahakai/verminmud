package org.vermin.mudlib.commands;

import org.vermin.mudlib.*;
import org.vermin.wicca.ClientInputHandler;
import org.vermin.wicca.ClientOutput;
import org.vermin.driver.*;

public class Password implements Command {

	
	public boolean action(Living who, String command) {
		if(!(who instanceof Player)) {
			return false;
		}
		Player actor = (Player) who;		
		AuthenticationProvider a = Driver.getInstance().getAuthenticator();
		
		ClientOutput out = actor.getClientOutput();
		
		new PasswordChange(actor);
        return false;
	}

	private static class PasswordChange implements ClientInputHandler {
		private AuthenticationProvider auth;
		private Player who;
		private ClientOutput out;
		private int state=0;
		private String newpw, confirm;

		public PasswordChange(Player who) {
			this.who = who;
			out = who.getClientOutput();
			auth = Driver.getInstance().getAuthenticator();

			out.changeHandler(this);
			out.hideInput();
		}

		public void setClientOutput(ClientOutput out) {
			this.out = out;
		}

		public void prompt() {
			if(state == 0) {
				out.prompt("Current password: ");
				out.hideInput();
			} else if(state == 1) {
				out.prompt("New password: ");
			} else if(state == 2) {
				out.prompt("Confirm password: ");
			}
		}

		public boolean clientCommand(String cmd) {
			if(state == 0) {
				if(auth.authenticate(who.getName(), cmd) != null)
					state++;
				else {
					who.notice("Incorrect password.");
					end();
				}
			} else if(state == 1) {
				newpw = cmd;
				state++;
			} else if(state == 2) {
				confirm = cmd;
					
				if(!newpw.equals(confirm)) {
					who.notice("The passwords don't match.");
				} else {
					try {

					    auth.changePassword(who.getName(), cmd);
						who.notice("Your password has been changed.");
					} catch(Exception e) {
						who.notice("Error changind password ("+e.getMessage()+"). Please report this bug.");
					}
				}
				
				end();
			}
			return true;
		}

		private void end() {
			out.changeHandler(who);
			out.showInput();
		}

        /* (non-Javadoc)
         * @see org.vermin.driver.ClientInputHandler#startSession()
         */
        public void startSession() {}
        public void endSession() {}
	}
}
