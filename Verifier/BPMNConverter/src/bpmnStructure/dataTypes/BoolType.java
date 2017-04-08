package bpmnStructure.dataTypes;



public class BoolType extends PromelaType {

	private boolean defaultValue;

	public BoolType()  {
		this(false);
	}

	public BoolType(boolean defaultValue)  {
		// this.typeName = name;
		this.defaultValue = defaultValue;
	}


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
