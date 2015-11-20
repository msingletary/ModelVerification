public class DataTypeInt implements DataType {
	
	private int dataValue;
	private boolean initialized;
	
	public DataTypeInt(int data) {
		this.dataValue = data;
		this.initialized = true;
	}
	
	public DataTypeInt() {
		this.initialized = false;
	}
	
	public boolean isInitialized() {
		return initialized;
	}
	
	
	int getData() {
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