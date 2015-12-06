import java.util.ArrayList;
import java.util.HashMap;


public class DataRecordManager {
	
	boolean toPrint = true;
	
	private ArrayList<DataRecord> dataRecordList;
	private HashMap<String, Integer> variableMap;
	private HashMap<String, String> mapOfTypes;
	
	private int numVariables;
	// This could be provided from the DSL, or can get from processing data for the HashMap
	
	public DataRecordManager(int numVariables) {
		dataRecordList = new ArrayList<DataRecord>();
		this.numVariables = numVariables;
	}
	
	
	public void defineType(String varName, String type) {
		if (mapOfTypes == null)
				mapOfTypes = new HashMap<String, String>();
		//if (mapOfTypes.get(varName) != null & !mapOfTypes.get(varName).equals(type)) {
		//	// overriting the type
		//}
		mapOfTypes.put(varName, type);
	}
	
	
	/**
	 * Records the result from an event occurring in the repast model execution.
	 * @param methodEventName The name of the event; also referred to as the
	 * 	variable name when constructing the FSA
	 * @param data	The result returned from the event execution.
	 * Accepts DataTypeBoolean, DataTypeDouble, DataTypeInt, DataTypeString.
	 */
	void recordData(String methodEventName, DataType data) {
		DataRecord newRecord = new DataRecord(methodEventName, data);
		dataRecordList.add(newRecord);
	}

	
	/**
	 * PParse and organize the data after the Repast model execution finishes.
	 * Produces output formatted for use with the FSA creation program
	 * @return A list of DataType arrays; each array represents the values of
	 * each variable at that single point in the execution.
	 */
	private ArrayList<DataType[]> createFullMatrixFromDataList() {
		variableMap = getMapOfVariableIndices();
		
		if (variableMap == null)
			throw new NullPointerException("The variable names have not been"
						+ " assigned indices.");
		else if (dataRecordList == null)
			throw new NullPointerException("The list of all data from the "
					+ "model execution run has not been created correctly.");
		
		ArrayList<DataType[]> fullDataMatrix = new ArrayList<DataType[]>();
		
		for (int i = 0; i < dataRecordList.size(); i++) {
			// array of each variable's value at this point in the execution:
			DataType[] nextDataStep = new DataType[numVariables];
	
			
			DataRecord nextDataRecord = dataRecordList.get(i);
			String currentVariable = nextDataRecord.variableName;
			int currVarIndex = variableMap.get(currentVariable);
			
			for (int v = 0; v < numVariables; v++) {
				System.out.println("int v: " + v);
				if (v != currVarIndex && fullDataMatrix.size() > 0) {
					nextDataStep[v] =
							fullDataMatrix.get(fullDataMatrix.size() - 1)[v];
				} else if (v != currVarIndex) {
					nextDataStep[v] = getUninitializedDataType(currentVariable);
				} else if (v == currVarIndex) {
					nextDataStep[currVarIndex] = nextDataRecord.data;
				}
			}
			
			fullDataMatrix.add(nextDataStep);
			
			if (toPrint) System.out.println("nextDataRecord: " + nextDataRecord.toString());
			if (toPrint) System.out.println("[" + nextDataStep[0] + ", "
					+ nextDataStep[1] + ", " + nextDataStep[2] + "]");
		}
		return fullDataMatrix;
	}
	
	DataType getUninitializedDataType(String varName) {
		if (mapOfTypes.get(varName).equals("int"))
			return new DataTypeInt();
		if (mapOfTypes.get(varName).equals("double"))
			return new DataTypeDouble();
		if (mapOfTypes.get(varName).equals("String"))
			return new DataTypeString();
		if (mapOfTypes.get(varName).equals("boolean"))
			return new DataTypeBoolean();
		return null;
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
