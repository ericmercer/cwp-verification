package tests;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bpmnStructure.BpmnDiagram;
import bpmnStructure.BpmnProcess;
import xmlConversion.ConvertToBpmn;

public class ConvertToBpmnTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testConvertToBpmn() {
		fail("not implemented yet");
	}

	@Test
	public void test2_Step() {
		ConvertToBpmn convert = new ConvertToBpmn();
		BpmnDiagram diagram = convert.importXML("../diagrams/2_step.bpmn");
		BpmnDiagram expected = new BpmnDiagram();
		BpmnProcess correct = expected.addProcess("Process_1");
//		correct = convert.importXML("tests/diagrams/2_step.bpmn");
		correct.addStartEvent("StartEvent_1");
		correct.addEndEvent("EndEvent_1aqgj4v");
		correct.addSequenceFlow("StartEvent_1", "EndEvent_1aqgj4v");
		
		System.out.print("Testing: ");
		System.out.println( expected.equals(diagram) );
	}
	
	@Test
	public void test4_Step() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testData_split() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testdataScope() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testjamie() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testMedicalRecords() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testMyName() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testonline_purchase() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testorderFullfillment() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testpurchaseCWP() {
		fail("Not yet implemented");
	}
	
	@Test
	public void teststructure() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testsub_processes() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testvendingMachine() {
		fail("Not yet implemented");
	}

}
