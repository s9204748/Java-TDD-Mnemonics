package mnemonic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Main Phone Number Converter Class. Entry point to application.
 * @author Kevin Flattery
 */
public class NumberToLetterConverter {

	private final static Logger LOGGER = Logger.getLogger(NumberToLetterConverter.class.getName());
	private final static String DEFAULT_DICTIONARY = "/DigitToCharacterMap.txt";
	/**
	 * Upper bound for amount of letters to be mapped to a number (phonepad logic)
	 */
	private static final int MAX_LETTERS = 4;
	/**
	 * @see #convertPhoneNumberToLetters(char[], int, int)
	 */
	private int count;
	/**
	 * Dictionary specific to each runtime instance
	 */
	private Dictionary dictionary = new Dictionary();
	/**
	 * Filtered, holding list for conversion results.
	 */
	private List<String> converstionResults;
	/**
	 * Assigned once {@link #dictionary} has loaded. Typically holding regular expression used to filter results from
	 * permutations according to business rules.
	 */
	private Pattern p;

	/**
	 * Constructor
	 * @param url referencing dictionary file location
	 */
	public NumberToLetterConverter(URL url) throws IOException {
		LOGGER.setLevel(Level.FINE);
		dictionary.load(url);
		p = Pattern.compile(dictionary.getFilterRegExp());
	}

	/**
	 * Constructor
	 * @fileName referencing dictionary file location
	 */
	public NumberToLetterConverter(String dictionaryFile) throws IOException {
		LOGGER.setLevel(Level.FINE);
		dictionary.load(dictionaryFile);
		p = Pattern.compile(dictionary.getFilterRegExp());
	}

	/**
	 * Entry point to application; typically included in JAR manifest.
	 * @param args: <i>optional</i> first arg (index 1) can contain single phone number
	 * @throws IOException if file not found or child <code>MalformedURLException
	 * </code> if any URL references don't resolve.
	 */
	public static void main(String[] args) throws IOException {
		Properties props = System.getProperties();

		List<String> phoneNumbers = readPhoneNumbers(args, props);

		NumberToLetterConverter converter = createConverter(props);

		if (phoneNumbers.isEmpty()) {
			LOGGER.log(Level.WARNING, "no phone numbers available to be processed");
		}

		for (Iterator iterator = phoneNumbers.iterator(); iterator.hasNext();) {
			String phone = (String) iterator.next();
			List<String> list = converter.convertPhoneNumberToLetters(phone.toCharArray());
			LOGGER.log(Level.INFO, "*** For " + phone + ", conversions: " + list.toString());
		}
		LOGGER.log(Level.INFO, "FilterRegExp: " + converter.getDictionary().getFilterRegExp());
	}

	/**
	 * @param props JRE system properties which could contain reference to dictionary file 
	 * @return <code>NumberToLetterConverter<code> instance
	 * @throws IOException if file not found or child <code>MalformedURLException
	 * @throws MalformedURLException if any URL references don't resolve.
	 */
	private static NumberToLetterConverter createConverter(Properties props) throws IOException,
			MalformedURLException {
		URL url;
		NumberToLetterConverter converter = null;
		String dictionary = props.getProperty("dictionary");
		if (dictionary != null) {
			try {
				url = new URL(dictionary);
				converter = new NumberToLetterConverter(url);
			} catch (MalformedURLException e1) {
				LOGGER.log(Level.SEVERE, "Malformed input URL: " + dictionary);
				throw e1;
			}
		} else {
			converter = new NumberToLetterConverter(DEFAULT_DICTIONARY);
		}
		return converter;
	}

	/**
	 * @param args <i>optional</i> first arg (index 1) can contain single phone number
	 * @param props JRE system properties which could contain reference to phone 
	 * number file.
	 * @return <code>List<code> of <String> representation of phone numbers to process
	 * @throws IOException if file not found or child <code>MalformedURLException
	 * @throws MalformedURLException if any URL references don't resolve.
	 */
	private static List<String> readPhoneNumbers(String[] args, Properties props)
			throws IOException, MalformedURLException {
		List<String> phoneNumbers = new ArrayList<String>();
		String phoneNumberFile = props.getProperty("phoneNumberFile");
		if (phoneNumberFile == null) {
			if (args != null && args.length > 1 && args[1] != null) {
				LOGGER.log(Level.INFO, "For number " + args[1]);
				phoneNumbers.add(args[1]);
			}
		} else {
			// parse file & add to phoneNumbers
			// NOTE: Java 7 resource closure, so no need for finally block
			try (BufferedReader br = new BufferedReader(new InputStreamReader(new URL(
					phoneNumberFile).openStream()))) {
				String s;
				while ((s = br.readLine()) != null) {
					phoneNumbers.add(s);
				}
			}
		}
		return phoneNumbers;
	}

	/**
	 * Delegator method with default values specific to this implementation.
	 * The starting index is at the start of the number (index 0).
	 * @see {{@link #convertPhoneNumberToLetters(char phone[], int nArraySize, int nStart)}
	 * @param phone number to convert to letters
	 */
	public List<String> convertPhoneNumberToLetters(char[] phone) {
		// reset when initiating a new conversation; cannot do in delegate due to recursion
		converstionResults = new ArrayList<String>();
		return convertPhoneNumberToLetters(phone, phone.length, 0);
	}

	/**
	 * <b>Recursive</b> method to determine all permutations of available mappings for each digit in phone array.
	 * <code>conversionResults</code> instance variable should be reset for top level entry. <b>Not re-factored into
	 * small blocks due to recursion and complexity</b>
	 * @param phone number to convert to letters
	 * @param nArraySize typically length of phone number
	 * @param nStart which index of #phone as to where to start matching
	 * @return list of String representing all letter combinations
	 */
	private List<String> convertPhoneNumberToLetters(char phone[], int nArraySize, int nStart) {
		if (nStart < nArraySize) {
			char chDigit = phone[nStart];
			if (!Utilities.isPunctuationOrWhitespace(chDigit)) {
				int nDigit = chDigit - '0';
				for (int iLetterCount = 0; iLetterCount < MAX_LETTERS; ++iLetterCount) {
					char chLetter = dictionary.charAt(nDigit, iLetterCount);
					if (chLetter != ' ') {

						// check that previous digit is not a number
						// if previous index valid and not at first index and
						// not a number continue to process conversion
						if (nStart >= 1) {
							// test for previous numerical digit
							int previous = Utilities.parseToDigit(phone[nStart - 1]);
							if ((previous >= 0) && dictionary.charHasEmptyMapping(Character.valueOf(phone[nStart]))) {
								LOGGER.fine("skipped " + String.valueOf(phone)
										+ " as two consecutive numbers [");
								phone[nStart] = chDigit;
								continue;
							}
						}

						phone[nStart] = chLetter;
						convertPhoneNumberToLetters(phone, nArraySize, nStart + 1);
						phone[nStart] = chDigit;
					}
				}
			} else {
				phone[nStart] = '-'; // force current punctuation to dash
				convertPhoneNumberToLetters(phone, nArraySize, nStart + 1);
			}
		} else {
			count++;
			String result = new String(phone).toUpperCase();
			if ((result.indexOf(Utilities.NULL_CHAR) < 0) && (p.matcher(result).matches())) {
				LOGGER.log(Level.FINE, count + " => " + result);
				converstionResults.add(result);
			}
		}
		return converstionResults;
	}

	/**
	 * Accessor for such needs as unit test assertions.
	 * @return instance variable for this class
	 */
	Dictionary getDictionary() {
		return dictionary;
	}
	
	/**
	 * Package access only as this method only for testing class
	 * in isolation. 
	 * @param dictionary
	 */
	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}
}
