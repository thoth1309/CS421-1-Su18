
/**
 * @author Jason Egbert
 *
 */
public class Item {
	private int value;
	private int weight;
	private int number;
	private boolean inKnapsack;
	
	public Item(int value, int weight, int number) {
		this.value = value;
		this.weight = weight;
		this.number = number;
		inKnapsack = false;
	}
	
	public int getValue() {
		return value;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public int getNumber() {
		return number;
	}
	
	public boolean inKnapsack() {
		return inKnapsack;
	}
}
