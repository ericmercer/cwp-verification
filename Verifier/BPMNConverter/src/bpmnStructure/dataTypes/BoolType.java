package bpmnStructure.dataTypes;



public class BoolType extends PromelaType {

	private boolean defaultValue;

	public BoolType()  {
		this(false);
	}

	public BoolType(boolean defaultValue)  {
		this.defaultValue = defaultValue;
	}


	public String getTypeName() {	
		return "bool";
	}

	@Override
	public String generateTypeString() {
		return null;
	}
	@Override
	public String getDefaultValue() {
		return this.defaultValue ? "true" : "false";
	}

}
