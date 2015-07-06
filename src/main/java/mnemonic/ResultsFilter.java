package mnemonic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class ResultsFilter {
	/**
	 * captures numbers in dictionary that have letters unassigned (for regExp filtering)
	 */
	private List<Integer> unMappedValues = new ArrayList<Integer>();
	/**
	 * captures numbers in dictionary that have letters assigned (for regExp filtering)
	 */
	private List<Integer> mappedValues = new ArrayList<Integer>();
	/**
	 * For filtering permutation results to suit business rules.
	 */
	private String regExp;
	/**
	 * Set internal regular expression that aligns with business requirements: <li>unmatched allowed but not more than
	 * one in sequence. <li>no numbers that should be matched.
	 */
	void setFilterPattern() {
		String numberPatterns = getValuesAsRegExp(false, unMappedValues)
				+ getValuesAsRegExp(true, mappedValues);
		regExp = "(" + numberPatterns + "[A-Z]+" + numberPatterns + "[-]*[\\.]*" + numberPatterns
				+ "[A-Z]+" + numberPatterns + ")";
	}

	/**
	 * @param mapped : UnMapped if false ; mapped if true
	 * @return regular expression applying business rules filter to permutations
	 * derived from dictionary. 
	 */
	public String getValuesAsRegExp(boolean mapped, List<Integer> values) {
		String negate = ((mapped) ? "^" : "");
		String regExp = "[" + negate;
		String quantifier = ((mapped) ? "*" : "?");
		for (Iterator<Integer> iterator = values.iterator(); iterator.hasNext();) {
			regExp += ((Integer) iterator.next()).toString();
		}
		return regExp + "]" + quantifier;
	}

	/**
	 * @return filter regular expression based on business rules
	 */
	String getFilterRegExp() {
		return regExp;
	}

	/**
	 * Delegate to add to list member
	 * @param integer telephone number value mapping to dictionary
	 */
	void addUnMappedValues(Integer integer) {
		unMappedValues.add(integer);
	}

	/**
	 * Delegate to add to list member
	 * @param integer telephone number value mapping to dictionary
	 */	
	void addMappedValues(Integer integer) {
		mappedValues.add(integer);		
	}

}
