package bpmnStructure.gateways;

import bpmnStructure.Gateway;
import visitor.Visitor;

public class DivergingParallelGateway extends Gateway{

	public DivergingParallelGateway(String elementId,String elementName) {
		super(elementId, elementName);
	}
	public void accept(Visitor v) {
		v.Visit(this);


	}

}
