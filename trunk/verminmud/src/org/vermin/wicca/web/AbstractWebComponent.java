package org.vermin.wicca.web;

public abstract class AbstractWebComponent {

	private WebClientOutput output;
	
	public AbstractWebComponent(WebClientOutput output) {
		this.output = output;
	}
	
	protected void write(short component, byte method, String...strings) {
		
		output.write(component, method, makeWiccaStringList(strings));
	}
	
	private String makeWiccaStringList(String...strings) {
	
		StringBuilder sb = new StringBuilder();
		
		sb.append(Integer.toString(strings.length));
		sb.append(":");
		
		for(String s : strings) {
			sb.append(Integer.toString(s.length()));
			sb.append(":");
			sb.append(s);
		}
		return sb.toString();
	}
	
}
