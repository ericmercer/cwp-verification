package bpmnStructure.dataTypes;

import bpmnStructure.exceptions.PromelaTypeSizeException;

public class MtypeType extends PromelaType {

	String defaultValue = "";

	public MtypeType(String[] mtypeValues) throws PromelaTypeSizeException {
		this(mtypeValues, "");
	}

	public MtypeType(String[] mtypeValues, String defaultValue) throws PromelaTypeSizeException {
		this.defaultValue = defaultValue;
		Mtypes.addMtypes(mtypeValues);
		// TODO Auto-generated constructor stub
	}

	// @Override
	// public boolean isKey(){
	// return this.typeName.substring(0,3).equals("key");
	// }

	@Override
	public String generateTypeString() {
		// TODO Auto-generated method stub
		return "mtype ";// +this.typeName;
	}

	public String getTypeName() {
		// TODO Auto-generated method stub
		return "mtype";
	
	}
	public String getDefaultValue(){
		return !defaultValue.equals("")?defaultValue:"0";
	}

}
