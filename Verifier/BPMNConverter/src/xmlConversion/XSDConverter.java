package xmlConversion;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import bpmnStructure.dataTypes.BoolType;
import bpmnStructure.dataTypes.IntType;
import bpmnStructure.dataTypes.MtypeType;
import bpmnStructure.dataTypes.PromelaTypeDef;
import bpmnStructure.dataTypes.PromelaVariable;

public class XSDConverter {
	
	private String namespace;
	
	public PromelaTypeDef importXSD(String filename) {
		try {
			File inputFile = new File( filename );
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse( inputFile );
			document.getDocumentElement().normalize();
			namespace = "";
			StringBuilder temp = new StringBuilder( document.getDocumentElement().getNodeName() );
			int semicolon = temp.indexOf(":");
			if(semicolon != -1) {
				namespace = temp.substring(0, semicolon + 1);
			}
//	        System.out.println("NameSpace: " + namespace);
	        
//	        NodeList elements = document.getElementsByTagName(namespace + "element");
//	        System.out.println("Elements.length: " + elements.getLength());
//	        System.out.println("NodeType: " + elements.item(0).getNodeType());
//	        System.out.println("NodeType: " + elements.item(0).getChildNodes().item(0).getNodeType());
	        
	        NodeList schema = document.getElementsByTagName(namespace + "schema");
	        NodeList types = schema.item(0).getChildNodes();
	        
	        for(int i = 0; i < types.getLength(); i++) {
	        	if(types.item(i).getNodeType() == Node.ELEMENT_NODE) {
//		        	System.out.println("typesType: " + types.item(i).getNodeType());
		        	addType( (Element) types.item(i) );
		        }
	        }
	        
	        System.out.println("\nDone");
	        
		} catch (ParserConfigurationException e) {
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
		
		return null;
	}
	
	private PromelaTypeDef addType(Element type) {
		System.out.println("Starting to add type");
		PromelaTypeDef tDef = new PromelaTypeDef(type.getAttribute("name"));
		System.out.println("Type: " + type.getAttribute("name"));
		NodeList variables = type.getElementsByTagName(namespace + "element");
		Element e = null;
		String name = null;
		PromelaVariable var = null;
		for(int i = 0; i < variables.getLength(); i++) {
			if(variables.item(i).getNodeType() == Node.ELEMENT_NODE) {
				e = (Element) variables.item(i);
				name = e.getAttribute("name");
				var = chooseVar(e, name);
				if(var != null) {
					tDef.addPromelaVariable(var);
				}
			}
		}
		return tDef;
	}
	
	private PromelaVariable chooseVar(Element e, String name) {
		NodeList restricts = e.getElementsByTagName(namespace + "restriction");
		Element restriction = null;
		String base = null;
		if(restricts == null || restricts.getLength() < 1) {
			base = e.getAttribute("type");
		}else {
			restriction = (Element) restricts.item(0);
			base = restriction.getAttribute("base");
		}
		PromelaVariable var = null;
		System.out.println("Starting to add var;\t" + base);
		
		String value = null;
		NodeList temp = null;
		switch(base) {
		case "xsd:integer":
			System.out.print("int: " + name);
			if(restriction != null) {
				temp = restriction.getElementsByTagName(namespace + "maxInclusive");
				assert(temp.getLength() == 1);
				assert(temp.item(0).getNodeType() == Node.ELEMENT_NODE);
				value = ((Element) temp.item(0)).getAttribute("value");
				System.out.print(", " + value);
			}
			System.out.println();
			var = new IntType(name, Integer.parseInt(value));
			break;
		case "xsd:string":
			temp = restriction.getElementsByTagName(namespace + "enumeration");
			assert(temp.getLength() > 0);
			String[] mes = new String[temp.getLength()];
			System.out.print("string: " + name);
			for(int i = 0; i < temp.getLength(); i++) {
				assert(temp.item(i).getNodeType() == Node.ELEMENT_NODE);
				value = ((Element) temp.item(i)).getAttribute("value");
				System.out.print(", " + value);
				mes[i] = value;
			}
			System.out.println();
			var = new MtypeType(name, mes);
			break;
		case "xsd:boolean":
			System.out.println("bool: " + name);
			var = new BoolType(name);
			break;
		case "xsd:bit":
			break;
		case "xsd:byte":
			break;
		case "xsd:float":
			break;
		default:
				break;
		}
		
		return var;
	}
	
	public static void main(String[] args) {
		XSDConverter converter = new XSDConverter();
		converter.importXSD("diagrams/testSchema.xsd");
	}

}
