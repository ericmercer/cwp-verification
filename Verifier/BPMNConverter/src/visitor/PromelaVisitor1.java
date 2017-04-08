package visitor;


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
		// TODO Future - Auto-generated method stub
		System.out.println(f.getElementId());

	}

	@Override
	public void Visit(BasicEndEvent f) {
		// TODO Future - Auto-generated method stub
		System.out.println(f.getElementId());
	}

	@Override
	public void Visit(Task f) {
		// TODO Future - Auto-generated method stub
		System.out.println(f.getElementId());
	}

	@Override
	public void Visit(ConvergingExclusiveGateway f) {
		// TODO Future - Auto-generated method stub
		System.out.println(f.getElementId());
	}

	@Override
	public void Visit(DivergingExclusiveGateway f) {
		// TODO Future - Auto-generated method stub
		System.out.println(f.getElementId());
	}

	@Override
	public void Visit(ExclusiveGateway f) {
		// TODO Future - Auto-generated method stub
		System.out.println(f.getElementId());
	}

	@Override
	public void Visit(FlowElement f) {
		// TODO Future - Auto-generated method stub
		System.out.println(f.getElementId());

	}

	@Override
	public void Visit(ConvergingParallelGateway f) {
		// TODO Future - Auto-generated method stub
		
	}

	@Override
	public void Visit(DivergingParallelGateway f) {
		// TODO Future - Auto-generated method stub
		
	}

}
