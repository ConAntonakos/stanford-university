/*
 * File: HangmanLexicon.java
 * -------------------------
 * This file contains a stub implementation of the HangmanLexicon
 * class that you will re-implement for Part III of the assignment.
 */

import java.io.BufferedReader;
import java.io.*;
import java.util.*;
import acm.util.*;

//Name: Constantine Antonakos
//Section Leader: Shayon Saleh

public class HangmanLexicon {


	private ArrayList <String> arrayListOfWords = new ArrayList <String> ();

	public HangmanLexicon() {
		//Adds the each individual words in the file to the specified array list
		try {
			BufferedReader hangmanWords = new BufferedReader(new FileReader("HangmanLexicon.txt"));
			//Reads each line
			while(true) {
				String line = hangmanWords.readLine();
				if(line == null) break;
				arrayListOfWords.add(line);
			}
			hangmanWords.close();
		//Exception
		} catch (IOException ex) {
			throw new ErrorException(ex);
		}
	}


	/** Returns the word at the specified index. */
	public String getWord(int index) {
		return arrayListOfWords.get(index);
	}

	/** Returns the number of words in the lexicon. */
	public int getWordCount() {
		return arrayListOfWords.size();
	}
}
