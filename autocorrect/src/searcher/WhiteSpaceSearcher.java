package searcher;

import java.util.ArrayList;
import java.util.Collection;

import dictionary.DoubleEntry;
import dictionary.Entry;
import dictionary.TreeSearchable;
import dictionary.Vocab;

public class WhiteSpaceSearcher implements Searcher {
	
	/**vocabulary: the Vocab the Searcher is working with**/
	Vocab vocabulary;
	
	/**root: the root node of Vocab's trie **/
	TreeSearchable root;
	
	/** Constructor
	 * 
	 * @param vocabulary: the Vocab that PrefixSearcher can search upon
	 */
	public WhiteSpaceSearcher(Vocab vocabulary){
		this.vocabulary = vocabulary;
		this.root = vocabulary.getRoot();
	}
	
	/** find tries to find valid suggestions for how to split word into two words
	 * @param word: the input String
	 * @return a Collection<Entry>, specifically DoubleEntry, of word bigrams which have the same characters as word but include a whitespace
	 */
	@Override
	public Collection<Entry> find(String word) {
		
		Collection<Entry> results = new ArrayList<Entry>();
		int length = word.length();
		
		//We loop through, trying out the spaces that would split the word into two pieces and trying to find valid word pairs
		for (int i = 1; i < length-1; i++){
			String firstWord = word.substring(0, i);
			String secondWord = word.substring(i);
			if (root.isWord(firstWord) && root.isWord(secondWord)){
				 Entry firstEntry = vocabulary.getEntry(firstWord);
				 Entry secondEntry = vocabulary.getEntry(secondWord);
				 results.add(new DoubleEntry(firstEntry, secondEntry));
			}
		}
		return results;
	}
	
}

