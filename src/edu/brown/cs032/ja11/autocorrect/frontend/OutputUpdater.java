package edu.brown.cs032.ja11.autocorrect.frontend;

import edu.brown.cs032.ja11.autocorrect.backend.Engine;
import edu.brown.cs032.ja11.autocorrect.dictionary.Entry;
import java.util.Collection;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Thomas Mercurio
 */
public class OutputUpdater implements DocumentListener {
	
	JTextField input;
	JTextArea output;
	Engine engine;

    /**
     *
     * @param input
     * @param output
     * @param engine
     */
    public OutputUpdater(JTextField input, JTextArea output, Engine engine){
		this.input = input;
		this.output = output;
		this.engine = engine;
	}
	
	@Override
	public void changedUpdate(DocumentEvent e) {
		updateOutput();
	}

    /**
     *
     */
    public void updateOutput(){
		output.setText("");
		String line = input.getText();
		Collection<String> results;
		if (line!=null&& !line.equals("")){
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
				 prec = prec +" "+ filteredLine[i];
			}
			for (String j : results){
				output.setText(output.getText()+prec+" "+j+"\n");
			}
		}
		}
	}
		

	@Override
	public void insertUpdate(DocumentEvent e) {
		updateOutput();
		
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		updateOutput();
		
	}





}
