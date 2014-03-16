package edu.brown.cs032.ja11.autocorrect.searcher;

import edu.brown.cs032.ja11.autocorrect.dictionary.Entry;
import java.util.Collection;
import java.util.HashSet;

/**GeneralSearcher combines various searches into one Searcher using all search strategies to generate suggestions 
 * 
 * @author ja11
 *
 */
public class GeneralSearcher implements Searcher {
	
	/** searches: a collection of all the Searchers GeneralSearcher will use **/
	Collection<Searcher> searchers;
	
	/**Constructor
	 * 
	 * @param searchers: a collection of all the Searchers that GeneralSearcher should use
	 */
	public GeneralSearcher(Collection<Searcher> searchers){
		this.searchers = searchers;
	}
	
	/** find: retrieves all suggestions for a given input word
	 * @param word, a String denoting the word to be corrected
	 * @return a Collection<Entry> of words in the dictionary that have been retrieved
	 */
	@Override
	public Collection<Entry> find(String word) {
		
		// The HashSet prevents duplicate suggestions
		HashSet<Entry> results = new HashSet<>();
		
		//iterate over searchers, collect all suggestions and return them
		for (Searcher searcher : searchers){
			results.addAll(searcher.find(word));
		}
		return results;
	}

}
