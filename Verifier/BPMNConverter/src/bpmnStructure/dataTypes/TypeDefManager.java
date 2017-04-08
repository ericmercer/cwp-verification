package bpmnStructure.dataTypes;

import java.util.ArrayList;



public class TypeDefManager {

	ArrayList<PromelaTypeDef> typeDefs = new ArrayList<PromelaTypeDef>();

	public PromelaTypeDef addTypeDef(String typeName)  {
		PromelaTypeDef td = new PromelaTypeDef(typeName);
		typeDefs.add(td);
	
		return td;
	}
	
	public String generateTypeDefString(){
		String output = "";
		
		for (PromelaTypeDef td: typeDefs){
			output += td.generateDefinitionString(true) + "\n";
		}
		
		return output;
	}
	
	public void orderByDependencies(){
		
	}

}
