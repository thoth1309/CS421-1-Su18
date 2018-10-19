import java.io.File;
import java.io.FileNotFoundException;

/**
 * Program takes in an adjacency matrix in a command line argument which indicates
 * a file name. The file name is converted into an file and passed to EventNodeGraph
 * object, where the contained adjacency matrix is parsed into an adjacency list, and 
 * processed to retrieve event information, including event name, Earliest completion,
 * latest completion, and slack time for each of the activities. These details are
 * returned to the user via the command console.
 * 
 * @author Jason Egbert
 *
 */
public class CriticalPath {
	
	/**
	 * Main Driver method for CriticalPath
	 * 
	 * @param args - the command line arguments to be processed
	 */
	public static void main(String[] args) {
		
		// ensures the correct number of command line arguments
		if(args.length != 1) {
			printUsage();
			return;	// ends program
		}
		
		// attempt to convert command line argument to file and run the program
		try {
			File adjacencyMatrix = new File(args[0]);	// file with matrix
			
			// create the eventNodeGraph
			EventNodeGraph activityPath = new EventNodeGraph(adjacencyMatrix);
			
			// retrieve the slack time information from the graph
			String dataString = activityPath.getSlackTime();
			
			// return the output values to the user
			String returnString = "";
			returnString += "Activity Node   EC     LC   SlackTime\n";
			returnString += "-----------------------------------------------------\n";
			
			// add in the retrieved data and print
			returnString += dataString;			
			System.out.println(returnString);
		} catch(FileNotFoundException e) {
			
			// file doesn't exist, inform the user of an error, end the program
			printUsage();
			return;
		}
	}
	
	/**
	 * Returns a print usage message if the command line arguments do not meet program 
	 * requirements.
	 * 
	 */
	private static void printUsage() {
		System.out.println("usage: $ java CriticalPath <file name>\n"
				+ "\tfile name: the name of the input file containing an ajacency matrix\n");
		return;
	}

}
