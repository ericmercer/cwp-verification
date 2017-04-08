package bpmnStructure.gateways;

import bpmnStructure.Gateway;
import visitor.Visitor;

public class ConvergingParallelGateway extends Gateway {

	public ConvergingParallelGateway(String name) {
		super(name);
	}

	public void accept(Visitor v) {
		v.Visit(this);

	}
	public String getProcessTemplateName(){
		return "merge_and_gate_only";
	}
}
