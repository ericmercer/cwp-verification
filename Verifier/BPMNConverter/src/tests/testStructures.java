package tests;

import bpmnStructure.BpmnDiagram;
import bpmnStructure.activities.Task;
import bpmnStructure.events.BasicEndEvent;
import bpmnStructure.events.BasicStartEvent;
import bpmnStructure.gateways.ConvergingExclusiveGateway;
import bpmnStructure.gateways.DivergingExclusiveGateway;
import bpmnStructure.gateways.ExclusiveGateway;
import visitor.PrintVisitor;

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
		b1.addParallelGateway("DivGateway");
		b1.addTask("Task1");
		b1.addTask("Task2");
		b1.addParallelGateway("ConvGateway");
		b1.addEndEvent("end");
		
		b1.addSequenceFlow("start", "DivGateway");
		b1.addSequenceFlow("DivGateway", "Task1");
		b1.addSequenceFlow("DivGateway", "Task2");
		b1.addSequenceFlow("Task1", "ConvGateway");
		b1.addSequenceFlow("Task2", "ConvGateway");
		b1.addSequenceFlow("ConvGateway", "end");
		
		
//		BasicStartEvent start2 = new BasicStartEvent("start");
//		ExclusiveGateway gateway1 = new ExclusiveGateway("DivGatway");
//		Task task1 = new Task("Task1");
//		Task task2 = new Task("Task2");
//		ExclusiveGateway gateway2 = new ExclusiveGateway("ConvGateway");
//		BasicEndEvent end2 = new BasicEndEvent("end");
//
//		start2.addSequenceFlow(gateway1);
//		gateway1.addSequenceFlow(task1);
//		gateway1.addSequenceFlow(task2);
//
//		task1.addSequenceFlow(gateway2);
//		task2.addSequenceFlow(gateway2);
//
//		gateway2.addSequenceFlow(end2);
		PrintVisitor pv = new PrintVisitor();
		b1.getFlowElement("start").accept(pv);

		System.out.println("done");

	}

}
