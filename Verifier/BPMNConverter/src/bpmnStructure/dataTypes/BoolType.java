package bpmnStructure.dataTypes;

import bpmnStructure.exceptions.PromelaTypeSizeException;

public class BoolType extends PromelaType {

	private boolean defaultValue;

	public BoolType() throws PromelaTypeSizeException {
		// this.typeName = name;
		this(false);
	}

	public BoolType(boolean defaultValue)  {
		// this.typeName = name;
		this.defaultValue = defaultValue;
	}

	// @Override
	// public boolean isKey() {
	// return this.typeName.substring(0, 3).equals("key");
	// }

	// public String toString() {
	//
	// return "bool " + typeName;
	// }

	// @Override
	// public String generateTypeString() {
	// // TODO Auto-generated method stub
	// return "bool " + this.typeName;
	// }

	public String getTypeName() {
		// TODO Auto-generated method stub
		return "bool";
	}

	@Override
	public String generateTypeString() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getDefaultValue() {
		return this.defaultValue ? "true" : "false";
	}

}
