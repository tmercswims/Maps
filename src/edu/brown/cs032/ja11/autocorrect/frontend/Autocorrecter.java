package edu.brown.cs032.ja11.autocorrect.frontend;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.brown.cs032.ja11.autocorrect.backend.Engine;
import edu.brown.cs032.ja11.autocorrect.backend.RankerEnums;
import edu.brown.cs032.ja11.autocorrect.dictionary.Vocab;
import edu.brown.cs032.ja11.autocorrect.searcher.GeneralSearcher;
import edu.brown.cs032.ja11.autocorrect.searcher.LevenSearcher;
import edu.brown.cs032.ja11.autocorrect.searcher.PrefixSearcher;
import edu.brown.cs032.ja11.autocorrect.searcher.Searcher;
import edu.brown.cs032.ja11.autocorrect.searcher.WhiteSpaceSearcher;
import edu.brown.cs032.tmercuri.TSV.TSVReader;

/**
 *
 * @author Thomas Mercurio
 */
public class Autocorrecter {
	
	Engine engine;
	String filepath;

    /**
     *
     * @param filepath
     */
    public Autocorrecter(String filepath){
		Vocab vocab = new Vocab();
		this.filepath = filepath;
		try{
		TSVReader vocabGenerator = new TSVReader(filepath);
		List<String> inputWords = vocabGenerator.getAllEntriesInColumn("name");
		vocab.addVocab(inputWords);
		
		Collection<Searcher> searchers = new ArrayList<Searcher>();
		searchers.add(new LevenSearcher(1, vocab));
		searchers.add(new PrefixSearcher(vocab));
		searchers.add(new WhiteSpaceSearcher(vocab));
		Searcher masterSearcher = new GeneralSearcher(searchers);
		engine = new Engine(masterSearcher, RankerEnums.BASICRANKER);
		}
		catch (FileNotFoundException e){
			System.out.println("ERROR: couldn't find files with names of stuff");
		}
		catch (IOException e){
			System.out.println("ERROR: IO exception in reading names of roads");
		}
	}

    /**
     *
     * @param input
     * @return
     */
    public List<String> getResults(String input){
		return engine.getTopFiveResults(input);
	}
}
