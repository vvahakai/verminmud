package org.vermin.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for Print utilities.
 * 
 * @author Tatu Tarvainen
 */
public class PrintTest {

	@Test
	public void testHumanReadable() {
		assertEquals("12k 345", Print.humanReadable(12345));
		
		// FIXME: please evaluate humanReadable, is that output ok? I think it is not pretty
		assertEquals("42G 007M 000k 069", Print.humanReadable(42007000069l));
	}
	
	@Test
	public void testColumnize() {
		String[] left = new String[] { 
				"  X  ",
				" XXX ",
				"X---X",
				"|XXX|",
		};
		
		String[] right = new String[] { 
				"A reasonably short desc.", 
				"This is the long description that should be split to to multiple lines and formatted alongside the left column"+
				" and taking as many rows as necessary" 
		};
		
		String[] lines = Print.columnize(left, 6, right, 34);
		assertNotNull(lines);
		assertEquals(6, lines.length);
		assertTrue(lines[5].startsWith("      ")); // at this point the left column is empty
		for(String line : lines) System.out.println("LINE: "+line);
	}
}
