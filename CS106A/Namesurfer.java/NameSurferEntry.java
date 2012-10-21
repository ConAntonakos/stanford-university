/*
 * File: NameSurferEntry.java
 * --------------------------
 * This class represents a single entry in the database.  Each
 * NameSurferEntry contains a name and a list giving the popularity
 * of that name for each decade stretching back to 1900.
 */

import acm.util.*;
import java.io.*;
import java.util.*;

public class NameSurferEntry implements NameSurferConstants {
	
//	private Map<String, Integer> mapNameSurfer;
	
	/** Instance variable **/
	private String entryName;
	private int[] entryRankings = new int[NDECADES];

/* Constructor: NameSurferEntry(line) */
/**
 * Creates a new NameSurferEntry from a data line as it appears
 * in the data file.  Each line begins with the name, which is
 * followed by integers giving the rank of that name for each
 * decade.
 */
	public NameSurferEntry(String line) {
		
		/*Split the line that is read from NameSurferDataBase
		 *into tokens in an array of strings*/
		String[] tokens = line.split(" ");
		//The zeroth index is the entry name of the line
		entryName = tokens[0];
		
//		mapNameSurfer = new HashMap<String, Integer>();
//		constructMap(mapNameSurfer, tokens);
		
		constructRankingsArray(tokens);
	}
	//Construct an array of rankings of integers from the token list
	//The token list is of data type "string" which has to be changed to "int"
	private void constructRankingsArray(String[] tokens) {
		for (int rank = 1; rank < tokens.length; rank++){
			int popularity = Integer.parseInt(tokens[rank]);
			entryRankings[rank-1] = popularity;
		}
	}

	/*
	private void constructMap(Map<String, Integer> map, String[] tokens) {

		for (int i = 1; i < tokens.length; i++){
			if (tokens[i].length() != 0){
				int popularity = Integer.parseInt(tokens[i]);
				map.put(tokens[0], popularity);				
			}

		}
	}*/

/* Method: getName() */
/**
 * Returns the name associated with this entry.
 */
	public String getName() {
		
		return entryName;
		
	}

/* Method: getRank(decade) */
/**
 * Returns the rank associated with an entry for a particular
 * decade.  The decade value is an integer indicating how many
 * decades have passed since the first year in the database,
 * which is given by the constant START_DECADE.  If a name does
 * not appear in a decade, the rank value is 0.
 */
	public int getRank(int decade) {
		
		return entryRankings[decade];
	}

/* Method: toString() */
/**
 * Returns a string that makes it easy to see the value of a
 * NameSurferEntry.
 */
	public String toString() {
		String convertToString = entryName + " [";
		for (int i = 0; i < NDECADES; i++){
			if (i == NDECADES-1){
				convertToString += getRank(i);
			} else {	
				convertToString += getRank(i) + " ";
			}

		}
		convertToString += "]";
		return convertToString;
	}
}

