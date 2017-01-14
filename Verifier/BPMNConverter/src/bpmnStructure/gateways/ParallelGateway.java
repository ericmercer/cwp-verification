package bpmnStructure.gateways;

import bpmnStructure.Gateway;


public class ParallelGateway extends Gateway {

	/*possibly translate normal gateways into two gatways if multiple inputs*/
	/*coud use a visitor to translate structure*/
	public ParallelGateway(String name) {
		super(name);
	}
}