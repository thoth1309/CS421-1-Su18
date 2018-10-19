
public class TableObject {
	private int optimalValue;
	private int tookIt;
	
	public TableObject(int value, boolean taken) {
		optimalValue = value;
		tookIt = taken ? 1:0;
	}
	
	public int getOptimalValue() {
		return optimalValue;
	}
	
	public void setOptimalValue(int optimalValue) {
		this.optimalValue = optimalValue;
	}
	
	public int getTookIt() {
		return tookIt;
	}
	
	public int setTookIt(boolean taken) {
		tookIt = taken ? 1:0;
		return tookIt;
	}
	
}
