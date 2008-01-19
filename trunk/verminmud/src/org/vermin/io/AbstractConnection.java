package org.vermin.io;

import java.io.*;

public abstract class AbstractConnection implements ClientConnection {


	protected PrintStream output;

	protected AbstractConnection(PrintStream output) {
		this.output = output;
	}

	public void print(String str) {
		output.print(str);
	}

	public void print(int ch) {
		output.write((int) ch);
	}

	public void write(int ch) {
		output.write(ch);
	}

	public void flush() {
		output.flush();
	}
	
	public void println(String str) {
		print(str+"\r\n");
	}

}
