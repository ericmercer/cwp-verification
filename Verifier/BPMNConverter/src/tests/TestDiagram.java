package tests;

import java.util.Scanner;

import bpmnStructure.BpmnDiagram;
import xmlConversion.ConvertToBpmn;

public class TestDiagram {
	
	private static String[] tests = {
			"Test_2_step", "Test_4_Step", "sub_process_test1", "MyName", "test_Order_fulfillment", "jamie", 
			"vendingMachine", "MedicalRecords", "sub_process",
	};
	
	public static void main( String args[] ) {
		
//		int selection = Integer.parseInt(args[0]);
		TestDiagram tester = new TestDiagram();
		Scanner input = new Scanner(System.in);
		
		System.out.println("Here are your options:");
		for(int i = 0; i < tests.length; i++) {
			System.out.println(i + ": " + tests[i]);
		}
		System.out.print("Choose: ");
		int test = Integer.parseInt(input.next());
		switch(test) {
		case 0: 
			tester.test_2_step();
			break;
		case 1:
			tester.test_4_step();
			break;
		case 2:
			tester.test_sub_process_test1();
			break;
		case 3:
			tester.test_MyName();
			break;
		case 4:
			tester.test_Order_fulfillment();
			break;
		case 5:
			tester.test_jamie();
			break;
		case 6:
			tester.test_vendingMachine();
			break;
		case 7:
			tester.test_MedicalRecords();
			break;
		case 8:
			tester.test_sub_processes();
			break;
		}
//		tester.test_2_step();
		return;
	}
	
	public void test_2_step() {
		ConvertToBpmn convert = new ConvertToBpmn();
		BpmnDiagram diagram = convert.importXML("tests/diagrams/2_step.bpmn");
		
		BpmnDiagram correct;
		correct = new BpmnDiagram("Process_1");
//		correct = convert.importXML("tests/diagrams/2_step.bpmn");
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
		correct.addTask("Task_1", null);
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
		
		correct.addTask("Task_1", null);
		
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
	
	public void test_MyName() {
		ConvertToBpmn convert = new ConvertToBpmn();
		BpmnDiagram diagram = convert.importXML("tests/diagrams/MyName.bpmn");
		
		BpmnDiagram correct = new BpmnDiagram("Process_1");
		correct.addStartEvent("StartEvent_1");
		correct.addEndEvent("EndEvent_0259rfj");
		
		correct.addTask("Task_1vukq14", null);
		correct.addTask("Task_1c3wjjf", null);
		
		correct.addExclusiveGateway("ExclusiveGateway_0hpt3dk");
		correct.addExclusiveGateway("ExclusiveGateway_1");
		
		BpmnDiagram sub = correct.addNormalSubProcess("SubProcess_0zaafna");
		sub.addExclusiveGateway("ExclusiveGateway_1jr1gc4");
		sub.addTask("Task_0q22whk", null);
		sub.addSequenceFlow("ExclusiveGateway_1jr1gc4", "Task_0q22whk");
		sub.addTask("Task_12uwadk", null);
		sub.addSequenceFlow("ExclusiveGateway_1jr1gc4", "Task_12uwadk");
		sub.addExclusiveGateway("ExclusiveGateway_1anf5gt");
		sub.addSequenceFlow("Task_0q22whk", "ExclusiveGateway_1anf5gt");
		sub.addSequenceFlow("Task_12uwadk", "ExclusiveGateway_1anf5gt");
		sub.addIntermediateEvent("IntermediateThrowEvent_060vul7", null);
		sub.addIntermediateEvent("IntermediateThrowEvent_1bva7ei", null);
		sub.addSequenceFlow("ExclusiveGateway_1jr1gc4", "IntermediateThrowEvent_060vul7");
		sub.addSequenceFlow("IntermediateThrowEvent_060vul7", "IntermediateThrowEvent_1bva7ei");
		sub.addSequenceFlow("IntermediateThrowEvent_1bva7ei", "ExclusiveGateway_1anf5gt");
		
		correct.addSequenceFlow("StartEvent_1", "ExclusiveGateway_0hpt3dk");
		correct.addSequenceFlow("ExclusiveGateway_0hpt3dk", "SubProcess_0zaafna");
		correct.addSequenceFlow("SubProcess_0zaafna", "Task_1vukq14");
		correct.addSequenceFlow("ExclusiveGateway_0hpt3dk", "Task_1c3wjjf");
		correct.addSequenceFlow("Task_1c3wjjf", "ExclusiveGateway_1");
		correct.addSequenceFlow("Task_1vukq14", "ExclusiveGateway_1");
		correct.addSequenceFlow("ExclusiveGateway_1", "EndEvent_0259rfj");
		
		System.out.print("Test: ");
		System.out.println( correct.equals(diagram) );
	}
	
	public void test_Order_fulfillment() {
		ConvertToBpmn convert = new ConvertToBpmn();
		BpmnDiagram diagram = convert.importXML("tests/diagrams/orderFulfillment.bpmn");
		
		BpmnDiagram correct = new BpmnDiagram("orderFulfillment");
		correct.addStartEvent("StartEvent_1");
		correct.addEndEvent("EndEvent_2");
		correct.addEndEvent("EndEvent_3");
		
		correct.addTask("UserTask_1", null);
		
		correct.addExclusiveGateway("ExclusiveGateway_1");
		correct.addExclusiveGateway("ExclusiveGateway_2");
		correct.addExclusiveGateway("ExclusiveGateway_3");
		correct.addExclusiveGateway("ExclusiveGateway_5");
		correct.addExclusiveGateway("ExclusiveGateway_6");
		
		BpmnDiagram sub = correct.addNormalSubProcess("SubProcess_2");
		sub.addStartEvent("StartEvent_2");
		sub.addEndEvent("EndEvent_4");
		sub.addEndEvent("EndEvent_5");
		sub.addTask("UserTask_2", null);
		sub.addExclusiveGateway("ExclusiveGateway_4");
		sub.addSequenceFlow("ExclusiveGateway_4", "EndEvent_4");
		sub.addSequenceFlow("ExclusiveGateway_4", "EndEvent_5");
		sub.addSequenceFlow("UserTask_2", "ExclusiveGateway_4");
		sub.addSequenceFlow("StartEvent_2", "UserTask_2");
		
		
		correct.addSequenceFlow("UserTask_1", "ExclusiveGateway_1");
		correct.addSequenceFlow("ExclusiveGateway_1", "ExclusiveGateway_3");
		correct.addSequenceFlow("ExclusiveGateway_2", "EndEvent_2");
		correct.addSequenceFlow("ExclusiveGateway_3", "EndEvent_3");
		correct.addSequenceFlow("ExclusiveGateway_2", "ExclusiveGateway_3");
		correct.addSequenceFlow("ExclusiveGateway_6", "ExclusiveGateway_2");
		correct.addSequenceFlow("ExclusiveGateway_1", "SubProcess_2");
		correct.addSequenceFlow("StartEvent_1", "ExclusiveGateway_5");
		correct.addSequenceFlow("ExclusiveGateway_5", "UserTask_1");
		correct.addSequenceFlow("SubProcess_2", "ExclusiveGateway_6");
		
		System.out.print("Test: ");
		System.out.println( correct.equals(diagram) );
	}
	
	public void test_jamie() {
		ConvertToBpmn convert = new ConvertToBpmn();
		BpmnDiagram diagram = convert.importXML("tests/diagrams/jamie.bpmn");
		
		BpmnDiagram correct = new BpmnDiagram("process_1");
		correct.addStartEvent("StartEvent_1");
		correct.addEndEvent("EndEvent_1");
		
		correct.addScriptTask("ScriptTask_1", null);
		correct.addScriptTask("ScriptTask_2", null);
		
		correct.addExclusiveGateway("ExclusiveGateway_1");
		correct.addExclusiveGateway("ExclusiveGateway_2");
		
		correct.addSequenceFlow("ScriptTask_1", "ExclusiveGateway_1");
		correct.addSequenceFlow("ExclusiveGateway_1", "ScriptTask_2");
		correct.addSequenceFlow("ScriptTask_2", "ExclusiveGateway_2");
		correct.addSequenceFlow("ExclusiveGateway_1", "ExclusiveGateway_2");
		correct.addSequenceFlow("StartEvent_1", "ScriptTask_1");
		
		BpmnDiagram sub = correct.addNormalSubProcess("SubProcess_1");
		sub.addStartEvent("StartEvent_2");
		sub.addEndEvent("EndEvent_2");
		sub.addScriptTask("ScriptTask_4", null);
		sub.addSequenceFlow("StartEvent_2", "ScriptTask_4");
		sub.addSequenceFlow("ScriptTask_4", "EndEvent_2");
		
		correct.addSequenceFlow("ExclusiveGateway_2", "SubProcess_1");
		correct.addSequenceFlow("SubProcess_1", "EndEvent_1");
		
		System.out.print("Test: ");
		System.out.println( correct.equals(diagram) );
	}
	
	public void test_vendingMachine() {
		ConvertToBpmn convert = new ConvertToBpmn();
		BpmnDiagram diagram = convert.importXML("tests/diagrams/vendingMachine.bpmn");
		
		BpmnDiagram correct = new BpmnDiagram("vendingMachine");
		
		correct.addStartEvent("StartEvent_1");
		correct.addEndEvent("EndEvent_1");
		
		correct.addTask("Task_1", null);
		correct.addSequenceFlow("StartEvent_1", "Task_1");
		
		correct.addTask("UserTask_1", null);
		correct.addExclusiveGateway("ExclusiveGateway_1");
		correct.addScriptTask("ScriptTask_1", null);
		correct.addSequenceFlow("ExclusiveGateway_1", "ScriptTask_1");
		correct.addSequenceFlow("UserTask_1", "ExclusiveGateway_1");
		
		correct.addTask("SendTask_2", null);
		correct.addSequenceFlow("ExclusiveGateway_1", "SendTask_2");
		
		correct.addExclusiveGateway("ExclusiveGateway_2");
		correct.addSequenceFlow("Task_1", "ExclusiveGateway_2");
		correct.addSequenceFlow("ExclusiveGateway_2", "UserTask_1");
		correct.addSequenceFlow("SendTask_2", "ExclusiveGateway_2");
		
		correct.addTask("Task_2", null);
		correct.addTask("Task_3", null);
		
		correct.addSequenceFlow("Task_3", "EndEvent_1");
		correct.addSequenceFlow("ScriptTask_1", "Task_2");
		correct.addSequenceFlow("Task_2", "Task_3");
		
		System.out.print("Test: ");
		System.out.println( correct.equals(diagram) );
	}
	
	public void test_MedicalRecords() {
		
		ConvertToBpmn convert = new ConvertToBpmn();
		BpmnDiagram diagram = convert.importXML("tests/diagrams/MedicalRecords.bpmn");
		
		BpmnDiagram correct = new BpmnDiagram("MedicalRecords");
		
		correct.addStartEvent("StartEvent_3");
		correct.addTask("Task_3", null);
		correct.addSequenceFlow("StartEvent_3", "Task_3");

		correct.addExclusiveGateway("ExclusiveGateway_3");
		correct.addExclusiveGateway("ExclusiveGateway_4");
		correct.addTask("Task_4", null);
		correct.addTask("Task_5", null);
		correct.addEndEvent("EndEvent_3");
		correct.addSequenceFlow("Task_4", "ExclusiveGateway_4");
		correct.addSequenceFlow("Task_5", "ExclusiveGateway_4");
		correct.addSequenceFlow("ExclusiveGateway_4", "EndEvent_3");
		correct.addSequenceFlow("ExclusiveGateway_3", "Task_5");
		correct.addSequenceFlow("ExclusiveGateway_3", "Task_4");
		correct.addSequenceFlow("Task_3", "ExclusiveGateway_3");
		
		System.out.print("Test: ");
		System.out.println( correct.equals(diagram) );
	}
	
	public void test_sub_processes() {
		
		ConvertToBpmn convert = new ConvertToBpmn();
		BpmnDiagram diagram = convert.importXML("tests/diagrams/sub_processes.bpmn");
		
		BpmnDiagram correct = new BpmnDiagram("sub_processes");
		
		correct.addStartEvent("StartEvent_1");
		correct.addEndEvent("EndEvent_1");
		correct.addExclusiveGateway("ExclusiveGateway_1");
		correct.addSequenceFlow("StartEvent_1", "ExclusiveGateway_1");
		
		correct.addExclusiveGateway("ExclusiveGateway_2");
		correct.addSequenceFlow("ExclusiveGateway_2", "EndEvent_1");
		
		BpmnDiagram sub1 = correct.addNormalSubProcess("SubProcess_1");
		sub1.addTask("SendTask_1", null);
		sub1.addStartEvent("StartEvent_2");
		sub1.addSequenceFlow("StartEvent_2", "SendTask_1");
		sub1.addEndEvent("EndEvent_4");
		
		BpmnDiagram sub2 = sub1.addNormalSubProcess("SubProcess_2");
		sub2.addTask("Task_1", null);
		sub2.addStartEvent("StartEvent_3");
		sub2.addEndEvent("EndEvent_2");
		sub2.addSequenceFlow("StartEvent_3", "Task_1");
		sub2.addSequenceFlow("Task_1", "EndEvent_2");
		
		sub1.addSequenceFlow("SendTask_1", "SubProcess_2");
		sub1.addSequenceFlow("SubProcess_2", "EndEvent_4");
		
		BpmnDiagram sub3 = correct.addNormalSubProcess("AdHocSubProcess_1");
		sub3.addTask("SendTask_2", null);
		sub3.addTask("ReceiveTask_1", null);
		sub3.addSequenceFlow("SendTask_2", "ReceiveTask_1");
		sub3.addTask("ManualTask_1", null);
		sub3.addSequenceFlow("ReceiveTask_1", "ManualTask_1");
		
		correct.addSequenceFlow("ExclusiveGateway_1", "SubProcess_1");
		correct.addSequenceFlow("ExclusiveGateway_1", "AdHocSubProcess_1");
		correct.addSequenceFlow("SubProcess_1", "ExclusiveGateway_2");
		correct.addSequenceFlow("AdHocSubProcess_1", "ExclusiveGateway_2");
		
		System.out.print("Test: ");
		System.out.println( correct.equals(diagram) );
	}

}














