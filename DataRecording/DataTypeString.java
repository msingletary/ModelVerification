public class DataTypeString implements DataType {
	
	private String dataValue;
	private boolean initialized;
	
	public DataTypeString(String data) {
		this.dataValue = data;
		this.initialized = true;
	}
	
	public DataTypeString() {
		this.initialized = false;
	}
	
	public boolean isInitialized() {
		return initialized;
	}
	
	String getData() {
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
