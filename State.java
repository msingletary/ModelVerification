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

	boolean isStateSatisfiedBy(int[] values) {
		for (int i = 0; i < this.conditions.length; i++) {
			Condition c1 = this.conditions[i];
			if (!c1.isConditionSatisfiedBy(values[i]))
				return false;
		}
		return true;
	}

	// adds a transition to the FSA between the specified states only if it doesn't already exist
	protected void addTransitionIfNotPresent(int nextStateIndex) {
		// This makes sure duplicate transitions are not added. Thus it is
		// not necessary to check if the transition is present before calling this function
		if (!this.transitions.contains(nextStateIndex)) {
			this.transitions.add(nextStateIndex);
		}
	}

	/*
	void addTransition(int currentStateIndex, int nextStateIndex) {
		if (!transitionPresent) {
			states[currentStateIndex].transitions.add(nextStateIndex);
		}
	}
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
