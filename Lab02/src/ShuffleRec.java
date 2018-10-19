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
 * 
 * @author Jason Egbert
 *
 */
public class ShuffleRec {
	private static int numCalls = 0;
	private static String stringX;
	private static String stringY;
	private static String stringZ;


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
	 * Parses command line arguments and determines if the third argument is a shuffle
	 * of the first two. Prints yes/no solution to the command line problem, and the
	 * number of recursive calls made to arrive at the solution.
	 * 
	 * @param args - the 3 strings to be compared for shuffleness
	 */
	private static void shuffleness(String[] args) {
		stringX = args[0];	// First base string
		stringY = args[1];	// Second base string
		stringZ = args[2];	// Potentially shuffled string
		int xStringSize = stringX.length();	// starting index of first base string
		int yStringSize = stringY.length();	// starting index of second base string
		boolean shuffle;	// boolean to determine whether or not there is a shuffle
		String retString;	// message to be printed to the screen upon program completion
		
		// check for shuffle
		shuffle = checkShuf(xStringSize, yStringSize);
		
		// determine the output
		retString = shuffle ? "yes" : "no";
		
		// print results to the screen
		System.out.println("java ShuffleRec " + args[0] + " " + args[1] + " " 
				+ args[2] + " \n" + retString + "\n" 
				+ "Number of recursive calls: " + numCalls);
	}

	/**
	 * Compares a global string with two base strings
	 * to determine whether or not the the global string
	 * is a shuffle of the two base strings.
	 * 
	 * @param stringX - first base string
	 * @param stringY - second base string
	 * @return victory - boolean indicating successful shuffle
	 */
	private static boolean checkShuf(int xSize, int ySize) {
		numCalls++;	// increment the number of recursive calls
		boolean victory = false;	// keeps track of shuffle success

		// if all strings are 0
		if(xSize <= 0 && ySize <= 0) {
			victory = true;
		} else if(xSize > 0 && ySize <= 0) {	// if stringX has characters, and stringY does not
			// check characters at the end of stringX and stringZ, if they're the same, check the
			// next characters recursively
			if(compChar(stringX.charAt(xSize-1),stringZ.charAt(xSize-1)) && checkShuf(xSize-1,0)) {
				victory = true;	// it's a shuffle
			} 
		} else if(xSize <= 0 && ySize > 0) {	// if stringY has characters, and stringX does not
			// check characters at the end of stringY and stringZ, if they're the same, check the
			// next characters recursively
			if(compChar(stringY.charAt(ySize-1),stringZ.charAt(ySize-1)) && checkShuf(0,ySize-1)) {
				victory = true;	// it's a shuffle
			}
		} else {	// both stringX and stringY have characters
			// check characters at the end of StringX and stringZ, if they're the same, check the
			// next characters recursively until the shuffle is proven, or disproven, or check
			// characters at the end of StringY and stringZ, if they're the same, check the next 
			// characters recursively.
			if((compChar(stringX.charAt(xSize-1),stringZ.charAt(xSize+ySize-1)) && checkShuf(xSize-1,ySize)) 
					|| compChar(stringY.charAt(ySize-1),stringZ.charAt(xSize+ySize-1)) && checkShuf(xSize,ySize-1)) {
				victory = true;	// it's a shuffle
			}
		}

		return victory;
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
}