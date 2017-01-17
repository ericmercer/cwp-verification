package bpmnStructure.gateways;

import bpmnStructure.Gateway;
import visitor.Visitor;

public class DivergingParallelGateway extends Gateway{

	public DivergingParallelGateway(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	public void accept(Visitor v) {
		v.Visit(this);


	}

}
