package projectManagement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

/* Format of input from DSL: 
 * (easily changed to whatever is appropriate for DSL:
 * method: fully.qualified.methodName; params: type1, type2, type3; return: returnType
 * 
 * or:
 * fully.qualified.methodName; type1, type2, type3; returnType
 * 
 * or:
 * fully.qualified.methodName, type1, type2, type3, returnType
 * and take first as methodName, last as returnType, and params as anything in the middle
 */

public class FileGenerator {

	String answerPackage; 	// = "answers"; // name of the package the AspectJ package will be located in
	String answerName; 		//  = "Answer5"; // name of the AspectJ answer file and class
	
	String modelPackageName; 	// get somehow
	
	int numberMethods;
	
	/**
	 * Constructor for a new FileGenerator object. 
	 * @param modelPackageName The fully qualified name of the package in which
	 * 	the model of interest is located
	 */
	public FileGenerator(String modelPackageName) {
		this.modelPackageName = modelPackageName;
		this.numberMethods = -1;
	}
		
	
	/**
	 * Generates the AspectJ code needed to record executions of the specified
	 * methods, and writes this AspectJ code to a .aj file.
	 * Generates the list of method names and types, and writes to a txt file.
	 * @param answerFileName The desired name of the AspectJ answer file
	 * @param answerPackage The desired package location for the AspectJ file
	 * @param inputMethodInfo List of Strings describing the methods to record
	 */
	public void generate(String answerPackageName, String answerFileName, 
			String[] methods) {
		// strip anything after the "." if the answer name has been provided including a file extension
		if (answerFileName.indexOf(".") > 0)
			this.answerName =
					answerFileName.substring(0, answerFileName.indexOf("."));
		if (methods.length < 1) {
			System.out.println("Must provide information about one or more " + 
					"method to record.");
			return;
		}
		this.answerName = answerFileName;
		this.answerPackage = answerPackageName;
		this.numberMethods = methods.length;
		
		MethodInfo[] mList = processInput(methods);

		String aspectContentToWrite = getAspectJContent(mList);
		writeAspectJContentToFile(aspectContentToWrite);
		writeVariableListToFile(mList);
	}
	
	
	public int getNumVariables() {
		return numberMethods;
	}
	
	
	/**
	 * Processes the information provided about each method to record, and
	 * stores this information in an instance of the MethodInfo class to make
	 * access of this information easier when generating the AspectJ code.	
	 * Modify this function to process input correctly if the format changes.
	 * @param inputMethodInfo
	 * @return list of MethodInfo objects; each represents one method to record
	 */
	private MethodInfo[] processInput(String[] inputMethodInfo) {
		// could make ignore invalid method descriptions, or throw error?
		MethodInfo[] methods = new MethodInfo[inputMethodInfo.length];
		for (int i = 0; i < inputMethodInfo.length; i++) {
			String[] methodInfoAsList = inputMethodInfo[i].split(",");
			for (int j = 0; j < methodInfoAsList.length; j++)
				methodInfoAsList[j] = methodInfoAsList[j].trim();
			methods[i] = new MethodInfo(methodInfoAsList);
		}
		return methods;
	}
	
	
	/**
	 * Class representing a single method in the repast model that should be
	 * recorded.
	 */
	private class MethodInfo {
		String qualifiedMethodName;
		String shortMethodName;
		String[] paramTypes;
		String returnType;
		
		//[qualifiedMethodName, params..., returnType]
		public MethodInfo(String[] description) {
			// throw error if description.length < 2
			// which should have been caught by DSL (?)
			qualifiedMethodName = description[0];
			shortMethodName = getShortMethodName(qualifiedMethodName);
			returnType = description[description.length - 1];
			if (description.length > 2)
				paramTypes =
					Arrays.copyOfRange(description, 1, description.length - 1);
			else
				paramTypes = new String[0];
		}
		
		/*
		 * This method can be used for testing & manually verifying the
		 * processInput function and MethodInfo constructor were modified
		 * correctly, if the DSL input format is changed.
		 */
		void printMethodInfo() {
			System.out.println("\nMethod name: " + qualifiedMethodName +
					"\nShort method name: " + shortMethodName);
			System.out.print("Parameter Types: ");
			for (String p : paramTypes)
				System.out.print(p + " ");
			if (paramTypes.length == 0)
				System.out.print("no params");
			System.out.println("\nReturn type: " + returnType);
		}
	}
	

	/* This method generates a String that represents the content that 
	 * the AspectJ advice file should have in order to record the
	 * occurrences of the specified methods from the repast model.
	 * 
	 * pointcut METHODNAMEEvent():
	 *    call(RETURNTYPE METHODNAME(PARAMLIST items);
	 * RETURNTYPE around(): METHODNAMEEvent() {
	 *    RETURNTYPE result = proceed();
	 *    dataMgr.recordData("METHODNAME", result);
	 *    return result;
	 * }
	 *  */
	/**
	 * Generates a String that represents the content that the AspectJ advice
	 * file should contain in order to record the executions of each of the
	 * specified methods from the repast model.
	 * @param methodEventsToRecord
	 * @return
	 */
	private String getAspectJContent(MethodInfo[] methodEventsToRecord) {
		// create AspectJ answer heading:
		String answer = "package " + answerPackage + ";\n\n";
		answer += "import DataRecording.DataRecordManager;\n";
		answer += "import DataRecording.DataType;\n";
		answer += "import java.util.ArrayList;\n";
		answer += "public aspect " + answerName + " {\n\n";
		answer += "\tint numVariables = " + methodEventsToRecord.length + ";\n";
		answer += "\tString dataStorageLocation = \"src/" + answerPackage + "/" + answerName + "Data.txt\";\n\n";
		answer += "\tDataRecordManager dataMgr;\n\n";
		answer += getAspectJCodeToManageData();
		answer += "\n\n";
		
		// add AspectJ pointcuts & advice for each method to record:
		for (MethodInfo m : methodEventsToRecord)
			answer += getAspectJCodeToRecordMethodExecution(m);
		
		answer += "\n" + "}"; // closing bracket for file
		return answer;
	}
	
	
	/**
	 * Generates the AspectJ pointcut and advice to record a single method.
	 * 
	 * pointcut METHODNAMEEvent():
	 *    call(RETURNTYPE METHODNAME(PARAMLIST items);
	 * RETURNTYPE around(): METHODNAMEEvent() {
	 *    RETURNTYPE result = proceed();
	 *    *record*
	 *    return result;
	 * }
	 * 
	 * @param method Information representing a single method in the model
	 * @return
	 */
	private static String getAspectJCodeToRecordMethodExecution(MethodInfo method) {
		String aspectJPointcut = "\tpointcut " + method.shortMethodName + "Event():\n"
				+ "\t\texecution(" + method.returnType + " " + method.qualifiedMethodName + "("
				+ getParamTypeString(method.paramTypes) + "));\n";
		String aspectJAdvice = 
				"\t" + method.returnType + " around(): " + method.shortMethodName + "Event() {\n"
				+ "\t\t" + method.returnType + " result = proceed();\n"
				+ "\t\t" + getDataRecordingTemplate(method.shortMethodName) + "\n"
				+ "\t\treturn result;\n"
				+ "\t}"
				+ "\n\n";
		return aspectJPointcut + aspectJAdvice;
	}
	
	
	/**
	 * @param methodName The name of the method that this code should record
	 * @return The format/template for recording a single execution of this method.
	 */
	private static String getDataRecordingTemplate(String methodName) {
		return ("dataMgr.recordData(\"" + methodName + "\", result);");
	}
	
	
	/**
	 * Generates and returns the AspectJ code that will be used to instantiate
	 * a new DataRecordManager for the execution of the model. Generates code
	 * to instantiate the development of the FSA from the data from the run.
	 * @return AspectJ code for invoking the data recording and FSA creation
	 */
	private String getAspectJCodeToManageData() {
		String output = "\tpointcut setupExecution() :\n"
				+ "\t\texecution(void " + modelPackageName + ".setup());";
		output += "\n\n\tafter() : execution(void " + modelPackageName 
				+ ".setup()) {";
		output += "\n\t\tdataMgr = new DataRecordManager(numVariables, dataStorageLocation);";
		output += "\n\t}";
		
		output += "\n\tafter() :" 
				+ "execution(void " + modelPackageName + ".atEnd()) {";
		output += "\n\t\tdataMgr.writeRecentDataToFile();";
		output += "\n\t}";
		return output;
	}
	
	
	/** @returns String method name without package and class name. */
	private static String getShortMethodName(String qualifiedMethodName) {
		int lastIndex = qualifiedMethodName.lastIndexOf('.');
		return qualifiedMethodName.substring(lastIndex + 1);
	}
	
	
	/** @returns The array of parameter types as a comma delimited String. */
	private static String getParamTypeString(String[] paramTypeList) {
		String paramTypesString = "";
		int numAdded = 1;
		for (String type : paramTypeList) {
			paramTypesString += type;
			// if not the last parameter, add comma and space:
			if (numAdded++ != paramTypeList.length)
				paramTypesString += ", ";
		}
		return paramTypesString;
	}
	
	
	/**
	 * Writes the AspectJ code to the aspectJ file.
	 * @param String content to write to an AspectJ file.
	 */
	private boolean writeAspectJContentToFile(String content) {
		// should this return boolean representing success?
		if (answerPackage == null || answerName == null) {
			throw new NullPointerException("AspectJ destination package or " +
					"file name is undefined");
		}
		String destinationLocation = "src/" + answerPackage + "/" + answerName + ".aj";
		File aspectJFile = new File(destinationLocation);
		FileWriter output;
		try {
			//dataFile.createNewFile();
			output = new FileWriter(aspectJFile);
			try {
				output.write(content);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} finally {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	/**
	 * Writes the list of method/variables names and types to the data file.
	 * @param methodList list of methods recorded
	 * @return
	 */
	private boolean writeVariableListToFile(MethodInfo[] methodList) {
		
		String methodNames = "";
		String methodTypes = "";
		for (int i = 0; i < methodList.length; i++) {
			methodNames += methodList[i].shortMethodName;
			methodTypes += methodList[i].returnType;
			if (i < methodList.length - 1) {
				methodNames += ", ";
				methodTypes += ", ";
			}
		}
		
		String destinationLocation = "src/" + answerPackage + "/" + answerName + "Data.txt";
		
		File dataFile = new File(destinationLocation);
		FileWriter outputWriter;
		try {
			//dataFile.createNewFile();
			outputWriter = new FileWriter(dataFile);
			try {
				outputWriter.write(methodNames + "\n");
				outputWriter.write(methodTypes + "\n");
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} finally {
				try {
					outputWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
		return true;
	}

}

