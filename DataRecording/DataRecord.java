package DataRecording;
/**
 * This class represents a single data value recorded from a function during
 * the model execution.
 */
public class DataRecord {
	
	String variableName;
	DataType data;
	
	public DataRecord(String variableName, DataType dataToRecord) {
		this.variableName = variableName;
		this.data = dataToRecord;
	}
	
	public String toString() {
		return (variableName + "\t" + data);
	}
}