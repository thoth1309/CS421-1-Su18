/**
 * Board to keep track of Knight's tour, as well as the 
 * functional code that determines the movements of the 
 * knight on the KnightBoard
 * 
 * @author Jason Egbert
 *
 */
public class KnightBoard {
	/* enumeration for Heuristic Type */
	private enum Heuristic {
		NO_HEUR, BORDERS, WARNSDORFF, ERROR
	}
	
	/* Private variables for KnightBoard */
	private Heuristic type;	// the heuristic being used by this board
	private int boardSize;	// the size of one dimension of the board
	private Position[][] knightBoard;	// the actual board as an array
	private int attemptedMoves;	// the number of moves attempted to complete the tour
	private int moveNum;	// the current move number (never higher than maxMoves)
	private int maxMoves;	// the maximum number of moves that can be made in an ideal tour
	
	/**
	 * initializes KnightBoard of specified size, and runs through the scenario given by the user
	 * 
	 * @param initSize
	 * @param begX
	 * @param begY
	 * 
	 */
	public KnightBoard(int type, int initSize) {
		this.type = retMoveType(type);
		initBoard(initSize);
		boardSize = initSize;
		maxMoves = initSize*initSize;
		moveNum = 0;
		attemptedMoves = 0;
	}
	
	/**
	 * Passes input parameters to the correct function for processing
	 * 
	 * @param xPos - starting x position
	 * @param yPos - starting y position
	 * 
	 */
	public boolean runKnightBoard(int xPos, int yPos) {
		boolean retVal;
		
		Position startPoint = knightBoard[xPos][yPos];
		
		switch (this.type) {
		case NO_HEUR:
			retVal = visitNoHeur(startPoint);
			break;
		case BORDERS:
			retVal = visitBorders(startPoint);
			break;
		case WARNSDORFF:
			retVal = visitWarnsdorff(startPoint);
			break;
		default:
			retVal = visitNoHeur(startPoint);
		}		
		return retVal;
	}
	
	/**
	 * get solution count for use outside the KnightBoard object
	 * 
	 * @return solnCount - the number of moves it took to get a solution.
	 * 
	 */
	public int getAttemptedMoves() {
		return attemptedMoves;
	}
	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 * 
	 */
	public String toString() {
		String retString;
		int index;	// allows Y index to be appended on first line
		retString = "  ";	// offset for first index

		// append Y index values on the first line
		for(index = 0;index < boardSize; index++) {
			// offsets to ensure values line up for board sizes with fewer than 100000 squares
			if(maxMoves >= 10 && index < 10) {
				retString += " ";
			}
			if(maxMoves >= 100 && index < 100) {
				retString += " ";
			}
			if(maxMoves >= 1000 && index < 1000) {
				retString += " ";
			}
			if(maxMoves >= 10000 && index < 10000) {
				retString += " ";
			}
			// append the index
			retString += Integer.toString(index) + " ";
		}
	
		// end of first line
		retString += "\n";

		// Fill in the rest of the lines
		for(int i = 0; i < boardSize; i++) {
			// appends X index at the start of a given line
			retString += Integer.toString(i);
			if(i < 10) {
				retString += " ";
			}
			
			// appends the move number of a given square to the line
			for(int j = 0; j < boardSize; j++) {
				// offsets to ensure values line up for board sizes with fewer than 100000 squares
				if(knightBoard[i][j].getMoveNum() < 10) {
					retString += " ";
				}
				if(knightBoard[i][j].getMoveNum() < 100 && maxMoves >= 100) {
					retString += " ";
				}
				if(knightBoard[i][j].getMoveNum() < 1000 && maxMoves >= 1000) {
					retString += " ";
				}
				if(knightBoard[i][j].getMoveNum() < 10000 && maxMoves >= 10000) {
					retString += " ";
				}
				// append the value
				retString += knightBoard[i][j].toString() + " ";
			}
		
			// end of line
			if(i < boardSize-1) {
				retString += "\n";
			}
			if(i == boardSize-1) {
				retString += "\n";
			}
		}

		return retString;
	}	
	
	/**
	 * Initializes all spaces in the KnightBoard array to empty, new
	 * positions
	 * 
	 */
	private void initBoard(int initSize) {
		knightBoard = new Position[initSize][initSize];
		// create a board composed of new Positions
		for(int xIndex = 0; xIndex < initSize; xIndex++) {
			for(int yIndex = 0; yIndex < initSize; yIndex++) {
				knightBoard[xIndex][yIndex] = new Position(xIndex,yIndex);
			}
		}
	}
	
	/**
	 * Parses the input "type", and returns an enumeration value
	 * representing the movement algorithm type for the current
	 * KnightBoard
	 * 
	 * @param type - the movement type as an integer
	 * @return retVal - the enumeration type representing the movement type
	 * 
	 */
	private Heuristic retMoveType(int type) {
		// the enumeration to be returned
		Heuristic retVal;	
		
		switch (type) {
		case 0:		// selected no Heuristic
			retVal = Heuristic.NO_HEUR;
			break;
		case 1:		// selected Heuristic approach
			retVal = Heuristic.BORDERS;
			break;
		case 2:		// selected Warnsdorff's Heuristic
			retVal = Heuristic.WARNSDORFF;
			break;
		default:	// if something weird happened
			retVal = Heuristic.ERROR;
			break;
		}
		return retVal;
	}
	
	/**
 	 * moves the "knight" according to a simple clockwise strategy, operating without 
	 * heuristics to try to find a route to complete the Knight's Tour with the constructed
	 * board.
	 * 
	 * @param startPoint - starting position
	 * @return false for failure, true for success
	 * 
	 */
	private boolean visitNoHeur(Position startPoint) {
		// the number of moves taken, to be placed in each position.
		int tmpVal;
		boolean finished;
		boolean retVal;
		Position tmpPosit;
		int xPos;
		int yPos;
		
		// initialize relevant variables
		tmpVal = moveNum;
		finished = false;
		xPos = startPoint.getXIndex();
		yPos = startPoint.getYIndex();
		
		// increment moveNum and total attempts
		moveNum++;
		attemptedMoves++;

		// set current position to visited
		startPoint.setMoveNum(moveNum);
		startPoint.setOccupation(1);
				
		// up 2,  right 1
		if(xPos > 1 && yPos < boardSize-1 && notVisited(xPos-2, yPos+1) && !finished) {
			tmpPosit = knightBoard[xPos-2][yPos+1];
			finished = visitNoHeur(tmpPosit);	// visit the position
			if(!finished) {
				resetPosition(tmpPosit);	// didn't work, reset the numbers
			}				
		}
		
		// right 2 
		if(yPos < boardSize-2) {
			// up 1
			if(xPos > 0 && notVisited(xPos-1, yPos+2) && !finished) {
				tmpPosit = knightBoard[xPos-1][yPos+2];
				finished = visitNoHeur(tmpPosit);	// visit the position
				if(!finished) {
					resetPosition(tmpPosit);	// didn't work, reset the numbers
				}
			}
			
			// down 1
			if(xPos < boardSize-1 && notVisited(xPos+1, yPos+2) && !finished) {
				tmpPosit = knightBoard[xPos+1][yPos+2];
				finished = visitNoHeur(tmpPosit);	// visit the position
				if(!finished) {
					resetPosition(tmpPosit);	// didn't work, reset the numbers
				}
			}
		}
		
		// down 2 
		if(xPos < boardSize-2) {
			// right 1
			if(yPos < boardSize-1 && notVisited(xPos+2, yPos+1) && !finished) {
				tmpPosit = knightBoard[xPos+2][yPos+1];
				finished = visitNoHeur(tmpPosit);	// visit the position
				if(!finished) {
					resetPosition(tmpPosit);	// didn't work, reset the numbers
				}
			}
			
			// left 1
			if(yPos > 0 && notVisited(xPos+2, yPos-1) && !finished) {
				tmpPosit = knightBoard[xPos+2][yPos-1];
				finished = visitNoHeur(tmpPosit);	// visit the position
				if(!finished) {		
					resetPosition(tmpPosit);	// didn't work, reset the numbers
				}
			}
		}	
		
		// left 2
		if(yPos > 1) {
			// down 1
			if(xPos < boardSize-1 && notVisited(xPos+1, yPos-2) && !finished) {
				tmpPosit = knightBoard[xPos+1][yPos-2];
				finished = visitNoHeur(tmpPosit);	// visit the position
				if(!finished) {
					resetPosition(tmpPosit);	// didn't work, reset the numbers
				}
			}
			
			// up 1
			if(xPos > 0 && notVisited(xPos-1,yPos-2) && !finished) {
				tmpPosit = knightBoard[xPos-1][yPos-2];
				finished = visitNoHeur(tmpPosit);	// visit the position
				if(!finished) {
					resetPosition(tmpPosit);	// didn't work, reset the numbers
				}
			}
		}
		// up 2, left 1
		if(xPos > 1 && yPos >  0 && notVisited(xPos-2, yPos-1) && !finished) {
			tmpPosit = knightBoard[xPos-2][yPos-1];
			finished = visitNoHeur(tmpPosit);	// visit the position
			if(!finished) {
				resetPosition(tmpPosit);	// didn't work, reset the numbers
			}
		}
		
		// make sure we were successful, if not, then we're backtracking and have to account for it
		if(moveNum != maxMoves) {
//			finished = false;
			moveNum = tmpVal;
		} else {	// we were successful
			finished = true;
		}
		
		retVal = finished;
		
		return retVal;
	}
	
	/**
	 * Moves the "knight" according to a simple heuristic in which, whenever
	 * there are multiple eligible moves from the starting position, the preferred
	 * starting move is the move closer to the border of the chess board.
	 * 
	 * @param startPoint - starting position
	 * @return false for failure, true for success
	 * 
	 */
	private boolean visitBorders(Position startPoint) {
		Position potMove[];	// holds each valid position in index from 0-7, leaves invalid moves null
		int distFromBord[];	// collects ditance from border of each valid position in corresponding indexes
		int xPos;	// x index
		int yPos;	// y index
		int tmpMoves;		// the number of moves taken, to be placed in each position upon completion of the tour
		boolean finished;	// checks whether or not the tour is complete
		boolean retVal;	// return value
		
		// initialize relevant values
		tmpMoves = moveNum;
		finished = false;
		potMove = new Position[8];
		distFromBord = new int[8];
		xPos = startPoint.getXIndex();
		yPos = startPoint.getYIndex();

		moveNum++;	// increments the number of moves required to reach a given space
		attemptedMoves++;	// increments the total number of attempted moves during the given knight's tour

		// change current position to used
		startPoint.setMoveNum(moveNum);
		startPoint.setOccupation(1);
		
		// check distance from border for all valid moves, add the valid moves to 
		// potMove array, with their corresponding distance from the border in the 
		// same index of distFromBord array.
		// check distance from border up
//		if(xPos > 1) {
			// right
			if(xPos > 1 && yPos < boardSize-1 && notVisited(xPos-2,yPos+1)) {
				potMove[0] = knightBoard[xPos-2][yPos+1];
				distFromBord[0] = checkBorder(xPos-2) + checkBorder(yPos+1);
			} else {	// not a valid move, set distance from border to -1
				distFromBord[0] = -1;
			}
//		} else {	// not a valid move, set distance from border to -1
//			distFromBord[0] = distFromBord[1] = -1;
//		}
		
		// check distance from border right
		if(yPos < boardSize-2) {
			// up
			if(xPos > 0 && notVisited(xPos-1, yPos+2)) {
				potMove[1] = knightBoard[xPos-1][yPos+2];
				distFromBord[1] = checkBorder(xPos-1) + checkBorder(yPos+2);
			} else {	// not a valid move, set distance from border to -1
				distFromBord[1] = -1;
			}
			
			// down
			if(xPos < boardSize-1 && notVisited(xPos+1, yPos+2)) {
				potMove[2] = knightBoard[xPos+1][yPos+2];
				distFromBord[2] = checkBorder(xPos+1) + checkBorder(yPos+2);
			} else {	// not a valid move, set distance from border to -1
				distFromBord[2] = -1;
			}
		} else {	// not a valid move, set distance from border to -1
			distFromBord[1] = distFromBord[2] = -1;
		}
		
		// check distance from border down
		if(xPos < boardSize-2) {
			// right
			if(yPos < boardSize-1 && notVisited(xPos+2, yPos+1)) {
				potMove[3] = knightBoard[xPos+2][yPos+1];
				distFromBord[3] = checkBorder(xPos+2) + checkBorder(yPos+1);
			} else {	// not a valid move, set distance from border to -1
				distFromBord[3] = -1;
			}
			
			// left
			if(yPos > 0 && notVisited(xPos+2, yPos-1)) {
				potMove[4] = knightBoard[xPos+2][yPos-1];
				distFromBord[4] = checkBorder(xPos+2) + checkBorder(yPos-1);
			} else {	// not a valid move, set distance from border to -1
				distFromBord[4] = -1;
			}
		} else {	// not a valid move, set distance from border to -1
			distFromBord[3] = distFromBord[4] = -1;
		}
		
		// check distance from border left
		if(yPos > 1) {
			// down
			if(xPos < boardSize-1 && notVisited(xPos+1, yPos-2)) {
				potMove[5] = knightBoard[xPos+1][yPos-2];
				distFromBord[5] = checkBorder(xPos+1) + checkBorder(yPos-2);
			} else {	// not a valid move, set distance from border to -1
				distFromBord[5] = -1;
			}
			
			// up
			if(xPos > 0 && notVisited(xPos-1, yPos-2)) {
				potMove[6] = knightBoard[xPos-1][yPos-2];
				distFromBord[6] = checkBorder(xPos-1) + checkBorder(yPos-2);
			} else {	// not a valid move, set distance from border to -1
				distFromBord[6] = -1;
			}
		} else {	// not a valid move, set distance from border to -1
			distFromBord[5] = distFromBord[6] = -1;
		}
		// look up and left
		if(xPos > 1 && yPos > 0 && notVisited(xPos-2,yPos-1)) {
			potMove[7] = knightBoard[xPos-2][yPos-1];
			distFromBord[7] = checkBorder(xPos-2) + checkBorder(yPos-1);
		} else {	// not a valid move, set distance from border to -1
			distFromBord[7] = -1;
		}
		

		
		int arrayCounter = 8;	// counts down number of indices to check
		
		// adds indices of the valid distFromBorder entries to queue searchOrder 
		// from smallest distance in distFromBord to largest distance. This enables
		// easy access to the correct order of positions from the potMove array.
		while(arrayCounter > 0 && !finished) {
			int tmpIndex = 0;
			
			// ignores leading -1 values (invalid indexes)
			while(tmpIndex < 8 && distFromBord[tmpIndex] < 0) {
				tmpIndex++;
			}
			
			// all indexes are -1, end the loop
			if(tmpIndex > 7) {
				break;
			}
			
			// finds the smallest value in the array
			for(int index = tmpIndex+1; index < 8; index++) {
				if(distFromBord[index] > -1 && distFromBord[index] < distFromBord[tmpIndex]) {
					tmpIndex = index;
				}
			}
			
			// calls moveBorders on the potential moves
			finished = visitBorders(potMove[tmpIndex]);
			
			// if moved is true, we found a successful path, don't worry about any other positions!!
			if(!finished) {
				resetPosition(potMove[tmpIndex]);
			} 

			// set the value to -1 so it won't be used again
			distFromBord[tmpIndex] = -1;
			
			// decrease arrayCounter
			arrayCounter--;
		}
			
		// if we made it through the for:each loop and haven't found the path, make sure that
		// the return value is false, and reset the moveNum to it's incoming value
		if(moveNum != maxMoves) {
			finished = false;
			moveNum = tmpMoves;
		} else {	// we were successful
			finished = true;
		}	
		
		retVal = finished;
		
		return retVal;
	}
	
	/**
	 * Moves the "knight" according to Warnsdorff's heuristic in which, 
	 * whenever there are multiple eligible next moves, the desired first
	 * move is the move with the fewest onward moves.
	 * 
	 * @param startPoint - starting position for the function
	 * @return false for failure, true for success
	 * 
	 */
	private boolean visitWarnsdorff(Position startPoint) {
		boolean retVal;	// return boolean
		boolean finished;	// boolean to determine whether we're finished	
		int xPos;	// x coordinate
		int yPos;	// y coordinate
		int tmpMoves;	// number of moves at the start of the visit
		Position[] positArray;	// array of available positions
		int[] movesArray;	// array of the number of moves available from each position
		
		// initialize relevant values
		positArray = new Position[8];
		movesArray = new int[8];
		tmpMoves = moveNum;
		finished = false;
		xPos = startPoint.getXIndex();
		yPos = startPoint.getYIndex();
		
		// increment count values
		moveNum++;
		attemptedMoves++;
		
		// set the position to used, and give it a visitation number
		startPoint.setMoveNum(moveNum);
		startPoint.setOccupation(1);
		
		// checking up
		if(xPos > 1) {
			// look left and add number of moves to movesArray
			if(yPos > 0 && notVisited(xPos-2, yPos-1)) {
				positArray[7] = knightBoard[xPos-2][yPos-1];
				movesArray[7] = checkMoves(positArray[7]);
			} else {
				movesArray[7] = -1;
			}
			// look right and add number of moves to movesArray
			if(yPos < boardSize-1 && notVisited(xPos-2, yPos+1)) {
				positArray[0] = knightBoard[xPos-2][yPos+1];
				movesArray[0] = checkMoves(positArray[0]);
			} else {
				movesArray[0] = -1;
			}
		} else {
			movesArray[7] = movesArray[0] = -1;
		}
		
		// checking right
		if(yPos < boardSize-2) {
			// look up and add number of moves to moves array
			if(xPos > 0 && notVisited(xPos-1, yPos+2)) {
				positArray[1] = knightBoard[xPos-1][yPos+2];
				movesArray[1] = checkMoves(positArray[1]);
			} else {
				movesArray[1] = -1;
			}
			// look down and add number of moves to moves array
			if(xPos < boardSize-1 && notVisited(xPos+1, yPos+2)) {
				positArray[2] = knightBoard[xPos+1][yPos+2];
				movesArray[2] = checkMoves(positArray[2]);
			} else {
				movesArray[2] = -1;
			}
		} else {
			movesArray[1] = movesArray[2] = -1;
		}
		
		// checking down
		if(xPos < boardSize-2) {
			// look right and add number of moves to moves array
			if(yPos < boardSize-1 && notVisited(xPos+2, yPos+1)) {
				positArray[3] = knightBoard[xPos+2][yPos+1];
				movesArray[3] = checkMoves(positArray[3]);
			} else {
				movesArray[3] = -1;
			}
			
			// look left and add number of moves to moves array
			if(yPos > 0 && notVisited(xPos+2, yPos-1)) {
				positArray[4] = knightBoard[xPos+2][yPos-1];
				movesArray[4] = checkMoves(positArray[4]);
			} else {
				movesArray[4] = -1;
			}
		} else {
			movesArray[3] = movesArray[4] = -1;
		}
		
		// checking left
		if(yPos > 1) {
			// look down and add number of moves to moves array
			if(xPos < boardSize-1 && notVisited(xPos+1, yPos-2)) {
				positArray[5] = knightBoard[xPos+1][yPos-2];
				movesArray[5] = checkMoves(positArray[5]);
			} else {
				movesArray[5] = -1;
			}
			
			// look up and add number of moves to moves array
			if(xPos > 0 && notVisited(xPos-1, yPos-2)) {
				positArray[6] = knightBoard[xPos-1][yPos-2];
				movesArray[6] = checkMoves(positArray[6]);
			} else {
				movesArray[6] = -1;
			}
		} else {
			movesArray[5] = movesArray[6] = -1;
		}
		
		// count down indicator value
		int countDown = 8;
		
		// while the array still has values
		while(countDown > 0 && !finished) {
			int tmpIndex = 0;	// index of the smallest value
			
			// skip leading invalid moves
			while(tmpIndex < 8 && movesArray[tmpIndex] < 0) {
				tmpIndex++;
			}
			
			// there are no valid moves
			if(tmpIndex > 7) {
				break;
			}
			
			// search for the smallest value, collect its index
			for(int index = tmpIndex+1; index < 8; index++) {
				if(movesArray[index] > -1 && movesArray[tmpIndex] > movesArray[index]) {
					tmpIndex = index;
				}
			}
			
			// visit the position with the lowest number of moves
			finished = visitWarnsdorff(positArray[tmpIndex]);
			
			// if moved is true, we found a successful path, don't worry about any other positions!!
			if(!finished) {
				resetPosition(positArray[tmpIndex]);
			} 

			// eliminate the used move from the list of available moves
			movesArray[tmpIndex] = -1;			
			countDown--;
		}
		
		// we didn't find a valid option
		if(moveNum != maxMoves) {
			finished = false;
			moveNum = tmpMoves;
		} else {	// we were successful
			finished = true;
		}		
		
		retVal = finished;
		
		return retVal;
	}
	
	/**
	 * returns boolean to determine whether the position at the 
	 * provided x and y coordinates is open
	 * 
	 * @param xPos - the x coordinate of the position to be checked
	 * @param yPos - the y coordinate of the position to be checked
	 * @return the occupation status of the specified position
	 * 				- true if open
	 * 				- false if not open
	 * 
	 */
	private boolean notVisited(int xPos, int yPos) {
		return (knightBoard[xPos][yPos].getOccupation() == 0);
	}
	
	/**
	 * resets a position to its open state
	 * 
	 * @param xPos - the x coordinate of the position to be reset
	 * @param yPos - the y coordinate of the position to be reset
	 * 
	 */
	private void resetPosition(Position posit) {
		posit.setMoveNum(0);
		posit.setOccupation(0);		
	}
	
	/**
	 * Checks the distance from the input index to the nearest edge
	 * 
	 * @param index - the x or y coordinate of the position to be checked
	 * @return retVal - the distance of the coordinate from the closest edge
	 * 
	 */
	private int checkBorder(int index) {
		int retVal;
		int middle = boardSize/2;

		// check location of index
		if(index < middle) {	// index is closer to low value border
			retVal = index;
		} else {	// index is closer to high value border
			retVal = boardSize-1 - index;
		}
		
		return retVal;
	}
	
	/**
	 * returns the number of valid moves from a given position determined
	 * by a provided x and y coordinate. Will return an integer between 0 and 8
	 * 
	 * @param posit - the position to be checked
	 * @return validMoves - the number of valid moves from the given location
	 * 
	 */
	private int checkMoves(Position posit) {
		int validMoves = 0;
		int xPos = posit.getXIndex();
		int yPos = posit.getYIndex();
		
		// up from start
		if(xPos > 1) {
			// left
			if(yPos > 0 && notVisited(xPos-2, yPos-1)) {
				validMoves++;
			}
			
			// right
			if(yPos < boardSize-1 && notVisited(xPos-2, yPos+1)) {
				validMoves++;
			}
		}
		
		// right from start
		if(yPos < boardSize-2) {
			// up
			if(xPos > 0 && notVisited(xPos-1, yPos+2)) {
				validMoves++;
			}
			
			// down
			if(xPos < boardSize-1 && notVisited(xPos+1, yPos+2)) {
				validMoves++;
			}
		}
		
		// down from start
		if(xPos < boardSize-2) {
			// right
			if(yPos < boardSize-1 && notVisited(xPos+2, yPos+1)) {
				validMoves++;
			}
			
			// left
			if(yPos > 0 && notVisited(xPos+2, yPos-1)) {
				validMoves++;
			}
		}
		
		// left from start
		if(yPos > 1) {
			// down
			if(xPos < boardSize-1 && notVisited(xPos+1, yPos-2)) {
				validMoves++;
			}
			
			// up
			if(xPos > 0 && notVisited(xPos-1, yPos-2)) {
				validMoves++;
			}
		}
		
		return validMoves;		
	}
}
