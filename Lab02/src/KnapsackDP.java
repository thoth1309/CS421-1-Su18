import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class KnapsackDP {
	private final static boolean TOOK = true;
	private final static boolean LEFT = false;
	private static int numCalls;
	private static Item[] items;
	private static LinkedList<Integer> knapsack;
	private static TableObject[][] truthTable;

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
		
		String optimalSolution = returnOptimalSolution(numItems, maxWeight);
		
		Iterator<Integer> it = knapsack.iterator();
		while(it.hasNext()) {
			int val = it.next();
			weight += items[val].getWeight();
		}

		String returnString = "Optimal Solution:\n{" + optimalSolution + "}\n"
				+ "Total Weight: " + weight + "\n" + "Optimal Value: " + value + "\n"
				+ "Number of recursive calls: " + numCalls;

		System.out.println(returnString);

		weightScan.close();
		valueScan.close();
		return;
	}
	
	private static int fillKnapsack(int numItems, int maxWeight) {
		int xMax = numItems+1;
		int yMax = maxWeight+1;
		truthTable = new TableObject[xMax][yMax];
		
		for(int xIndex = 0; xIndex < xMax; xIndex++) {
			for(int yIndex = 0; yIndex < yMax; yIndex++) {
				if(xIndex == 0 || yIndex == 0) {
					truthTable[xIndex][yIndex] = new TableObject(0, LEFT);
				} else if(items[xIndex-1].getWeight() > yIndex) {
					truthTable[xIndex][yIndex] = new TableObject(getTotal(xIndex-1,yIndex), LEFT);
				} else {
					int valueOne = getTotal(xIndex-1, yIndex);
					int valueTwo = getTotal(xIndex-1, yIndex-items[xIndex-1].getWeight())+items[xIndex-1].getValue();
					
					if(valueOne > valueTwo) {
						truthTable[xIndex][yIndex] = new TableObject(valueOne, LEFT);
					} else {
						truthTable[xIndex][yIndex] = new TableObject(valueTwo, TOOK);
					}					
				}
			}
		}
		
		System.out.println("finished");
		
		return truthTable[xMax-1][yMax-1].getOptimalValue();
	}
	
	private static int getTotal(int xIndex, int yIndex) {
		return truthTable[xIndex][yIndex].getOptimalValue();
	}
	
	private static String returnOptimalSolution(int numItems, int maxWeight) {
		String optimalSolution = "";
		int xIndex = 1;
		int yIndex = maxWeight;
		
		while(xIndex < numItems+1) {
			if(truthTable[xIndex][yIndex].getTookIt() == 1) {
				knapsack.add(xIndex);
			}
			xIndex++;
		}
		
		Iterator<Integer> it = knapsack.iterator();
		
		for(int i = 0; i < knapsack.size()-1; i++) {
			int next = it.next();
			optimalSolution += items[next].getNumber() + ",";
		}
				
		optimalSolution += items[it.next()].getNumber();
		
		return optimalSolution;
		
	}
}
