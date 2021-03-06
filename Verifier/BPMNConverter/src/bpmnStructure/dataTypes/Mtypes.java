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

	public static String toPromela() {
		if (mtypes.size() > 0){
		String out = "mtype = {";
		int i = 0;
		for (String mtypeString : mtypes) {
			i++;
			out += mtypeString;
			if (i < mtypes.size()){
				out += ", ";
			}
		}
		out += "};\n";
		return out;}
		else{
			return "";
		}
	}

}
