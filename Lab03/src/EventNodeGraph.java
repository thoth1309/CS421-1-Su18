import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * EventNodeGraph takes in an input file containing an adjacency matrix for an
 * activity-node graph, and converts it to an adjacency list. From the adjacency
 * list information can be obtained regarding the events, critical path, earliest
 * and latest completion time, and slack time for each of the events in the event
 * node graph.
 * 
 * @author Jason Egbert
 *
 */
public class EventNodeGraph {
	private ActivityNode[] adjacencyList;	// list containing the graph
	private int topoTime;	// start and end time for topological search
	private LinkedList<ActivityNode> topoSort;	// list containing topological sort info
	
	/**
	 * Constructs the initial graph. Sets topoTime to 0 for the topological sort,
	 * instantiates global variables, and begins both the parsing of the input file,
	 * and the topological sort.
	 * 
	 * @param inputMatrix - the input file
	 * @throws FileNotFoundException	
	 */
	public EventNodeGraph(File inputMatrix) throws FileNotFoundException {
		topoTime = 0;
		topoSort = new LinkedList<ActivityNode>();
		adjacencyList = parseInputFile(inputMatrix);
		discoverNodes();
	}
	
	/**
	 * retrieves the Adjacency list containing the graph and returns it
	 * to the user.
	 * 
	 * @return adjacencyList
	 */
	public ActivityNode[] getList() {
		return adjacencyList;
	}
	
	/**
	 * Retrieves the list containing the topological sort of the nodes
	 * in the graph
	 * 
	 * @return topoSort
	 */
	public LinkedList<ActivityNode> getTopoSort(){
		return topoSort; 
	}
	
	/**
	 * Processes the graph to retrieve the events, and the earliest completion time, 
	 * latest completion time, and slack time for each of the nodes. Information is j
	 * stored in the individual node, and converted into a string for return.
	 * 
	 * @return returnString - the string representation of the EC, LC, and slack time
	 */
	public String getSlackTime() {
		String returnString = "";
		LinkedList<ActivityNode> returnList = new LinkedList<ActivityNode>();	// for the return info
		LinkedList<ActivityNode> tmpECList = new LinkedList<ActivityNode>();	// for the EC info
		LinkedList<ActivityNode> tmpLCList = new LinkedList<ActivityNode>();	// for the LC info
		
		// Iterates through the topoSort list and creates three copies, which will all be destroyed
		Iterator<ActivityNode> lit = topoSort.iterator();
		while(lit.hasNext()) {
			ActivityNode tmpNode = lit.next();
			returnList.addFirst(tmpNode);
			tmpECList.addFirst(tmpNode);
			tmpLCList.addFirst(tmpNode);
		}
		
		// find Earliest completion of all the nodes.
		while(!tmpECList.isEmpty()) {
			ActivityNode tmpNode = tmpECList.removeFirst();	// remove the first node from the list
			
			if(tmpNode.getPredNode().size() == 0) {	// if there is no predecessor to the current node
				tmpNode.setEarliestCompletion(tmpNode.getTime());	// EC is time to complete this node
			} else if(tmpNode.getPredNode().size() == 1) {	// if there is one predecessor to the current node
				// time is the EC of the predecessor node + the time to complete this node
				int time = tmpNode.getPredNode().getFirst().getEarliestCompletion() + tmpNode.getTime();
				tmpNode.setEarliestCompletion(time);
			} else {	// if there is more than one predecessor to the current node
				topoTime = -1;
				Iterator<ActivityNode> it = tmpNode.getPredNode().iterator();
				
				// find the predecessor node with the largest EC
				while(it.hasNext()) {
					ActivityNode notReal = it.next();
					if(notReal.getEarliestCompletion() > topoTime) {
						topoTime = notReal.getEarliestCompletion();
					}
				}
				
				// add the time to complete the current node to the longest EC
				topoTime += tmpNode.getTime();
				
				// set the EC for the current node
				tmpNode.setEarliestCompletion(topoTime);
			}
		}
		
		// find Latest completion of all the nodes
		while(!tmpLCList.isEmpty()) {
			ActivityNode tmpNode = tmpLCList.removeLast();	// remove the last node from the list
			
			if(tmpNode.getNextNode().size() == 0) {	// this is the last node, there is no successor
				// LC is EC
				tmpNode.setLatestCompletion(tmpNode.getEarliestCompletion());
			} else if(tmpNode.getNextNode().size() == 1) {	// there is only one next node
				// LC is LC of next minus time of current
				int time = tmpNode.getNextNode().getFirst().getLatestCompletion() - tmpNode.getNextNode().getFirst().getTime();
				tmpNode.setLatestCompletion(time);
			} else {	// there is more than one next node
				// LC is LC of shortest next minus time of current
				topoTime = Integer.MAX_VALUE;
				Iterator<ActivityNode> it = tmpNode.getNextNode().iterator();
				
				// find the successor node with the largest LC
				while(it.hasNext()) {
					ActivityNode notReal = it.next();
					int tmpTime = notReal.getLatestCompletion()-notReal.getTime();
					if(tmpTime < topoTime) {
						topoTime = tmpTime;
					}
				}
								
				// set the LC for the current node
				tmpNode.setLatestCompletion(topoTime);
			}
		}

		/* 
		 * retrieves node name, EC, LC, and slack time from each node in 
		 * topological order, and converts it into a formatted string
		 * ready to be passed to the driver for printing. 
		 *  
		 */
		while(!returnList.isEmpty()) {
			// removes and temporarily stores the first node in the list
			ActivityNode tmpNode = returnList.removeFirst();
			
			// stores required data as strings
			String activity = tmpNode.getActivity();
			String earComp = tmpNode.getEarliestCompletion() + "";
			String latComp = tmpNode.getLatestCompletion() + "";
			String slackTime = tmpNode.getSlackTime() + "";
			
			// determines length of strings to inform formatting later
			int actStrLen = activity.length();
			int earCompStrLen = earComp.length();
			int latCompStrLen = latComp.length();
			
			// adds in activity name, with necessary spacing for formatting.
			returnString += activity;
			for(int i = 0; i <= 15-actStrLen; i++) {
				returnString += " ";
			}
			
			// adds in earliest completion string with necessary spacing for formatting.
			returnString += earComp;
			for(int i = 0; i <= 6-earCompStrLen; i++) {
				returnString += " ";
			}
			
			// adds in latest completion string with necessary spacing for formatting
			returnString += latComp;
			for(int i = 0; i <= 7-latCompStrLen; i++) {
				returnString += " ";
			}
			
			// adds in a newline at the end of each line
			returnString += slackTime + "\n";			
		}
		
		return returnString;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String retString = "";
		
		// returns the node string data for each node in the adjacency list
		for(int i = 0; i < adjacencyList.length; i++) {
			retString += adjacencyList[i].toString();
			if(i != adjacencyList.length-1) {
				retString += "\n";
			}
		}
		
		return retString;
	}
	
	/**
	 * Private function to parse the input file, converting it from Adjacency Matrix to
	 * adjacency list. Stores the newly created graph in an array.
	 * 
	 * @param inputFile	- the input file containing the adjacency matrix
	 * @return returnList - the adjacency list
	 * @throws FileNotFoundException
	 */
	private ActivityNode[] parseInputFile(File inputFile) throws FileNotFoundException{
		int index = 0;
		ActivityNode[] returnList;
		
		Scanner fileScan = new Scanner(inputFile);	// opens the file
		
		String firstLine = fileScan.nextLine();	// saves the first line as a string
		
		// determines the necessary size for our array
		for(int i = 0; i < firstLine.length(); i++) {
			if(firstLine.charAt(i) != ' ') {
				index++;
			}
		}
		
		// initialize the array according to the size just retrieved
		returnList = new ActivityNode[index];
		
		// creates nodes to put in each of the spaces
		int j = 0;
		for(int i = 0; i < firstLine.length(); i++) {
			if(firstLine.charAt(i) != ' ') {
				returnList[j] = new ActivityNode(firstLine.charAt(i) + " ");
				j++;
			}
		}
			
		// generates the adjacency lists for each of the nodes from the remaining 
		// lines in the file
		j = 0;
		while(fileScan.hasNextLine()) {
			int i = -1;
			Scanner lineScan = new Scanner(fileScan.nextLine());
			boolean isFirst = true;
			while(lineScan.hasNext()) {
				String next = lineScan.next();
				if(isFirst) {
					isFirst = false;
				} else if(Integer.parseInt(next) != -1){
					returnList[j].addNext(returnList[i]);
					returnList[i].addPred(returnList[j]);
					returnList[i].setTime(Integer.parseInt(next));
				} 
				i++;
			}
			j++;
			lineScan.close();
		}
				
		fileScan.close();
		
		// ensures time of start node is set to 0
		returnList[0].setTime(0);
		
		return returnList;
	}
	
	/**
	 * Starts with the first node and discovers all nodes topologically
	 */
	private void discoverNodes() {
		visitNode(adjacencyList[0]);
	}
	
	/**
	 * recursively discovers all nodes in a topological sequence, recording
	 * the discovery time, finish time, and saving the topological order in 
	 * a linked list.
	 * 
	 * @param vertex
	 */
	private void visitNode(ActivityNode vertex) {
		topoTime++;
		vertex.setDiscovery(topoTime);
		
		// iterates through each node in the list
		Iterator<ActivityNode> it = vertex.getNextNode().iterator();
		while(it.hasNext()) {
			ActivityNode tmpNode = it.next();
			if(!tmpNode.foundIt()) {
				visitNode(tmpNode);
			}
		}
		topoTime++;
		vertex.setFinish(topoTime);
		topoSort.add(vertex);
	}
}