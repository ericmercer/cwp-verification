package bpmnStructure.dataTypes;



public class MtypeType extends PromelaType {

	String defaultValue = "";

	public MtypeType(String[] mtypeValues)  {
		this(mtypeValues, "");
	}

	public MtypeType(String[] mtypeValues, String defaultValue)  {
		this.defaultValue = defaultValue;
		Mtypes.addMtypes(mtypeValues);
	}


	@Override
	public String generateTypeString() {
		
		return "mtype ";
	}

	public String getTypeName() {
		return "mtype";
	
	}
	public String getDefaultValue(){
		return !defaultValue.equals("")?defaultValue:"0";
	}

}
