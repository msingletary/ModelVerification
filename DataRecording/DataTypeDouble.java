package DataRecording;

public class DataTypeDouble implements DataType {
	
	private double dataValue;
	private boolean initialized;
	
	
	public DataTypeDouble(double data) {
		this.dataValue = data;
		this.initialized = true;
	}
	

	/** Constructor for an uninitialized object (one with an unknown value). */
	public DataTypeDouble() {
		this.initialized = false;
	}
	
	
	public DataTypeDouble(String dataStr) {
		if (dataStr.contains("null")) {
			this.initialized = false;
		} else {
			this.dataValue = Double.parseDouble(dataStr);
			this.initialized = true;
		}
	}
	
	
	public boolean isInitialized() {
		return initialized;
	}
	
	
	double getData() {
		if (!initialized)
			throw new NullPointerException("This DataType object has not yet" +
					" been initialized");
		return dataValue;
	}
	

	@Override
	public int compareTo(DataType o) {
		double d2 = ((DataTypeDouble)o).getData();
		if (this.getData() < d2) {
			return -1;
		}
		else if (this.getData() > d2) {
			return 1;
		}
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
