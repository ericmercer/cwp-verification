package tests;

import bpmnStructure.BpmnDiagram;
import bpmnStructure.BpmnProcess;



public class SimpleFlow {

	

	
	
	public  BpmnDiagram getManualBpmn() {
		BpmnDiagram diagram = new BpmnDiagram();

		/******* Define Types ***************/
		// TODO: Figure out how to add these in any order and still
		// put them in promela in the right order for dependencies to work

		

		BpmnProcess simpleFlow = diagram.addProcess("SimpleFlow");
		simpleFlow.addStartEvent("start");
		simpleFlow.addParallelGateway("divParGW");
		simpleFlow.addParallelGateway("conParGW");
		simpleFlow.addTask("task1");
		simpleFlow.addTask("task2");
		simpleFlow.addEndEvent("end");
		
		simpleFlow.addSequenceFlow("start", "divParGW");
		simpleFlow.addSequenceFlow("divParGW", "task1");
		simpleFlow.addSequenceFlow("divParGW", "task2");
		simpleFlow.addSequenceFlow("task1", "conParGW");
		simpleFlow.addSequenceFlow("task2", "conParGW");
		simpleFlow.addSequenceFlow("conParGW", "end");
		
		return diagram;
	}
}
