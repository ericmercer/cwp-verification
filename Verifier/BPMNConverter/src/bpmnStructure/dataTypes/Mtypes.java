package bpmnStructure.dataTypes;

import java.util.TreeSet;
//TODO: Make this a singleton?
public class Mtypes {

	private static TreeSet<String> mtypes = new TreeSet<String>();

	public static void addMtype(String m) {
		mtypes.add(m);
	}

	public static void addMtypes(String[] mt) {
		for (String m : mt) {
			if(m == null) {
				System.out.println("message null");
			}
			mtypes.add(m);
		}
	}
	
	public static String mtypesToString() {
		StringBuilder output = new StringBuilder();
		for (String m : mtypes) {
			output.append("\n\t\t" + m);
		}
		return output.toString();
	}

}
