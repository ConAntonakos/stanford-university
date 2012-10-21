/*
 * File: NameSurferDataBase.java
 * -----------------------------
 * This class keeps track of the complete database of names.
 * The constructor reads in the database from a file, and
 * the only public method makes it possible to look up a
 * name and get back the corresponding NameSurferEntry.
 * Names are matched independent of case, so that "Eric"
 * and "ERIC" are the same names.
 */

import acm.util.*;
import java.util.*;
import java.io.*;


public class NameSurferDataBase implements NameSurferConstants {
	
	private Map<String, NameSurferEntry> namesDataBase = new HashMap<String, NameSurferEntry>();
	
/* Constructor: NameSurferDataBase(filename) */
/**
 * Creates a new NameSurferDataBase and initializes it using the
 * data in the specified file.  The constructor throws an error
 * exception if the requested file does not exist or if an error
 * occurs as the file is being read.
 */
	
	//Read in the data of the names-data.txt file line by line and enter it into a HashMap
	public NameSurferDataBase(String filename) {
		try {
			BufferedReader rd = new BufferedReader(new FileReader(filename));
			
			while (true){
				String line = rd.readLine();
				if (line == null) break;
				
				NameSurferEntry nameEntry = new NameSurferEntry(line);
				namesDataBase.put(nameEntry.getName(), nameEntry);
			}
			rd.close();
		} catch (IOException e){
			throw new ErrorException(e);
		}
	}
	
/* Method: findEntry(name) */
/**
 * Returns the NameSurferEntry associated with this name, if one
 * exists.  If the name does not appear in the database, this
 * method returns null.
 */
	public NameSurferEntry findEntry(String name) {
		//Check to see whether the first letter of the name entered is lowercase
		char firstChar = name.charAt(0);
		//If it is, then change it to uppercase
		if (Character.isLowerCase(firstChar)){
			firstChar = Character.toUpperCase(firstChar);
		}
		
		String subWord = name.substring(1);
		
		name = firstChar + subWord;
		
		//If the HashMap namesDataBase contains the name, retrive the data associated with it
		if (namesDataBase.containsKey(name)){
			return namesDataBase.get(name);
		} else {
			return null;
		}
	}
}

