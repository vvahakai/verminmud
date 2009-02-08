package org.vermin.mudlib;

import static org.junit.Assert.*;
import org.junit.Test;


public class DiceTest {

	@Test(expected=IllegalArgumentException.class)
	public void testRandomElementFromNull() {
		Dice.randomElement((Object[])null);
	}
	
	@Test
	public void testRandomElement() {
		Long[] elements = { 1l, 2l, 4l, 8l, 16l };
		for(int i=0; i<100; i++) {
			long random = Dice.randomElement(elements);
			assertTrue(random > 0);
			assertTrue(random < 17);
		}
	}
	
	@Test
	public void testZeroForNegative() {
		assertTrue(Dice.random(-1) == 0);
	}
	

}
