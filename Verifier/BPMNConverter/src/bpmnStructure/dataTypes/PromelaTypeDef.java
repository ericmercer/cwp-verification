package bpmnStructure.dataTypes;

import java.util.ArrayList;

public class PromelaTypeDef extends PromelaVariable{

	public PromelaTypeDef(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	ArrayList<PromelaVariable> vars = new ArrayList<PromelaVariable>();

	public void addPromelaVariable(PromelaVariable p) {
		vars.add(p);
	}

}
