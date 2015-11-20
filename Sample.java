import java.util.ArrayList;


public class Sample {
	
	public static void main(String[] args) {
		ArrayList<DataValue[]> allData = DataRecordingTest.abcTest();
		int numVariables = 3;
		FSAImplementation fsa = new FSAImplementation(numVariables);

		fsa.developFSAFromData(allData);
		System.out.println("FSA after the first batch of data:\n\n" + fsa.toString());
	}

}
