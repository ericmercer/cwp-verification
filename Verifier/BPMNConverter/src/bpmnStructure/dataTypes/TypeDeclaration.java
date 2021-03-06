package bpmnStructure.dataTypes;

public class TypeDeclaration {
	private PromelaType type;
	private int capacity;
	private String varName;

	public TypeDeclaration(String name, PromelaType p, int capacity) {
		this.type = p;
		this.capacity = capacity;
		this.setVarName(name);

	}

	public PromelaType getType() {
		return type;
	}

	public void setType(PromelaType type) {
		this.type = type;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String generateDeclaration() {
		// String typeDeclaration = this.type.generateTypeString();
		if (this.type.getMaxSize() >= 0) {
		
			PromelaConstants.addConstant("MAX_" + this.getVarName().toUpperCase(), this.type.getMaxSize());
		}
		String typeDeclaration = this.type.getTypeName() + " " + this.getVarName();
		if (this.capacity > 0) {
			typeDeclaration += "[" + capacity + "]";
		}
		/* this assumes that complex types always have a zero default */
		if (!this.type.getDefaultValue().equals("0")) {
			typeDeclaration += " = " + this.type.getDefaultValue();
		}
		return typeDeclaration;
	}

	public String generateDefinitionString() {
		
		return this.type.generateDefinitionString(false);
	}


	public String getVarName() {
		return varName;
	}

	public void setVarName(String varName) {
		this.varName = varName;
	}


}
