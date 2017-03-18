package bpmnStructure.dataTypes;

import java.util.ArrayList;

public class PromelaTypeDef extends PromelaType {

	public PromelaTypeDef(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	ArrayList<PromelaType> vars = new ArrayList<PromelaType>();

	public void addPromelaType(PromelaType p) {
		vars.add(p);
	}

	public void addPromelaTypeArray(String arrayName, PromelaType type, int size) {
		vars.add(new PromelaArray(arrayName, type, size));
	}

}
