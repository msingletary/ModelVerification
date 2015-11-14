
public class Condition {
	int low;
	int high;
	// should I have an int single, to make it more clear that it is not always a range?

	public Condition(int single) {
		low = single;
		high = single;
	}

	public Condition(int low, int high) {
		this.low = low;
		this.high = high;
	}

	boolean isConditionSatisfiedBy(int value) {
		if (this.low == this.high) {
			// if low == high this condition is a single value, not a range
			// so it doesn't matter whether you check low or high.
			return (this.low == value);
		} else {
			return ((this.low <= value) && (value < this.high));
		}
	}
	
	public String toString() {
		String output = "";
		if (low == high)
			output += "(" + low + ")";
		else 
			output += "(" + low + ", " + high + ")";
		return output;
	}
}
