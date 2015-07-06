package mnemonic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to load Phone Number to Letter translation dictionary.
 * @author Kevin Flattery
 */
public class Dictionary {

	private final static Logger LOGGER = Logger.getLogger(Dictionary.class.getName());
	/**
	 * upper bound on dictionary character array (for English)
	 */
	public static final int ENGLISH_ALPHABET_MAX = 26;
	/**
	 * Encapsulated 2D array of primitives representing directory model
	 */
	private char[][] dictionary = new char[ENGLISH_ALPHABET_MAX][ENGLISH_ALPHABET_MAX];

	private final ResultsFilter rules = new ResultsFilter();
	
	/**
	 * Default Constructor; only setting log verbosity
	 */
	public Dictionary() {
		LOGGER.setLevel(Level.FINE);
	}

	/**
	 * Encapsulated accessor for Dictionary primatives.
	 * @param i row index
	 * @param j column index
	 * @return character in 2D array
	 */
	public char charAt(int i, int j) {
		return dictionary[i][j];
	}

	/**
	 * Delegates to {@link #load(InputStream)}
	 * @param fileName of the dictionary (relative within Classpath)
	 * @throws IOException if file not found
	 */
	void load(String fileName) throws IOException {
		load(this.getClass().getClass().getResourceAsStream(fileName));
	}

	/**
	 * Delegates to {@link #load(InputStream)}
	 * @param url of the dictionary (relative within Classpath)
	 * @throws IOException if URL not resolved
	 */	
	public void load(URL url) throws IOException {
		load(url.openStream());
	}

	/**
	 * @param rowIndex index of dictionary mapping digit to letters
	 * @return whether numerical representation of char contains any mappings
	 */
	public Boolean charHasEmptyMapping(Character rowIndex) {
		return charHasEmptyMapping(Utilities.parseToDigit(rowIndex));
	}

	/**
	 * <b>Assumes row leading value starts at 0</b>
	 * @param rowIndex index of dictionary mapping digit to letters
	 * @return whether that index has no mappings
	 */
	boolean charHasEmptyMapping(int rowIndex) {
		char[] row = dictionary[rowIndex];
		// is a workaround where dictionary row is fully populated with the same
		// value if there is no mapping
		return new Boolean((Utilities.hasDuplicatedValues(row))); // ie no mappings
	}

	/**
	 * Core function with package access only.
	 * <b>Note:<b>Using try with resource so no need to use explicit closure.
	 * @param fileName relative path reference
	 * @return 2D character array representing the dictionary
	 * @throws IOException if fileName is invalid (to be handled in calling class)
	 */
	void load(InputStream iS) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(iS))) {
			String s = "";
			char[] r2;
			int i = 0, j = 0;
			while ((s = br.readLine()) != null) {
				s = s.replaceAll("\\s", ""); // strip space delimters for array
				r2 = s.toCharArray();

				if (r2.length == 1) {
					// there are no mappings; fill row completely with value
					// this is a workaround for an un-solved issue from
					// PhoneNumberToLetterConverter.convertPhoneNumberToLetters
					rules.addUnMappedValues(new Integer(Utilities.parseToDigit(r2[0])));
					for (j = 0; j < ENGLISH_ALPHABET_MAX; j++) {
						dictionary[i][j] = r2[0];
					}
				} else {
					rules.addMappedValues(new Integer(Utilities.parseToDigit(r2[0])));
					for (j = 0; j < r2.length; j++) {
						dictionary[i][j] = r2[j];
						LOGGER.log(Level.FINE, "[" + i + "," + j + "=" + dictionary[i][j]);
					}
				}
				i++;
				LOGGER.log(Level.FINE, "\n");
			}
		}
		rules.setFilterPattern();
	}

	/**
	 * Delegator to BusinessRule class implementation
	 * @return the correct dictionary according to the business rules
	 */
	public String getFilterRegExp() {
		return rules.getFilterRegExp();
	}


}
