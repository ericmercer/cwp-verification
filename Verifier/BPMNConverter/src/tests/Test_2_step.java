package tests;

import bpmnStructure.BpmnProcess;
import xmlConversion.ConvertToBpmn;

public class Test_2_step {
	
	public static void main( String args[] ) {
		
		ConvertToBpmn convert = new ConvertToBpmn();
		BpmnProcess diagram = convert.importXML("tests/2_step.bpmn");
		
		BpmnProcess correct = new BpmnProcess("Process_1");
		correct.addStartEvent("StartEvent_1");
		correct.addEndEvent("EndEvent_1aqgj4v");
		correct.addSequenceFlow("StartEvent_1", "EndEvent_1aqgj4v");
		
		System.out.print("Testing: ");
		System.out.println( correct.equals(diagram) );
		
		return;
	}

}
