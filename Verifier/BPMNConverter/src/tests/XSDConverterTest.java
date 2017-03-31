package tests;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;

import org.junit.Test;

import bpmnStructure.BpmnDiagram;
import bpmnStructure.dataTypes.PromelaType;
import xmlConversion.XSDConverter;

public class XSDConverterTest {

	@Test
	public void testPurchaseCWP3() {
		XSDConverter converter = new XSDConverter();
		BpmnDiagram diagram = new BpmnDiagram();
		HashMap<String, PromelaType> map = converter.importXSD("diagrams/purchaseCWP3.xsd", diagram);
		System.out.println("**********************************");
		System.out.println("Map:");
		StringBuilder result = new StringBuilder();
		for (String key : map.keySet()) {
			System.out.println( map.get(key).generateDefinitionString(true) );
			result.append( map.get(key).generateDefinitionString(true) + "\n" );
		}
		Scanner scanner = null;
		try {
			scanner  = new Scanner( new BufferedReader( new FileReader( "src/tests/purchaseCWP3_expected.txt" ) ) );
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuilder temp = new StringBuilder();
		while (scanner.hasNextLine()) {
			temp.append(scanner.nextLine() + "\n");
		}
		String expected = temp.toString();
		System.out.println(expected);
		assertTrue(expected.equals(result));
	}

}
