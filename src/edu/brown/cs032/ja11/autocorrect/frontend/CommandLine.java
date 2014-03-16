package edu.brown.cs032.ja11.autocorrect.frontend;

import edu.brown.cs032.ja11.autocorrect.backend.Engine;
import edu.brown.cs032.ja11.autocorrect.dictionary.Entry;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**CommandLine provides a command line interface to using the autocorrect program **/

public class CommandLine implements PresentingResults {
	
	/** The engine that will actually carry out the program. **/
	private final Engine engine;
	
	/** Constructor
	 * 
	 * @param engine: an Engine to run autocorrect
	 */
	public CommandLine(Engine engine){
		this.engine = engine;
	}
	
	/**
	 * run: until the user escapes using an empty line or an EOF, the program will read the user's input and submit suggestions
	 */
    @Override
	public void run(){
		//Startup
		System.out.println("Ready");
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		
		//Start the Read-loop
		try{
		String line = input.readLine();
		//Escape characters
		while (!((line.equals("\n") || (line.equals(""))))){
			List<Entry> results;
			// Doesn't search if there's trailing whitespace
			if (line.charAt(line.length()-1) != ' '){
				//Filters and splits the line
				String[] filteredLine = line.replaceAll("\\W", " ").replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
				//Checks for multiple words
				if (filteredLine.length>=2){
					results = engine.getTopFiveResults(filteredLine[filteredLine.length-2], filteredLine[filteredLine.length-1]);
				}
				//otherwise: unigram probabilities
				else results = engine.getTopFiveResults(filteredLine[filteredLine.length-1]);
				String prec ="";
				for (int i = 0; i<filteredLine.length-1; i++){
					prec = prec + filteredLine[i]+" ";
				}
				//Outputs the results
				for (Entry i : results){
					System.out.println(prec+ i.getString());
			}
			System.out.print("\n");
			line = input.readLine();
			}
		}
		}
		catch (IOException e){
			System.out.println("Bye!");	
		}
	}
}
