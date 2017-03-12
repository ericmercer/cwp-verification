package bpmnStructure.dataTypes;

public class MtypeType extends PromelaVariable {

	public MtypeType(String name, String[] mtypeValues) {
		super(name);
		Mtypes.addMtypes(mtypeValues);
		// TODO Auto-generated constructor stub
	}

}
