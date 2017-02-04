package tests;

import bpmnStructure.BpmnDiagram;
import xmlConversion.ConvertToBpmn;

public class Test_2_step {
	
	public static void main( String args[] ) {
		
		ConvertToBpmn convert = new ConvertToBpmn();
		BpmnDiagram diagram = convert.importXML(args[0]);
		
		BpmnDiagram correct = new BpmnDiagram("Process_1");
		correct.addStartEvent("StartEvent_1");
		correct.addEndEvent("EndEvent_1aqgj4v");
		correct.addSequenceFlow("StartEvent_1", "EndEvent_1aqgj4v");
		
		System.out.println( correct.equals(diagram) );
		
		
	}

}
