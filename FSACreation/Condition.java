package FSACreation;

import DataRecording.DataType;

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
		if (initialized && !value.isInitialized())
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
		else if ((low.compareTo(value) <= 0) && (high.compareTo(value) > 0)) {
			return true;
		}

		return false;
	}
	
	
	/**
	 * Determines if this condition object is equal to a condition with the 
	 * specified values. This method is used when manually defining states from
	 * the DSL and 
	 * @param vLow
	 * @param vHigh
	 * @return
	 */
	boolean isConditionEqual(DataType vLow, DataType vHigh) {

		// An uninitialized value is equal to an uninitialized condition value.
		if (!initialized && !vLow.isInitialized() && !vHigh.isInitialized())
			return true;

		// If one or two (but not all) of the values are not initialized,
		// they cannot be equal to an initialized value.
		if (!initialized || !vLow.isInitialized() || !vHigh.isInitialized())
			return false;
		
		// Evaluate condition values for equality
		return ((low.compareTo(vLow) == 0) && (high.compareTo(vHigh) == 0));
	}
	

	/**
	 * Generates a String representation of this condition.
	 * Has the form: "(singleValue)" or "(lowValue, highValue)"
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
