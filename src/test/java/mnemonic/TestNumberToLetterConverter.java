package mnemonic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import mnemonic.NumberToLetterConverter;
import mnemonic.Utilities;

import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import org.easymock.TestSubject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Testing {@link mnemonic.NumberToLetterConverter}
 * @author Kevin Flattery
 */
public class TestNumberToLetterConverter extends EasyMockSupport {

	private static final String FILE = "/DigitToCharacterMap.txt";
	private final static Logger LOGGER = Logger.getLogger(TestNumberToLetterConverter.class
			.getName());	
	private NumberToLetterConverter converter;

	@Before
	public void setUp() throws Exception {
		converter = new NumberToLetterConverter(this.getClass().getResource(FILE));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLoadMap() throws IOException {
		assertEquals(converter.getDictionary().charAt(2, 0), '2');
		assertEquals(converter.getDictionary().charAt(2, 1), 'a');
	}

	@Test
	public void testMultipleUnchangedNumbers() throws Exception {
		List<String> s = converter.convertPhoneNumberToLetters("22055.631".toCharArray());
		assertTrue(s.contains("AA0JJ-MD1"));
	}

	@Test
	public void testSingleDigitLeftIfNoMatch() throws Exception {
		List<String> s = converter.convertPhoneNumberToLetters("1333.30".toCharArray());
		assertTrue(!s.isEmpty());
		char digit = s.get(0).charAt(0);
		LOGGER.info("digit: " + digit);
		assertEquals(Integer.valueOf("1"), Integer.valueOf(Utilities.parseToDigit(digit)));
	}

	@Test
	public void testSkipOverNumberIfTwoConsecutiveDigitsRemainUnchanged() throws Exception {
		List<String> s = converter.convertPhoneNumberToLetters("1111.00".toCharArray());
		assertTrue("list should be empty", s.isEmpty());
		s = converter.convertPhoneNumberToLetters("1022-99".toCharArray());
		assertTrue("list should be empty", s.isEmpty());
	}

	@Test
	public void testPunctuationIgnoredInDictionary() throws Exception {
		List<String> s = converter.convertPhoneNumberToLetters("2255?63".toCharArray());
		assertTrue(s.contains("CALL-ME"));
	}

	@Test
	public void testWhitespaceIgnoredInDictionary() throws Exception {
		List<String> s = converter.convertPhoneNumberToLetters("2255 63".toCharArray());
		assertTrue(s.contains("CALL-ME"));
	}

	@Test
	public void testConverterIgnoresCaseInDictionary() throws Exception {
		assertEquals('a', converter.getDictionary().charAt(2, 1));
		List<String> s = converter.convertPhoneNumberToLetters("2255.63".toCharArray());
		assertTrue(s.contains("CALL-ME"));
	}

}
