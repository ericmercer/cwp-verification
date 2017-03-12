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
			mtypes.add(m);
		}
	}

}
