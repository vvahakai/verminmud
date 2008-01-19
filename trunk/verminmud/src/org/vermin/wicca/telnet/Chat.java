package org.vermin.wicca.telnet;

public class Chat extends AbstractTelnetComponent implements org.vermin.wicca.remote.Chat {

	public Chat(TelnetClientOutput co) {
		super(co);
	}
	
	public void message(String who, String channel, String text) {
		StringBuilder m = new StringBuilder();
		m.append(who);
		m.append(" [");
		m.append(channel);
		m.append("]: ");
		m.append(text);
		
		getClientOutput().put(m.toString());
	}

	public void tell(String source, String dest, String text) {
		if(dest.equalsIgnoreCase(getPlayer()))
			getClientOutput().put(source +" tells you '&B;"+text+"&;'.");
		else if(source.equalsIgnoreCase(getPlayer()))
			getClientOutput().put("You tell "+dest+" '&B;"+text+"&;'.");
		else
			getClientOutput().put("BUG in tell command: "+source+" --> "+dest+" not meant for you! (you are: '"+getPlayer()+"')");
		
	}
}
