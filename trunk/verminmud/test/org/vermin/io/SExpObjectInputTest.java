package org.vermin.io;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;

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
	
	public static class SomePrimitives {
		private int theInteger;
		private long theLong;
		private boolean theBoolean;
		private byte theByte;
		private double theDouble;
	}
	
	public static class Item {
		private String value;
		public void withValue(String v) {
			this.value = "|"+v+"|";
		}
	}
	public static class Container {
		ArrayList<Item> items = new ArrayList<Item>();
		public void addItem(Item i) {
			items.add(i);
		}
		public void addItems(Collection<Item> items) {
			this.items.addAll(items);
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
	
	@Test
	public void testOldStylePrimitives() throws Exception {
		SomePrimitives sp = (SomePrimitives) in("(object \"org.vermin.io.SExpObjectInputTest$SomePrimitives\""+
				" (field theInteger (int 77)) "+
				" (field theLong (long 112)) "+
				" (field theBoolean (boolean true)) "+
				" (field theByte (byte 100)) "+
				" (field theDouble (double 10.7)))");
		assertEquals(77, sp.theInteger);
		assertEquals(112, sp.theLong);
		assertTrue(sp.theBoolean);
		assertEquals(100, sp.theByte);
		assertTrue(sp.theDouble > 10.6);
		assertTrue(sp.theDouble < 10.8);
	}
	
	@Test 
	public void testNewStylePrimitives() throws Exception {
		SomePrimitives sp = (SomePrimitives) in("(object \"org.vermin.io.SExpObjectInputTest$SomePrimitives\""+
				" (=! theInteger 42) (=! theLong 911) (=! theBoolean false) (=! theByte 64) (=! theDouble 3.1415) )");
		assertEquals(42, sp.theInteger);
		assertEquals(911, sp.theLong);
		assertFalse(sp.theBoolean);
		assertEquals(64, sp.theByte);
		assertTrue(sp.theDouble > 3.1);
		assertTrue(sp.theDouble < 3.2);
	}
	
	@Test
	public void testCallWithComplexParameter() throws Exception {
		Container c = (Container) in("(object \"org.vermin.io.SExpObjectInputTest$Container\""+
				" (=> addItem (object \"org.vermin.io.SExpObjectInputTest$Item\" (=! value \"first\"))) "+
				" (=> addItems [(object \"org.vermin.io.SExpObjectInputTest$Item\" (=> withValue \"second\"))" +
				"               (object \"org.vermin.io.SExpObjectInputTest$Item\" (=! value \"third\"))]) )");
		assertEquals(3, c.items.size());
		assertEquals("first", c.items.get(0).value);
		assertEquals("|second|", c.items.get(1).value);
		assertEquals("third", c.items.get(2).value);
		
	}
	
	@Test
	public void testCallStar() throws Exception {
		Container c = (Container) in("(object \"org.vermin.io.SExpObjectInputTest$Container\""+
				" (=>* addItem ((object \"org.vermin.io.SExpObjectInputTest$Item\" (=! value \"first\"))) "+
				"              ((object \"org.vermin.io.SExpObjectInputTest$Item\" (=! value \"second\")))) )");
		assertEquals(2, c.items.size());
		assertEquals("first", c.items.get(0).value);
		assertEquals("second", c.items.get(1).value);
		
	}
	
	@Test(expected=IllegalStateException.class)
	public void testCallingNopFails() throws Exception {
		/* If the first position is not a callable function but a list, 
		 * the call must fail. NOP cannot be executed.
		 */
		in("((object \"org.vermin.io.SExpObjectInputTest$Container\") 1 2)");
	}
}
