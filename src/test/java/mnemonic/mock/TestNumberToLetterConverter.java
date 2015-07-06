package mnemonic.mock;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.verify;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertTrue;

import java.util.List;

import mnemonic.Dictionary;
import mnemonic.NumberToLetterConverter;

import org.easymock.Capture;
import org.easymock.EasyMockSupport;
import org.easymock.IAnswer;
import org.easymock.Mock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestNumberToLetterConverter extends EasyMockSupport {

	private static final String FILE = "/DigitToCharacterMap.txt";
	private Dictionary dictionary;
	private NumberToLetterConverter converter;
	/**
	 * for isolated testing
	 */
	@Mock
	private Dictionary mockDictionary;

	@Before
	public void setUp() throws Exception {
		converter = new NumberToLetterConverter(this.getClass().getResource(FILE));
		mockDictionary = createNiceMock(Dictionary.class);
		converter.setDictionary(mockDictionary);
		dictionary = new Dictionary();
		dictionary.load(this.getClass().getResource(FILE));
	}

	@After
	public void tearDown() throws Exception {
	}

	
	/**
	 * Testing of Converter class in isolation of Dictionary
	 * @throws Exception
	 */
	public void testConvertPhoneNumberToLetters() throws Exception {
		loadMockDictionary();
		expect(mockDictionary.getFilterRegExp()).andReturn(dictionary.getFilterRegExp());
		final Capture<Character> myCapture = newCapture();
		expect(mockDictionary.charHasEmptyMapping(capture(myCapture))).andAnswer(
				new IAnswer<Boolean>() {
					@Override
					public Boolean answer() throws Throwable {
						return dictionary
								.charHasEmptyMapping((Character) getCurrentArguments()[0]);
					}

				}).anyTimes();

		replayAll();

		List<String> s = converter.convertPhoneNumberToLetters("2255?63".toCharArray());
		final String filterRegExp = mockDictionary.getFilterRegExp();

		assertTrue(filterRegExp.indexOf("[A-Z]") >= 0);
		assertTrue(s.contains("CALL-ME"));
		verify(mockDictionary);
	}

	/**
	 * helper method to fully load dictionary model
	 */
	private void loadMockDictionary() {
		for (int i = 0; i < Dictionary.ENGLISH_ALPHABET_MAX; i++) {
			for (int j = 0; j < Dictionary.ENGLISH_ALPHABET_MAX; j++) {
				final Character valueOf = dictionary.charAt(i, j);
				expect(mockDictionary.charAt(i, j)).andReturn(valueOf);
			}
		}
	}

}
