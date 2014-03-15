package searcher;

import java.util.Collection;

import dictionary.Entry;

/** Searcher is the interface to which all Searchers should adhere in order to find words
 * 
 * @author ja11
 *
 */
public interface Searcher {
	
	/** @return a Collection<Entry> of words that are potential corrects for word **/
	public Collection<Entry> find(String word);
	
}
