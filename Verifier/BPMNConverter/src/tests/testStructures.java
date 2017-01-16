package tests;

import bpmnStructure.BpmnDiagram;
import bpmnStructure.FlowElement;
import bpmnStructure.activities.Task;
import bpmnStructure.events.BasicEndEvent;
import bpmnStructure.events.BasicStartEvent;
import bpmnStructure.gateways.ConvergingExclusiveGateway;
import bpmnStructure.gateways.DivergingExclusiveGateway;
import bpmnStructure.gateways.ExclusiveGateway;
import visitor.PrintVisitor;
import visitor.PromelaVisitor1;

public class testStructures {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		BpmnDiagram b = new BpmnDiagram();
		b.addStartEvent("start");
		b.addTask("Task1");
		b.addEndEvent("end");
		b.addSequenceFlow("start", "Task1");
		b.addSequenceFlow("Task1", "end");

		
		

		// // Basic Task Structure
		// BasicStartEvent start = new BasicStartEvent("start");
		// Task task = new Task("Task1");
		// BasicEndEvent end = new BasicEndEvent("end");
		// start.addSequenceFlow(task);
		// task.addSequenceFlow(end);

		// Simple Gateway Structure
		BpmnDiagram b1 = new BpmnDiagram();
		
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
		b1.splitIntoPieces();
		
//		PromelaVisitor1 pmv = new PromelaVisitor1();
//		for (FlowElement f: b1.getFlowelements()){
//			pmv.Visit(f);
//		}
		


		
		System.out.println(b1.generatePromelaString());

	}

}
