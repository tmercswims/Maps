package edu.brown.cs032.ja11.autocorrect.backend;


import java.util.List;

import edu.brown.cs032.ja11.autocorrect.dictionary.Entry;

/**Runnable specifies the interaction between a backend running the autocorrect and the frontend which needs to display
 * the top suggestions
 * 
 * @author ja11
 *
 */
public interface Runnable {
	
	/** getTopFiveResults: given a word, will try to find the top 5 corrections for it
	 * 
	 * @param word: the String to be searched
	 * @return a List<Entry> of at most five Entries corresponding to possible corrections of the word
	 */
	public List<Entry> getTopFiveResults(String word);
	
	/** getTopFiveResults: given a word and the word immediately preceding, will try to find the top 5 corrections for it
	 * 
	 * @param prevWord: the word right before word
	 * @param word: the String to be searched
	 * @return a List<Entry> of at most five Entries corresponding to possible corrections of hte word
	 */
	public List<Entry> getTopFiveResults(String prevWord, String word);
}
