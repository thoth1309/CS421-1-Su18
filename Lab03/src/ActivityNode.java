import java.util.Iterator;
import java.util.LinkedList;

/**
 * ActivityNode object. Stores details about an activity, including
 * the time the activity takes, its topological order details, incoming
 * activities, and subsequent activities, and possible completion times.
 * 
 * @author Jason Egbert
 *
 */
public class ActivityNode {
	private String activityName;
	private int activityTime;
	private int discoverTime;
	private int finishTime;
	private LinkedList<ActivityNode> predecessors;
	private LinkedList<ActivityNode> children;
	private boolean foundIt;
	private int earliestCompletion;
	private int latestCompletion;
	
	/**
	 * Constructor for Activity Node. Requires an activity,
	 * sets all other data to invalid values, and initializes
	 * lists
	 * 
	 * @param activity
	 */
	public ActivityNode(String activity) {
		activityName = activity.trim();
		activityTime = -1;
		discoverTime = -1;
		finishTime = -1;
		predecessors = new LinkedList<ActivityNode>();
		children = new LinkedList<ActivityNode>();
		foundIt = false;
		earliestCompletion = -1;
		latestCompletion = -1;
	}
	
	/**
	 * Sets earliest completion time
	 * 
	 * @param time - time to set EC to
	 */
	public void setEarliestCompletion(int time) {
		earliestCompletion = time;
	}
	
	/**
	 * retrieves earliest completion time
	 * 
	 * @return earliestCompletion
	 */
	public int getEarliestCompletion() {
		return earliestCompletion;
	}
	
	/**
	 * sets the latest completion time
	 * 
	 * @param time - the time to set
	 */
	public void setLatestCompletion(int time) {
		latestCompletion = time;
	}
	
	/**
	 * Retrieves latest completion time
	 * 
	 * @return latestCompletion
	 */
	public int getLatestCompletion() {
		return latestCompletion;
	}
	
	/**
	 * retrieves the slack time of the activity
	 * 
	 * @return 
	 */
	public int getSlackTime() {
		return latestCompletion-earliestCompletion;
	}
	
	/**
	 * Returns boolean to determine whether or not
	 * the node has been discovered in a topological sort
	 * 
	 * @return foundIt
	 */
	public boolean foundIt() {
		return foundIt;
	}
	
	/**
	 * Sets discovery time, and changes foundIt to true
	 * 
	 * @param time - time node was discovered
	 */
	public void setDiscovery(int time) {
		foundIt = true;
		discoverTime = time;
	}
	
	/**
	 * Retrieves discovery time
	 * 
	 * @return discoverTime
	 */
	public int getDiscovery() {
		return discoverTime;
	}
	
	/**
	 * Sets the finish time for topological sort
	 * 
	 * @param time
	 */
	public void setFinish(int time) {
		finishTime = time;
	}
	
	/**
	 * returns the topological finish time
	 * 
	 * @return
	 */
	public int getFinish() {
		return finishTime;
	}
	
	/**
	 * adds a predecessor node to the predecessors list
	 * 
	 * @param predNode
	 */
	public void addPred(ActivityNode predNode) {
		predecessors.add(predNode);
	}
	
	/**
	 * returns the predecessors list
	 * 
	 * @return predecessors
	 */
	public LinkedList<ActivityNode> getPredNode() {
		return predecessors;
	}
	
	/**
	 * adds a new child node to the children list
	 * 
	 * @param nextNode
	 */
	public void addNext(ActivityNode nextNode) {
		children.add(nextNode);
	}
	
	/**
	 * returns the list of children
	 * 
	 * @return children
	 */
	public LinkedList<ActivityNode> getNextNode() {
		return children;
	}
	
	/**
	 * @return
	 */
	public String getActivity() {
		return activityName;
	}
	
	/**
	 * sets the activity time
	 * 
	 * @param time
	 */
	public void setTime(int time) {
		activityTime = time;
	}
	
	/**
	 * retrieves activity time
	 * 
	 * @return
	 */
	public int getTime() {
		return activityTime;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String retString = "";
		
		retString += activityName;
		
		if(children.size() > 0) {
			retString += ": ";
		}
		
		// retrieves the node, and adds each of its adjacent nodes to the string 
		Iterator<ActivityNode> it = children.iterator();
		for(int i = 0; i < children.size()-1; i++) {
			retString += it.next().getActivity();
			retString += ", ";
		}
		if(it.hasNext()) {
			retString += it.next().getActivity();
		}
		
		return retString;
	}
}