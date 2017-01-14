package bpmnStructure.gateways;

import bpmnStructure.Gateway;

public class ExclusiveGateway extends Gateway {

	/*possibly translate normal gateways into two gatways if multiple inputs*/
	/*coud use a visitor to translate structure*/
	public ExclusiveGateway(String name) {
		super(name);
	}
}
