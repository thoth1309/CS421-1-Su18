
/**
 * Driver for The Knight's Tour
 * 
 * @author Jason Egbert
 *
 */
public class KnightTour {
	/**
	 * Builds a KnightBoard, and runs a knight's tour, attempting to 
	 * touch each square only once.
	 * 
	 * @param args
	 * 
	 */
	public static void main(String args[]) {
		int hType;
		int boardSize;
		int xPos;
		int yPos;
		boolean success;
		String retString;

		// too few or too many command line arguments
		if(args.length < 4 || args.length > 4) {
			printUsage();
			return;
		}
		
		// attempt to parse the command line args into a coherent set of 
		// variables to build and run a KnightBoard
		try {
			hType = Integer.parseInt(args[0]);
			boardSize = Integer.parseInt(args[1]);
			xPos = Integer.parseInt(args[2]);
			yPos = Integer.parseInt(args[3]);
		} catch(NumberFormatException e) {
			// you put in something that isn't a number
			printUsage();
			return;	// end main
		}
		
		// integers parsed have unreasonable values
		if(hType < 0 || hType > 3 || boardSize < 3 || xPos < 0 || xPos >= boardSize || yPos < 0 || yPos >= boardSize) {
			printUsage();
			return;	// end main
		}
		
		// Build the board and run the test
		KnightBoard testBoard = new KnightBoard(hType, boardSize);
		success = testBoard.runKnightBoard(xPos, yPos);

		// The start of the string to be returned no matter what.
		retString = "The total number of moves is ";
		
		// determine whether the algorithm found a solution or not
		if (success) {
			retString += testBoard.getAttemptedMoves() + "\n";
			System.out.println(retString + testBoard.toString());
		} else {
			retString += testBoard.getAttemptedMoves() + "\n" + "No solution found!\n";
			System.out.println(retString);
		}
	}
	
	/**
	 * Instructions for correct usage of the KnightTour program.
	 * 
	 */
	private static void printUsage() {
		System.out.println("usage: $ java KnightTour heuristic_level board_size starting_x starting_y\n"
				+ "	heuristic_level: 0 for no heuristic, 1 for heuristic I, 2 for Warnsdorff’s heuristic\n"
				+ "	board_size:      integer >= 3\n"
				+ "	xPos:            integer 0 - board_size-1\n"
				+ "	yPos:            integer 0 - board_size-1\n");
	}
}