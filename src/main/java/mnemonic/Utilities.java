package mnemonic;
/**
 * Utilities used with {@link NumberToLetterConverter}
 * @author Kevin Flattery
 */
public class Utilities {
	
	public static final char NULL_CHAR = '\u0000';

	/**
	 * @param c test character
	 * @return if <code>c</code> is Punctuation Or Whitespace
	 */
	public static  boolean isPunctuationOrWhitespace(char c) {
		return (isPunctuation(c) || c == ' ');
	}
	
	/**
	 * Delegate to {#link Character.digit} with primitive integer result 
	 * in base 10.
	 */	
	public static int parseToDigit(char c) {
		return Character.digit(c, 10);
	}
	
    /**
     * Returns true if specified character is a punctuation character.
     * Punctuation includes comma, period, exclamation point, question
     * mark, colon, <i>dash</i> and semicolon.  Note that quotes and apostrophes
     * are not considered punctuation by this method.
     *
     * @param c Character to test.
     * @return <code>true</code> if specified character is a
     * whitespace.
     * @see java.lang.Character
     */
    public static boolean isPunctuation(char c) {
        return c == ','
            || c == '.'
            || c == '!'
            || c == '?'
            || c == ':'
            || c == ';'
            || c == '-'
            ;
    }	
    
    /**
     * @param array contains non-nulls then possible 'char-nulls' <i>sequentially</i>.
     * @return where first null occurs
     */
    public static int nullArrayLength(char[] array) {
    	for (int i = 0; i < array.length; i++) {
			if (array[i] == NULL_CHAR) {
				return i;
			}
		}
    	return array.length; // no nulls found
    }

    /**
     * @return Checking that char array contain all the same value
     */
	public static boolean hasDuplicatedValues(char[] row) {
		for (int i = 0; i < row.length; i++) {
			if ((i+1 < row.length - 1) && row[i] != row[i+1]) {
				return false;
			}
		}
		return true;
	}
}
