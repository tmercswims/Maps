package edu.brown.cs032.ja11.autocorrect.dictionary;

/**Entry regulates the functionality of a dictionary entry: how we can add and retrieve information about 
 * words in the vocabulary
 * @author ja11
 *
 */
public interface Entry {

	/**@return the Entry's string **/
	public String getString();
	
	/**@return the Entry's probability of appearing in the corpus **/
	public double getUnigramProb();
	
	/** adds one count to the appearance count of the word in a corpus **/
	public void addUnigramCount();
	
	/** lets the user store the total number of words in the corpus from which the Entry is derived.
	 * This needs to be set before getUnigramProb() or getBigramProb() output reasonable values.
	 * @param total: the integer total of words in the corpus
	 */
	public void setTotal(int total);
	
	/** adds one count to the appearance count of the bigram prevWord - word in the corpus
     * @param prevWord: the previous word
     */
	public void addBigramCount(String prevWord);
	
	/** Finds the bigram probability of a previous word and the Entry's word
	 * 
	 * @param prevWord : the word that should preceed the Entry's word in the bigram
	 * @return: a double denoting the probability of the bigram. 0 if it appears nowhere in the corpus.
	 */
	public double getBigramProb(String prevWord);
	
}
