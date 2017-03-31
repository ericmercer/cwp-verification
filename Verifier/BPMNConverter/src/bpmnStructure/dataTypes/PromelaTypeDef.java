package bpmnStructure.dataTypes;

import java.util.ArrayList;

public class PromelaTypeDef extends PromelaType {

	private ArrayList<PromelaType> keyVars = new ArrayList<PromelaType>();
	private ArrayList<TypeDeclaration> vars = new ArrayList<TypeDeclaration>();
	private String typeName;
	public PromelaTypeDef(String typeName) {
		this.typeName = typeName;

	}

	public void addPromelaType(PromelaType p) {
		this.addPromelaType(p.getTypeName(), p);
	}

	public void addPromelaType(PromelaType p, int capacity) {

	}

	public void addPromelaType(String varName, PromelaType p) {
		this.addPromelaType(varName, p, 0);
	}

	public void addPromelaType(String varName, PromelaType p, int capacity) {
		vars.add(new TypeDeclaration(varName, p, capacity));
		if (capacity == 1 && varName.substring(0, 3).equalsIgnoreCase("key")) {
			keyVars.add(p);
		}
	}

	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append(typeName + "\n{");
		for (int i = 0; i < vars.size(); i++) {
			output.append("\t" + vars.get(i) + "\t");
		}
		output.append("}");
		return output.toString();
	}

	public String generateTypeString() {
		return this.getTypeName() + " " + this.typeName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String name) {
		this.typeName = name;
	}

	@Override
	public String generateDefinitionString(boolean baseLevel) {
		String output;
		if (baseLevel) {
			output = "typedef " + this.getTypeName() + "{\n";
			for (TypeDeclaration pt : vars) {

				output += pt.generateDeclaration() + ";\n";

			}
			output += "};";
		} else {
			output = this.generateTypeString();
		}

		return output;
	}

}
