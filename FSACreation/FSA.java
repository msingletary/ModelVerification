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
	
	private String[] variableNames;

	
	/** 
	 * Constructor for a new FSAImplementation object. Constructs an empty FSA.
	 * @param numVariables The number of variables recorded from the model
	 * 		& also the number of variables considered when evaluating states
	 */
	public FSA(int numVariables) {
		this.numVariables = numVariables;
		states = new ArrayList<State>();
	}
	
	
	public void defineVariableNames(String[] varNames) {
		this.variableNames = varNames;
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
	private int addNewStateIfNotPresent(DataType[] dataValues) {
		for (int i = 0; i < states.size(); i++) {
			if (states.get(i).isStateSatisfiedBy(dataValues)) {
				return i;
			}
		}
		int newIndex = states.size();
		states.add(new State(newIndex, dataValues));
		return newIndex;
	}
	
	
	public int defineNewState(DataType[] dataValues) {
		// dealing with range conditions
		for (int i = 0; i < states.size(); i++) {
			if (states.get(i).isStateEqual(dataValues)) {
				return i;
			}
		}
		Condition[] conditions = new Condition[numVariables];
		for (int i = 0; i < conditions.length; i++) {
			if (dataValues[i*2].compareTo(dataValues[i*2+1]) == 0) {
				conditions[i] = new Condition(dataValues[i*2]);
			}
			else {
				// range condition
				conditions[i] =
						new Condition(dataValues[i*2], dataValues[i*2 + 1]);
			}
		}
		
		int newIndex = states.size();
		states.add(new State(newIndex, conditions));
		return newIndex;
	}

	
	/**
	 * Parse a matrix of data obtained from the execution of the model, and add
	 * to the FSA, any state or transition represented by this data sequence
	 * that is not already present in the FSA.
	 * @param allData The matrix representing a list of 'data sets' (where each
	 *    data set is a list of values - one value for each variable examined in
	 *    the FSA). This is obtained from parsing the data file
	 */
	public void developFSAFromData(ArrayList<DataType[]> allData) {
		// obtain the first state, which should be the all null state:
		DataType[] firstDataSet = allData.get(0);
		int firstStateIndex = addNewStateIfNotPresent(firstDataSet);
		State currentState = states.get(firstStateIndex);

		// Analyze the rest of the data, and add to the FSA if needed
		for (int numAnalyzed = 1; numAnalyzed < allData.size(); numAnalyzed++) {
		 	DataType[] nextDataSet = allData.get(numAnalyzed);
			if (currentState.isStateSatisfiedBy(nextDataSet)) {
				// remain in the same state
				currentState.addTransitionIfNotPresent(currentState.getIndex());
			} else {
				int nextStateIndex = addNewStateIfNotPresent(nextDataSet);
				currentState.addTransitionIfNotPresent(nextStateIndex);
				currentState = states.get(nextStateIndex);
			}
		}
	}
	
	
	/**
	 * Create a String representation of this FSA.
	 */
	public String toString() {
		String output = "Number of states in the FSA: " + states.size() + "\n";
		if (variableNames != null) {
			output += "variable names: ";
			for (int i = 0; i < variableNames.length; i++) {
				output += "(" + variableNames[i] + ") ";
			}
		}
		for (int i = 0; i < states.size(); i++) {
			State s = states.get(i);
			output += "\nState " + s.getIndex() + ":";
			output += s.toString();
		}
		return output;
	}
	
	
	public class StateDefinitionManager {
		
		HashMap<String, Integer> variableNameIndexMap;
		HashMap<String, String> variableTypeMap;
		
		DataType[] dataValues;
		/* 0 -> 0, 1 = i * 2, i*2 + 1
		 * 1 -> 2, 3 = i*2, i*2 + 1
		 * 2 -> 4, 5 = i*2, i*2 + 1
		 * 3 -> 6, 7
		 * 
		 */
		
		public StateDefinitionManager(HashMap<String, Integer> varNameIndices, HashMap<String, String> varTypes) {
			variableNameIndexMap = varNameIndices;
			variableTypeMap = varTypes;
			
			dataValues = new DataType[numVariables * 2];
		}
		
		// could get rid of this and only call the other one
		private boolean defineStateCondition(String variable, DataType value) {
			int index = variableNameIndexMap.get(variable);
			int dvIndex1 = index * 2;
			int dvIndex2 = index * 2 + 1;
			dataValues[dvIndex1] = value;
			dataValues[dvIndex2] = value;
			return true;
		}
		

		private boolean defineStateCondition(String variable, DataType valueLow, DataType valueHigh) {
			int index = variableNameIndexMap.get(variable);
			int dvIndex1 = index * 2;
			int dvIndex2 = index * 2 + 1;
			dataValues[dvIndex1] = valueLow;
			dataValues[dvIndex2] = valueHigh;
			return false;
		}

		public boolean defineStateCondition(String variable, boolean value) {
			//			return defineStateCondition(variable, new DataTypeBoolean(value), new DataTypeBoolean(value));
			return defineStateCondition(variable, new DataTypeBoolean(value));
		}
		
		public boolean defineStateCondition(String variable, boolean valueLow, boolean valueHigh) {
			return defineStateCondition(variable, new DataTypeBoolean(valueLow), new DataTypeBoolean(valueHigh));
		}
		
		public boolean defineStateCondition(String variable, double value) {
			//			return defineStateCondition(variable, new DataTypeBoolean(value), new DataTypeBoolean(value));
			return defineStateCondition(variable, new DataTypeDouble(value));
		}
		
		public boolean defineStateCondition(String variable, double valueLow, double valueHigh) {
			return defineStateCondition(variable, new DataTypeDouble(valueLow), new DataTypeDouble(valueHigh));
		}
		

		public boolean defineStateCondition(String variable, int value) {
			//			return defineStateCondition(variable, new DataTypeBoolean(value), new DataTypeBoolean(value));
			return defineStateCondition(variable, new DataTypeInt(value));
		}
		
		public boolean defineStateCondition(String variable, int valueLow, int valueHigh) {
			return defineStateCondition(variable, new DataTypeInt(valueLow), new DataTypeInt(valueHigh));
		}
		
		public boolean defineStateCondition(String variable, long value) {
			//			return defineStateCondition(variable, new DataTypeBoolean(value), new DataTypeBoolean(value));
			return defineStateCondition(variable, new DataTypeLong(value));
		}
		
		public boolean defineStateCondition(String variable, long valueLow, long valueHigh) {
			return defineStateCondition(variable, new DataTypeLong(valueLow), new DataTypeLong(valueHigh));
		}

		public boolean defineStateCondition(String variable, String value) {
			//			return defineStateCondition(variable, new DataTypeBoolean(value), new DataTypeBoolean(value));
			return defineStateCondition(variable, new DataTypeString(value));
		}
		
		public boolean defineStateCondition(String variable, String valueLow, String valueHigh) {
			return defineStateCondition(variable, new DataTypeString(valueLow), new DataTypeString(valueHigh));
		}
		
		// Must modify if defining states that include uninitialized values is desired
		public int createState() {
			// validate all info has been added!
			for (int i = 0; i < dataValues.length; i++) {
				if (dataValues[i] == null) {
					throw new NullPointerException("Cannot define a state without defining target values for all variables.");
				}
			}
			// what should happen if you try to define a state with a range that overlaps another state?
			return defineNewState(dataValues);
		}
	}
}
