package dictionary;

import java.util.Collection;

public interface TreeSearchable {
	
	public char getElement();
	
	public boolean isWord(String word);
	
	public boolean isWordEnd();
	
	public TreeSearchable getNode(char element);
	
	public Collection<TreeSearchable> getNodes();
	
	public void addWord(String word);
	
	public TreeSearchable searchNode(String route);

}
