package tests;

import bpmnStructure.BpmnDiagram;
import bpmnStructure.BpmnProcess;
import promela.PromelaGenerator;

public class testEquality {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		BpmnDiagram diagram = new BpmnDiagram();
		// Simple Gateway Structure
		BpmnProcess b1 = diagram.addProcess("test2");

		b1.addStartEvent("start");
		b1.addParallelGateway("Gateway1");
		b1.addTask("Task1", null);
		b1.addTask("Task2", null);
		b1.addParallelGateway("Gateway2");
		b1.addEndEvent("end");

		b1.addSequenceFlow("start", "Gateway1");
		b1.addSequenceFlow("Gateway1", "Task1");
		b1.addSequenceFlow("Gateway1", "Task2");
		b1.addSequenceFlow("Task1", "Gateway2");
		b1.addSequenceFlow("Task2", "Gateway2");
		b1.addSequenceFlow("Gateway2", "end");
		// b1.splitIntoPieces();

		// PromelaVisitor1 pmv = new PromelaVisitor1();
		// for (FlowElement f: b1.getFlowelements()){
		// pmv.Visit(f);
		// }
		// b1.unambiguate();

		// Simple Gateway Structure
		BpmnProcess b2 = diagram.addProcess("test", "test");

		b2.addStartEvent("start");
		b2.addParallelGateway("Gateway1");
		b2.addTask("Task1", null);
		b2.addTask("Task2", null);
		b2.addParallelGateway("Gateway2");
		b2.addEndEvent("end");

		b2.addSequenceFlow("start", "Gateway1");
		b2.addSequenceFlow("Gateway1", "Task1");
		b2.addSequenceFlow("Gateway1", "Task2");
		b2.addSequenceFlow("Task1", "Gateway2");
		b2.addSequenceFlow("Task2", "Gateway2");
		b2.addSequenceFlow("Gateway2", "end");
		// b1.splitIntoPieces();

		System.out.println("b1 == b2 ? " + b2.equals(b1));

		 PromelaGenerator pg = new PromelaGenerator(diagram);

		 System.out.println(pg.generatePromela(0));

	}

}
