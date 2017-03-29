package xmlConversion;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import bpmnStructure.BpmnDiagram;
import bpmnStructure.dataTypes.BoolType;
import bpmnStructure.dataTypes.MtypeType;
import bpmnStructure.dataTypes.PositiveIntType;
import bpmnStructure.dataTypes.PromelaType;
import bpmnStructure.dataTypes.PromelaTypeDef;
import bpmnStructure.exceptions.PromelaTypeSizeException;

public class XSDConverter {
	
	private String namespace;
	private HashMap<String, PromelaType> types;
	
	public HashMap<String, PromelaType> importXSD(String fileName, BpmnDiagram diagram) {
		
		File inputFile = new File( fileName );
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document document = null;
		try {
			builder = factory.newDocumentBuilder();
			document = builder.parse( inputFile );
			document.getDocumentElement().normalize();
			namespace = "";
			StringBuilder temp = new StringBuilder( document.getDocumentElement().getNodeName() );
			int semicolon = temp.indexOf(":");
			if(semicolon != -1) {
				namespace = temp.substring(0, semicolon + 1);
			}
			
			NodeList baseNodes = document.getChildNodes();
			Element e = null;
			types = new HashMap<>();
			if (baseNodes.item(0).getNodeType() == Node.ELEMENT_NODE) {
				e = (Element) baseNodes.item(0);
//				baseNodes = e.getChildNodes();
				schema(e, diagram);
			}
			
		}  catch (ParserConfigurationException e) {
			System.err.println( "You have an error in the configuration of your xml file!" );
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {
			System.err.println("The namespace stops at the semicolon?");
			e.printStackTrace();
		}catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("The file doesn't exist!");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Not a valid input file!");
			e.printStackTrace();
		}

		return types;
	}
	
	private PromelaType schema(Element e, BpmnDiagram diagram) throws NumberFormatException, PromelaTypeSizeException {
		if (!e.getTagName().equals(namespace + "schema")) {
			return null;
		}
		NodeList nodes = e.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
				e = (Element) nodes.item(i);
//				System.out.println(e.getTagName() + ", " + e.getAttribute("name"));
				String name = e.getAttribute("name");
				PromelaTypeDef temp = diagram.addTypeDef(name);
				temp = (PromelaTypeDef) element(e, temp);
				types.put(name, temp);
			}
		}
		
		return null;
	}
	
	private PromelaType element(Element e, PromelaTypeDef typeDef) throws NumberFormatException, PromelaTypeSizeException {
		PromelaType def = null;
		NodeList nodes = e.getChildNodes();
		Element temp = null;
		System.out.print(e.getAttribute("name") + ": ");
		String t = e.getAttribute("type");
		if (t != null && !t.isEmpty()) {
			def = pickType(t, null);
			typeDef.addPromelaType(e.getAttribute("name"), def);
			System.out.println(t);
		}else {
			for (int i = 0; i < nodes.getLength(); i++) {
				if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
					temp = (Element) nodes.item(i);
					def = simpleType(temp);
					if (def != null) {
						typeDef.addPromelaType(e.getAttribute("name"), def);
						System.out.println("\t" + def);
					}
					complexType(temp, typeDef);
				}
			}
		}
		return typeDef;
	}
	
	private PromelaType complexType(Element e, PromelaTypeDef typeDef) throws NumberFormatException, PromelaTypeSizeException {
		if(!e.getTagName().equals("xsd:complexType")) {
			return null;
		}
		NodeList nodes = e.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
				return sequence((Element) nodes.item(i), typeDef);
			}
		}
		return null;
	}
	
	private PromelaType sequence(Element e, PromelaTypeDef typeDef) throws NumberFormatException, PromelaTypeSizeException {
		if(!e.getTagName().equals("xsd:sequence")) {
			return null;
		}
		NodeList nodes = e.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
				element((Element) nodes.item(i), typeDef);
			}
		}
		return typeDef;
	}
	
	private PromelaType simpleType(Element e) throws NumberFormatException, PromelaTypeSizeException {
		if(!e.getTagName().equals("xsd:simpleType")) {
			return null;
		}
		NodeList nodes = e.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
				return restriction((Element) nodes.item(i));
			}
		}
		return null;
	}
	
	private PromelaType restriction(Element e) throws NumberFormatException, PromelaTypeSizeException {
		if(!e.getTagName().equals("xsd:restriction")) {
			return null;
		}
		String type = e.getAttribute("base");
		System.out.print(type);
		return pickType(type, e);
	}
	
	private String enumeration(Element e) {
		if(!e.getTagName().equals("xsd:enumeration")) {
			return null;
		}
		return e.getAttribute("value");
	}
	
	private String maxInclusive(Element e) {
		if (!e.getTagName().equals("xsd:maxInclusive")) {
			return null;
		}
		return e.getAttribute("value");
	}
	
	private PromelaType pickType(String typeName, Element e) throws PromelaTypeSizeException {
		NodeList nodes = null;
		PromelaType def = null;
		String value = null;
		switch(typeName) {
		case "xsd:integer":
			if (e != null && e.getChildNodes() != null) {
				value = maxInclusive((Element) e.getChildNodes().item(1));
			}
			if (value == null) {
				def = new PositiveIntType();
			}else {
				def = new PositiveIntType(Integer.parseInt(value));
				System.out.print(", " + value);
			}
			break;
		case "xsd:int":
			if (e != null && e.getChildNodes() != null) {
				value = maxInclusive((Element) e.getChildNodes().item(1));
			}
			if (value == null) {
				def = new PositiveIntType();
			}else {
				def = new PositiveIntType(Integer.parseInt(value));
			}
			break;
		case "xsd:string":
			if (e == null) {
				break;	
			}
			nodes = e.getChildNodes();
			int count = 0;
			for (int i = 0; i < nodes.getLength(); i++) {
				if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
					count++;
				}
			}
			String[] enums = new String[count];
			int index = 0;
			for (int i = 0; i < nodes.getLength(); i++) {
				if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
					value = enumeration((Element) nodes.item(i));
					enums[index++] = value;
					System.out.print(", " + value);
				}
			}
			def = new MtypeType(enums);
			break;
		case "xsd:boolean":
			def = new BoolType();
			break;
		}
		return def;
	}
	
	public static void main(String[] args) {
		XSDConverter converter = new XSDConverter();
		BpmnDiagram diagram = new BpmnDiagram();
		HashMap<String, PromelaType> map = converter.importXSD("diagrams/purchaseCWP.xsd", diagram);
		System.out.println("**********************************");
		System.out.println( map );
	}

}// ************************ THE END ************************


