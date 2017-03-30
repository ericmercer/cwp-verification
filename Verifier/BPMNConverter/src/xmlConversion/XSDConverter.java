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
	private HashMap<String, Boolean> declared;
	private BpmnDiagram diagram;
	
	public XSDConverter() {
		types = new HashMap<>();
		declared = new HashMap<>();
	}
	
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
			if (baseNodes.item(0).getNodeType() == Node.ELEMENT_NODE) {
				e = (Element) baseNodes.item(0);
//				baseNodes = e.getChildNodes();
				this.diagram = diagram;
				addTypes(e);
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
	
	private PromelaType addTypes(Element element) throws Exception {
		if (!element.getTagName().equals(namespace + "schema")) {
			return null;
		}
		Element tag = null;
		PromelaTypeDef def = null;
		NodeList nodes = element.getChildNodes();
		String name = null;
		for (int i = 0; i < nodes.getLength(); i++) {
			if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
				tag = (Element) nodes.item(i);
				name = tag.getAttribute("name");
				
				if (!types.containsKey(name)) {
					def = diagram.addTypeDef(name);
					types.put(name, def);
					declared.put(name, true);
					
					if (tag.getTagName().equals("xsd:complexType")) {
						System.out.println("complexType: " + name + ": ");
						sequence((Element) tag.getChildNodes().item(1), def);
						
					}else if (tag.getTagName().equals("xsd:simpleType")) {
						System.out.println("simpleType: " + name + ": ");
						PromelaType type = simpleType(tag);
						System.out.println();
						def.addPromelaType(type);
					}
					
				}else {
					if (!declared.get(name)) { // it was made but not declared
						declared.put(name, true);
						
						if (tag.getTagName().equals("xsd:complexType")) {
							System.out.println("complexType: " + name + ": ");
							sequence((Element) tag.getChildNodes().item(1), def);
							
						}else if (tag.getTagName().equals("xsd:simpleType")) {
							System.out.println("simpleType: " + name + ": ");
							PromelaType type = simpleType(tag);
							System.out.println();
							def.addPromelaType(type);
						}
						
					}else { // it was made and declared before; bark!
						System.err.println("This type has already been declared!!!");
						throw new Exception();
					}
				}
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
		if (!t.isEmpty()) {
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
						System.out.println();
//						System.out.println("\t" + def);
					}else {
						System.err.println("It wasn't declared in the element type and it's not a simple!");
					}
				}
			}
		}
		return typeDef;
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
			if (e != null && e.getChildNodes() != null && e.getChildNodes().getLength() > 1) {
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
		default:
			String otherTypeName = typeName;
			if (typeName.contains(":")) {
				otherTypeName = typeName.substring(typeName.indexOf(":") + 1);
//				System.out.println("typeName, otherTypeName: " + typeName + ", " + otherTypeName);
			}
			if (types.containsKey(otherTypeName)) {
				def = types.get(otherTypeName);
			}else {
				def = diagram.addTypeDef(otherTypeName);
				types.put(otherTypeName, def);
				declared.put(otherTypeName, false);
			}
		}
		return def;
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
	
	public static void main(String[] args) {
		XSDConverter converter = new XSDConverter();
		BpmnDiagram diagram = new BpmnDiagram();
		HashMap<String, PromelaType> map = converter.importXSD("diagrams/purchaseCWP3.xsd", diagram);
		System.out.println("**********************************");
		System.out.println("{");
		for (String key : map.keySet()) {
			System.out.println( "\t" + map.get(key) );
		}
	}

}// ************************ THE END ************************


