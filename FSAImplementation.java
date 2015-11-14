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

	/** 
	 * Add a new state to the FSA.
	 * @param conditionValues
	 * @return
	 */
	int addNewState(int[] conditionValues) {
		int newIndex = states.size();
		Condition[] conds = new Condition[conditionValues.length];
		for (int i = 0; i < conditionValues.length; i++) {
			//System.out.println(conditionValues[i]);
			conds[i] = new Condition(conditionValues[i]);
		}
		states.add(new State(newIndex, conds));
		return newIndex;
	}
	
	/**
	 * Searches the existing states in the FSA for one that is satisfied by the specified condition values.
	 * If no matching state is found, a new state is created and added to the FSA.
	 * @param conditionValues
	 * @return
	 */
	int addNewStateIfNotPresent(int[] conditionValues) {
		// could check currentCondition first here, instead of the other ?
		for (int i = 0; i < states.size(); i++) {
			if (states.get(i).isStateSatisfiedBy(conditionValues)) {
				return i;
			}
		}
		// could call addNewState(data) instead of the following if want to delegate;
		
		int newIndex = states.size();
		Condition[] conds = new Condition[conditionValues.length];
		for (int i = 0; i < conditionValues.length; i++) {
			conds[i] = new Condition(conditionValues[i]);
		}
		states.add(new State(newIndex, conds));
		return newIndex;
	}



	// rename to reflect that it might not be the first run
	// add data to FSA? modify FSA? extendFSA?
	void createFSAFromData(int[][] allData) {
		// obtain the first current state:
		int[] firstDataSet = allData[0];
		int firstStateIndex = addNewStateIfNotPresent(firstDataSet);
		
		/**
		if (states.size() == 0) {
			// there are no states in the states array
			// which means none were defined from the DSL and this is the first run of data for this FSA
			firstStateIndex = addNewState(allData[0]); 
			// dont need to call addNewStateIfNotPresent because we know none of present
			// but if get rid of plain addNewState it would cause no issues to just call addNewStateIfNonePresent
		} else {
			// if there are states defined in the DSL
			// or if this is not the first set of data for this FSA
			for (int i = 0; i < states.size(); i++) {
				if (states.get(i).isStateSatisfiedBy(allData[0])) {
					firstStateIndex = i;
					break;
				}
			}
			if (firstStateIndex == -1) {
				firstStateIndex = addNewState(allData[0]);
			}
		}
		*/

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
