
/**
 * This class represents a single condition of a state within an FSA. 
 * This condition can either specify a single value or a range of values.
 */
public class Condition {
	private int low;
	private int high;

	/**
	 * Instantiates a Condition object that represents a single value
	 * @param singleConditionValue A single value
	 */
	public Condition(int singleConditionValue) {
		low = singleConditionValue;
		high = singleConditionValue;
	}

	/**
	 * Create a new Condition object that represents a range.
	 * @param low The bottom value in a range, inclusive
	 * @param high The highest value in a range, exclusive
	 */
	public Condition(int low, int high) {
		this.low = low;
		this.high = high;
	}

	/**
	 * Satisfaction of this condition is defined by equality if the condition represents a single
	 * value. If the condition represents a range of values, the condition is satisfied by the value
	 * if it falls within the range.
	 * @return whether the provided value 'satisfies' this condition
	 */
	boolean isConditionSatisfiedBy(int value) {
		if (this.low == this.high) {
			// This condition is a single value
			return (this.low == value);
		} else {
			return ((this.low <= value) && (value < this.high));
		}
	}
	
	/**
	 * Generates a String that represents the condition.
	 * Has the form: (singleValue) or (lowValue, highValue)
	 */
	public String toString() {
		String output = "";
		if (low == high)
			output += "(" + low + ")";
		else 
			output += "(" + low + ", " + high + ")";
		return output;
	}
}
