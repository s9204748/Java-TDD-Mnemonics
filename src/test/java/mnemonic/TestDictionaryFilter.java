package mnemonic;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import mnemonic.Dictionary;
import mnemonic.ResultsFilter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit testing for {@link mnemonic.ResultsFilter}
 * @author Asus i7
 */
public class TestDictionaryFilter {

	private ResultsFilter rules = new ResultsFilter();
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
	public void testGetValuesAsRegExp() {		
		List<Integer> dictionaryNumbers = new ArrayList<Integer>();
		dictionaryNumbers.add(new Integer(0));
		String s = rules.getValuesAsRegExp(false, dictionaryNumbers);
		assertTrue(s.indexOf("[0]?")>=0);
		dictionaryNumbers.clear();
		dictionaryNumbers.add(new Integer(2));
		s = rules.getValuesAsRegExp(true, dictionaryNumbers);
		assertTrue(s.indexOf("[^2]*")>=0);	
		dictionaryNumbers.add(new Integer(3));
		s = rules.getValuesAsRegExp(true, dictionaryNumbers);
		assertTrue(s.indexOf("[^23]*")>=0);	
	}
	
	@Test
	public void testFilterRegExp() throws Exception {
		List<String> list = new ArrayList<String>();
		Pattern p = Pattern.compile(dictionary.getFilterRegExp());
		// assert first and second are filtered as 2 is mapped in test dictionary
		assertTrue(p.matcher("D0-ABC").matches());
		assertTrue(p.matcher("DABC").matches());
		assertFalse(p.matcher("2255-N3").matches());;
	}
}
