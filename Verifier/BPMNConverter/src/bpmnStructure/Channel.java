package bpmnStructure;

import bpmnStructure.dataTypes.PromelaTypeDef;

public class Channel {

	public String name;
	public PromelaTypeDef type;

	public Channel(String name, PromelaTypeDef type) {
		this.name = name;
		this.type = type;
	}

}
