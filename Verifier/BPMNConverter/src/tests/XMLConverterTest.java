package tests;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Test;

import bpmnStructure.BpmnDiagram;
import bpmnStructure.BpmnProcess;
import bpmnStructure.dataTypes.PromelaType;
import bpmnStructure.dataTypes.PromelaTypeDef;
import xmlConversion.XMLConverter;
import xmlConversion.XSDConverter;

public class XMLConverterTest {

	@Test
	public void test2_Step() {
		XMLConverter convert = new XMLConverter();
		BpmnDiagram diagram = convert.importXML("", "diagrams/2_step.bpmn");
		BpmnDiagram expected = new BpmnDiagram();
		BpmnProcess correct = expected.addProcess("Process_1");
//		correct = convert.importXML("tests/diagrams/2_step.bpmn");
		correct.addStartEvent("StartEvent_1");
		correct.addEndEvent("EndEvent_1aqgj4v");
		correct.addSequenceFlow("StartEvent_1", "EndEvent_1aqgj4v");
		
		assertTrue( expected.equals(diagram) );
	}
	
	@Test
	public void test4_Step() {
		XMLConverter convert = new XMLConverter();
		BpmnDiagram diagram = convert.importXML("", "diagrams/4_step.bpmn");
		BpmnDiagram expected = new BpmnDiagram();
		BpmnProcess correct = expected.addProcess("Process_1");
		correct.addStartEvent("StartEvent_1");
		correct.addEndEvent("EndEvent_1aqgj4v");
		correct.addTask("Task_1", null);
		correct.addExclusiveGateway("ExclusiveGateway_1");
		correct.addSequenceFlow("StartEvent_1", "Task_1");
		correct.addSequenceFlow("Task_1", "ExclusiveGateway_1");
		correct.addSequenceFlow("ExclusiveGateway_1", "EndEvent_1aqgj4v");
		
		assertTrue( expected.equals(diagram) );
	}
	
	@Test
	public void test2_processes() {
		XMLConverter converter = new XMLConverter();
		BpmnDiagram result = converter.importXML("", "diagrams/2_processes.bpmn");
		assertTrue(result != null);
		BpmnDiagram expected = new BpmnDiagram();
		XSDConverter xsd = new XSDConverter();
		HashMap<String, PromelaType> vars = xsd.importXSD("diagrams/purchaseCWP2.xsd", expected);
		expected.addProcess("processes_2");
		BpmnProcess proc1 = expected.addProcess("Process_1");
		proc1.addDataObject("shoppingCart", vars.get("shoppingCart"), 1);
		
		proc1.addMessageStartEvent("StartEvent_2", "shoppingCart");
		proc1.addMessageEndEvent("EndEvent_2", "shoppingCart");
		proc1.addSequenceFlow("StartEvent_2", "EndEvent_2");
		
		BpmnProcess proc2 = expected.addProcess("Process_2");
		proc2.addDataObject("shoppingCart", vars.get("shoppingCart"), 1);
		proc2.addEndEvent("EndEvent_1");
		proc2.addMessageCatchEvent("IntermediateCatchEvent_1", "shoppingCart");
		proc2.addMessageThrowEvent("IntermediateThrowEvent_1", "shoppingCart");
		proc2.addStartEvent("StartEvent_1");
		proc2.addSequenceFlow("StartEvent_1", "IntermediateThrowEvent_1");
		proc2.addSequenceFlow("IntermediateCatchEvent_1", "EndEvent_1");
		
		expected.addMessageFlow("MessageFlow_1", proc2, "IntermediateThrowEvent_1", 
				proc1, "StartEvent_2", expected.addTypeDef("shoppingCart"));
		expected.addMessageFlow("MessageFlow_2", proc1, "EndEvent_2", 
				proc2, "IntermediateCatchEvent_1", expected.addTypeDef("shoppingCart"));
		
		assertTrue(expected.equals(result));
	}
	
//	@Test
//	public void testjamie() {
//		XMLConverter convert = new XMLConverter();
//		BpmnDiagram diagram = convert.importXML("diagrams/jamie.bpmn");
//		BpmnDiagram expected = new BpmnDiagram();
//		BpmnProcess correct = expected.addProcess("process_1");
//		correct.addStartEvent("StartEvent_1");
//		correct.addEndEvent("EndEvent_1");
//		
//		correct.addScriptTask("ScriptTask_1", null);
//		correct.addScriptTask("ScriptTask_2", null);
//		
//		correct.addExclusiveGateway("ExclusiveGateway_1");
//		correct.addExclusiveGateway("ExclusiveGateway_2");
//		
//		correct.addSequenceFlow("ScriptTask_1", "ExclusiveGateway_1");
//		correct.addSequenceFlow("ExclusiveGateway_1", "ScriptTask_2");
//		correct.addSequenceFlow("ScriptTask_2", "ExclusiveGateway_2");
//		correct.addSequenceFlow("ExclusiveGateway_1", "ExclusiveGateway_2");
//		correct.addSequenceFlow("StartEvent_1", "ScriptTask_1");
//		
//		BpmnProcess sub = correct.addNormalSubProcess("SubProcess_1");
//		sub.addStartEvent("StartEvent_2");
//		sub.addEndEvent("EndEvent_2");
//		sub.addScriptTask("ScriptTask_4", null);
//		sub.addSequenceFlow("StartEvent_2", "ScriptTask_4");
//		sub.addSequenceFlow("ScriptTask_4", "EndEvent_2");
//		
//		correct.addSequenceFlow("ExclusiveGateway_2", "SubProcess_1");
//		correct.addSequenceFlow("SubProcess_1", "EndEvent_1");
//		
//		assertTrue( expected.equals(diagram) );
//	}
	
//	@Test
//	public void testMedicalRecords() {
//		XMLConverter convert = new XMLConverter();
//		BpmnDiagram diagram = convert.importXML("diagrams/MedicalRecords.bpmn");
//		BpmnDiagram expected = new BpmnDiagram();
//		BpmnProcess correct = expected.addProcess("MedicalRecords");
//		
//		correct.addStartEvent("StartEvent_3");
//		correct.addTask("Task_3", null);
//		correct.addSequenceFlow("StartEvent_3", "Task_3");
//
//		correct.addExclusiveGateway("ExclusiveGateway_3");
//		correct.addExclusiveGateway("ExclusiveGateway_4");
//		correct.addTask("Task_4", null);
//		correct.addTask("Task_5", null);
//		correct.addEndEvent("EndEvent_3");
//		correct.addSequenceFlow("Task_4", "ExclusiveGateway_4");
//		correct.addSequenceFlow("Task_5", "ExclusiveGateway_4");
//		correct.addSequenceFlow("ExclusiveGateway_4", "EndEvent_3");
//		correct.addSequenceFlow("ExclusiveGateway_3", "Task_5");
//		correct.addSequenceFlow("ExclusiveGateway_3", "Task_4");
//		correct.addSequenceFlow("Task_3", "ExclusiveGateway_3");
//		
//		assertTrue( expected.equals(diagram) );
//	}
	
//	@Test
//	public void testMyName() {
//		XMLConverter convert = new XMLConverter();
//		BpmnDiagram diagram = convert.importXML("diagrams/MyName.bpmn");
//		BpmnDiagram expected = new BpmnDiagram();
//		BpmnProcess correct = expected.addProcess("Process_1");
//		correct.addStartEvent("StartEvent_1");
//		correct.addEndEvent("EndEvent_0259rfj");
//		
//		correct.addTask("Task_1vukq14", null);
//		correct.addTask("Task_1c3wjjf", null);
//		
//		correct.addExclusiveGateway("ExclusiveGateway_0hpt3dk");
//		correct.addExclusiveGateway("ExclusiveGateway_1");
//		
//		BpmnProcess sub = correct.addNormalSubProcess("SubProcess_0zaafna");
//		sub.addExclusiveGateway("ExclusiveGateway_1jr1gc4");
//		sub.addTask("Task_0q22whk", null);
//		sub.addSequenceFlow("ExclusiveGateway_1jr1gc4", "Task_0q22whk");
//		sub.addTask("Task_12uwadk", null);
//		sub.addSequenceFlow("ExclusiveGateway_1jr1gc4", "Task_12uwadk");
//		sub.addExclusiveGateway("ExclusiveGateway_1anf5gt");
//		sub.addSequenceFlow("Task_0q22whk", "ExclusiveGateway_1anf5gt");
//		sub.addSequenceFlow("Task_12uwadk", "ExclusiveGateway_1anf5gt");
//		sub.addIntermediateEvent("IntermediateThrowEvent_060vul7");
//		sub.addIntermediateEvent("IntermediateThrowEvent_1bva7ei");
//		sub.addSequenceFlow("ExclusiveGateway_1jr1gc4", "IntermediateThrowEvent_060vul7");
//		sub.addSequenceFlow("IntermediateThrowEvent_060vul7", "IntermediateThrowEvent_1bva7ei");
//		sub.addSequenceFlow("IntermediateThrowEvent_1bva7ei", "ExclusiveGateway_1anf5gt");
//		
//		correct.addSequenceFlow("StartEvent_1", "ExclusiveGateway_0hpt3dk");
//		correct.addSequenceFlow("ExclusiveGateway_0hpt3dk", "SubProcess_0zaafna");
//		correct.addSequenceFlow("SubProcess_0zaafna", "Task_1vukq14");
//		correct.addSequenceFlow("ExclusiveGateway_0hpt3dk", "Task_1c3wjjf");
//		correct.addSequenceFlow("Task_1c3wjjf", "ExclusiveGateway_1");
//		correct.addSequenceFlow("Task_1vukq14", "ExclusiveGateway_1");
//		correct.addSequenceFlow("ExclusiveGateway_1", "EndEvent_0259rfj");
//		
//		assertTrue( expected.equals(diagram) );
//	}
	
//	@Test
//	public void testonline_purchase() {
//		XMLConverter convert = new XMLConverter();
//		BpmnDiagram diagram = convert.importXML("diagrams/online_purchase.bpmn");
//		BpmnDiagram expected = new BpmnDiagram();
////		expected.addDataStore("DataStore_2", "CWPArray", 5);
//		
//		BpmnProcess first = expected.addProcess("Process_1");
//		
//		first.addStartEvent("StartEvent_1");
//		first.addTask("UserTask_1", "shoppingCart.msg = order\n"
//				+ "select(shoppingCart.item : 0 .. MAX_ITEMS)\n"
//				+ "select(shpppingCart.buyer : 0 .. MAX_BUYERS)\n"
//				+ "select(shoppingCart.cost : 0 .. MAX_COST)");
//		
//		first.addSequenceFlow("StartEvent_1", "UserTask_1");
//		first.addMessageThrowEvent("IntermediateThrowEvent_1");
//		first.addSequenceFlow("UserTask_1", "IntermediateThrowEvent_1");
//		first.addMessageCatchEvent("IntermediateCatchEvent_2");
//		first.addSequenceFlow("IntermediateThrowEvent_1", "IntermediateCatchEvent_2");
//		first.addEndEvent("EndEvent_7");
////		first.addDataObject("DataObject_2", "shoppingCart", 1);
//		
//		BpmnProcess sub = first.addNormalSubProcess("SubProcess_1");
//		sub.addStartEvent("StartEvent_3");
//		sub.addParallelGateway("ParallelGateway_1");
//		sub.addParallelGateway("ParallelGateway_2");
//		sub.addEndEvent("EndEvent_1");
//		sub.addTask("Task_1");
//		sub.addTask("Task_2");
//		sub.addSequenceFlow("ParallelGateway_1", "Task_1");
//		sub.addSequenceFlow("ParallelGateway_1", "Task_2");
//		sub.addSequenceFlow("Task_1", "ParallelGateway_2");
//		sub.addSequenceFlow("Task_2", "ParallelGateway_2");
//		sub.addSequenceFlow("StartEvent_3", "ParallelGateway_1");
//		sub.addSequenceFlow("ParallelGateway_2", "EndEvent_1");
//		
//		first.addSequenceFlow("IntermediateCatchEvent_2", "SubProcess_1");
//		first.addSequenceFlow("SubProcess_1", "EndEvent_7");
//		
//		BpmnProcess sec = expected.addProcess("Process_2");
//		
//		sec.addExclusiveGateway("ExclusiveGateway_1");
//		sec.addExclusiveGateway("ExclusiveGateway_2");
//		sec.addScriptTask("ScriptTask_1", "orderStatus.msg = outOfStock");
//		sec.addSequenceFlow("ExclusiveGateway_1", "ScriptTask_1");
//		sec.addSequenceFlow("ExclusiveGateway_1", "ExclusiveGateway_2");
//		sec.addTask("UserTask_4", "cwpArray[cwpArrayIndex].paymentOwner = cwpArray[cwpArrayIndex].seller");
//		sec.addScriptTask("ScriptTask_3", "orderStatus.msg = cardDenied");
//		sec.addSequenceFlow("ExclusiveGateway_2", "ScriptTask_3");
//		sec.addTask("UserTask_5", "cwpArray[cwpArrayIndex].itemOwner = cwpArray[cwpArrayIndex].buyer");
//		sec.addSequenceFlow("UserTask_4", "UserTask_5");
//		sec.addMessageStartEvent("StartEvent_2");
//		
//		sec.addSequenceFlow("StartEvent_2", "ExclusiveGateway_1");
//		sec.addSequenceFlow("ExclusiveGateway_2", "UserTask_4");
////		sec.addDataObject("DataObject_6", "orderStatus", 1);
//		sec.addExclusiveGateway("ExclusiveGateway_5");
//		sec.addSequenceFlow("UserTask_5", "ExclusiveGateway_5");
//		sec.addSequenceFlow("ScriptTask_3", "ExclusiveGateway_5");
//		sec.addExclusiveGateway("ExclusiveGateway_6");
//		sec.addSequenceFlow("ScriptTask_1", "ExclusiveGateway_6");
//		sec.addSequenceFlow("ExclusiveGateway_5", "ExclusiveGateway_6");
//		sec.addMessageEndEvent("EndEvent_8");
//		sec.addSequenceFlow("ExclusiveGateway_6", "EndEvent_8");
//		
//		expected.addProcess("Process_3");
//		
//		expected.addMessageFlow("MessageFlow_8", first, "IntermediateThrowEvent_1", 
//				sec, "StartEvent_2", expected.addTypeDef("ns1.xsd:msgType"));
//		expected.addMessageFlow("MessageFlow_8", sec, "EndEvent_8", 
//				first, "IntermediateCatchEvent_2", expected.addTypeDef("ns1.xsd:msgType"));
//		
//		assertTrue( expected.equals(diagram) );
//	}
	
	@Test
	public void testonline_purchase2() {
		XMLConverter convert = new XMLConverter();
		BpmnDiagram diagram = convert.importXML("", "diagrams/online_purchase2.bpmn");
		BpmnDiagram expected = new BpmnDiagram();
		XSDConverter xsd = new XSDConverter();
		HashMap<String, PromelaType> types = xsd.importXSD("diagrams/purchaseCWP2.xsd", expected);
		HashMap<String, PromelaType> vars = xsd.getVariables();
		expected.addDataStore("CWPArray", vars.get("CWPArray"), 5);
		
		BpmnProcess first = expected.addProcess("Process_1");
		first.addDataObject("shoppingCart", types.get(vars.get("shoppingCart")), 1);
		first.addStartEvent("StartEvent_1");
		first.addScriptTask("UserTask_1", "shoppingCart.msg = order\n"
				+ "select(shoppingCart.item : 0 .. MAX_ITEMS)\n"
				+ "select(shpppingCart.buyer : 0 .. MAX_BUYERS)\n"
				+ "select(shoppingCart.cost : 0 .. MAX_COST)");
		
		first.addSequenceFlow("StartEvent_1", "UserTask_1");
		first.addMessageThrowEvent("IntermediateThrowEvent_1", "shoppingCart");
		first.addSequenceFlow("UserTask_1", "IntermediateThrowEvent_1");
		first.addMessageCatchEvent("IntermediateCatchEvent_2", "shoppingCart");
		first.addSequenceFlow("IntermediateThrowEvent_1", "IntermediateCatchEvent_2");
		first.addEndEvent("EndEvent_7");
		
		BpmnProcess sub = first.addNormalSubProcess("SubProcess_1");
		sub.addStartEvent("StartEvent_3");
		sub.addParallelGateway("ParallelGateway_1");
		sub.addParallelGateway("ParallelGateway_2");
		sub.addEndEvent("EndEvent_1");
		sub.addTask("Task_1");
		sub.addTask("Task_2");
		sub.addSequenceFlow("ParallelGateway_1", "Task_1");
		sub.addSequenceFlow("ParallelGateway_1", "Task_2");
		sub.addSequenceFlow("Task_1", "ParallelGateway_2");
		sub.addSequenceFlow("Task_2", "ParallelGateway_2");
		sub.addSequenceFlow("StartEvent_3", "ParallelGateway_1");
		sub.addSequenceFlow("ParallelGateway_2", "EndEvent_1");
		
		first.addSequenceFlow("IntermediateCatchEvent_2", "SubProcess_1");
		first.addSequenceFlow("SubProcess_1", "EndEvent_7");
		
		BpmnProcess sec = expected.addProcess("Process_2");
		sec.addDataObject("orderStatus", types.get(vars.get("orderStatus")), 1);
		
		sec.addExclusiveGateway("ExclusiveGateway_1");
		sec.addExclusiveGateway("ExclusiveGateway_2");
		sec.addScriptTask("ScriptTask_1", "orderStatus.msg = outOfStock");
		sec.addSequenceFlow("ExclusiveGateway_1", "ScriptTask_1");
		sec.addSequenceFlow("ExclusiveGateway_1", "ExclusiveGateway_2");
		sec.addScriptTask("UserTask_4", "cwpArray[cwpArrayIndex].paymentOwner = cwpArray[cwpArrayIndex].seller");
		sec.addScriptTask("ScriptTask_3", "orderStatus.msg = cardDenied");
		sec.addSequenceFlow("ExclusiveGateway_2", "ScriptTask_3");
		sec.addScriptTask("UserTask_5", "cwpArray[cwpArrayIndex].itemOwner = cwpArray[cwpArrayIndex].buyer");
		sec.addSequenceFlow("UserTask_4", "UserTask_5");
		sec.addMessageStartEvent("StartEvent_2", "orderStatus");
		
		sec.addSequenceFlow("StartEvent_2", "ExclusiveGateway_1");
		sec.addSequenceFlow("ExclusiveGateway_2", "UserTask_4");
		
		sec.addExclusiveGateway("ExclusiveGateway_5");
		sec.addSequenceFlow("UserTask_5", "ExclusiveGateway_5");
		sec.addSequenceFlow("ScriptTask_3", "ExclusiveGateway_5");
		sec.addExclusiveGateway("ExclusiveGateway_6");
		sec.addSequenceFlow("ScriptTask_1", "ExclusiveGateway_6");
		sec.addSequenceFlow("ExclusiveGateway_5", "ExclusiveGateway_6");
		sec.addMessageEndEvent("EndEvent_8", "orderStatus");
		sec.addSequenceFlow("ExclusiveGateway_6", "EndEvent_8");
		
		expected.addProcess("Process_3");
		
		expected.addMessageFlow("MessageFlow_8", first, "IntermediateThrowEvent_1", 
				sec, "StartEvent_2", (PromelaTypeDef) vars.get("order"));
		expected.addMessageFlow("MessageFlow_9", sec, "EndEvent_8", 
				first, "IntermediateCatchEvent_2", (PromelaTypeDef) vars.get("order"));
		
		assertTrue( expected.equals(diagram) );
	}
	
//	@Test
//	public void testorderFullfillment() {
//		XMLConverter convert = new XMLConverter();
//		BpmnDiagram diagram = convert.importXML("diagrams/orderFulfillment.bpmn");
//		BpmnDiagram expected = new BpmnDiagram();
//		BpmnProcess correct = expected.addProcess("orderFulfillment");
//		correct.addStartEvent("StartEvent_1");
//		correct.addEndEvent("EndEvent_2");
//		correct.addEndEvent("EndEvent_3");
//		
//		correct.addTask("UserTask_1", null);
//		
//		correct.addExclusiveGateway("ExclusiveGateway_1");
//		correct.addExclusiveGateway("ExclusiveGateway_2");
//		correct.addExclusiveGateway("ExclusiveGateway_3");
//		correct.addExclusiveGateway("ExclusiveGateway_5");
//		correct.addExclusiveGateway("ExclusiveGateway_6");
//		
//		BpmnProcess sub = correct.addNormalSubProcess("SubProcess_2");
//		sub.addMessageStartEvent("StartEvent_2");
//		sub.addEndEvent("EndEvent_4");
//		sub.addEndEvent("EndEvent_5");
//		sub.addTask("UserTask_2", null);
//		sub.addExclusiveGateway("ExclusiveGateway_4");
//		sub.addSequenceFlow("ExclusiveGateway_4", "EndEvent_4");
//		sub.addSequenceFlow("ExclusiveGateway_4", "EndEvent_5");
//		sub.addSequenceFlow("UserTask_2", "ExclusiveGateway_4");
//		sub.addSequenceFlow("StartEvent_2", "UserTask_2");
//		
//		
//		correct.addSequenceFlow("UserTask_1", "ExclusiveGateway_1");
//		correct.addSequenceFlow("ExclusiveGateway_1", "ExclusiveGateway_3");
//		correct.addSequenceFlow("ExclusiveGateway_2", "EndEvent_2");
//		correct.addSequenceFlow("ExclusiveGateway_3", "EndEvent_3");
//		correct.addSequenceFlow("ExclusiveGateway_2", "ExclusiveGateway_3");
//		correct.addSequenceFlow("ExclusiveGateway_6", "ExclusiveGateway_2");
//		correct.addSequenceFlow("ExclusiveGateway_1", "SubProcess_2");
//		correct.addSequenceFlow("StartEvent_1", "ExclusiveGateway_5");
//		correct.addSequenceFlow("ExclusiveGateway_5", "UserTask_1");
//		correct.addSequenceFlow("SubProcess_2", "ExclusiveGateway_6");
//		
//		assertTrue( expected.equals(diagram) );
//	}
	
//	@Test
//	public void testsub_processes() {
//		XMLConverter convert = new XMLConverter();
//		BpmnDiagram diagram = convert.importXML("diagrams/sub_processes.bpmn");
//		BpmnDiagram expected = new BpmnDiagram();
//		BpmnProcess correct = expected.addProcess("sub_processes");
//		
//		correct.addStartEvent("StartEvent_1");
//		correct.addEndEvent("EndEvent_1");
//		correct.addExclusiveGateway("ExclusiveGateway_1");
//		correct.addSequenceFlow("StartEvent_1", "ExclusiveGateway_1");
//		
//		correct.addExclusiveGateway("ExclusiveGateway_2");
//		correct.addSequenceFlow("ExclusiveGateway_2", "EndEvent_1");
//		
//		BpmnProcess sub1 = correct.addNormalSubProcess("SubProcess_1");
//		sub1.addTask("SendTask_1");
//		sub1.addStartEvent("StartEvent_2");
//		sub1.addSequenceFlow("StartEvent_2", "SendTask_1");
//		sub1.addEndEvent("EndEvent_4");
//		
//		BpmnProcess sub2 = sub1.addNormalSubProcess("SubProcess_2");
//		sub2.addTask("Task_1");
//		sub2.addStartEvent("StartEvent_3");
//		sub2.addEndEvent("EndEvent_2");
//		sub2.addSequenceFlow("StartEvent_3", "Task_1");
//		sub2.addSequenceFlow("Task_1", "EndEvent_2");
//		
//		sub1.addSequenceFlow("SendTask_1", "SubProcess_2");
//		sub1.addSequenceFlow("SubProcess_2", "EndEvent_4");
//		
//		BpmnProcess sub3 = correct.addNormalSubProcess("AdHocSubProcess_1");
//		sub3.addTask("SendTask_2");
//		sub3.addTask("ReceiveTask_1");
//		sub3.addSequenceFlow("SendTask_2", "ReceiveTask_1");
//		sub3.addTask("ManualTask_1", null);
//		sub3.addSequenceFlow("ReceiveTask_1", "ManualTask_1");
//		
//		correct.addSequenceFlow("ExclusiveGateway_1", "SubProcess_1");
//		correct.addSequenceFlow("ExclusiveGateway_1", "AdHocSubProcess_1");
//		correct.addSequenceFlow("SubProcess_1", "ExclusiveGateway_2");
//		correct.addSequenceFlow("AdHocSubProcess_1", "ExclusiveGateway_2");
//		
//		assertTrue( expected.equals(diagram) );
//	}
	
	@Test
	public void testvendingMachine() {
		XMLConverter convert = new XMLConverter();
		BpmnDiagram diagram = convert.importXML("", "diagrams/vendingMachine.bpmn");
		BpmnDiagram expected = new BpmnDiagram();
		XSDConverter xsd = new XSDConverter();
		HashMap<String, PromelaType> types = xsd.importXSD("diagrams/vendingMachineData.xsd", expected);
		System.out.println(types);
		HashMap<String, PromelaType> vars = xsd.getVariables();
		System.out.println(vars);
		expected.addDataStore("DataStore_2", vars.get("ItemValuesArray"), 0);
		BpmnProcess correct = expected.addProcess("vendingMachine");
		
		correct.addStartEvent("StartEvent_1");
		correct.addEndEvent("EndEvent_1");
		correct.addDataObject("DataObject_2", vars.get("Money"), 0);
		correct.addTask("Task_1", "Count money"); // has promela code inside
		correct.addSequenceFlow("StartEvent_1", "Task_1");
		
		correct.addTask("UserTask_1", "Input what you want to buy");
		correct.addExclusiveGateway("ExclusiveGateway_1");
		correct.addScriptTask("ScriptTask_1", "Script Task 1", "Money = Money - ItemValuesArray[ItemValuesArrayIndex]");
		correct.addSequenceFlow("ExclusiveGateway_1", "ScriptTask_1");
		correct.addSequenceFlow("UserTask_1", "ExclusiveGateway_1");
		
		correct.addTask("SendTask_2", "Tell user there is not enough money for that");
		correct.addSequenceFlow("ExclusiveGateway_1", "SendTask_2");
		
		correct.addExclusiveGateway("ExclusiveGateway_2");
		correct.addSequenceFlow("Task_1", "ExclusiveGateway_2");
		correct.addSequenceFlow("ExclusiveGateway_2", "UserTask_1");
		correct.addSequenceFlow("SendTask_2", "ExclusiveGateway_2");
		
		correct.addTask("Task_2", "Give item");
		correct.addScriptTask("Task_3", "userMoney = Money - ItemValuesArray[ItemValuesArrayIndex]", "Return change"); // userMoney = Money - ItemValuesArray[ItemValuesArrayIndex]
		
		correct.addSequenceFlow("Task_3", "EndEvent_1");
		correct.addSequenceFlow("ScriptTask_1", "Task_2");
		correct.addSequenceFlow("Task_2", "Task_3");
		
		correct.addDataObject("DataObject_3", vars.get("BackUpData"), 0);
		
		assertTrue( expected.equals(diagram) );
	}

}
