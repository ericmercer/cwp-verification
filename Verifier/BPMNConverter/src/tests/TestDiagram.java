package tests;

import bpmnStructure.BpmnDiagram;
import xmlConversion.ConvertToBpmn;

public class TestDiagram {
	
//	private static String[] tests = {
//			"Test_2_step", "Test_4_Step", "sub_process_test1", "Test_book_example10", "MyName", "test_Order_fulfillment"
//	};
	
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
		case 5:
			tester.test_MyName();
			break;
		case 6:
			tester.test_Order_fulfillment();
			break;
		}
		
		return;
	}
	
	public void test_2_step() {
		ConvertToBpmn convert = new ConvertToBpmn();
		BpmnDiagram diagram = convert.importXML("tests/diagrams/2_step.bpmn");
		
		BpmnDiagram correct = new BpmnDiagram("Process_1");
		correct.addStartEvent("StartEvent_1");
		correct.addEndEvent("EndEvent_1aqgj4v");
		correct.addSequenceFlow("StartEvent_1", "EndEvent_1aqgj4v");
		
		System.out.print("Testing: ");
		System.out.println( correct.equals(diagram) );
	}
	
	public void test_4_step() {
		ConvertToBpmn convert = new ConvertToBpmn();
		BpmnDiagram diagram = convert.importXML("tests/diagrams/4_step.bpmn");
		
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
		BpmnDiagram diagram = convert.importXML("tests/diagrams/sub_process_test1.bpmn");
		
		BpmnDiagram correct = new BpmnDiagram("sub_process_test1");
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
	
	public void test_book_example10() {
		ConvertToBpmn convert = new ConvertToBpmn();
		BpmnDiagram diagram = convert.importXML("tests/diagrams/book_example10.1.bpmn");
		
		BpmnDiagram correct = new BpmnDiagram("book_example10Test");
		correct.addStartEvent("StartEvent_1");
		correct.addEndEvent("EndEvent_1");
		
		correct.addTask("Task_1");
		correct.addTask("Task_2");
		correct.addTask("Task_3");
		correct.addTask("Task_4");
		correct.addTask("Task_5");
		correct.addTask("Task_6");
		correct.addTask("Task_7");
		correct.addTask("Task_8");
		
		correct.addExclusiveGateway("ExclusiveGateway_1");
		correct.addExclusiveGateway("ExclusiveGateway_2");
		correct.addExclusiveGateway("ExclusiveGateway_1");
		correct.addExclusiveGateway("ExclusiveGateway_2");
		correct.addExclusiveGateway("ExclusiveGateway_1");
		
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
	
	public void test_MyName() {
		ConvertToBpmn convert = new ConvertToBpmn();
		BpmnDiagram diagram = convert.importXML("tests/diagrams/MyName.bpmn");
		
		BpmnDiagram correct = new BpmnDiagram("Process_1");
		correct.addStartEvent("StartEvent_1");
		correct.addEndEvent("EndEvent_0259rfj");
		
		correct.addTask("Task_0q22whk");
		correct.addTask("Task_12uwadk");
		correct.addTask("Task_1vukq14");
		correct.addTask("Task_1c3wjjf");
		
		correct.addIntermediateEvent("IntermediateThrowEvent_060vul7");
		correct.addIntermediateEvent("IntermediateThrowEvent_1bva7ei");
		
		correct.addExclusiveGateway("ExclusiveGateway_1jr1gc4");
		correct.addExclusiveGateway("ExclusiveGateway_1anf5gt");
		correct.addExclusiveGateway("ExclusiveGateway_0hpt3dk");
		correct.addExclusiveGateway("ExclusiveGateway_1");
		
		correct.addNormalSubProcess("SubProcess_0zaafna");
		
		correct.addSequenceFlow("StartEvent_1", "ExclusiveGateway_0hpt3dk");
		correct.addSequenceFlow("ExclusiveGateway_0hpt3dk", "SubProcess_0zaafna");
		correct.addSequenceFlow("SubProcess_0zaafna", "Task_1vukq14");
		correct.addSequenceFlow("ExclusiveGateway_0hpt3dk", "Task_1c3wjjf");
		correct.addSequenceFlow("Task_1c3wjjf", "ExclusiveGateway_1");
		correct.addSequenceFlow("Task_1vukq14", "ExclusiveGateway_1");
		correct.addSequenceFlow("ExclusiveGateway_1", "EndEvent_0259rfj");
		correct.addSequenceFlow("ExclusiveGateway_1jr1gc4", "Task_0q22whk");
		correct.addSequenceFlow("ExclusiveGateway_1jr1gc4", "Task_12uwadk");
		correct.addSequenceFlow("Task_12uwadk", "ExclusiveGateway_1anf5gt");
		correct.addSequenceFlow("Task_0q22whk", "ExclusiveGateway_1anf5gt");
		correct.addSequenceFlow("ExclusiveGateway_1jr1gc4", "IntermediateThrowEvent_060vul7");
		correct.addSequenceFlow("IntermediateThrowEvent_060vul7", "IntermediateThrowEvent_1bva7ei");
		correct.addSequenceFlow("IntermediateThrowEvent_1bva7ei", "ExclusiveGateway_1anf5gt");
		
		System.out.print("Test: ");
		System.out.println( correct.equals(diagram) );
	}
	
	public void test_Order_fulfillment() {
		ConvertToBpmn convert = new ConvertToBpmn();
		BpmnDiagram diagram = convert.importXML("tests/diagrams/orderFulfillment.bpmn");
		
		BpmnDiagram correct = new BpmnDiagram("Process_1");
		correct.addStartEvent("StartEvent_1");
		correct.addEndEvent("EndEvent_2");
		correct.addEndEvent("EndEvent_3");
		
		correct.addTask("UserTask_1");
		
		correct.addExclusiveGateway("ExclusiveGateway_1");
		correct.addExclusiveGateway("ExclusiveGateway_2");
		correct.addExclusiveGateway("ExclusiveGateway_3");
		correct.addExclusiveGateway("ExclusiveGateway_5");
		correct.addExclusiveGateway("ExclusiveGateway_6");
		
		correct.addNormalSubProcess("SubProcess_2");
			correct.addStartEvent("StartEvent_2");
			correct.addEndEvent("EndEvent_4");
			correct.addEndEvent("EndEvent_5");
			correct.addTask("UserTask_2");
			correct.addExclusiveGateway("ExclusiveGateway_4");
			correct.addSequenceFlow("ExclusiveGateway_4", "EndEvent_4");
			correct.addSequenceFlow("ExclusiveGateway_4", "EndEvent_5");
			correct.addSequenceFlow("UserTask_2", "ExclusiveGateway_4");
			correct.addSequenceFlow("StartEvent_2", "UserTask_2");
		
//		    <bpmn2:boundaryEvent id="BoundaryEvent_3" name="" attachedToRef="SubProcess_2">
//		      <bpmn2:outgoing>SequenceFlow_22</bpmn2:outgoing>
//		      <bpmn2:signalEventDefinition id="SignalEventDefinition_2"/>
//		    </bpmn2:boundaryEvent>
//		    <bpmn2:boundaryEvent id="BoundaryEvent_4" name="" attachedToRef="SubProcess_2">
//		      <bpmn2:outgoing>SequenceFlow_26</bpmn2:outgoing>
//		      <bpmn2:timerEventDefinition id="TimerEventDefinition_2"/>
//		    </bpmn2:boundaryEvent>
		
		correct.addSequenceFlow("UserTask_1", "ExclusiveGateway_1");
		correct.addSequenceFlow("ExclusiveGateway_1", "ExclusiveGateway_3");
		correct.addSequenceFlow("ExclusiveGateway_2", "EndEvent_2");
		correct.addSequenceFlow("ExclusiveGateway_3", "EndEvent_3");
		correct.addSequenceFlow("ExclusiveGateway_2", "ExclusiveGateway_3");
		correct.addSequenceFlow("ExclusiveGateway_6", "ExclusiveGateway_2");
		correct.addSequenceFlow("ExclusiveGateway_1", "SubProcess_2");
		correct.addSequenceFlow("BoundaryEvent_3", "ExclusiveGateway_5");
		correct.addSequenceFlow("StartEvent_1", "ExclusiveGateway_5");
		correct.addSequenceFlow("ExclusiveGateway_5", "UserTask_1");
		correct.addSequenceFlow("SubProcess_2", "ExclusiveGateway_6");
		correct.addSequenceFlow("BoundaryEvent_4", "ExclusiveGateway_6");
		
		System.out.print("Test: ");
		System.out.println( correct.equals(diagram) );
	}

}
