package tests;

import bpmnStructure.BpmnDiagram;
import promela.PromelaGenerator;

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

		b1.unambiguate();

		PromelaGenerator pg = new PromelaGenerator(b1);

		System.out.println(pg.generatePromela());

	}

}
