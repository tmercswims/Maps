package edu.brown.cs032.ja11.autocorrect.backend;

import edu.brown.cs032.ja11.autocorrect.dictionary.Entry;
import edu.brown.cs032.ja11.autocorrect.ranker.BasicRanker;
import edu.brown.cs032.ja11.autocorrect.searcher.Searcher;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/** Engine coordinates the searching and running of autocorrect, and generates the top five results for use by the UIs
 * 
 * @author ja11
 *
 */
public class Engine implements Runnable {
	
	/** The Searcher which the Engine will use to find suggestions **/
	private final Searcher searcher;
	/** The Enum determining which Ranking Method will eventually be chosen **/
	private final RankerEnums ranker;
	
	/**Constructor
	 * 
	 * @param searcher: a Searcher
	 * @param ranker: a Rankers enum
	 */
	public Engine(Searcher searcher, RankerEnums ranker){
		this.searcher = searcher;
		this.ranker = ranker;
	}
	
	private List<String> convertEntryListToStringList(List<Entry> input){
		List<String> result = new ArrayList<String>();
		for (Entry i: input){
			result.add(i.getString());
		}
		return result;
	}

	/**getTopFiveResults outputs an ordered list of the best corrects for word given it follows prevWord
	 * @param prevWord: the String word preceding the word we're searching for
	 * @param word: the word we're seeking to autocorrect
	 * 
	 * @return a List<String> of the words that are most likely to be good corrections for word
	 */
	@Override
	public List<String> getTopFiveResults(String prevWord, String word) {
		
		//Chooses the Ranking Method based on the ranker enum
		Comparator<Entry> rankingMethod;
		switch(ranker){
		case BASICRANKER: rankingMethod = new BasicRanker(prevWord, word); 
							break;
		case MYRANKER:	 rankingMethod = new BasicRanker(prevWord, word);
						break;
		default: rankingMethod = new BasicRanker(prevWord, word); break;
		}
		
		//finds all the words and sorts them according to the ranking method
		ArrayList<Entry> toRank = new ArrayList<>();
		toRank.addAll(searcher.find(word));
		Collections.sort(toRank, Collections.reverseOrder(rankingMethod));
		
		//Returns at most five results
		if (toRank.size() < 5) return convertEntryListToStringList(toRank);
		else return convertEntryListToStringList(toRank.subList(0, 5));
	}
	
	/**getTopFiveResults outputs an ordered list of the best corrects for word without context
	 * @param word: the word we're seeking to autocorrect
	 * 
	 * @return a List<Entry> of the words that are most likely to be good corrections for word
	 */
	@Override
	public List<String> getTopFiveResults(String word) {
		//Chooses the Ranking Method based on the ranker enum
		Comparator<Entry> rankingMethod;
		switch(ranker){
		case BASICRANKER: rankingMethod = new BasicRanker(word); 
							break;
		case MYRANKER:	 rankingMethod = new BasicRanker(word);
						break;
		default: rankingMethod = new BasicRanker(word); break;
		}
		
		//finds all the words and sorts them according to the ranking method
		ArrayList<Entry> toRank = new ArrayList<>();
		toRank.addAll(searcher.find(word));
		Collections.sort(toRank, Collections.reverseOrder(rankingMethod));
		//Returns at most five results
		if (toRank.size() < 5) return convertEntryListToStringList(toRank);
		else return convertEntryListToStringList(toRank.subList(0, 5));
	}

}
