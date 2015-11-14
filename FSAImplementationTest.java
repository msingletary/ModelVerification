import java.util.ArrayList;


public class FSAImplementationTest {
	public static void main(String[] args) {
		testFSACreationSecondRun();
	}
	
	static ArrayList<State> testFSACreation() {
		int numVariables = 3;
		int numDataCollected = 7;

		int[] data_a = {0,1,1,1,1,2,2};
		int[] data_b = {1,1,1,2,1,1,1};
		int[] data_c = {1,1,1,1,1,1,0};
		
		int[][] dataABC = new int[numDataCollected][numVariables];
		
		for (int i = 0; i < numDataCollected; i++) {
			dataABC[i][0] = data_a[i];
			dataABC[i][1] = data_b[i];
			dataABC[i][2] = data_c[i];
		}
		
		FSAImplementation fsa = new FSAImplementation(numVariables);

		fsa.createFSAFromData(dataABC);

		System.out.println(fsa.toString());
		
		return fsa.states;
	}
	
	static int[][] createAllDataArray(int numVariables, int[] a, int[] b, int[] c) {
		int numDataCollected = a.length;
	
		int[][] allData= new int[numDataCollected][numVariables];
		
		for (int i = 0; i < numDataCollected; i++) {
			allData[i][0] = a[i];
			allData[i][1] = b[i];
			allData[i][2] = c[i];
		}
		
		return allData;
	}
	
	static void testFSACreationSecondRun() {
		int numVariables = 3;
		
		int[] data_a = {0,1,1,1,1,2,2};
		int[] data_b = {1,1,1,2,1,1,1};
		int[] data_c = {1,1,1,1,1,1,0};

		int[] data_a2 = {0,1,1,1,1,2,2,0,1};
		int[] data_b2 = {1,1,3,0,1,1,1,1,1};
		int[] data_c2 = {1,1,1,1,1,1,0,1,1};
		
		int[][] dataABC = createAllDataArray(numVariables, data_a, data_b, data_c);
		int[][] dataABC_2 = createAllDataArray(numVariables, data_a2, data_b2, data_c2);
		
		FSAImplementation fsa = new FSAImplementation(numVariables);

		fsa.createFSAFromData(dataABC);
		System.out.println("FSA after the first batch of data:\n\n" + fsa.toString());
		
		fsa.createFSAFromData(dataABC_2);
		System.out.println("\n\n---------------------\n\n");
		System.out.println("FSA after the second batch of data:\n\n" + fsa.toString());
	}
}
