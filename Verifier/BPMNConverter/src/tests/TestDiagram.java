package tests;

import bpmnStructure.BpmnDiagram;
import xmlConversion.ConvertToBpmn;

public class TestDiagram {
	
	private static String[] tests = {
			"Test_2_step", "Test_4_Step", "Test_book_example10"
	};
	
	public static void main( String args[] ) {
		
		ConvertToBpmn convert = new ConvertToBpmn();
		BpmnDiagram diagram = convert.importXML(args[0]);
		
		diagram.addStartEvent("");
		diagram.addEndEvent("");
		diagram.addSequenceFlow("", "");
	}
	
	

}
