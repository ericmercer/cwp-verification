package tests;

import java.util.HashMap;

import org.junit.Test;

import bpmnStructure.BpmnDiagram;
import bpmnStructure.dataTypes.PromelaType;
import xmlConversion.XSDConverter;

public class XSDConverterTest {

	@Test
	public void testImportXSD() {
		XSDConverter converter = new XSDConverter();
		BpmnDiagram diagram = new BpmnDiagram();
		HashMap<String, PromelaType> map = converter.importXSD("diagrams/purchaseCWP.xsd", diagram);
		System.out.println("**********************************");
		System.out.println( map );
	}

}
