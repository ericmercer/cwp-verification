package tests;

import bpmnStructure.BpmnDiagram;
import bpmnStructure.BpmnProcess;
import promela.PromelaGenerator;

public class testStructures {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// Simple Gateway Structure
		BpmnDiagram diagram = new BpmnDiagram();
		BpmnProcess b1 = diagram.addProcess("test2", "test2");

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

		b1.unambiguate();

		PromelaGenerator pg = new PromelaGenerator(diagram);

		System.out.println(pg.generatePromela(0));

	}

}
