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
		assertTrue(lines[5].startsWith("&;     ")); // at this point the left column is empty
		for(String line : lines) System.out.println("LINE: "+line);
	}
	
	@Test
	public void testPrintableLength() {
		assertEquals(6, Print.printableLength("&B2;12&3;3456&;")); // 123456 without control codes
	}
	
	@Test
	public void testColumnizeColors() {
		/* test that columnize does the right thing and does
		 * not include color control codes in width calculations.
		 */
		String[] left = new String[] {
				"&2;XX&B;YYYY&;" // length is 6 (XXYYYY without the controls)
		};
		String[] right = new String [] {
				"This is some nice &4;colored&; text" // length is 30 without controls 
		};
		String[] lines = Print.columnize(left, 7, right, 20);
		assertEquals(2, lines.length);
		assertTrue("left column of second line is 'empty'", lines[1].startsWith("&;       "));
		
	}
}
