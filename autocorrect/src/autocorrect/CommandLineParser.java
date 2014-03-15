package autocorrect;



import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import dictionary.Vocab;

import searcher.GeneralSearcher;
import searcher.LevenSearcher;
import searcher.PrefixSearcher;
import searcher.Searcher;
import searcher.WhiteSpaceSearcher;


import frontend.CommandLine;
import frontend.Gui;
import frontend.PresentingResults;
/** CommandLineParser oversees the creation of a runnable program 
 * and deals with the nitty-gritty of parsing the command line 
 * 
 * @author ja11
 * **/
public class CommandLineParser {
	
	/**This enum controls whether the UI is a command line or GUI **/
	UIEnums ui;
	
	/**Constructor. Default: Command line interface **/
	public CommandLineParser(){
	this.ui = UIEnums.CLI;
	}
	
	public static void main (String[] args){
		try{
		CommandLineParser toRun = new CommandLineParser();
		toRun.run(args);
		}
		catch (FileNotFoundException e){
			System.out.println("ERROR: No files found for corpus");
		}
	 }
	
	/**@param ui: a UIEnum to set the UI to GUI or CLI **/
	private void setUI(UIEnums ui){
		this.ui = ui;
	}
	
	/** Sets up the UI internally and then runs autocorrect
	 * @param engine: the Engine - ie the run configuration for autocorrect
	 **/
	private void run(Engine engine){
		//Determine UI
		PresentingResults interactive = null;
		switch(ui){
		case CLI: interactive = new CommandLine(engine); break;
		case GUI: interactive = new Gui(engine); break;
		}
		//Run the program
		interactive.run();
	}
	
	/** Calls parseArgs to create an Engine to run and then runs it
	 * @param args: the command line arguments determining the configuration
	 **/
	public void run(String[] args) throws FileNotFoundException{
		//Configure and run the engine
		try{
		Engine engine = parseArgs(args);
		run(engine);
		}
		//Log things that went wrong
		catch(FileNotFoundException e){
			System.out.println("ERROR: One of the filenames is not correct, or a command was not input correctly");
		}
		catch(IOException e){
			System.out.println("ERROR: your file is corrupted and cannot be read");
		}
		catch(NumberFormatException e){
			System.out.println("ERROR: you did not give a valid number for Levenshtein edit distance. Please enter a nonnegative whole number after the '--led' command");
		}
	}
	
	/** Takes the arguments from the command line and configures an engine by the specifications
	 * 
	 * @param args: the Command Line arguments
	 * @return an Engine with the specified searchers, UI, vocabulary and ranking system. 
	 * Defaults to a searcher looking for exact matches, command line interface, BasicRanker. Requires a filename for dictionary
	 * @throws FileNotFoundException if the filename is inexact
	 * @throws IOException if an IO error occurs during the reading of the file
	 * @throws NumberFormatException if the command '--led' is not followed by a non-negative integer
	 */
	private Engine parseArgs (String[] args) throws FileNotFoundException, IOException, NumberFormatException {
		//Where we will store the Enums determining what Searchers should be added
		Collection<SearcherEnums> searchers = new ArrayList<SearcherEnums>();
		
		// A list of the filenames from which the vocabulary should be made
		Collection<String> filenames = new ArrayList<String>();
		
		//Initialised variables
		//pos keeps track of where we are in the arguments
		int pos = 0;
		
		//The enum which will allow for the default BasicRanker if no other argument is given
		RankerEnums ranker = RankerEnums.BASICRANKER;
		
		//The LED distance for the default searcher if no searcher is specified
		int ledDistance = 0;
		
		
		while(pos< args.length){
			switch(args[pos]){
			
			//Configures the GUI if the user calls for it
		case "--gui": {
			setUI(UIEnums.GUI); 
			pos+=1; 
			break;
		}
		
		//Adds a Whitespace searcher
		case "--whitespace": {
			searchers.add(SearcherEnums.WHITESPACE);
			pos+=1;
			break;
		}
		
		//Adds a levenshtein searcher only if the second argument is correctly given
		case "--led": {
			pos+=1;
			ledDistance = Integer.valueOf(args[pos]);
			if (ledDistance<0) throw new NumberFormatException("ERROR: Number not positive");
			searchers.add(SearcherEnums.LEVEN); 
			pos+=1;
			break;
		}
		
		//Adds a prefix searcher
		case "--prefix":{
			searchers.add(SearcherEnums.PREFIX);
			pos+=1;
			break;
		}
		
		//Adds the smart ranking system if called for
		case "--smart":{
			ranker = RankerEnums.MYRANKER;
			break;
		}
		
		//Adds all filenames to the list of files to be added to the vocabulary
		default:{
			filenames.add(args[pos]);
			pos+=1;
			break;
		}
			}
		}
		
		//Sets up and adds the files to the vocabulary
		Vocab vocabulary = new Vocab();
		if (filenames.isEmpty()) throw new FileNotFoundException("ERROR: No files!");
		else{
			for (String filename: filenames){
			vocabulary.addVocab(filename);
			}
		}
		
		//Sets up the searchers and collects them into one general searcher
		Collection<Searcher> allSearchers = new ArrayList<Searcher>();
		if (searchers.isEmpty())
			allSearchers.add(new LevenSearcher(0, vocabulary));
		else {
			for (SearcherEnums searcher : searchers){
			switch (searcher){
			case PREFIX: allSearchers.add(new PrefixSearcher(vocabulary)); break;
			case LEVEN: allSearchers.add(new LevenSearcher(ledDistance, vocabulary)); break;
			case WHITESPACE: allSearchers.add(new WhiteSpaceSearcher(vocabulary)); break;
				}
			}
		}
		
		//Configures the engine
		Searcher master = new GeneralSearcher(allSearchers);
		Engine result = new Engine(master, ranker);
		return result;
		}
	}