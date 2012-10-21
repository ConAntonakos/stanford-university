/*
 * File: NameSurfer.java
 * ---------------------
 * When it is finished, this program will implements the viewer for
 * the baby-name database described in the assignment handout.
 */

import acm.program.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;

public class NameSurfer extends ConsoleProgram implements NameSurferConstants {

	
	private JTextField textField;
	private JButton graphButton;
	private JButton clearButton;
	private NameSurferDataBase namesDB;
	private NameSurferGraph graph;
	
	/**
	 * This method has the responsibility for reading in the database
	 * and initializing the interactors at the bottom of the window.
	 */
	public void run() {
	    // You fill this in, along with any helper methods //
		setupNameSurfer();
		
		graph = new NameSurferGraph();
		add(graph);

		addActionListeners();
		namesDB = new NameSurferDataBase(NAMES_DATA_FILE);

		// validate at bottom. only needed for macs. but ok to leave in otherwise //
		validate();
	}
	//Setups the interface of NameSurfer
	private void setupNameSurfer() {
		add(new JLabel("Name"), SOUTH);
		textField = new JTextField(20);
		add(textField, SOUTH);
		textField.addActionListener(this);
		setButtons();
		add(new JLabel("NameSurfer!"), NORTH);		
	}
	//Setups the JButtons at the bottom of the UI
	private void setButtons() {
		graphButton = new JButton("Graph");
		add(graphButton, SOUTH);
		clearButton = new JButton("Clear");
		add(clearButton, SOUTH);
				
	}

	/** this method gets called when your interactors fire events. */
	public void actionPerformed(ActionEvent e) {
		//When the "Graph" button gets pressed, pull the data from the name in the text field
		if (e.getSource() == graphButton){
			String inputName = textField.getText();
			NameSurferEntry rankings = namesDB.findEntry(inputName);
			if (rankings != null){
				println("Graph: " + rankings.toString());
				graph.addEntry(rankings);
				graph.update();
			}
		//Signal that the data has been cleared, and clear the graph display	
		} else if (e.getSource() == clearButton){
			println(" ");
			println("------------------------ CLEARED ------------------------");
			println(" ");
			graph.clear();
			graph.update();
		}
	}
}
