

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class DataRecordingTest {
	
	
	private HashMap<String, Integer> allVariableNames;
	
	
	
	

	ArrayList<DataValue[]> convertDataListToMatrix(
			ArrayList<DataRecord> dataStream) {
		HashMap<String, Integer> nameIndexMap = getMapOfNameIndices(dataStream);
		int numVariables = 3;
		ArrayList<DataValue[]> dataMatrix = new ArrayList<DataValue[]>();
		
		for (int i = 3; i < dataRecordList.size(); i++) {
			DataValue[] nextDataStep = new DataValue[numVariables];
			
			DataRecord nextDataRecord = dataStream.get(i);
			
			int currVarIndex = nameIndexMap.get(nextDataRecord.variableName);
			nextDataStep[currVarIndex] = new DataValue(nextDataRecord.data);
			
			for (int v = 0; v < numVariables; v++) {
				if (v != currVarIndex && v > 0) {
					// this for loop cannot be used for the first data step as the previous might not exist
					nextDataStep[v] = dataMatrix.get(dataMatrix.size() - 1)[v];
					// this could just be assigning another uninitialized value, which is fine
				} else if (v != currVarIndex){
					nextDataStep[v] = new DataValue();
				}
			}
			
			dataMatrix.add(nextDataStep);
			
			System.out.println(nextDataRecord.toString());
			System.out.println("[" + nextDataStep[0] + ", "
					+ nextDataStep[1] + ", " + nextDataStep[2] + "]");
			
			
		}
		
		
		return null;
		
	}
	
	ArrayList<DataValue[]> generateAllDataList() {
		generateMapOfName();
		// supports uninitialized 
		
		int numVariables = 3;
		ArrayList<DataValue[]> all = new ArrayList<DataValue[]>();
		
		//int dataCount = 1;
		for (int i = 3; i < dataRecordList.size(); i++) {
			DataValue[] nextDataStep = new DataValue[numVariables];
			
			DataRecord nextDataRecord = dataRecordList.get(i);
			
			String varName = nextDataRecord.variableName;
			int currVarIndex = allVariableNames.get(varName);
			nextDataStep[currVarIndex] = new DataValue(nextDataRecord.data);
			
			for (int v = 0; v < numVariables; v++) {
				if (all.size() > 0 && v != currVarIndex) {
					// this for loop cannot be used for the first data step as the previous might not exist
					nextDataStep[v] = all.get(all.size() - 1)[v];
				} else if (v != currVarIndex){
					nextDataStep[v] = new DataValue();
				}
			}
			
			all.add(nextDataStep);
			
			//System.out.println(nextDataRecord.toString());
			System.out.println("values at this data step: [" + nextDataStep[0] + ", "
					+ nextDataStep[1] + ", " + nextDataStep[2] + "]");
			
			
		}
		
		return all;
	}
	
	ArrayList<int[]> generateMatrix() {
		generateMapOfName();
		// or can iterate over entire dataRecordList at the end to determine total number of variables
		//Set<String> s = allVariableNames.keySet();
		//int numVariables = s.size();
		//actually, numVariables is a variable provided in the DSL.
		// well, hashmap might make things easier 

		int numVariables = 3;
		//int[][] allData = new int[numVariables][];
		ArrayList<int[]> all = new ArrayList<int[]>();
		
		int[] nextDataStep = new int[numVariables];
		nextDataStep[0] = 0;
		nextDataStep[1] = 1;
		nextDataStep[2] = 1;
		
		all.add(nextDataStep);
		
		int dataCount = 1;
		for (int i = 3; i < dataRecordList.size(); i++) {
			nextDataStep = new int[numVariables];
			
			DataRecord d1 = dataRecordList.get(i);
			
			int currVarIndex = allVariableNames.get(d1.variableName);
			//System.out.println("currIndex: " + currVarIndex + ", varName: " + d1.variableName);
			nextDataStep[currVarIndex] = d1.data;
			for (int v = 0; v < numVariables; v++) {
				if (v != currVarIndex) {
					// this for loop cannot be used for the first data step as the previous might not exist
					nextDataStep[v] = all.get(all.size() - 1)[v];
				}
			}
			
			all.add(nextDataStep);
			
			//System.out.println(d1.toString());
			System.out.println("[" + nextDataStep[0] + ", "
					+ nextDataStep[1] + ", " + nextDataStep[2] + "]");
			
			
		}
		
		return all;
		
		
		
		/*
		int[] data_a = {0,1,1,1,1,2,2,3};
		int[] data_b = {1,1,1,2,1,1,1,1};
		int[] data_c = {1,1,1,1,1,1,0,0};

		int[] data_a2 = {0,1,1,1,1,2,2,0,1};
		int[] data_b2 = {1,1,3,0,1,1,1,1,1};
		int[] data_c2 = {1,1,1,1,1,1,0,1,1};
		*/
		
	}
	
	void generateMatrixArray() {
		//int numVariables = 3;
		//int[][] allData = new int[numVariables][];
		
		
		// generate first datas / how to determine first state?
		
		// dataCount = index in dataRecordList to examine
		/*
		int dataCount = 1;
		for (int i = 3; dataCount < dataRecordList.size(); dataCount++) {
			
			DataRecord d1 = dataRecordList.get(i);
			int currVarIndex = allVariableNames.get(d1.variableName);
			allData[dataCount][currVarIndex] = d1.data;
			
			for (int v = 0; v < numVariables; v++) {
				if (v != currVarIndex) {
					if (v - 1 >= 0)
						allData[dataCount][v] = allData[dataCount][v - 1];
					else
						allData[dataCount][v] = -99;
				}
			}
			
		}
		
		return allData;
		*/
		
	
	}
	
	public static ArrayList<DataValue[]> abcTest() {
		DataRecordingTest t = new DataRecordingTest();
		t.recordData("a", 0);
		t.recordData("b", 1);
		t.recordData("c", 1);
		

		t.recordData("b", 2);
		t.recordData("a", 1);
		t.recordData("b", 1);
		t.recordData("b", 2);
		t.recordData("b", 1);
		t.recordData("a", 2);
		t.recordData("c", 0);
		t.recordData("a", 3);
		
		//t.printData();
		
		ArrayList<DataValue[]> matrix = t.generateAllDataList();
		for (int i = 0; i < matrix.size(); i++) {
			System.out.println("[" + matrix.get(i)[0] + ", "
					+ matrix.get(i)[1] + ", " + matrix.get(i)[2] + "]");
			
		}
		
		return matrix;
	}

	
	
	public static void main(String[] args) {
		abcTest();
	}
	
	
	public static void simpleTest() {
		DataRecordingTest t = new DataRecordingTest();
		t.recordData("happinessEvent", 5);
		t.recordData("clickEvent", 5);
		t.recordData("happinessEvent", 10);
		t.recordData("regionsEvent", 10);
		t.printData();
	}
	
	void printData() {
		for (DataRecord dr : dataRecordList) {
			System.out.println(dr.toString());
		}
	}
}
