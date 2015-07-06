package mnemonic;

import static org.junit.Assert.*;

import mnemonic.Utilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit testing for {@link mnemonic.Utilities}
 * @author Asus i7
 */
public class TestUtilties {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testNullArrayLength() {
		char[] array = {'1', '2', '\u0000'};
		assertEquals(2, Utilities.nullArrayLength(array));		
	}

}
