package DataRecording;

public class DataTypeBoolean implements DataType {
	
	private boolean dataValue;
	private boolean initialized;
	
	
	public DataTypeBoolean(boolean data) {
		this.dataValue = data;
		this.initialized = true;
	}
	
	
	/** Constructor for an uninitialized object (one with an unknown value). */	
	public DataTypeBoolean() {
		this.initialized = false;
	}
	
	
	public DataTypeBoolean(String dataStr) {
		dataStr = dataStr.trim();
		if (dataStr.contains("null")) {
			this.initialized = false;
		} else if (dataStr.equals("true")) {
			this.dataValue = true;
			this.initialized = true;
		} else if (dataStr.equals("false")) {
			this.dataValue = false;
			this.initialized = true;
		} else {
			throw new NullPointerException("\"" + dataStr + "\" is not a " + 
					"valid string for a boolean type!");
		}
	}
	
	
	public boolean isInitialized() {
		return initialized;
	}
	
	
	boolean getData() {
		if (!initialized)
			throw new NullPointerException("This DataType object has not yet" +
					" been initialized");
		return dataValue;
	}

	
	// how to indicate invalid value for a boolean?
	// this returns -99; when checking it; always check for equality first
	@Override
	public int compareTo(DataType o) {
		if (this.getData() == ((DataTypeBoolean)o).getData())
			return 0;
		return -99;
	}
	
	
	/** @return A String representation of this DataValue */
	public String toString() {
		if (initialized && dataValue)
			return "true";
		else if (initialized && !dataValue)
			return "false";
		else 
			return "null";
	}
}