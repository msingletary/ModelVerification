import java.util.ArrayList;
import java.util.HashMap;

public class DataRecordManager {
	
	boolean toPrint = true;
	
	private ArrayList<DataRecord> dataRecordList;
	private HashMap<String, Integer> variableMap;
	
	public DataRecordManager() {
		dataRecordList = new ArrayList<DataRecord>();
	}
	
	
	/**
	 * Records the result from an event in the repast model execution.
	 * @param methodEventName The name of the event; also referred to as the
	 * 	variable name when constructing the FSA
	 * @param data	The result returned from the event execution
	 */
	void recordData(String methodEventName, int data) {
		DataRecord newRecord = new DataRecord(methodEventName, data);
		dataRecordList.add(newRecord);
	}

	
	/** 
	 * Process and organize the data after the Repast model execution.
	 * Produces output formatted for use with the FSA creation program.
	 */
	ArrayList<DataValue[]> processData() {
		variableMap = getMapOfVariableIndices();  
		return convertDataListToMatrix();
	}
	
	
	private ArrayList<DataValue[]> convertDataListToMatrix() {
		int numVariables = 3;
		// This could be provided from the DSL
		// or can get from processing data for the hashmap
		
		if (variableMap == null)
			throw new NullPointerException("The variable names have not been"
						+ " assigned indices.");
		else if (dataRecordList == null)
			throw new NullPointerException("The list of all data from the "
					+ "model execution run has not been created correctly.");
		
		ArrayList<DataValue[]> fullDataMatrix = new ArrayList<DataValue[]>();
		
		for (int i = 0; i < dataRecordList.size(); i++) {
			
			// Array each variable's value at this one point in the execution:
			DataValue[] nextDataStep = new DataValue[numVariables];
	
			DataRecord nextDataRecord = dataRecordList.get(i);
			int currVarIndex = variableMap.get(nextDataRecord.variableName);
			
			for (int v = 0; v < numVariables; v++) {
				if (v != currVarIndex && v > 0) {
					nextDataStep[v] =
							fullDataMatrix.get(fullDataMatrix.size() - 1)[v];
				} else if (v != currVarIndex){
					nextDataStep[v] = new DataValue();
				} else if (v == currVarIndex) {
					nextDataStep[currVarIndex] =
							new DataValue(nextDataRecord.data);
				}
			}
			
			fullDataMatrix.add(nextDataStep);
			
			if (toPrint) System.out.println(nextDataRecord.toString());
			if (toPrint) System.out.println("[" + nextDataStep[0] + ", "
					+ nextDataStep[1] + ", " + nextDataStep[2] + "]");
		}
		return fullDataMatrix;
	}
	
	
	HashMap<String, Integer> getMapOfVariableIndices() {
		// or, could get a list of all the event names from the DSL
		// user needs to provide these when determining what to record anyways
		int nameCount = 0;
		HashMap<String, Integer> namesMap = new HashMap<String, Integer>();
		for (DataRecord dr : this.dataRecordList) {
			if (namesMap.get(dr.variableName) == null)
				namesMap.put(dr.variableName, nameCount++);
		}
		return namesMap;
		// key: name of variable
		// value: index of this variable in the overall data array or matrix
	}
	
	
	void printDataRecordList() {
		for (DataRecord dr : dataRecordList) {
			System.out.println(dr.toString());
		}
	}
}
