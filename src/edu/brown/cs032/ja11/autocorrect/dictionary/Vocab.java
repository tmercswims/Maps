package edu.brown.cs032.ja11.autocorrect.dictionary;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/** Vocab maintains both the trie and the hashtable dictionary to look up valid words, and significant data about those words **/
public class Vocab {
	
	
	/** root: stores the Trie **/
	private final TreeSearchable root;
	
	/**dictionary: stores the Entries which contain information about each word **/
	private final HashMap<String, Entry> dictionary;
	
	/**total: keeps track of the total number of words in the corpus **/
	private int total;
	
	
	/**Constructor
	 * 
	 */
	public Vocab (){
		root = new TrieNode('\0');
		dictionary = new HashMap<>();
		total = 0;
	}
	
	
	/** @return the root of the tree, so Searchers can get to it easily **/
	public TreeSearchable getRoot(){
		return root;
	}
	
	/** getEntry returns the Entry for a given word
	 * 
	 * @param word, the String we want the Entry for
	 * @return the Entry for the word. null if the word doesn't exist in the dictionary.
	 */
	public Entry getEntry(String word){
		return dictionary.get(word);
	}
	
	
	/** addVocab builds the vocabulary stored in Vocab
	 * 
     * @param inputWords
	 * @throws FileNotFoundException if the filename is invalid
	 * @throws IOException if the filereading encounters an IO error
	 */
	public void addVocab(List<String> inputWords) throws FileNotFoundException, IOException{
		
		int total = inputWords.size();
                    
        //Add the words to the dictionary and the root
		for (String newWord : inputWords)
          if (dictionary.containsKey(newWord))
          dictionary.get(newWord).addUnigramCount();
          //Or add a new Entry alltogether
          else {
             Entry newEntry = new WordEntry(newWord);
             root.addWord(newEntry.getString());
             dictionary.put(newWord, newEntry);
          }
    //Once the entire dictionary has been consumed, relate the total value to all the Entries
            for (Entry i: dictionary.values()){
                i.setTotal(total);
            }
        }
	}			

