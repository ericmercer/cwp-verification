package bpmnStructure.dataTypes;

import bpmnStructure.exceptions.PromelaTypeSizeException;

public class PidType extends PromelaType {

	public PidType(String name) throws PromelaTypeSizeException {
		
		// TODO Auto-generated constructor stub
	}

	
//	@Override
//	public boolean isKey(){
//		return this.typeName.substring(0,3).equals("key");
//	}

	@Override
	public String generateTypeString() {

		return "pid ";// + this.typeName ;
	}

	public String getTypeName() {
		// TODO Auto-generated method stub
		return "pid";
	}



}
