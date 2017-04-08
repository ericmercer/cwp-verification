package visitor;

import java.util.ArrayList;

import bpmnStructure.FlowElement;
import bpmnStructure.activities.Task;
import bpmnStructure.events.BasicEndEvent;
import bpmnStructure.events.BasicStartEvent;
import bpmnStructure.gateways.ConvergingExclusiveGateway;
import bpmnStructure.gateways.ConvergingParallelGateway;
import bpmnStructure.gateways.DivergingExclusiveGateway;
import bpmnStructure.gateways.DivergingParallelGateway;
import bpmnStructure.gateways.ExclusiveGateway;


public class PromelaVisitor1 implements Visitor {

	/* Instantiate a PROMELA template class that */
	/* handles building the PROMELA file */

	
	@Override
	public void Visit(BasicStartEvent f) {
		// TODO Auto-generated method stub
		System.out.println(f.getName());

	}

	@Override
	public void Visit(BasicEndEvent f) {
		// TODO Auto-generated method stub
		System.out.println(f.getName());
	}

	@Override
	public void Visit(Task f) {
		// TODO Auto-generated method stub
		System.out.println(f.getName());
	}

	@Override
	public void Visit(ConvergingExclusiveGateway f) {
		// TODO Auto-generated method stub
		System.out.println(f.getName());
	}

	@Override
	public void Visit(DivergingExclusiveGateway f) {
		// TODO Auto-generated method stub
		System.out.println(f.getName());
	}

	@Override
	public void Visit(ExclusiveGateway f) {
		// TODO Auto-generated method stub
		System.out.println(f.getName());
	}

	@Override
	public void Visit(FlowElement f) {
		// TODO Auto-generated method stub
		System.out.println(f.getName());

	}

	@Override
	public void Visit(ConvergingParallelGateway f) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Visit(DivergingParallelGateway f) {
		// TODO Auto-generated method stub
		
	}

}
