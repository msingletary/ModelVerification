import java.util.*;

public class FSAImplementation{
	
	/* Number of variables that are recorded from the model;
	 * Number of variables are used as conditions for the States in this FSA.
	 */
	private int numVariables;
	private ArrayList<State> states;

	
	/** 
	 * Constructor for a new FSAImplementation object. Constructs an empty FSA.
	 * @param numVariables The number of variables recorded from the model
	 * 		& also the number of variables considered when evaluating states
	 */
	public FSAImplementation(int numVariables) {
		states = new ArrayList<State>();
	}
	
	
	/** @return the number of states that have been defined for this FSA */
	public int getNumStates() {
		return this.states.size();
	}
	
	
	/** @return A list of all states in this FSA */
	public ArrayList<State> getStates() {
		return this.states;
	}

	
	/**
	 * Searches the existing states in the FSA for one that is satisfied by the
	 * specified values. If no matching state is found, a new state is created
	 * and added to the FSA.
	 * @param conditionValues
	 * @return
	 */
	private int addNewStateIfNotPresent(DataValue[] dataValues) {
		// could check currentCondition first here, instead of the other ?
		for (int i = 0; i < states.size(); i++) {
			if (states.get(i).isStateSatisfiedBy(dataValues))
				return i;
		}
		int newIndex = states.size();
		states.add(new State(newIndex, dataValues));
		return newIndex;
	}

	
	/**
	 * Parse a matrix of data obtained from the model execution run, and add
	 * any state or transition represented by this data sequence that is not
	 * already present in the FSA.
	 * @param allData
	 */
	void developFSAFromData(ArrayList<DataValue[]> allData) {
		// obtain the first state:
		// (should the first state be the all null state?)
		DataValue[] firstDataSet = allData.get(0);
		int firstStateIndex = addNewStateIfNotPresent(firstDataSet);

		State currentState = states.get(firstStateIndex);

		// Analyze the rest of the data, and add to the FSA if needed
		for (int numAnalyzed = 1; numAnalyzed < allData.size(); numAnalyzed++) {
		 	DataValue[] nextData = allData.get(numAnalyzed);
			if (currentState.isStateSatisfiedBy(nextData)) {
				// remain in the same state
				currentState.addTransitionIfNotPresent(currentState.getIndex());
			} else {
				int nextStateIndex = addNewStateIfNotPresent(nextData);
				currentState.addTransitionIfNotPresent(nextStateIndex);
				currentState = states.get(nextStateIndex);
			}
		}
	}
	
	
	public String toString() {
		String output = "Number of states in the FSA: " + this.states.size() + "\n";
		for (int i = 0; i < this.states.size(); i++) {
			State s = this.states.get(i);
			output += "\nState " + s.getIndex() + ":";
			output += s.toString();
		}
		return output;
	}
}
