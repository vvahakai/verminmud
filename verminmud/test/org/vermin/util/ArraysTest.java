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
	
	@Test
	public void testReverseMap() {
		String[] map = Arrays.reverseCopy(new String[] { "111111", "222222", "333333", "444444", "555555", "666666" });
		assertEquals(6, map.length);
		assertEquals("666666", map[0]);
		assertEquals("555555", map[1]);
		assertEquals("444444", map[2]);
		assertEquals("333333", map[3]);
		assertEquals("222222", map[4]);
		assertEquals("111111", map[5]);
		
		// test contains method also
		assertTrue(Arrays.contains(map, "444444"));
		assertFalse(Arrays.contains(map, "123123"));
		
	}
}
