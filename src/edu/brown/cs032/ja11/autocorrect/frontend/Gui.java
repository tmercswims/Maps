package edu.brown.cs032.ja11.autocorrect.frontend;

import edu.brown.cs032.ja11.autocorrect.backend.Engine;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Thomas Mercurio
 */
public class Gui implements PresentingResults {

	private final Engine engine;

    /**
     *
     * @param engine
     */
    public Gui(Engine engine){
		this.engine = engine;
	}

    /**
     *
     */
    @Override
	public void run() {
		JFrame frame = new JFrame("Autocorrect");
		frame.setSize(500, 170);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel(new BorderLayout());
		JLabel inputLabel = new JLabel("Enter your search here:");
		panel.add(inputLabel, BorderLayout.NORTH);
		JTextField input = new JTextField();
		input.setColumns(50);
		input.setVisible(true);
		JTextArea output = new JTextArea();
		output.setRows(5);
		output.setEditable(false);

		panel.add(output, BorderLayout.SOUTH);
		panel.add(input, BorderLayout.CENTER);
		input.getDocument().addDocumentListener(new OutputUpdater(input, output, engine));
		frame.add(panel);

		
		frame.setVisible(true);
		
		

	}

}
