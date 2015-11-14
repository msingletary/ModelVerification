import java.util.ArrayList;

public class State {

	private int index;
	private Condition[] conditions;
	private ArrayList<Integer> transitions;

	public State(int newIndex, Condition[] newConditions) {
		this.index = newIndex;
		this.conditions = newConditions;
		transitions = new ArrayList<Integer>();
	}
	
	public State(int newIndex, int[] newDataValues) {
		Condition[] conds = new Condition[newDataValues.length];
		for (int i = 0; i < newDataValues.length; i++)
			conds[i] = new Condition(newDataValues[i]);
		this.index = newIndex;
		this.conditions = conds;
		transitions = new ArrayList<Integer>();
	}

	/**
	 * @param values List of data values representing the values of the variables of interest
	 * at a specific point in time
	 * @return true if these values fit satisfy every condition specified by this state
	 */
	boolean isStateSatisfiedBy(int[] values) {
		for (int i = 0; i < this.conditions.length; i++) {
			Condition c1 = this.conditions[i];
			if (!c1.isConditionSatisfiedBy(values[i]))
				return false;
		}
		return true;
	}

	/**
	 * Adds a transition to the FSA between this state and the state at the provided index,
	 * if this transition does not already exist in the current FSA.
	 * @param nextStateIndex
	 */
	protected void addTransitionIfNotPresent(int nextStateIndex) {
		if (!this.transitions.contains(nextStateIndex))
			this.transitions.add(nextStateIndex);
	}

	/**
	 * @return true if this state has a transition defined between it and the state at the provided index
	 */
	boolean isTransitionPresent(int nextStateIndex) {
		return this.transitions.contains(nextStateIndex);
	}

	
	public String toString() {
		String output = "\tConditions: ";
		
		for (Condition c : this.conditions) {
			output += c.toString() + " ";
		}
		
		output += "\n\tTransitions: ";
		for (int i = 0; i < transitions.size(); i++) {
			output += transitions.get(i);
			if (i != transitions.size() - 1)
				output += ", ";
		}
		return output;
	}
	
	public int getIndex() {
		return index;
	}
}
