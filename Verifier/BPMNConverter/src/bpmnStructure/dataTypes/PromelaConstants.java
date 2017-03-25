package bpmnStructure.dataTypes;

import java.util.Map;
import java.util.TreeMap;

/*All #defines are defined here*/
public class PromelaConstants {

	private static TreeMap<String, Integer> constants = new TreeMap<String, Integer>();

	public static void addConstant(String constantName, int value) {
	
		if (constants.containsKey(constantName) && !constants.get(constantName).equals(value)) {
			// TODO:Throw error
			System.err.println("new constant not added; conflicting values for constant " + constantName + ", " + value + " != " + constants.get(constantName));
		} else {

			constants.put(constantName, value);
		}

	}

	public static String generateConstantString() {
		String output = "";
	
		for (Map.Entry<String, Integer> entry : constants.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();
		
			output = output  + "#define " + key + " " + value + ";\n";
		
		}
		return output;

	}

}
