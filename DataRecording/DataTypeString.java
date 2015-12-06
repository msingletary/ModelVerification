package DataRecording;

public class DataTypeString implements DataType {
	
	// Strings with leading or trailing spaces will have these spaces removed
	private String dataValue;
	private boolean initialized;
	
	
	public DataTypeString(String data) {
		String dataTrimmed = data.trim();

		if (dataTrimmed.equals("null")) {
			this.initialized = false;
		} else {
			this.dataValue = dataTrimmed;
			this.initialized = true;
		}
	}
	
	
	/** Constructor for an uninitialized object (one with an unknown value). */	
	public DataTypeString() {
		this.initialized = false;
	}
	
	public boolean isInitialized() {
		return initialized;
	}
	
	
	String getData() {
		if (!initialized)
			throw new NullPointerException("This DataType object has not yet" +
					" been initialized");
		return dataValue;
	}
	

	@Override
	public int compareTo(DataType o) {
		return this.getData().compareTo(((DataTypeString)o).getData());
	}
	
	
	/** @return A String representation of this DataValue */
	public String toString() {
		if (initialized)
			return dataValue + "";
		else 
			return "null";
	}
}
