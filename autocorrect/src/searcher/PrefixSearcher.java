package searcher;

import java.util.ArrayList;
import java.util.Collection;

import dictionary.Entry;
import dictionary.TreeSearchable;
import dictionary.Vocab;

/**PrefixSearcher tries to complete words given to its find operation by finding appropriate suffixes
 * 
 * @author ja11
 *
 */
public class PrefixSearcher implements Searcher {

	/**vocabulary: the Vocab the Searcher is working with**/
	Vocab vocabulary;
	
	/**root: the root node of Vocab's trie **/
	TreeSearchable root;
	
	
	/** Constructor
	 * 
	 * @param vocabulary: the Vocab that PrefixSearcher can search upon
	 */
	public PrefixSearcher(Vocab vocabulary){
		this.vocabulary = vocabulary;
		this.root = vocabulary.getRoot();
	}
	
	/** findWords: outputs all the words and their paths (Strings) from a certain node)
	 * 
	 * @param node: the starting node from which the search should descend
	 * @return a Collection<String> of all options below it
	 */
	private Collection<String> findWords(TreeSearchable node){
		
		Collection<String> results = new ArrayList<String>();
		
		//Base case
		if (node.isWordEnd()) {
			results.add("");
		}
		
		//Recur down into the node's children
		for (TreeSearchable child : node.getNodes()){
			Collection<String> childResults = findWords(child);
			for (String subchild : childResults)
				results.add(child.getElement()+ subchild);
		}

		return results;
	}
	
	/**find returns a Collection<Entry> of words starting with the given prefix
	 * @param prefix: the first letters of the words to be retrieved
	 * @return a Collection<Entry> with words all beginning with prefix
	 */
	@Override
	public Collection<Entry> find(String prefix) {
		
		Collection<Entry> results = new ArrayList<Entry>();
		
		//Navigate towards the large parent node
		TreeSearchable currNode = root.searchNode(prefix);
		
		if(currNode != null){
		Collection<String> suggestions = findWords(currNode);
		for (String suggestion : suggestions){
			results.add(vocabulary.getEntry(prefix + suggestion));
		}
		}
		return results;
	}

}
