package org.vermin.io;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import jscheme.JScheme;

public class SExpTokenizer {
	private static JScheme js = new JScheme();
	
	public static final Object SEXP_START           = "(";
	public static final Object SEXP_END             = ")";
	public static final Object SEQUENCE_START       = "[";
	public static final Object SEQUENCE_END         = "]";
	public static final Object DICTIONARY_START     = "{";
	public static final Object DICTIONARY_END       = "}";
	public static final Object DICTIONARY_SEPARATOR = ":"; // separates dictionary key and value
	public static final Object STREAM_END  = new Object();
	
	private Reader in;
	private Object token; // last token
	private int readPositionLine=0, readPositionColumn=0;
	
	public SExpTokenizer(Reader in) {
		this.in = in;
	}
	
	private int read(boolean eofOk) throws IOException {
		int ch = in.read();
		if(!eofOk && ch == -1)
			throw new IOException("Unexpected end of stream.");
		
		if(ch == '\n') {
			readPositionLine++;
			readPositionColumn = 0;
		} else {
			readPositionColumn++;
		}
		return ch;
	}
		
	public int getCurrentLine() {
		return readPositionLine;
	}
	public int getCurrentColumn() {
		return readPositionColumn;
	}
	
	/**
	 * Returns the next SExp token from the stream. 
	 * 
	 * @return the next token
	 */
	public Object nextToken() throws IOException {
		
		if(token != null) {
			Object ret = token;
			token = null;
			return ret;
		}
		
		StringBuilder sb = new StringBuilder();
		while(true) {
			int ch = read(true);
			
			if(ch == '\"')
				return readQuotedString();
			
			if(ch == '#')
				return readFixedLengthStringOrDispatch();
			//return readFixedLengthString();
			
			if(ch == ';') { // comment
				ch = read(false);
				while(ch != '\n' && ch != -1) {
					ch = read(false);
				}
				continue;
			}
			
			switch(ch) {
			case '(':
				if(sb.length() > 0) {
					token = SEXP_START;
					return nullOrString(sb);
				} else {
					return SEXP_START;
				}
				
			case ')':
				if(sb.length() > 0) {
					token = SEXP_END;
					return nullOrString(sb);
				} else {
					return SEXP_END;
				}
				
			case '[':
				if(sb.length() > 0) {
					token = SEQUENCE_START;
					return nullOrString(sb);
				} else {
					return SEQUENCE_START;
				}
				
			case ']':
				if(sb.length() > 0) {
					token = SEQUENCE_END;
					return nullOrString(sb);
				} else {
					return SEQUENCE_END;
				}
				
			case '{':
				if(sb.length() > 0) {
					token = DICTIONARY_START;
					return nullOrString(sb);
				} else {
					return DICTIONARY_START;
				}
				
			case '}':
				if(sb.length() > 0) {
					token = DICTIONARY_END;
					return nullOrString(sb);
				} else {
					return DICTIONARY_END;
				}
				
			case ':':
				if(sb.length() > 0) {
					token = DICTIONARY_SEPARATOR;
					return nullOrString(sb);
				} else {
					return DICTIONARY_SEPARATOR;
				}
				
			case -1:
				if(sb.length() > 0) {
					token = STREAM_END;
					return nullOrString(sb);
				} else {
					return STREAM_END;
				}
				
			default:
				if(Character.isWhitespace((char) ch)) {
					if(sb.length() > 0) {
						return nullOrString(sb);
					}
				} else {
					sb.append((char) ch);
				}
			
			}
		}
	}
	
	private String readFixedLengthString(int initial) throws IOException {
		StringBuilder sb = new StringBuilder();
		int len = initial;
		int ch;
		
		// read string length
		while((ch = read(false)) != ':') {
			len = 10*len + Character.digit(ch, 10);
		}
		
		for(int i=0; i<len; i++)
			sb.append((char) read(false));
		
		return sb.toString();
	}
	
	/* Read a string until "|#" is encountered */
	private String readDelimitedString() throws IOException {
		StringBuffer str = new StringBuffer();
		int last = read(false);
		
		while(true) {
			
			int ch = read(false);
			if(ch == '#' && last == '|')
				break;
			
			str.append((char) last);
			last = ch;
		}
		
		return str.toString();
		
	}
	
	/* Read a string until '"' is encountered. */
	private String readQuotedString() throws IOException {
		StringBuilder sb = new StringBuilder();
		int ch;
		while((ch = read(false)) != '\"') {
			if(ch == -1)
				throw new RuntimeException("Unterminated string literal.");
			sb.append((char) ch);
		}
		return sb.toString();
	}
	
	private Object readFixedLengthStringOrDispatch() throws IOException {
		
		int ch = read(false);
		
		if(Character.isDigit(ch)) {
			return readFixedLengthString(Character.digit(ch, 10));
		} else if(ch == '<') {
			
			return readScheme();
		} else if(ch == '|') {
			// read #| ...stuff... |# delimited string
			return readDelimitedString();
		} else {
			throw new RuntimeException("Unrecognized dispath character: "+(char) ch);
		}
		
	}
	/* Read scheme code until ">#" is encountered and evaluate it */
	private Object readScheme() throws IOException {
		
		StringBuffer code = new StringBuffer();
		int last = read(false);
		
		while(true) {
			
			int ch = read(false);
			if(ch == '#' && last == '>')
				break;
			
			code.append((char) last);
			last = ch;
		}
		
		return js.eval(code.toString());
		
	}
	
	public void close() throws IOException {
		in.close();
	}
	
	private String nullOrString(StringBuilder sb) {
		String str = sb.toString();
		if(str.equals("null"))
			return null;
		return str;
	}
	
	public static void main(String[] args) {
		try {
			SExpTokenizer st = new SExpTokenizer(new StringReader("; "));
			System.out.println(st.nextToken());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
