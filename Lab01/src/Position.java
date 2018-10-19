/**
 * Position object, stores data about a given position, including
 * the x and y coordinates of the position, whether or not it has
 * been used, and the move number for when the knight hit the 
 * position.
 * 
 * @author Jason Egbert
 *
 */
public class Position {
	private enum Occupation {
		OPEN, OCCUPIED
	}
	private int moveNum;
	private Occupation occ;
	private int xIndex;
	private int yIndex;
	
	/**
	 * creates a position object for use in keeping track of the occupation status
	 * of a position on a board
	 * 
	 */
	public Position(int xPos, int yPos) {
		occ = Occupation.OPEN;
		moveNum = 0;
		xIndex = xPos;
		yIndex = yPos;
	}
	
	/**
	 * Allows the position to receive a new moveNum
	 * 
	 * @param move - the moveNumber to change moveNum to
	 * 
	 */
	public void setMoveNum(int move) {
		moveNum = move;
	}
	
	/**
	 * Method to retrieve moveNum from the position object
	 * 
	 * @return moveNum - the move number at which the position was occupied
	 * 
	 */
	public int getMoveNum() {
		return moveNum;
	}
	
	/**
	 * Alters the status of the position object's occupation
	 * 
	 * @param status - 0 is open, 1 is used, 2 is occupied
	 * 
	 */
	public void setOccupation(int status) {
		switch (status) {
		case 0:
			occ = Occupation.OPEN;
			break;
		case 1:
			occ = Occupation.OCCUPIED;
			break;
		default:
			System.out.println("Invalid Occupation Status\n");
			break;
		}
	}
	
	/**
	 * Method to retrieve the occupation status of a position object
	 * 
	 * @return retVal - 0 if open, 1 if used, 2 if occupied, and -1 if error
	 * 
	 */
	public int getOccupation() {
		int retVal;
		switch (occ) {
		case OPEN:
			retVal = 0;
			break;
		case OCCUPIED:
			retVal = 1;
			break;
		default:
			retVal = -1;
			break;
		}
		
		return retVal;
	}
	
	/**
	 * returns this positions' xIndex
	 * @return
	 * 
	 */
	public int getXIndex() {
		return xIndex;
	}
	
	/**
	 * returns this positions' yIndex
	 * 
	 * @return yIndex
	 * 
	 */
	public int getYIndex() {
		return yIndex;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.valueOf(moveNum);
	}
}