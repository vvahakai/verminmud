package org.vermin.io;

import java.io.StringReader;

import org.junit.Test;
import static org.junit.Assert.*;

public class SExpObjectInputTest {

	public static class CallTest {
		private int value;
		private int result;
		int multiplications = 0;
		String message;
		
		public int multiplyBy(int otherValue) {
			if(multiplications==0) {
				System.out.println("Multiply "+value+" by "+otherValue);
				multiplications++;
				return value * otherValue;	
			}
			System.out.println("called more");
			try {
				throw new RuntimeException("you called me more than once!");
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
			return 0;
		}
		
		public void greet(String[] people) {
			StringBuilder sb = new StringBuilder("Hello");
			boolean first = true;
			for(String person : people) {
				sb.append(first?" ":", ").append(person);
				first=false;
			}
			this.message = sb.toString();
		}
	}
	private Object in(String data) throws Exception {
		return new SExpObjectInput(new StringReader(data)).deserialize();
	}
	
	@Test
	public void testCall() throws Exception {
		CallTest ct = (CallTest) in(
				"(object \"org.vermin.io.SExpObjectInputTest$CallTest\" "+
				"  (field value 17) "+
				"  (=> greet [\"Erno\" \"Pentti\" \"Tatu\"])"+
				"  (field result (=> multiplyBy 10)))");
		assertEquals(17, ct.value);
		assertEquals(170, ct.result);
		assertEquals("Hello Erno, Pentti, Tatu", ct.message);
	}
}
