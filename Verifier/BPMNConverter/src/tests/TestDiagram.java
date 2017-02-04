package tests;

import bpmnStructure.BpmnDiagram;
import xmlConversion.ConvertToBpmn;

public class TestDiagram {
	
	private static String[] tests = {
			"Test_2_step", "Test_4_Step", "Test_book_example10"
	};
	
	public static void main( String args[] ) {
		
		int selection = Integer.parseInt(args[0]);
		TestDiagram tester = new TestDiagram();
		
		switch(selection) {
		case 1: 
			tester.test_2_step();
			break;
		case 2:
			tester.test_4_step();
			break;
		case 3:
			tester.test_sub_process_test1();
			break;
		case 4:
			tester.test_book_example10();
			break;
		}
		
		return;
	}
	
	public void test_2_step() {
		ConvertToBpmn convert = new ConvertToBpmn();
		BpmnDiagram diagram = convert.importXML("tests/2_step.bpmn");
		
		BpmnDiagram correct = new BpmnDiagram("Process_1");
		correct.addStartEvent("StartEvent_1");
		correct.addEndEvent("EndEvent_1aqgj4v");
		correct.addSequenceFlow("StartEvent_1", "EndEvent_1aqgj4v");
		
		System.out.print("Testing: ");
		System.out.println( correct.equals(diagram) );
	}
	
	public void test_4_step() {
		ConvertToBpmn convert = new ConvertToBpmn();
		BpmnDiagram diagram = convert.importXML("tests/4_step.bpmn");
		
		BpmnDiagram correct = new BpmnDiagram("Process_1");
		correct.addStartEvent("StartEvent_1");
		correct.addEndEvent("EndEvent_1aqgj4v");
		correct.addTask("Task_1");
		correct.addExclusiveGateway("ExclusiveGateway_1");
		correct.addSequenceFlow("StartEvent_1", "Task_1");
		correct.addSequenceFlow("Task_1", "ExclusiveGateway_1");
		correct.addSequenceFlow("ExclusiveGateway_1", "EndEvent_1aqgj4v");
		
		System.out.print("Testing: ");
		System.out.println( correct.equals(diagram) );
	}
	
	public void test_sub_process_test1() {
		ConvertToBpmn convert = new ConvertToBpmn();
		BpmnDiagram diagram = convert.importXML("tests/sub_process_test1.bpmn");
		
		BpmnDiagram correct = new BpmnDiagram("Process_1");
		correct.addStartEvent("StartEvent_1");
		correct.addEndEvent("EndEvent_1aqgj4v");
		correct.addTask("Task_1");
		correct.addExclusiveGateway("ExclusiveGateway_1");
		correct.addSequenceFlow("StartEvent_1", "Task_1");
		correct.addSequenceFlow("Task_1", "ExclusiveGateway_1");
		correct.addSequenceFlow("ExclusiveGateway_1", "EndEvent_1aqgj4v");
		
		System.out.print("Test: ");
		System.out.println( correct.equals(diagram) );
	}
	
	public void test_book_example10() {
		ConvertToBpmn convert = new ConvertToBpmn();
		BpmnDiagram diagram = convert.importXML("tests/book_example10.1.bpmn");
		
		BpmnDiagram correct = new BpmnDiagram("Process_1");
		correct.addStartEvent("StartEvent_1");
		correct.addEndEvent("EndEvent_1");
		
		correct.addTask("Task_1");
		
		correct.addExclusiveGateway("ExclusiveGateway_1");
		correct.addExclusiveGateway("ExclusiveGateway_2");
		
		correct.addNormalSubProcess("SubProcess_1");
		correct.addNormalSubProcess("AdHocSubProcess_1");
		
		correct.addSequenceFlow("StartEvent_1", "ExclusiveGateway_2");
		correct.addSequenceFlow("ExclusiveGateway_2", "Task_1");
		correct.addSequenceFlow("ExclusiveGateway_2", "AdHocSubProcess_1");
		correct.addSequenceFlow("Task_1", "ExclusiveGateway_1");
		correct.addSequenceFlow("AdHocSubProcess_1", "SubProcess_1");
		correct.addSequenceFlow("SubProcess_1", "ExclusiveGateway_1");
		correct.addSequenceFlow("ExclusiveGateway_1", "EndEvent_1");
		
		System.out.print("Test: ");
		System.out.println( correct.equals(diagram) );
	}

}
