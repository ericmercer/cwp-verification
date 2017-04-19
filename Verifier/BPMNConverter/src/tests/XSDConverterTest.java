package tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;

import org.junit.Test;

import bpmnStructure.BpmnDiagram;
import bpmnStructure.dataTypes.BoolType;
import bpmnStructure.dataTypes.MtypeType;
import bpmnStructure.dataTypes.PositiveIntType;
import bpmnStructure.dataTypes.PromelaType;
import bpmnStructure.dataTypes.TypeDeclaration;
import xmlConversion.XSDConverter;

public class XSDConverterTest {
	
	@Test
	public void testStructure() {
		XSDConverter converter = new XSDConverter();
		BpmnDiagram diagram = new BpmnDiagram();
		HashMap<String, PromelaType> map = converter.importXSD("diagrams/structure.xsd", diagram);
//		System.out.println(map);
		assertTrue(map == null);
		map = converter.importXSD("diagrams/testStructure2.xsd", diagram);
		diagram = new BpmnDiagram();
		assertTrue(map == null);
		map = converter.importXSD("diagrams/testStructure3.xsd", diagram);
		diagram = new BpmnDiagram();
		assertTrue(map == null);
//		diagram = new BpmnDiagram();
//		map = converter.importXSD("diagrams/structure.xsd", diagram);
//		assertTrue(map == null);
	}
	
	@Test
	public void testTypeDeclarationEquality() {
		String[] strings = new String[] {
				"hello", "message", "world"
		};
		PromelaType myBool = new BoolType();
		PromelaType myInt = new PositiveIntType();
		PromelaType myMes = new MtypeType(strings);
		
		
		TypeDeclaration dec1 = new TypeDeclaration("thisIsMyMessage", myMes, 0);
		TypeDeclaration dec2 = new TypeDeclaration("thisIsAnInteger", myInt, 1);
		TypeDeclaration dec3 = new TypeDeclaration("booleanThisIs", myBool, 2);
		
		TypeDeclaration compare1 = new TypeDeclaration("thisIsMyMessage", myMes, 0);
		TypeDeclaration compare2 = new TypeDeclaration("thisIsAnInteger", myInt, 1);
		TypeDeclaration compare3 = new TypeDeclaration("booleanThisIs", myBool, 2);
		
		assertTrue(dec1.equals(dec1));
		assertTrue(dec1.equals(compare1));
		assertFalse(dec1.equals(dec2));
		assertFalse(dec1.equals(dec3));
		
		assertTrue(dec2.equals(dec2));
		assertTrue(dec2.equals(compare2));
		assertFalse(dec2.equals(dec1));
		assertFalse(dec2.equals(dec3));
		
		assertTrue(dec3.equals(dec3));
		assertTrue(dec3.equals(compare3));
		assertFalse(dec3.equals(dec1));
		assertFalse(dec3.equals(dec2));
	}
	
	@Test
	public void testVendingMachineData() {
		XSDConverter converter = new XSDConverter();
		BpmnDiagram diagram = new BpmnDiagram();
		HashMap<String, PromelaType> map = converter.importXSD("diagrams/vendingMachineData.xsd", diagram);
//		System.out.println("**********************************");
//		System.out.println("Map:");
		StringBuilder result = new StringBuilder();
		for (String key : map.keySet()) {
//			System.out.println( map.get(key).generateDefinitionString(true) );
			result.append( map.get(key).generateDefinitionString(true) + "\n" );
		}

		Scanner scanner = null;
		try {
			scanner  = new Scanner( new BufferedReader( new FileReader( "src/tests/vendingMachineData_expected.txt" ) ) );
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuilder temp = new StringBuilder();
		String line = null;
		while (scanner.hasNextLine()) {
			line = scanner.nextLine();
			temp.append(line + "\n");
		}
		String[] r = result.toString().split("\n");
		String[] e = temp.toString().split("\n");
		System.out.println(result.toString());
		System.out.println(temp.toString());
		assertEquals(r.length, e.length);
		for (int i = 0; i < r.length; i++) {
			if (!r[i].equals(e[i])) {
				System.out.println(r[i] + ", " + e[i]);
			}
			assertTrue(r[i].equals(e[i]));
		}
	}

	@Test
	public void testPurchaseCWP3() {
		XSDConverter converter = new XSDConverter();
		BpmnDiagram diagram = new BpmnDiagram();
		HashMap<String, PromelaType> map = converter.importXSD("diagrams/purchaseCWP3.xsd", diagram);
//		System.out.println("**********************************");
//		System.out.println("Map:");
		StringBuilder result = new StringBuilder();
		for (String key : map.keySet()) {
//			System.out.println( map.get(key).generateDefinitionString(true) );
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
		String line = null;
		while (scanner.hasNextLine()) {
			line = scanner.nextLine();
			temp.append(line + "\n");
		}
		String[] r = result.toString().split("\n");
		String[] e = temp.toString().split("\n");
		System.out.println(result.toString());
		System.out.println(temp.toString());
		assertEquals(r.length, e.length);
		for (int i = 0; i < r.length; i++) {
			if (!r[i].equals(e[i])) {
				System.out.println("Error: " + r[i] + ", " + e[i]);
			}
			assertTrue(r[i].equals(e[i]));
		}
//		String expected = temp.toString();
//		System.out.println(expected);
//		assertTrue(expected.equals(result));
	}

}
