package FSACreation;
import java.util.*;

import DataRecording.DataType;
import DataRecording.DataTypeBoolean;
import DataRecording.DataTypeDouble;
import DataRecording.DataTypeInt;
import DataRecording.DataTypeLong;
import DataRecording.DataTypeString;

public class FSA {
	
	/* Number of methods that are of interest in the model, and are recorded.
	 * Number of variables are used as conditions for the States in this FSA.
	 */
	private int numVariables;
	private ArrayList<State> states;

	
	/** 
	 * Constructor for a new, empty FSA.
	 * @param numVariables The number of variables recorded from the model
	 * 		& also the number of variables considered when evaluating states
	 */
	public FSA(int numVariables) {
		this.numVariables = numVariables;
		states = new ArrayList<State>();
	}
	
	
	/** @return the number of states that have been defined for this FSA */
	public int getNumStates() {
		return this.states.size();
	}

	
	/**
	 * Parse a matrix of data obtained from the execution of the model, and add
	 * to the FSA, any state or transition represented by this data sequence
	 * that is not already present in the FSA.
	 * @param allData The matrix obtained from parsing the data file.
	 */
	public void developFSAFromData(ArrayList<DataType[]> allData) {
		// Obtain the first state, which should be all uninitialized values
		DataType[] firstDataSet = allData.get(0);
		int firstStateIndex = retrieveState(firstDataSet);
		State currentState = states.get(firstStateIndex);

		// Analyze the rest of the data, and add new states to the FSA if needed
		for (int numAnalyzed = 1; numAnalyzed < allData.size(); numAnalyzed++) {
		 	DataType[] nextDataSet = allData.get(numAnalyzed);
			if (currentState.isStateSatisfiedBy(nextDataSet)) {
				// remain in the same state
				currentState.addTransitionIfNotPresent(currentState.getIndex());
			} else {
				int nextStateIndex = retrieveState(nextDataSet);
				currentState.addTransitionIfNotPresent(nextStateIndex);
				currentState = states.get(nextStateIndex);
			}
		}
	}
	
	
	/**
	 * Searches the existing states in the FSA for one that is satisfied by the
	 * specified values. If no matching state is found, a new state is created
	 * and added to the FSA.
	 * @param dataValues the target values of each variable in this state
	 * @return the index in the states array of the state these values satisfy
	 */
	private int retrieveState(DataType[] dataValues) {
		for (int i = 0; i < states.size(); i++) {
			if (states.get(i).isStateSatisfiedBy(dataValues))
				return i;
		}
		int newIndex = states.size();
		states.add(new State(newIndex, dataValues, true));
		return newIndex;
	}
	
	
	/**
	 * Create a String representation of this FSA.
	 */
	public String toString() {
		String output = "Number of states in the FSA: " + states.size() + "\n";
		for (int i = 0; i < states.size(); i++) {
			State s = states.get(i);
			output += "\nState " + s.getIndex() + ":";
			output += s.toString();
		}
		return output;
	}
	
	
	
	/**
	 * This class is used to define states 'manually' from information provided
	 * by the DSL. 
	 */
	public class StateDefinitionBuilder {
		
		HashMap<String, Integer> variableNameIndexMap;
		
		
		DataType[] dataValues;
		
		
		/**
		 * Constructor for a new StateDefinitionManager. This object will allow
		 * you to define a new state for the FSA manually (as opposed to
		 * defining new states from the data recorded). 
		 * @param varNameIndices
		 * @param varTypes
		 */
		public StateDefinitionBuilder(HashMap<String, Integer> varNameIndices) {
			variableNameIndexMap = varNameIndices;
			dataValues = new DataType[numVariables * 2];
		}

		
		/**
		 * Creates a new state from the information that has been provided to
		 * this StateDefinitionManager object. 
		 * @return index of the new state that has been created. Returns -1 if 
		 *    there is one or more variables that do not have a condition defined
		 */
		public int createState() {
			// verify all info has been added
			for (int i = 0; i < dataValues.length; i++) {
				if (dataValues[i] == null) {
					// Cannot define a state without defining state values for all variables.
					return -1;
				}
			}
			int newIndex = states.size();
			states.add(new State(newIndex, dataValues, false));
			return newIndex;
		}
		
		
		/**
		 * Define a new condition for this state. Will override any previous
		 * value(s) assigned for this variable during this state definition.
		 * @param variable Name of the variable to define a condition for
		 * @param valueLow The low end of a range value, or a single value
		 * @param valueHigh The high end of a range value, or a single value
		 */
		private boolean defineStateCondition(String variable,
				DataType valueLow, DataType valueHigh) {
			int index = variableNameIndexMap.get(variable);
			int lowIndex = index * 2;
			int highIndex = index * 2 + 1;
			dataValues[lowIndex] = valueLow;
			dataValues[highIndex] = valueHigh;
			return true;
		}
		
		
		public void defineStateCondition(String variable, boolean value) {
			defineStateCondition(variable, new DataTypeBoolean(value), new DataTypeBoolean(value));
		}
		
		public void defineStateCondition(String variable, boolean valueLow, boolean valueHigh) {
			if (valueLow != valueHigh)
				throw new IllegalArgumentException("Invalid condition values. "
					+ "Cannot define a range for boolean values");
				
			defineStateCondition(variable, new DataTypeBoolean(valueLow), new DataTypeBoolean(valueHigh));
		}
		
		public void defineStateCondition(String variable, double value) {
			defineStateCondition(variable, new DataTypeDouble(value), new DataTypeDouble(value));
		}
		
		public void defineStateCondition(String variable, double valueLow, double valueHigh) {
			if (valueLow > valueHigh)
				throw new IllegalArgumentException("Invalid condition values. "
						+ valueLow + " should not be greater than " + valueHigh);
			defineStateCondition(variable, new DataTypeDouble(valueLow), new DataTypeDouble(valueHigh));
		}
		
		public void defineStateCondition(String variable, int value) {
			defineStateCondition(variable, new DataTypeInt(value), new DataTypeInt(value));
		}
		
		public void defineStateCondition(String variable, int valueLow, int valueHigh) {
			if (valueLow > valueHigh)
				throw new IllegalArgumentException("Invalid condition values. "
						+ valueLow + " should not be greater than " + valueHigh);
			defineStateCondition(variable, new DataTypeInt(valueLow), new DataTypeInt(valueHigh));
		}
		
		public void defineStateCondition(String variable, long value) {
			defineStateCondition(variable, new DataTypeLong(value), new DataTypeLong(value));
		}
		
		public void defineStateCondition(String variable, long valueLow, long valueHigh) {
			if (valueLow > valueHigh)
				throw new IllegalArgumentException("Invalid condition values. "
						+ valueLow + " should not be greater than " + valueHigh);
			defineStateCondition(variable, new DataTypeLong(valueLow), new DataTypeLong(valueHigh));
		}

		public void defineStateCondition(String variable, String value) {
			defineStateCondition(variable, new DataTypeString(value), new DataTypeString(value));
		}
		
		public void defineStateCondition(String variable, String valueLow, String valueHigh) {
			if (valueLow.compareTo(valueHigh) > 0)
				throw new IllegalArgumentException("Invalid condition values. "
						+ valueLow + " should not be greater than " + valueHigh);
			defineStateCondition(variable, new DataTypeString(valueLow), new DataTypeString(valueHigh));
		}
	}
}
