package edu.brown.cs032.ja11.autocorrect.searcher;

import edu.brown.cs032.ja11.autocorrect.dictionary.Entry;
import java.util.Collection;

/** Searcher is the interface to which all Searchers should adhere in order to find words
 * 
 * @author ja11
 *
 */
public interface Searcher {
	
	/**
     * @param word: the word to find
     * @return a Collection<Entry> of words that are potential corrects for word
     */
	public Collection<Entry> find(String word);
	
}
