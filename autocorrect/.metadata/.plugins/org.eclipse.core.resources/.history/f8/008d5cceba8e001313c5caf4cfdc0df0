package dictionary;

/** DoubleEntry allows WhiteSpaceSearch to output two words within one Entry
 * 
 * @author ja11
 *
 */

public class DoubleEntry implements Entry {
	
	/** Two entry fields to store the two separate Entries **/
	private Entry firstWord;
	private Entry secondWord;
	
	/** Constructor
	 * 
	 * @param firstWord: the first Entry
	 * @param secondWord: the second Entry
	 */
	public DoubleEntry(Entry firstWord, Entry secondWord){
		this.firstWord = firstWord;
		this.secondWord = secondWord;
	}
	/** getString()
	 * @return the String formed by the two words of the Entries separated by a space
	 */
	@Override
	public String getString() {
		String result = firstWord.getString() +" "+  secondWord.getString();
		return result;
	}
	
	/** getUnigramProb()
	 * @return the probability of the first word in the corpus - as this is the probability used for ranking
	 */
	@Override
	public double getUnigramProb() {
		return firstWord.getUnigramProb();
	}

	/** getBigramProb()
	 * @return the bigramProb of the first word of the DoubleEntry and the previous word
	 */
	@Override
	public double getBigramProb(String prevWord) {
		return firstWord.getBigramProb(prevWord);
	}
	
	/**addUnigramCount()
	 * adds one to both of the Entries' Unigram counts
	 */
	@Override
	public void addUnigramCount() {
		firstWord.addUnigramCount();
		secondWord.addUnigramCount();
	}
	
	/** addBigramCount adds to the bigramcount of the first word only
	 * @param prevWord: the word preceding the DoubleEntry
	 * 
	 */
	@Override
	public void addBigramCount(String prevWord) {
		firstWord.addBigramCount(prevWord);
	}

	/** setTotal sets both of the word's totals
	 * @param total: the total number of words in the corpus
	 */
	@Override
	public void setTotal(int total) {
		firstWord.setTotal(total);
		secondWord.setTotal(total);
	}


}
