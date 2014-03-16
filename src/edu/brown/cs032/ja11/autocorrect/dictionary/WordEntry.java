package edu.brown.cs032.ja11.autocorrect.dictionary;

import java.util.HashMap;

/**WordEntry determines how records of Strings are kept in Vocab's dictionary 
 * 
 * @author ja11
 *
 **/
public class WordEntry implements Entry {
	
	/**word: the String stored **/
	private final String word;
	
	/**unigramCount: how many times word appears in the Vocab's corpora **/
	private int unigramCount;
	
	/**bigramCount: stores each word that has preceded word in the corpora, and how often is has been preceded **/
	private final HashMap <String, Integer> bigramCount;
	
	/**total: the total number of words in the corpora **/
	private double total;
	
	/** Constructor
	 * 
	 * @param word: the word to be stored.
	 * 
	 * As the constructor is only called if the word exists, the default unigramCount is set to 1. 
	 * total is initalised to 0 as correct functioning requires it is notified by Vocab of the final total.
	 */
	public WordEntry(String word){
		this.word = word;
		unigramCount = 1;
		bigramCount = new HashMap<>();
		total = 0.0;
	}
	
	/** getString() returns the WordEntry's string
	 * @return the String stored in the WordEntry
	 */
	@Override
	public String getString() {
		return word;
	}
	
	/** setTotal(int total) sets the total int in the WordEntry
	 * @param total: the total number of words in all the corpora combined
	 */
    @Override
	public void setTotal(int total){
		this.total = total;
	}
	
	/** addUnigramCount() adds one to the WordEntry's unigramCount. 
	 * This function should be called for each time the word is found in the corpus.
	 * 
	 */
    @Override
	public void addUnigramCount(){
		unigramCount +=1;
	}
	
	/** addBigramCount updates the counter keeping track of the bigrams the word belongs to and how often they appear
	 * @param prevWord: the first word of the bigram that has been encountered
	 */
    @Override
	public void addBigramCount(String prevWord){
		if (bigramCount.containsKey(prevWord))
			bigramCount.put(prevWord, bigramCount.get(prevWord)+1);
		else bigramCount.put(prevWord, 1);
	}
	
	/**getUnigramProb()
	 * @return the probability of encountering the word in the corpus
	 */
	@Override
	public double getUnigramProb() {
		return unigramCount/total;
	}

	/** getBigramProb
	 * @param prevWord: the word preceding word
	 * @return the probability of the bigram occurring in the total corpus. 0 if the bigram never appears
	 */
	@Override
	public double getBigramProb(String prevWord) {
		Integer count = bigramCount.get(prevWord);
		if (count!= null)
				return count/total;
		else return 0;
	}
	


}
