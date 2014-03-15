package frontend;

import java.util.Collection;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import autocorrect.Engine;
import dictionary.Entry;

public class OutputUpdater implements DocumentListener {
	
	JTextField input;
	JTextArea output;
	Engine engine;
	
	public OutputUpdater(JTextField input, JTextArea output, Engine engine){
		this.input = input;
		this.output = output;
		this.engine = engine;
	}
	
	@Override
	public void changedUpdate(DocumentEvent e) {
		updateOutput();
	}
	public void updateOutput(){
		output.setText("");
		String line = input.getText();
		Collection<Entry> results = null;
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
			for (Entry j : results){
				output.setText(output.getText()+prec+" "+j.getString()+"\n");
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
