package bpmnStructure.dataTypes;

public abstract class PromelaType {
	

	

	public boolean isKey() {

		return false;
	}

	public String generateDefinitionString(boolean baseLevel) {
		return generateTypeString();
	}

	public abstract String getTypeName();
	
	public abstract String generateTypeString();

	public String getDefaultValue() {
		return "0";
	}
	
	/*if max size is not zero then it will create a constant as well*/
	public int getMaxSize() {
		return 0;
	}

}