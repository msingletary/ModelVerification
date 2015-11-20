
/**
 * This class represents a single condition of a state within an FSA. 
 * This condition can either specify a single value or a range of values.
 */
public class Condition {
	
	private boolean initialized;
	private DataType low;
	private DataType high;
	//private int agentId;

	
	/**
	 * Instantiates a Condition object that represents a single value
	 * @param singleConditionValue A single value
	 */
	public Condition(DataType singleConditionValue) {
		if (singleConditionValue.isInitialized()) {
			initialized = true;
			this.low = singleConditionValue;
			this.high = singleConditionValue;
		} else {
			this.initialized = false;
		}
	}

	
	/**
	 * Create a new Condition object that represents a range.
	 * DataType that represents a boolean cannot be used to define a range.
	 * @param low The bottom value in a range, inclusive
	 * @param high The highest value in a range, exclusive
	 */
	public Condition(DataType low, DataType high) {
		this.low = low;
		this.high = high;
		this.initialized = true;
		assert(low.compareTo(high) < 0);
		// Should never have a situation where either are not initialized,
		// since range conditions are specifically defined in the DSL.
	}

	
	/**
	 * This condition is satisfied by equality if the condition represents a
	 * single value. If the condition represents a range of values, the
	 * condition is satisfied by the value if it falls within this range.
	 * An uninitialized condition is satisfied only by an uninitialized value.
	 * @return whether the provided value 'satisfies' this condition
	 */
	boolean isConditionSatisfiedBy(DataType value) {
		// An uninitialized value satisfies an uninitialized condition value.
		if (!initialized && !value.isInitialized())
			return true;

		// If this condition's value definition has not been initialized,
		// it cannot be satisfied by any initialized value
		if (!initialized && value.isInitialized())
			return false;
		
		// If query value has not been initialized,
		// it cannot be satisfied by an initialized condition value.
		else if (initialized && !value.isInitialized())
			return false;

		
		// This condition is a single value; evaluate for equality.
		if (low.compareTo(high) == 0) {
			if (low.compareTo(value) == 0) {
				return true;
			} else {
				return false;
			}
		}
		
		// This condition is a range;
		// evaluate whether the value falls inside this range
		else if ((low.compareTo(value) <= 0) && (high.compareTo(value) > 0))
			return true;

		return false;
	}
	

	/**
	 * Generates a String that represents the condition.
	 * Has the form: (singleValue) or (lowValue, highValue)
	 */
	public String toString() {
		String output = "";
		if (!initialized)
			output += "(null)";
		else if (low == high)
			output += "(" + low + ")";
		else 
			output += "(" + low + ", " + high + ")";
		return output;
	}
}
