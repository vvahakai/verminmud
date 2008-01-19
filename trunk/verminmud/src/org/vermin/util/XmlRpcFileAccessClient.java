package org.vermin.util;

import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.xmlrpc.*;


public class XmlRpcFileAccessClient {

	private XmlRpcClient client;

	public XmlRpcFileAccessClient(String url, String user, String password) {
		try {
			client = new XmlRpcClient(url);
		} catch(MalformedURLException mue) {
			throw new RuntimeException("Malformed URL: "+url);
		}
		client.setBasicAuthentication(user, password);
	}

	public InputStream getFile(String id) {

		Vector params = new Vector();
		params.add(id);

		try {
			Object retval = client.execute("files.getFile", params);
			if(retval instanceof byte[])
				return new ByteArrayInputStream((byte[]) retval);
		} catch(XmlRpcException xre) {
			throw new RuntimeException(xre.getMessage());
		} catch(IOException ioe) {
			throw new RuntimeException("IOException: "+ioe.getMessage());
		}
		return null;
	}

	public String putFile(String id, File file) {
		try {
			FileInputStream in = new FileInputStream(file);
			byte[] data = new byte[(int) file.length()];
			in.read(data, 0, (int) file.length());
			
			return putFile(id, data);
		} catch(IOException ioe) {
			throw new RuntimeException("IOException: "+ioe.getMessage());
		}
	}
	
	public String putFile(String id, byte[] data) {
		try {
			Vector params = new Vector();
			params.add(id);
			params.add(data);

			Object retval = client.execute("files.putFile", params);
			if(retval instanceof String)
				return (String) retval;
		} catch(XmlRpcException xre) {
			throw new RuntimeException(xre.getMessage());
		} catch(IOException ioe) {
			throw new RuntimeException("IOException: "+ioe.getMessage());
		}
		return null;
	}

	public Vector listFiles() {
		try {
			Object retval = client.execute("files.listFiles", new Vector());
			return (Vector) retval;
		} catch(XmlRpcException xre) {
			throw new RuntimeException(xre.getMessage());
		} catch(IOException ioe) {
			throw new RuntimeException(ioe.getMessage());
		}
	}

	public static void main(String[] args) throws Exception {
		XmlRpcFileAccessClient client = new XmlRpcFileAccessClient(args[0], args[1], args[2]);

		if(args[3].equalsIgnoreCase("put")) {
			System.out.println("files.putFile "+args[4]+": "+client.putFile(args[4], new File(args[5])));
		} else if(args[3].equalsIgnoreCase("get")) {
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getFile(args[4])));
			System.out.println("files.getFile "+args[4]+": "+in.readLine());
		} else if(args[3].equalsIgnoreCase("list")) {
			System.out.println("files.listFiles:");
			Vector files = client.listFiles();
			for(int i=0; i<files.size(); i++)
				System.out.println(files.get(i));
			
		} else {
			System.out.println("specify put / get / list method");
		}
	}
	
}
