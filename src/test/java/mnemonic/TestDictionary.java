package mnemonic;

import static org.junit.Assert.*;

import java.net.URL;

import mnemonic.Dictionary;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit testing for {@link mnemonic.Dictionary}
 * @author Asus i7
 */
public class TestDictionary {

	private Dictionary dictionary;
	private static final String FILE =  "/DigitToCharacterMap.txt";
	
	@Before
	public void setUp() throws Exception {
		dictionary = new Dictionary();
		dictionary.load(this.getClass().getResource(FILE));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCharHadEmptyMapping() {
		assertTrue(dictionary.charHasEmptyMapping(0));
		assertTrue(dictionary.charHasEmptyMapping(1));
		assertFalse(dictionary.charHasEmptyMapping(2));
		assertFalse(dictionary.charHasEmptyMapping(9));
	}
}
