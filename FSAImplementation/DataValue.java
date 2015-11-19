public class DataValue {
		
		boolean initialized;
		int data;
		
		/** Constructor for DataValue that represents an uninitialized value. */
		public DataValue() {
			initialized = false;
		}

		/** Constructor for a DataValue that represents a specific value. */
		public DataValue(int data) {
			this.initialized = true;
			this.data = data;
		}
		
		/** @return A String representation of this DataValue */
		public String toString() {
			if (initialized)
				return data + "";
			else 
				return "null";
		}
	}