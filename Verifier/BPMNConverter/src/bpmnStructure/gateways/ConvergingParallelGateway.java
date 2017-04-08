package bpmnStructure.gateways;

import bpmnStructure.Gateway;
import visitor.Visitor;

public class ConvergingParallelGateway extends Gateway {

	public ConvergingParallelGateway(String elementId,String elementName) {
		super(elementId, elementName);
	}

	public void accept(Visitor v) {
		v.Visit(this);

	}

}
