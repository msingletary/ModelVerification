package DataRecording;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class DataRecordManager {
	
	private int numVariables;
	private File dataFile;
	
	private ArrayList<DataRecord> dataRecordList;  // data recorded from a single run
	
	private HashMap<String, Integer> variableIndexMap;	// key: variable name, value: variable index in matrix
	private HashMap<String, String> variableTypeMap;	// key: variable name, value: variable type
	private String[] variableNamesList; 	// list of variable names, where the index of each name corresponds to its index in the matrix

	
	/**
	 * Constructor for a DataRecordManager object.
	 * @param numVariables Number of methods/variables recorded from the model
	 * @param dataStorageLocation The path and file name of the txt file where
	 * 	the data is stored
	 */
	public DataRecordManager(int numVariables, String dataStorageLocation) {
		this.numVariables = numVariables;
		dataRecordList = new ArrayList<DataRecord>();
		dataFile = new File(dataStorageLocation);
	}
	
	
	/**
	 * Records the result from a method executing in the repast model execution.
	 * @param methodEventName The name of the method/variable.
	 * @param data	The result returned from the event execution.
	 * Accepts DataTypeBoolean, DataTypeDouble, DataTypeInt, DataTypeString.
	 */
	private void recordData(String methodEventName, DataType data) {
		DataRecord newRecord = new DataRecord(methodEventName, data);
		dataRecordList.add(newRecord);
	}

	
	/**
	 * Records the result from a method executing in the repast model execution, 
	 *  when the method has a return type int.
	 * @param methodEventName The name of the method/variable.
	 * @param data	The result returned from the event execution.
	 */
	public void recordData(String methodEventName, int data) {
		recordData(methodEventName, new DataTypeInt(data));
	}

	
	/**
	 * Records the result from a method executing in the repast model execution, 
	 *  when the method has a return type double.
	 * @param methodEventName The name of the method/variable.
	 * @param data	The result returned from the event execution.
	 */
	public void recordData(String methodEventName, double data) {
		recordData(methodEventName, new DataTypeDouble(data));
	}
	
	
	/**
	 * Records the result from a method executing in the repast model execution, 
	 *  when the method has a return type long.
	 * @param methodEventName The name of the method/variable.
	 * @param data	The result returned from the event execution.
	 */
	public void recordData(String methodEventName, long data) {
		recordData(methodEventName, new DataTypeLong(data));
	}
	
	
	/**
	 * Records the result from a method executing in the repast model execution, 
	 *  when the method has a return type String.
	 * @param methodEventName The name of the method/variable.
	 * @param data	The result returned from the event execution.
	 */
	public void recordData(String methodEventName, String data) {
		recordData(methodEventName, new DataTypeString(data));
	}

	
	/**
	 * Records the result from a method executing in the repast model execution, 
	 *  when the method has a return type boolean.
	 * @param methodEventName The name of the method/variable.
	 * @param data	The result returned from the event execution.
	 */
	public void recordData(String methodEventName, boolean data) {
		recordData(methodEventName, new DataTypeBoolean(data));
	}
	
	
	public String[] getVariableNames() {
		if (variableNamesList == null)
			retrieveVariableInformationFromFile();
		return variableNamesList;
	}

	public HashMap<String, String> getVariableTypes() {
		if (variableTypeMap == null)
			retrieveVariableInformationFromFile();
		return variableTypeMap;
	}
	
	
	public HashMap<String, Integer> getVariableIndices() {
		if (variableIndexMap == null)
			retrieveVariableInformationFromFile();
		return variableIndexMap;
	}
	
	
	/**
	 * Retrieves a matrix from the list of data recorded from method executions 
	 * in the model, and writes this matrix to the data file at the location
	 * provided in the constructor.
	 * @return boolean representing the success of these operations.
	 */
	public boolean writeRecentDataToFile() {
		FileWriter outputWriter;
		ArrayList<DataType[]> matrixFromRecentRun = createMatrixFromRun();
		try {
			// appends the text matrix to the the end of the existing text file
			outputWriter = new FileWriter(dataFile, true);
			try {
				outputWriter.write(convertMatrixToText(matrixFromRecentRun));
				// writes a blank line between data from different runs
				outputWriter.write("\n");
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} finally {
				try {
					outputWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	/**
	 * Reads the list of DataRecord objects representing methods executed in 
	 * this model execution and their values, and creates a full matrix
	 * representing the value for each variable at each step in time.
	 * @return Matrix representing all data from a single repast model run.
	 */
	private ArrayList<DataType[]> createMatrixFromRun() {
		
		if (dataRecordList == null)
			throw new NullPointerException("The list of all data from the "
					+ "model execution run has not been created correctly.");
		
		/* Retrieve the list of method names and types from the data file
		   This allows this function to know what order the method values are
		   listed in within the data file, as well as the type of each method.
		   Stores info in variableNamesList, variableTypeMap, variableIndexMap. */
		retrieveVariableInformationFromFile();
		
		ArrayList<DataType[]> matrix = new ArrayList<DataType[]>();
		
		for (int i = 0; i < dataRecordList.size(); i++) {
			// array of each variable's value at this point in the execution:
			DataType[] nextDataStep = new DataType[numVariables];
	
			DataRecord nextDataRecord = dataRecordList.get(i);
			/* Name of the variable recorded at this step. 
			   This is the only value that might change from the
			   previous 'data step' (previous row in the matrix) */
			String currentVarName = nextDataRecord.variableName;
			int currVarIndex = variableIndexMap.get(currentVarName);
			for (int v = 0; v < numVariables; v++) {
				if (v != currVarIndex && matrix.size() > 0)
					nextDataStep[v] = matrix.get(matrix.size() - 1)[v];
				else if (v != currVarIndex)
					nextDataStep[v] = getUninitializedDataType(currentVarName);
				else if (v == currVarIndex)
					nextDataStep[currVarIndex] = nextDataRecord.data;
			}
			matrix.add(nextDataStep);
		}
		return matrix;
	}
	

	/**
	 * Parse and organize the data obtained from Repast model execution(s).
	 * Produces output formatted for use with the FSA creation program.
	 * @return An ArrayList of DataType arrays; each array represents the values
	 * of each variable at that single point in the execution.
	 */
	public ArrayList<DataType[]> getFullMatrixFromDataFile() {
		
		/* Retrieve the list of method names and types from the data file
		   This allows this function to know what order the method values are
		   listed in within the data file, as well as the type of each method.
		   Stores info in variableNamesList, variableTypeMap, variableIndexMap.
		 */
		retrieveVariableInformationFromFile();

		ArrayList<DataType[]> fullDataMatrix = new ArrayList<DataType[]>();
		
		try {
			Scanner scanFile = new Scanner(dataFile);
			// can ignore first two lines
			// they contain information about the methods and don't contain data
			scanFile.nextLine();
			scanFile.nextLine();
			
			while (scanFile.hasNextLine()) {
				// scan each line in the file
				String line = scanFile.nextLine();
				if (line.equals("") || line.equals("\n"))
					continue;

				String[] dataList = line.split(", ");
				DataType[] currentDataStep = new DataType[numVariables];
				
				for (int i = 0; i < numVariables; i++) {
					String data = dataList[i];
					String varName = variableNamesList[i];
					String varType = variableTypeMap.get(varName);
					currentDataStep[i] =
							getDataTypeObjectFromString(varType, data);
				}
				fullDataMatrix.add(currentDataStep);
			}
			scanFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
		return fullDataMatrix;
	}
	
	
	/**
	 * @param matrix The matrix to convert to text form
	 * @return Text representation of a matrix
	 */
	static private String convertMatrixToText(ArrayList<DataType[]> matrix) {
		String output = "";
		output += "null, null, null, null, null\n"; // represents the starting state
		for (DataType[] dataStep : matrix) {
			for (int i = 0; i < dataStep.length; i++) {
				if (dataStep[i] == null)
					output += "null";
				else
					output += dataStep[i].toString();
				if (i < dataStep.length - 1)
					output += ", ";
			}
			output += "\n";
		}
		return output;
	}
	
	private DataType getUninitializedDataType(String varName) {
		if (variableTypeMap == null) {
			throw new NullPointerException("Map of variable types was not " + 
					"initialized correctly. It is null");
		}

		String varType = variableTypeMap.get(varName);
		if (varType == null) {
			throw new NullPointerException("This variable's type was never " + 
					"defined in the variable type map");
		}
		else if (varType.equals("int"))
			return new DataTypeInt();
		else if (varType.equals("double"))
			return new DataTypeDouble();
		else if (varType.equals("String"))
			return new DataTypeString();
		else if (varType.equals("long"))
			return new DataTypeLong();
		else if (varType.equals("boolean"))
			return new DataTypeBoolean();
		return null;
	}
	
	
	/**
	 * Returns a DataType object that is created from converting the
	 * 	String data to the appropriate type, depending on the defined type.
	 * @param type The data type this String input should be converted to. 
	 * @param data A value as a String. "null" if uninitialized
	 * @return DataType object of the correct type, with the appropriate value
	 *   if initialized.
	 */
	private DataType getDataTypeObjectFromString(String type, String data) {
		if (type.equals("int"))
			return new DataTypeInt(data);
		else if (type.equals("double"))
			return new DataTypeDouble(data);
		else if (type.equals("String"))
			return new DataTypeString(data);
		else if (type.equals("long"))
			return new DataTypeLong(data);
		else if (type.equals("boolean"))
			return new DataTypeBoolean(data);
		return null;
	}
	
	
	/**
	 * Reads in the first two lines from the data storage file. 
	 * Reads and stores the method names (in the order they appear in each row 
	 *   of the matrix) and the method types.
	 * This is important to ensure that the data for each variable/method is
	 * 	 stored in the same column during each run.
	 * It is also important to know what type is assigned to each variable.
	 * Stores this information as global variables of this DataRecordManager.
	 * First two lines of the text file appear as follows:
	 * method1Name, method2Name, method3Name; i.e.: methodA, methodB, method3 
	 * method1Type, method2Type, method3Type; i.e.: boolean, int, double
	 * @return boolean representing the success of these operations
	 */
	private boolean retrieveVariableInformationFromFile() {
		try {
			Scanner scanFile = new Scanner(dataFile);
			// read in and store variable names and variable types:
			Scanner scanFirstLine = new Scanner(scanFile.nextLine());
			Scanner scanSecondLine = new Scanner(scanFile.nextLine());
			scanFirstLine.useDelimiter(", ");
			scanSecondLine.useDelimiter(", ");
			scanFile.close();
			
			variableIndexMap = new HashMap<String, Integer>();
			variableTypeMap = new HashMap<String, String>();
			variableNamesList = new String[numVariables];
			
			int i = 0;
			while (scanFirstLine.hasNext() && scanSecondLine.hasNext()) {
				if (i > numVariables) {
					// Invalid number of variables read from file header
					scanFirstLine.close();
					scanSecondLine.close();
					variableIndexMap = null;
					variableTypeMap = null;
					return false;
				}
				String varName = scanFirstLine.next();
				String varType = scanSecondLine.next();
				variableNamesList[i] = varName;
				variableIndexMap.put(varName, i);
				variableTypeMap.put(varName, varType);
				i++;
			}
			
			scanFirstLine.close();
			scanSecondLine.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		
		if (variableIndexMap.isEmpty() || variableIndexMap == null)
			throw new NullPointerException("The list of variable names was not read from the file correctly");	
		else if (variableTypeMap.isEmpty() || variableTypeMap == null)
			throw new NullPointerException("The list of variable types was not read from the file correctly");
		return true;
	}
}
