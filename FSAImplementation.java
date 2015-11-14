import java.util.*;

public class FSAImplementation{
	
	ArrayList<State> states;
	int numVariables; // number of variables being recorded, or the number of variables that matter to the states. (should be equal ?)

	/** 
	 * Constructor for the FSAImplementation program.
	 * @param numVariables is the number of variables recorded from the model & also the number of 
	 * 				different variables considered when evaluating state equality
	 */
	public FSAImplementation(int numVariables) {
		states = new ArrayList<State>();
	}
	
	public int getNumStates() {
		return states.size();
	}

	
	/**
	 * Searches the existing states in the FSA for one that is satisfied by the specified condition values.
	 * If no matching state is found, a new state is created and added to the FSA.
	 * @param conditionValues
	 * @return
	 */
	int addNewStateIfNotPresent(int[] dataValues) {
		// could check currentCondition first here, instead of the other ?
		for (int i = 0; i < states.size(); i++) {
			if (states.get(i).isStateSatisfiedBy(dataValues)) {
				return i;
			}
		}
		// could call addNewState(data) instead of the following if want to delegate;
		
		// 
		int newIndex = states.size();
		states.add(new State(newIndex, dataValues));
		return newIndex;
	}



	// rename to reflect that it might not be the first run
	// add data to FSA? modify FSA? extendFSA?
	void createFSAFromData(int[][] allData) {
		// obtain the first current state:
		int[] firstDataSet = allData[0];
		int firstStateIndex = addNewStateIfNotPresent(firstDataSet);

		State currentState = states.get(firstStateIndex);

		// read in and analyze FSA for rest of the data
		for (int numAnalyzed = 1; numAnalyzed < allData.length; numAnalyzed++) {
		 	int[] nextData = allData[numAnalyzed];
			if (currentState.isStateSatisfiedBy(nextData)) {
				// stays in the same state
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
		for (State s : this.states) {
			output += "\nState:";
			output += s.toString();
		}
		return output;
	}
}
