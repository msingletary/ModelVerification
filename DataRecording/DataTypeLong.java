package DataRecording;

public class DataTypeLong implements DataType {
	
	private long dataValue;
	private boolean initialized;
	
	
	public DataTypeLong(long data) {
		this.dataValue = data;
		this.initialized = true;
	}
	

	/** Constructor for an uninitialized object (one with an unknown value). */
	public DataTypeLong() {
		this.initialized = false;
	}
	
	
	public DataTypeLong(String dataStr) {
		if (dataStr.contains("null")) {
			this.initialized = false;
		} else {
			this.dataValue = Long.parseLong(dataStr);
			this.initialized = true;
		}
	}
	
	
	public boolean isInitialized() {
		return initialized;
	}
	
	
	long getData() {
		if (!initialized)
			throw new NullPointerException("This DataType object has not yet" +
					" been initialized");
		return dataValue;
	}
	

	@Override
	public int compareTo(DataType o) {
		long data2 = ((DataTypeLong)o).getData();
		if (this.getData() < data2) {
			return -1;
		}
		else if (this.getData() > data2) {
			return 1;
		}
		else
			return 0;
	}
	
	
	/** @return A String representation of this long DataValue */
	public String toString() {
		if (initialized)
			return dataValue + "";
		else 
			return "null";
	}
}
