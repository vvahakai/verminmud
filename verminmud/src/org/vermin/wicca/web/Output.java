package org.vermin.wicca.web;

public class Output extends AbstractWebComponent implements
		org.vermin.wicca.remote.Output {

	public Output(WebClientOutput wco) { super(wco); }
	
	public void put(String msg) {
		write((short)2,(byte)1, msg);
	}
	public void showLink(String title, String url) {
		write((short)2, (byte)2, title, url);
	}
}
