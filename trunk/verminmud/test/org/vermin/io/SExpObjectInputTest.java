package org.vermin.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.vermin.io.Executable.Dynvars;

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
	
	@Test
	public void testWith() throws Exception {
		Container c = (Container) in("(with (object \"org.vermin.io.SExpObjectInputTest$Container\")"+
				" (=> addItem (object \"org.vermin.io.SExpObjectInputTest$Item\" (=! value \"foobar\"))) )");
		assertNotNull(c);
	}
	
	@Test
	public void testAugmentedFunction() throws Exception {
		AugmentedObjectInput aoi = new AugmentedObjectInput(new StringReader("(with (make-it-so \"Picard\") (=> engage \"Enterprise\"))"));
		ScifiReference sr = (ScifiReference) aoi.deserialize();
		assertEquals("Captain Picard commanding the Enterprise", sr.toString());
	}
	
	class ScifiReference {
		private String captain;
		private String vessel;
		ScifiReference(String c) {
			this.captain = c;
		}
		public void engage(String v) {
			this.vessel = v;
		}
		public String toString() {
			return "Captain "+captain+" commanding the "+vessel;
		}
	}
	
	class AugmentedObjectInput extends SExpObjectInput {

		public AugmentedObjectInput(Reader in) {
			super(in);
		}

		@Override
		protected MethodInfo getAugmentedFunction(String name) {
			if(name.equals("make-it-so")) {
				try {
					return new MethodInfo(false, this.getClass().getDeclaredMethod("makeItSo", List.class, Dynvars.class));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			return super.getAugmentedFunction(name);
		}
		
		public ScifiReference makeItSo(List args, Executable.Dynvars d) {
			String captain = args.get(0).toString();
			return new ScifiReference(captain);
		}
		
	}
}
