package org.vermin.io.brpc;

import java.io.*;
import java.util.*;

/**
 * Decode bencoded objects from an InputStream.
 * 
 * @param Tatu Tarvainen
 */
public class BDecoder {

    private PeekReader in;
    
    public BDecoder(InputStream source) {
        in = new PeekReader(source);
    }
        
	private static class PeekReader {
		private static int NOTHING = -2;
		InputStream in;
		private int ch = NOTHING;
        
        PeekReader(InputStream in) {
            this.in = in;
        }
        
		int read() throws IOException {
			if(ch != NOTHING) {
				int retval = ch;
				ch = NOTHING;
				return retval;
			} else {
				return in.read();
			}
		}
		int peek() throws IOException {
			if(ch != NOTHING) {
				return ch;
			} else {
				ch = in.read();
				return ch;
			}
		}
	}

	/**
	 * Decode a bencoded object from the given Reader.
	 *
	 * @param in the Reader to decode from
	 * @return the decoded object
	 */
	public Object decode() throws IOException {
		return doDecode();
	}

	private Object doDecode() throws IOException {
		int ch = in.peek();
		
		switch(ch) {

		  case 'i': return decodeInt();
		  case 'l': return decodeList();
		  case 'd': return decodeDictionary();
		  case '1': case '2': case '3':
		  case '4': case '5': case '6':
		  case '7': case '8': case '9':
			  return decodeString();

		  default:
			  throw new RuntimeException("Invalid input '"+ch+"'.");
		}

	}

	private Integer decodeInt() throws IOException {
		in.read(); // discard the first 'i' char
		int luku = 0;
		int ch=in.read();
		while(ch != 'e') { // FIXME: what about -1 (EOF)... wrap in.read
			luku = (luku*10) + Character.digit((char) ch, 10);
			ch = in.read();
		}

		return Integer.valueOf(luku);
	}
	
    
	private Vector<Object> decodeList() throws IOException  {
		in.read();
		Vector<Object> l = new Vector<Object>();
		while(in.peek() != 'e') {
			l.addElement(doDecode());
		}
        in.read(); // read the final 'e'
		return l;
	}

	private Hashtable<String, Object> decodeDictionary() throws IOException {
		in.read();
		Hashtable<String, Object> m = new Hashtable<String, Object>();
		while(in.peek() != 'e') {
			m.put(decodeString(), doDecode());
		}
        in.read(); // read the final 'e'
		return m;
	}

	private String decodeString() throws IOException  {

		int len = 0;
		int ch = in.read();
		while(ch != ':') {
			len = (len*10) + Character.digit((char) ch, 10);
			ch = in.read();
		}

		StringBuffer sb = new StringBuffer(len);
		for(int i=0; i<len; i++)
			sb.append((char) in.read());

		return sb.toString();
	}

    public void close() throws IOException {
        in.in.close();
    }
}
