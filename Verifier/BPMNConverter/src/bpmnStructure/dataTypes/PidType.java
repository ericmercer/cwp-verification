package bpmnStructure.dataTypes;



public class PidType extends PromelaType {

	public PidType()  {
		
	}


	@Override
	public String generateTypeString() {

		return "pid ";// + this.typeName ;
	}

	public String getTypeName() {
		// TODO Auto-generated method stub
		return "pid";
	}



}
