package org.vermin.util;

import static org.junit.Assert.*;
import org.junit.Test;


public class ArraysTest {

	@Test
	public void testReverse() {
		String[] strings = new String[] { "first", "second", "third" };
		Arrays.reverse(strings);
		assertEquals("third", strings[0]);
		assertEquals("second", strings[1]);
		assertEquals("first", strings[2]);
	}
	
	@Test
	public void testReverseCopy() {
		String[] originalStrings = new String[] { "foo", "bar", "baz", "quux" };
		String[] reversed = Arrays.reverseCopy(originalStrings);
		assertNotNull(reversed);
		assertEquals(4, reversed.length);
		assertNotSame(originalStrings, reversed);
		assertEquals("quux", reversed[0]);
		assertEquals("foo", reversed[3]);
	}
}
