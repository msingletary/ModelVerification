package FSACreation;
import java.util.ArrayList;

import DataRecording.DataType;

public class State {

	private int index;
	private Condition[] conditions;
	private ArrayList<Integer> transitions;

	
	/**
	 * Create a new State object
	 * @param newIndex Index this state is stored at in the FSA's states array
	 * @param newDataValues Values representing the condition values
	 * 		for each variable in this state.
	 * @param fromData Whether this State is being defined from reading data
	 * 		or from a manual definition from information in the DSL
	 */
	public State(int newIndex, DataType[] newDataValues, boolean fromData) {
		if (fromData) {
			Condition[] newConditions = new Condition[newDataValues.length]; 
			// state is defined by parsing data. there will be no range conditions
			for (int i = 0; i < newDataValues.length; i++)
				newConditions[i] = new Condition(newDataValues[i]);
			this.index = newIndex;
			this.conditions = newConditions;
			transitions = new ArrayList<Integer>();
		} else {
			int numConditions = newDataValues.length / 2;
			Condition[] newConditions = new Condition[numConditions]; 
			for (int i = 0; i < (numConditions); i++) {
				if (newDataValues[i*2].compareTo(newDataValues[i*2+1]) == 0) {
					newConditions[i] = new Condition(newDataValues[i*2]);
				}
				else {
					// range condition
					newConditions[i] = new Condition(newDataValues[i*2],
										newDataValues[i*2 + 1]);
				}
			}
			this.index = newIndex;
			this.conditions = newConditions;
			transitions = new ArrayList<Integer>();
		}
	}
	
	
	/**
	 * Create a new State object. This is used when defining new states 
	 * that represent ranges of values.
	 * @param newIndex Index this state is stored at in the FSA's states array
	 * @param newDataValues Values representing the condition values
	 * for each variable in this state.
	 */
	public State(int newIndex, Condition[] newConditions) {
		this.index = newIndex;
		this.conditions = newConditions;
		transitions = new ArrayList<Integer>();
	}

	
	/**
	 * Determines whether a set of values fit the definition of this state.
	 * @param dataValues Values of variables at a specific point of time
	 * @return true if these values satisfy every condition of this state
	 */
	boolean isStateSatisfiedBy(DataType[] dataValues) {
		for (int i = 0; i < this.conditions.length; i++) {
			Condition stateCondition = this.conditions[i];
			if (!stateCondition.isConditionSatisfiedBy(dataValues[i]))
				return false;
		}
		return true;
	}

	boolean isStateEqual(DataType[] dataValues) {
		for (int i = 0; i < this.conditions.length; i++) {
			Condition stateCondition = this.conditions[i];
			if (!stateCondition.isConditionEqual(dataValues[i], dataValues[i + 1]))
				return false;
		}
		return true;
	}
	
	/**
	 * Adds a transition between this state and the state found at the provided
	 * index in the FSA's states array, if this transition does not already
	 * exist in the current FSA.
	 * @param nextStateIndex The index of the next state
	 */
	void addTransitionIfNotPresent(int nextStateIndex) {
		if (!this.transitions.contains(nextStateIndex))
			this.transitions.add(nextStateIndex);
	}

	
	/**
	 * @return true if there is a transition between this state and the state
	 * at the provided index
	 */
	boolean isTransitionPresent(int nextStateIndex) {
		return this.transitions.contains(nextStateIndex);
	}

	
	/**
	 * @return the index this state is stored at in the array of states for
	 * the current FSA
	 */
	public int getIndex() {
		return index;
	}
	
	
	/** Generate a String representation of this state */
	public String toString() {
		String output = "\tConditions: ";
		for (Condition c : this.conditions) {
			output += c.toString() + " ";
		}
		output += "\n\t\tTransitions: ";
		for (int i = 0; i < transitions.size(); i++) {
			output += transitions.get(i);
			if (i != transitions.size() - 1)
				output += ", ";
		}
		return output;
	}
}
