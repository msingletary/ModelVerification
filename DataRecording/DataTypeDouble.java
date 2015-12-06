public class DataTypeDouble implements DataType {
	
	private double dataValue;
	private boolean initialized;
	
	public DataTypeDouble(double data) {
		this.dataValue = data;
		this.initialized = true;
	}
	
	public DataTypeDouble() {
		this.initialized = false;
	}
	
	public boolean isInitialized() {
		return initialized;
	}
	
	double getData() {
		return dataValue;
	}

	@Override
	public int compareTo(DataType o) {
		double d2 = ((DataTypeDouble)o).getData();
		if (this.getData() < d2)
			return -1;
		else if (this.getData() < d2)
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
