package searcher;

import java.util.Collection;
import java.util.HashSet;

import dictionary.Entry;
import dictionary.TreeSearchable;
import dictionary.Vocab;


/**LevenSearcher uses the notion of Levenshtein distance to generate suggestions. It should hope to find
 * all words in the dictionary that lie within a certain levinshtein distance of the searched word.
 * @author ja11
 *
 */
public class LevenSearcher implements Searcher {
	
	/** distance: the maximum LED that is permitted **/
	int distance;
	
	/** vocabulary: the Vocab upon which the LevenSearcher will search **/
	Vocab vocabulary;
	
	/** root: the trie which the Searcher will traverse **/
	TreeSearchable root;
	
	/**Constructor
	 * 
	 * @param distance: an int specifying how far the LED can maximally be
	 * @param vocabulary: the Vocab that the Searcher can search in
	 */
	public LevenSearcher(int distance, Vocab vocabulary){
		this.distance = distance;
		this.vocabulary = vocabulary;
		this.root = vocabulary.getRoot();
	}
	
	/** levenSearch is a parametrized find. levensearch will find all word ends that are valid from a given starting node
	 * and a preceding trajectory, and will most likely spawn a bunch of child levenSearches and collect their values.
	 * 
	 * @param node: the node which acts as the root from which the function searches
	 * @param remainingDistance: how many more edits this iteration of the search is allowed to stray maximally
	 * @param remainingWord: how many letters of the original word still should be consumed before returning
	 * @param acc: a String accumulator denoting the path that the levinSearch has followed up until then
	 * @return a Collection<String> denoting the possible words rooting from node with a LED distance of remainingDistance
	 */
	private Collection<String> levenSearch(TreeSearchable node, int remainingDistance, String remainingWord, String acc){
		
		Collection<String> results = new HashSet<String>();
		
		//Case: LED Distance is 0: remainingWord is searched and added to results if it is in the dictionary;
		if (remainingDistance==0){
				TreeSearchable endNode = node.searchNode(remainingWord);
				if ((endNode!= null) && (endNode.isWordEnd())) {
					results.add(acc + remainingWord);
				}
			}
		//Case: LED distance is more than 0. Several things can happen.
		else {
			// Subcase: remainingWord has no more letters to consume.
			if (remainingWord.length() == 0){
				//The node we are at is a word end, so should be added to our results
				if (node.isWordEnd()) {
					results.add(acc);
				}
				//Other searches are done for more words lying just below this node, maximally remainingDistance away
				// from the node we are currently at.
				for (TreeSearchable child: node.getNodes()){
					results.addAll(levenSearch(child, remainingDistance - 1, remainingWord, acc +child.getElement()));
				}
			}
			//Subcase: remainingWord still has more letters to consume before the Searcher halts.
			else{
				//Option 1: a letter is deleted from remainingWord
				results.addAll(levenSearch(node, remainingDistance -1, remainingWord.substring(1), acc));
				
				//Other options: Doing nothing, substitution or insertion
				for (TreeSearchable child : node.getNodes()){
					if (child.getElement()==remainingWord.charAt(0))
						//Option 2: Nothing is done
						results.addAll(levenSearch(child, remainingDistance, remainingWord.substring(1), acc + child.getElement()));
					// Option 3: a letter is substituted
					else results.addAll(levenSearch(child, remainingDistance -1, remainingWord.substring(1), acc+ child.getElement()));
					//Option 4: a letter is inserted
					results.addAll(levenSearch(child, remainingDistance - 1, remainingWord, acc+ child.getElement()));
				}
			}
		}
		return results;
	}
	
	/** find returns a collection of Entry of words within the allowed LED
	 * @param word, the String we are trying to find edits for
	 * @return a Collection<Entry> of suggestions 
	 */
	public Collection<Entry> find(String word){
		//HashSet is used to prevent duplicate entries - various levenSearch branches could end up at the same place
		HashSet<Entry> results = new HashSet<Entry>();
		
		//levenSearch is called from vocab's root and standard initial values
		Collection<String> suggestions = levenSearch(root,distance, word,"");
		for (String suggestion : suggestions){
			results.add(vocabulary.getEntry(suggestion));
		}
		return results;
	}

}
