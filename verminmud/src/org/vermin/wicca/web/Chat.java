package org.vermin.wicca.web;

public class Chat extends AbstractWebComponent implements org.vermin.wicca.remote.Chat {
	
	public Chat(WebClientOutput c) { super(c); }
	
	public void tell(String source, String dest, String text) {
		write((short)1, (byte)2, source, dest, text); 
	}

	public void message(String who, String channel, String text) {
		write((short)1, (byte)2, who, channel, text);
	}

	
}
