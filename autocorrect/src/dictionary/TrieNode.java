package dictionary;

import java.util.Collection;
import java.util.HashMap;

public class TrieNode implements TreeSearchable{

	private char element;
	private boolean isWordEnd;
	private HashMap<Character, TreeSearchable> children;
	
	public TrieNode(char element){
		this.element = element;
		this.isWordEnd = false;
		this.children = new HashMap<Character, TreeSearchable>(26);
	}
	
	@Override
	public char getElement() {
		return element;
	}
	
	public boolean isWordEnd(){
		return isWordEnd;
	}

	@Override
	public boolean isWord(String word) {
		TreeSearchable end = searchNode(word);
		if (end!= null) 
				return end.isWordEnd();
		else return false; 
	}

	@Override
	public TreeSearchable getNode(char element) {
		return children.get(element);
	}

	@Override
	public Collection<TreeSearchable> getNodes() {
		return children.values();
	}

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
