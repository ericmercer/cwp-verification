package bpmnStructure.dataTypes;

public class PromelaArray extends PromelaType {

	private PromelaType innerType;

	public PromelaArray(String name, PromelaType type, int size) {
		super(name);
		this.setInnerType(type);

	}

	public PromelaType getInnerType() {
		return innerType;
	}

	public void setInnerType(PromelaType innerType) {
		this.innerType = innerType;
	}

}
