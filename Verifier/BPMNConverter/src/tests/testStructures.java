package tests;

import bpmnStructure.BpmnDiagram;
import bpmnStructure.FlowElement;
import bpmnStructure.activities.Task;
import bpmnStructure.events.BasicEndEvent;
import bpmnStructure.events.BasicStartEvent;
import bpmnStructure.gateways.ConvergingExclusiveGateway;
import bpmnStructure.gateways.DivergingExclusiveGateway;
import bpmnStructure.gateways.ExclusiveGateway;
import promela.PromelaGenerator;
import visitor.PrintVisitor;
import visitor.PromelaVisitor1;

public class testStructures {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// Simple Gateway Structure
		BpmnDiagram b1 = new BpmnDiagram("test2");

		b1.addStartEvent("start");
		b1.addParallelGateway("Gateway1");
		b1.addTask("Task1");
		b1.addTask("Task2");
		b1.addParallelGateway("Gateway2");
		b1.addEndEvent("end");

		b1.addSequenceFlow("start", "Gateway1");
		b1.addSequenceFlow("Gateway1", "Task1");
		b1.addSequenceFlow("Gateway1", "Task2");
		b1.addSequenceFlow("Task1", "Gateway2");
		b1.addSequenceFlow("Task2", "Gateway2");
		b1.addSequenceFlow("Gateway2", "end");
//		b1.splitIntoPieces();

		// PromelaVisitor1 pmv = new PromelaVisitor1();
		// for (FlowElement f: b1.getFlowelements()){
		// pmv.Visit(f);
		// }
//		b1.unambiguate();

		
		// Simple Gateway Structure
		BpmnDiagram b2 = new BpmnDiagram("test");

		b2.addStartEvent("start");
		b2.addParallelGateway("Gateway1");
		b2.addTask("Task1");
		b2.addTask("Task2");
		b2.addParallelGateway("Gateway2");
		b2.addEndEvent("end");

		b2.addSequenceFlow("start", "Gateway1");
		b2.addSequenceFlow("Gateway1", "Task1");
		b2.addSequenceFlow("Gateway1", "Task2");
		b2.addSequenceFlow("Task1", "Gateway2");
		b2.addSequenceFlow("Task2", "Gateway2");
		b2.addSequenceFlow("Gateway2", "end");
//		b1.splitIntoPieces();
		
		System.out.println("b1 == b2 ? " + b2.equals(b1));
		
//		PromelaGenerator pg = new PromelaGenerator(b1);

//		System.out.println(pg.generatePromela());

	}

}
