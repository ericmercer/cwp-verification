package mathflow.nodes;

public class MFInfoObjData {
	
	public String name;
	public String scope_class;
	public String type;
	public String state;
	public String description;
	public String[] attrNames;
	public String[] attrValues;
	
	public String toString() {
		return "{name= "+name+"\n\t"+
				"scope/class= "+scope_class+"\n\t"+
				"type= "+type+"\n\t"+
				"state= "+state+"\n\t"+
				"description= "+description+"\n\t"+
				"attributes= "+arrayToString(attrNames)+"}";
	}
	
	private String arrayToString(String[] array) {
		if (array == null) {
			return "[]";
		} else {
			String result = "[";
			for (int i=0; i<array.length; i++) {
				result += array[i];
				if (i != array.length-1) {
					result += ", ";
				}
			}
			result += "]";
			return result;
		}
	}
}
