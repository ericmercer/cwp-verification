package bpmnStructure;

import bpmnStructure.dataTypes.PromelaType;
import bpmnStructure.dataTypes.PromelaTypeDef;

public class Channel {

	private String name;
	private PromelaType type;

	public Channel(String name, PromelaType type) {
		this.setName(name);
		this.setType(type);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PromelaType getType() {
		return type;
	}

	public void setType(PromelaType type) {
		this.type = type;
	}


}
