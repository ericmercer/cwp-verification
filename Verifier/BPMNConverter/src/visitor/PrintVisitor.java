package visitor;

import bpmnStructure.FlowElement;
import bpmnStructure.SequenceFlow;
import bpmnStructure.activities.Task;
import bpmnStructure.events.BasicEndEvent;
import bpmnStructure.events.BasicStartEvent;
import bpmnStructure.gateways.ConvergingExclusiveGateway;
import bpmnStructure.gateways.ConvergingParallelGateway;
import bpmnStructure.gateways.DivergingExclusiveGateway;
import bpmnStructure.gateways.DivergingParallelGateway;
import bpmnStructure.gateways.ExclusiveGateway;

public class PrintVisitor implements Visitor {

	@Override
	public void Visit(BasicStartEvent f) {
		// TODO Auto-generated method stub
		System.out.println(f.getElementId());
//		f.visited = true;

		for (SequenceFlow f1 : f.sequenceFlowOut) {
//			if (!f1.getEnd().visited) {
//				f1.getEnd().accept(this);
//			}
		}
	}

	@Override
	public void Visit(BasicEndEvent f) {
		// TODO Auto-generated method stub
		System.out.println(f.getElementId());
//		f.visited = true;
	}

	@Override
	public void Visit(Task f) {
		// TODO Auto-generated method stub
		System.out.println(f.getElementId());
//		f.visited = true;
	}

	@Override
	public void Visit(ConvergingExclusiveGateway f) {
		// TODO Auto-generated method stub
		System.out.println(f.getElementId());
//		f.visited = true;
	}

	@Override
	public void Visit(DivergingExclusiveGateway f) {
		// TODO Auto-generated method stub
		System.out.println(f.getElementId());
//		f.visited = true;
	}

	@Override
	public void Visit(ExclusiveGateway f) {
		// TODO Auto-generated method stub
		System.out.println(f.getElementId());
//		f.visited = true;
	}

	@Override
	public void Visit(FlowElement f) {
		// TODO Auto-generated method stub
		System.out.println(f.getElementId());
//		f.visited = true;

		for (SequenceFlow f1 : f.sequenceFlowOut) {
//			if (!f1.getEnd().visited) {
//				f1.getEnd().accept(this);
//			}
		}
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
