import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class KnapsackRec {
	private static int numCalls;
	private static Item[] items;
	private static LinkedList<Integer> knapsack;

	public static void main(String[] args) {
		int numItems;
		int maxWeight;
		String weightFile;
		String valueFile;


		if(args.length != 4) {
			printUsage();
			return;
		}

		try { 
			numItems = Integer.parseInt(args[0]);
			maxWeight = Integer.parseInt(args[1]);				
		} catch(NumberFormatException e) {
			printUsage();
			return;
		}

		knapsack = new LinkedList<Integer>();
		weightFile = args[2];
		valueFile = args[3];

		try {
			knapsack(numItems, maxWeight, weightFile, valueFile);
		} catch (FileNotFoundException e) {
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
		System.out.println("usage: $ java KnapsackRec n W w.txt v.txt\n"
				+ "	    n: the number of items\n"
				+ "	    W: the maximum weight the Knapsack can carry\n"
				+ "	w.txt: a file containing each individual items' weight (one weight per line)\n"
				+ "        v.txt: a file containing each individual items' value (one value per line)");
		return;
	}

	/**
	 * Constructs a knapsack with arguments taken from the command line, and passes the items from
	 * the items list to further functions to recursively find the optimal load. Prints the results
	 * to the console.
	 * 
	 * @param numItems	- the number of items to take from the items files
	 * @param maxWeight	- the maximum weight the knapsack can hold
	 * @param weightFileName - the file item weights are taken from
	 * @param valueFileName	- the file item values are taken from
	 * 
	 * @throws FileNotFoundException
	 */
	private static void knapsack(int numItems, int maxWeight, String weightFileName, String valueFileName) throws FileNotFoundException {
		numCalls = 0;		
		items = new Item[numItems];
		File weightFile = new File(weightFileName);
		File valueFile = new File(valueFileName);

		Scanner weightScan = new Scanner(weightFile);
		Scanner valueScan = new Scanner(valueFile);

		int i = 0;
		while(weightScan.hasNextLine() && valueScan.hasNextLine() && i < numItems) {
			String weightString = weightScan.nextLine().trim();
			String valueString = valueScan.nextLine().trim();
			items[i] = new Item(Integer.parseInt(valueString), Integer.parseInt(weightString), i);
			i++;
		}
		
		int value = fillKnapsack(numItems, maxWeight);
		int weight = 0;

		Collections.sort(knapsack);
		
		Iterator<Integer> it = knapsack.iterator();
		while(it.hasNext()) {
			int val = it.next();
			weight += items[val].getWeight();
		}

		String returnString = "Optimal Solution:\n{" + returnOptimalSolution() + "}\n"
				+ "Total Weight: " + weight + "\n" + "Optimal Value: " + value + "\n"
				+ "Number of recursive calls: " + numCalls;

		System.out.println(returnString);

		weightScan.close();
		valueScan.close();
		return;
	}

	/**
	 * Takes in the number of the next item to be checked, and the remaining weight in 
	 * a knapsack, and finds the optimal load by value through recursion
	 * 
	 * @param itemNumber	- the number of the next item to be checked
	 * @param weightLeft	- the weight remaining in the knapsack
	 * @return knapsackValue	- the total current value of the items in the knapsack.
	 */
	private static int fillKnapsack(int itemNumber, int weightLeft) {
		int knapsackValue;
		
		numCalls++;

		if(itemNumber == 0 || weightLeft == 0) {
			knapsackValue = 0;
		} else if(items[itemNumber-1].getWeight() > weightLeft) {
			knapsackValue = fillKnapsack(itemNumber-1, weightLeft);
			if(knapsack.contains(itemNumber)) {
				knapsack.removeFirstOccurrence(itemNumber);				
			}
		} else {
			LinkedList<Integer> resetList = knapsack;
			int retValOne = fillKnapsack(itemNumber-1, weightLeft);
			LinkedList<Integer> tmpList = knapsack;
			knapsack = resetList;
			int retValTwo = fillKnapsack((itemNumber-1), (weightLeft-items[itemNumber-1].getWeight())) + items[itemNumber-1].getValue();

			knapsackValue = retValOne > retValTwo ? retValOne:retValTwo;

			if(retValTwo >= retValOne) {
				knapsack = resetList;
				if(!knapsack.contains(itemNumber)) {
					knapsack.add(itemNumber);
				}
			} else if( retValTwo < retValOne) {
				knapsack = tmpList;
				if(knapsack.contains(itemNumber)) {
					knapsack.removeFirstOccurrence(itemNumber);									
				}
			} else {
				knapsack = resetList;
			}	
		}		
		
		return knapsackValue;
	}
	
	/**
	 * Iterates through the list of items in the knapsack, and returns them as
	 * a single string.
	 * 
	 * @return returnString - the String representation of the items in the knapsack.
	 */
	private static String returnOptimalSolution() {
		String returnString = "";
		
		Iterator<Integer> it = knapsack.iterator();
		
		for(int i = 0; i < knapsack.size()-1; i++) {
			int next = it.next();
			returnString += items[next].getNumber() + ",";
		}
				
		returnString += items[(int) it.next()].getNumber();
		
		return returnString;
	}
}