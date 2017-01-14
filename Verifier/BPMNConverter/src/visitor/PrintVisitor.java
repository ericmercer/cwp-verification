package visitor;

import bpmnStructure.FlowElement;
import bpmnStructure.activities.Task;
import bpmnStructure.events.BasicEndEvent;
import bpmnStructure.events.BasicStartEvent;
import bpmnStructure.gateways.ConvergingExclusiveGateway;
import bpmnStructure.gateways.DivergingExclusiveGateway;
import bpmnStructure.gateways.ExclusiveGateway;

public class PrintVisitor implements Visitor {

	@Override
	public void Visit(BasicStartEvent f) {
		// TODO Auto-generated method stub
		System.out.println(f.name);
		f.visited = true;

		for (FlowElement f1 : f.sequenceFlow) {
			if (!f1.visited) {
				f1.accept(this);
			}
		}
	}

	@Override
	public void Visit(BasicEndEvent f) {
		// TODO Auto-generated method stub
		System.out.println(f.name);
		f.visited = true;
	}

	@Override
	public void Visit(Task f) {
		// TODO Auto-generated method stub
		System.out.println(f.name);
		f.visited = true;
	}

	@Override
	public void Visit(ConvergingExclusiveGateway f) {
		// TODO Auto-generated method stub
		System.out.println(f.name);
		f.visited = true;
	}

	@Override
	public void Visit(DivergingExclusiveGateway f) {
		// TODO Auto-generated method stub
		System.out.println(f.name);
		f.visited = true;
	}

	@Override
	public void Visit(ExclusiveGateway f) {
		// TODO Auto-generated method stub
		System.out.println(f.name);
		f.visited = true;
	}

	@Override
	public void Visit(FlowElement f) {
		// TODO Auto-generated method stub
		System.out.println(f.name);
		f.visited = true;

		for (FlowElement f1 : f.sequenceFlow) {
			if (!f1.visited) {
				f1.accept(this);
			}
		}
	}

}
