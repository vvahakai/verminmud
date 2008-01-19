package org.vermin.driver;

import org.apache.xmlrpc.WebServer;

public class XmlRpcService implements Service {

	private WebServer ws;
	private int port;

	public XmlRpcService(int port) {
		this.port = port;
	}

	public void startService() {
		if(ws != null)
			throw new ServiceException(this, "The service is already running.");

		ws = new WebServer(port);
		ws.start();
	}

	public void stopService() throws ServiceException {
		if(ws == null)
			throw new ServiceException(this, "The service isn't running.");

		ws.shutdown();
		ws = null;
	}

	public void addHandler(String name, Object handler) {
		if(ws == null)
			throw new ServiceException(this, "Unable to add handler, the service is not running.");
		ws.addHandler(name, handler);
	}

	public void removeHandler(String name) {
		if(ws == null)
			throw new ServiceException(this, "Unable to remove handler, the service is not running.");

		ws.removeHandler(name);
	}

	public String getName() {
		return "XML-RPC";
	}

	public boolean isActive() {
		return ws != null;
	}

}
