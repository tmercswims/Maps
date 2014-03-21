package edu.brown.cs032.ja11.autocorrect.dictionary;

import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author Thomas Mercurio
 */
public class TrieNode implements TreeSearchable{

	private final char element;
	private boolean isWordEnd;
	private final HashMap<Character, TreeSearchable> children;

    /**
     *
     * @param element
     */
    public TrieNode(char element){
		this.element = element;
		this.isWordEnd = false;
		this.children = new HashMap<>(26);
	}

    /**
     *
     * @return
     */
    @Override
	public char getElement() {
		return element;
	}
	
    /**
     *
     * @return
     */
    @Override
	public boolean isWordEnd(){
		return isWordEnd;
	}

    /**
     *
     * @param word
     * @return
     */
    @Override
	public boolean isWord(String word) {
		TreeSearchable end = searchNode(word);
		if (end!= null) 
				return end.isWordEnd();
		else return false; 
	}

    /**
     *
     * @param element
     * @return
     */
    @Override
	public TreeSearchable getNode(char element) {
		return children.get(element);
	}

    /**
     *
     * @return
     */
    @Override
	public Collection<TreeSearchable> getNodes() {
		return children.values();
	}

    /**
     *
     * @param word
     */
    @Override
	public void addWord(String word) {
		String toAdd = word;
		if (word.length()==0) 
			isWordEnd = true;
		else{
		TreeSearchable newNode = getNode(toAdd.charAt(0));
		if (newNode!=null){
			newNode.addWord(toAdd.substring(1));
			}
		else {
			TreeSearchable newChild = new TrieNode(toAdd.charAt(0));
			newChild.addWord(toAdd.substring(1));
			children.put(toAdd.charAt(0), newChild);
		}
		}
		
	}

    /**
     *
     * @param route
     * @return
     */
    @Override
	public TreeSearchable searchNode(String route) {
		if(route.length()==0){
			return this;
		}
		else{
		TreeSearchable nextNode = getNode(route.charAt(0));
		if (nextNode == null) 
			return null;
		else return nextNode.searchNode(route.substring(1));
		}
	}


}
