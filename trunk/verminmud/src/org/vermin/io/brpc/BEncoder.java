package org.vermin.io.brpc;

import java.io.*;
import java.util.*;

/**
 * Encode objects to an OutputStream.
 * Bencoding is a simple binary encoding format.
 *
 * The following object types can be bencoded:
 * <ul>
 *  <li>Strings</li>
 *  <li>Integers</li>
 *  <li>Vectors</li>
 *  <li>Hashtables</li>
 * </ul>
 * Values in vectors and hashtables can be of any type,
 * provided that the type can be encoded itself.
 * 
 * Hashtable keys must be strings.
 *
 * @author Tatu Tarvainen
 */
public class BEncoder {

    private OutputStream out;
    
    public BEncoder(OutputStream out) {
        this.out = out;
    }
    
	/**
	 * Encode an object to the given Writer.
	 *
	 * @param out the Writer to encode to
	 * @param obj the object to encode
	 */
	public void encode(Object obj) throws IOException {
		if(obj instanceof String)
			encodeString((String) obj);
		else if(obj instanceof Integer)
			encodeInt((Integer) obj);
		else if(obj instanceof List)
			encodeList((List<Object>) obj);
		else if(obj instanceof Map)
			encodeDictionary((Map<String,Object>) obj);
        out.flush();
	}

	public void encodeInt(Integer i) throws IOException {
		out.write('i');
		out.write(i.toString().getBytes());
		out.write('e');
	}

	public void encodeList(List<Object> l) throws IOException {
		out.write('l');
		for(Object o : l) encode(o);
		out.write('e');
	}

	public void encodeDictionary(Map<String,Object> m) throws IOException {
		out.write('d');
		for(Map.Entry<String,Object> entry : m.entrySet()) {
			encodeString(entry.getKey());
			encode(entry.getValue());
		}
		out.write('e');
	}

	public void encodeString(String str) throws IOException {
		out.write(Integer.toString(str.length()).getBytes());
		out.write(':');
		out.write(str.getBytes());
	}

    public void flush() throws IOException {
        out.flush();
    }
    
    public void close() throws IOException {
        flush();
        out.close();
    }
    
}
