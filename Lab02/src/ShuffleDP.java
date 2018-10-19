/**
 * Simple program which takes in three strings wherein the third string is equal in 
 * length to the first string plus the second string. The program them checks to
 * determine if the third string is a "shuffle" of the first two strings, i.e. if
 * it contains the same set of characters as the first two, or base, strings and 
 * and maintains the relative ordering of those characters from the base strings.
 * This is done recursively, and the number of recursive calls is tracked by the
 * program for data collection purposes.
 * 
 * Results from the program are returned to the console.

 * @author Jason Egbert
 *
 */
public class ShuffleDP {
	private static int numRefs = 0;
	private static String stringX;
	private static String stringY;
	private static String stringZ;
	private static int[][] shuffleRef;

	/**
	 * Main driver method for the program. Receives user input, ensures that proper
	 * parameters have been met, and sends the received data to helper functions to
	 * be checked for shuffle status. 
	 * 
	 * If there is an error in the input data with regards to program specifications,
	 * a print usage message is displayed and the program is ended.
	 * 
	 * @param args - the set of strings to be checked for shuffleness
	 */

	public static void main(String[] args) {
		// there is an error with the input arguments!
		if((args.length < 3 && args.length > 0) 
				|| (args.length != 0 && args[2].length() != args[0].length()+args[1].length())) {
			printUsage();
			return;
		}

		// empty string plus empty string shuffles to an empty string
		if(args.length == 0) {
			System.out.println("java ShuffleRec\nyes\nNumber of recursive calls: 0");
			return;
		}

		// passes the 3 arguments to shuffleness for parsing and return values
		shuffleness(args);
	}

	/**
	 * Returns a print usage message if the command line arguments do not meet program 
	 * requirements.
	 * 
	 */
	private static void printUsage() {
		System.out.println("usage: $ java ShuffleRec StringX StringY StringZ\n"
				+ "	StringX: First Base String to compare for Shuffled state\n"
				+ "	StringY: Second Base String to compare for Shuffled State\n"
				+ "	StringZ: Potentially Shuffled String to be compared against Base Strings\n"
				+ "                 StringZ must be the same length as StringX + StringY");
		return;
	}

	/**
	 * Takes the command line arguments from main, and parses them. Determines
	 * whether or not they can be used to perform the specified functions, and if so,
	 * checks the input strings for shuffleness.
	 * 
	 * @param args - the command line arguments passed from the main function
	 */
	private static void shuffleness(String[] args) {
		stringX = args[0];	// First base string
		stringY = args[1];	// Second base string
		stringZ = args[2];	// Potentially shuffled string
		boolean shuffle;	// boolean to determine whether or not there is a shuffle
		String retString;	// message to be printed to the screen upon program completion

		// check for shuffle
		shuffle = buildTable();

		// determine the output
		retString = shuffle ? "yes" : "no";
		String retTable = "";
		
		for(int i = 0; i < stringX.length()+1; i++) {
			for(int j = 0; j < stringY.length()+1; j++) {
				retTable += shuffleRef[i][j] + "  ";
			}
			if(i < stringX.length()) {
				retTable += "\n";
			}
		}
		
		// print results to the screen
		System.out.println("java ShuffleRec " + args[0] + " " + args[1] + " " 
				+ args[2] + " \n" + retTable + "\n" + retString + "\n" 
				+ "Number of table references: " + numRefs);

	}

	/**
	 * Compares two characters to see if they are the same.
	 * If true, return true
	 * If false, return false
	 * 
	 * @param first	- first character to compare
	 * @param second - second character to compare
	 * @return boolean true/false comparing first and second
	 */
	private static boolean compChar(char first,char second) {
		return first == second;
	}

	/**
	 * initializes each entry in the table to 0, then checks the previous entry in the
	 * table, and the next characters in stringZ, stringX, and stringY to determine if
	 * stringZ is a shuffle of stringX and stringY.
	 * 
	 * @return isShuffle - boolean value true for shuffle, false for not a shuffle
	 */
	private static boolean buildTable() {
		boolean isShuffle = true;
		int xIndex = 0;
		int yIndex = 0;		
		
		shuffleRef = new int[stringX.length()+1][stringY.length()+1];
		
		// initializes all entries to 0
		for(int i = 0; i < stringX.length()+1; i++) {
			for(int j = 0; j < stringY.length()+1; j++) {
				shuffleRef[i][j] = 0;
			}
		}
		
		// size 0 strings are always true, reflect it in the table
		shuffleRef[xIndex][yIndex] = 1;
		
		// check the strings for shuffle-ness
		for(int zIndex = 0; zIndex < stringZ.length(); zIndex++) {
			if(isOne(xIndex,yIndex) && yIndex < stringY.length() && compChar(stringY.charAt(yIndex), stringZ.charAt(zIndex))) {
				yIndex++;
				shuffleRef[xIndex][yIndex] = 1;
			} else if(isOne(xIndex,yIndex) && xIndex < stringX.length() && compChar(stringX.charAt(xIndex), stringZ.charAt(zIndex))) {
				xIndex++;
				shuffleRef[xIndex][yIndex] = 1;
			} else {
				isShuffle = false;
				break;
			}
		}		
		
		return isShuffle;
	}
	
	/**
	 * Takes in a set of indices to determine whether or not the table entry at the specified
	 * indices is a 1 or a 0. If it is a one the function returns true. Otherwise it returns
	 * false.
	 * 
	 * @param xIndex	- x index to be checked
	 * @param yIndex	- y index to be checked
	 * @return retVal   - the boolean return value
	 */
	private static boolean isOne(int xIndex, int yIndex) {
		numRefs++;	// increment the number of table references
		boolean retVal;
		
		if(xIndex < 0 || yIndex < 0) {
			retVal = false;
		} else {
			retVal = shuffleRef[xIndex][yIndex] == 1 ? true:false;
		}
		
		return retVal;
	}
}