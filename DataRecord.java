/**
 * This class represents a single data value recorded from a function during
 * the model execution.
 */
public class DataRecord {
	String variableName;
	int data;
	
	public DataRecord(String variableName, int dataToRecord) {
		this.variableName = variableName;
		this.data = dataToRecord;
	}
	
	public String toString() {
		return (variableName + "\t" + data);
	}
}