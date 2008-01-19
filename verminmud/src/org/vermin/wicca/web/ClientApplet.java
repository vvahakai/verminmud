/*
 * Created on 2.7.2005
 */
package org.vermin.wicca.web;

import java.applet.Applet;
import java.io.IOException;
import java.net.Socket;
import netscape.javascript.JSObject;

public class ClientApplet extends Applet {
	
	private JSObject jsObject;
		
	private ConnectionHandler connection;
	
	private class ConnectionHandler extends Connection {
		public ConnectionHandler(Socket s) throws IOException {
			super(s);
		}
		protected void handleMessage(short component, byte method, String data) {
	
			//System.out.println("\n\n-----------------");
			//System.out.println("comp: "+component+", method: "+method+" ---> DATA: "+data);
			int count = Integer.parseInt(data.substring(0, data.indexOf(':')));
			
			Object[] args = new String[count];
			String rest = data.substring(data.indexOf(':')+1);
			
			for(int i=0;i<count;i++) {
				int pos = rest.indexOf(':');
				int len = Integer.parseInt(rest.substring(0, pos));
				args[i] = rest.substring(pos+1, pos+1+len);
				rest = rest.substring(pos+len+1);
			}
			
			
			
			System.out.println("retrieving wicca JSObject");
			JSObject wicca = (JSObject) jsObject.getMember("wicca");
			System.out.println("member 'wicca': "+wicca);
			
			JSObject compDesc = (JSObject) wicca.getSlot((int)component);
			
			System.out.println("component "+component+": "+compDesc);
			
			JSObject realComponent = (JSObject) compDesc.getSlot(0);
			String compMethod = (String) compDesc.getSlot(method+1);
			System.out.println("realComponent: "+realComponent);
			System.out.println("method: "+compMethod);
			realComponent.call(compMethod, args);
			
		}
	};
	
	public void init() {
		try {
			super.init();
			int port = Integer.parseInt(getParameter("port"));
			
			connection = new ConnectionHandler(new Socket("vermin.game-host.org", port));
			new Thread(connection).start();
			
			try {
				jsObject = JSObject.getWindow(this);
				// jsObject.eval("alert('Uusi versio');");
			} catch(Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		} catch(Exception e) {
			System.out.println("exception in applet init: "+e.getMessage());
		}
	}
	
	public void put(String msg) {
		connection.write((short)0,(byte)1, msg);
	}
	public void login(String name, String password) {
		connection.write((short)0,(byte)0, name+":"+password);
	}
}
