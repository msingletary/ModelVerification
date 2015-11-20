public class DataTypeBoolean implements DataType {
	
	private boolean dataValue;
	private boolean initialized;
	
	public DataTypeBoolean(boolean data) {
		this.dataValue = data;
		this.initialized = true;
	}
	
	
	public DataTypeBoolean() {
		this.initialized = false;
	}
	
	
	public boolean isInitialized() {
		return initialized;
	}
	
	
	boolean getData() {
		return dataValue;
	}

	
	// how to indicate invalid value for a boolean?
	// this returns -99; when checking it, always check for equality first
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