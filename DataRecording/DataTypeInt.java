package DataRecording;

public class DataTypeInt implements DataType {
	
	private int dataValue;
	private boolean initialized;
	
	
	public DataTypeInt(int data) {
		this.dataValue = data;
		this.initialized = true;
	}
	
	/** Constructor for an uninitialized object (one with an unknown value). */	
	public DataTypeInt() {
		this.initialized = false;
	}
	
	
	public DataTypeInt(String dataStr) {
		if (dataStr.contains("null")) {
			this.initialized = false;
		} else {
			this.dataValue = Integer.parseInt(dataStr);
			this.initialized = true;
		}
	}
	
	
	public boolean isInitialized() {
		return initialized;
	}
	
	
	int getData() {
		if (!initialized)
			throw new NullPointerException("This DataType object has not yet" +
					" been initialized");
		return dataValue;
	}
	

	@Override
	public int compareTo(DataType o) {
		int int2 = ((DataTypeInt)o).getData();
		if (this.getData() < int2)
			return -1;
		else if (this.getData() > int2)
			return 1;
		else 
			return 0;
	}
	
	
	/** @return A String representation of this DataValue */
	public String toString() {
		if (initialized)
			return dataValue + "";
		else 
			return "null";
	}
}