package visitor;

import java.util.ArrayList;

import bpmnStructure.FlowElement;
import bpmnStructure.activities.Task;
import bpmnStructure.events.BasicEndEvent;
import bpmnStructure.events.BasicStartEvent;
import bpmnStructure.gateways.ConvergingExclusiveGateway;
import bpmnStructure.gateways.DivergingExclusiveGateway;
import bpmnStructure.gateways.ExclusiveGateway;
import promela.PromelaProcess;

public class PromelaVisitor1 implements Visitor {

	/* Instantiate a PROMELA template class that */
	/* handles building the PROMELA file */
	ArrayList<PromelaProcess> processes = new ArrayList<PromelaProcess>();
	
	@Override
	public void Visit(BasicStartEvent f) {
		// TODO Auto-generated method stub
		System.out.println(f.name);

	}

	@Override
	public void Visit(BasicEndEvent f) {
		// TODO Auto-generated method stub
		System.out.println(f.name);
	}

	@Override
	public void Visit(Task f) {
		// TODO Auto-generated method stub
		System.out.println(f.name);
	}

	@Override
	public void Visit(ConvergingExclusiveGateway f) {
		// TODO Auto-generated method stub
		System.out.println(f.name);
	}

	@Override
	public void Visit(DivergingExclusiveGateway f) {
		// TODO Auto-generated method stub
		System.out.println(f.name);
	}

	@Override
	public void Visit(ExclusiveGateway f) {
		// TODO Auto-generated method stub
		System.out.println(f.name);
	}

	@Override
	public void Visit(FlowElement f) {
		// TODO Auto-generated method stub
		System.out.println(f.name);

	}

}
