package bpmnStructure.dataTypes;

import java.util.ArrayList;

public class TypeDefManager {

	ArrayList<PromelaTypeDef> typeDefs = new ArrayList<PromelaTypeDef>();

	public PromelaTypeDef addTypeDef(String name) {
		PromelaTypeDef td = new PromelaTypeDef(name);
		typeDefs.add(td);
		return td;
	}

}
