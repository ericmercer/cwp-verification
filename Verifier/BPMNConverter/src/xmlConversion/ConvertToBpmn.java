package xmlConversion;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import bpmnStructure.BpmnDiagram;

public class ConvertToBpmn {
	
	private BpmnDiagram diagram;
	private PrintWriter writer;
	
	private String namespace;
	private HashMap<String, String> definitions;
	private HashMap<String, String> references;
	private HashMap<String, String> dataObjects;
	private HashMap<String, String> dataStores;
	
	public BpmnDiagram importXML( String fileName ) {
		
		initExport();
		
		try {
			File inputFile = new File( fileName );
			
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
	        NodeList process = document.getElementsByTagName( namespace + "process" );
	        if(process.item(0) == null) {
	        	throw new Exception();
	        }
	        
	        init(document, process);
	        
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
		writer.close();
		return diagram;
	}
	
	
	private void init(Document document, NodeList processList) throws WrongTypeException {
//        writer.println(namespace + " version: ");
        if(processList.item(0).getNodeType() != Node.ELEMENT_NODE) {
        	throw new WrongTypeException();
        }
        Element process = (Element) processList.item(0);
        
        String id = process.getAttribute( "id" );
		BpmnDiagram diagram = new BpmnDiagram(id);
		references = new HashMap<>();
		definitions = new HashMap<>();
		dataObjects = new HashMap<>();
		dataStores = new HashMap<>();
		
		NodeList list = process.getElementsByTagName(namespace + "itemDefinition");
		Element e = null;
		for(int i = 0; i < list.getLength(); i++) {
			e = (Element) list.item(i);
			definitions.put(e.getAttribute("id"), e.getAttribute("structureRef"));
		}
		
		list = process.getElementsByTagName(namespace + "dataObjectReference");
		for(int i = 0; i < list.getLength(); i++) {
			e = (Element) list.item(i);
			references.put(e.getAttribute("id"), e.getAttribute("dataObjectRef"));
		}
		
		list = process.getElementsByTagName(namespace + "dataStoreReference");
		for(int i = 0; i < list.getLength(); i++) {
			e = (Element) list.item(i);
			references.put(e.getAttribute("id"), e.getAttribute("dataStoreRef"));
		}
		
		list = process.getElementsByTagName(namespace + "dataObject");
		for(int i = 0; i < list.getLength(); i++) {
			e = (Element) list.item(i);
			initDataObject(e, diagram);
		}
		list = process.getElementsByTagName(namespace + "dataStore");
		for(int i = 0; i < list.getLength(); i++) {
			e = (Element) list.item(i);
			initDataStore(e, diagram);
		}
		initProcess(process, diagram);
		
		this.diagram = diagram;
	}
	
	private void initProcess(Element process, BpmnDiagram diagram) {
		NodeList children = process.getChildNodes();
		ArrayList<Element> flowSequences = new ArrayList<>();
		ArrayList<Element> associations = new ArrayList<>();
		for(int i = 0; i < children.getLength(); i++) {
			if(children.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element child = (Element) children.item(i);
				String tag = child.getTagName();
//				System.out.println("type: " + tag);
				
				if(tag.equals(namespace + "subProcess")) {
					writer.println();
					writer.println( "subProcess: " + child.getAttribute( "id" ) );
					initProcess(child, diagram.addNormalSubProcess(child.getAttribute("id")));
					writer.println();
				}
				else if(tag.equals(namespace + "adHocSubProcess")) {
					writer.println();
					writer.println( "subProcess: " + child.getAttribute( "id" ) );
					initProcess(child, diagram.addNormalSubProcess(child.getAttribute("id")));
					writer.println();
				}
				else if(tag.equals(namespace + "startEvent")) {
					initStartEvent(child, diagram);
				}
				else if(tag.equals(namespace + "endEvent")) {
					initEndEvent(child, diagram);
				}
				else if(tag.equals(namespace + "task")) {
					initTask(child, diagram);
				}
				else if(tag.equals(namespace + "userTask") || tag.equals(namespace + "manualTask") || 
						tag.equals(namespace + "sendTask") || tag.equals(namespace + "receiveTask")) {
					initTask(child, diagram);
				}
				else if(tag.equals(namespace + "scriptTask")) {
					initScriptTask(child, diagram);
				}
				else if(tag.equals(namespace + "exclusiveGateway")) {
					initExclusiveGate(child, diagram);
				}
//				else if(tag.equals(namespace + "inclusiveGateway")) {
//					initInclusiveGate(child, diagram);
//				}
				else if(tag.equals(namespace + "intermediateThrowEvent")) {
					initIntermediateEvent(child, diagram);
				}
				else if(tag.equals(namespace + "sequenceFlow")) {
					flowSequences.add(child);
				}
				else if(tag.equals(namespace + "association")) {
					associations.add(child);
				}
			}
		}
		
		initSequenceFlows(flowSequences, diagram);
		initAssociations(associations, diagram);
		
		return;
	}
	
	private void initExport() {
		try {
			writer = new PrintWriter( new BufferedWriter( new FileWriter("tests/output.txt") ) );
//			writer.print("worked?");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return;
	}
	
	private void initStartEvent(Element startEvent, BpmnDiagram diagram) {
		writer.println( "startEvent: " + startEvent.getAttribute( "id" ) );
		diagram.addStartEvent( startEvent.getAttribute("id") );
	}
	
	private void initEndEvent(Element endEvent, BpmnDiagram diagram) {
		writer.println( "endEvent: " + endEvent.getAttribute( "id" ) );
		diagram.addEndEvent( endEvent.getAttribute("id") );
	}
	
	private void initTask(Element task, BpmnDiagram diagram) {
		writer.println( "task: " + task.getAttribute( "id" ) );
		NodeList list = task.getElementsByTagName(namespace + "documentation");
		ArrayList<String> code = null;
		if(list != null && list.getLength() != 0) {
			Element doc = (Element) list.item(0);
			Scanner scan = new Scanner(doc.getTextContent());
			code = new ArrayList<>();
			while(scan.hasNext()) {
				code.add(scan.nextLine());
			}
			code.remove(code.size() - 1);
			code.remove(0);
			scan.close();
			writer.println(code.toString());
		}
		diagram.addTask( task.getAttribute("id"), code );
	}
	
	private void initScriptTask(Element task, BpmnDiagram diagram) {
		writer.println( "scriptTask: " + task.getAttribute( "id" ) );
		NodeList list = task.getElementsByTagName(namespace + "script");
		ArrayList<String> code = null;
		if(list != null && list.getLength() != 0) {
			Element doc = (Element) list.item(0);
			Scanner scan = new Scanner(doc.getTextContent());
			code = new ArrayList<>();
			while(scan.hasNext()) {
				code.add(scan.nextLine());
			}
			scan.close();
			writer.println(code.toString());
		}
		diagram.addScriptTask( task.getAttribute("id"), code );
	}
	
	private void initIntermediateEvent(Element intermediateEvent, BpmnDiagram diagram) {
		writer.println( "intermediateEvents: " + intermediateEvent.getAttribute( "id" ) );
		NodeList list = intermediateEvent.getElementsByTagName(namespace + "documentation");
		ArrayList<String> code = null;
		if(list != null && list.getLength() != 0) {
			Element doc = (Element) list.item(0);
			Scanner scan = new Scanner(doc.getTextContent());
			code = new ArrayList<>();
			while(scan.hasNext()) {
				code.add(scan.nextLine());
			}
			code.remove(code.size() - 1);
			code.remove(0);
			scan.close();
			writer.println(code.toString());
		}
		diagram.addIntermediateEvent( intermediateEvent.getAttribute("id"), code );
	}
	
	private void initExclusiveGate(Element exclusiveGate, BpmnDiagram diagram) {
		writer.println( "exclusiveGateway: " + exclusiveGate.getAttribute( "id" ) );
		diagram.addExclusiveGateway( exclusiveGate.getAttribute("id") );
	}
	
//	private void initInclusiveGate(Element inclusiveGate, BpmnDiagram diagram) {
//		writer.println( "inclusiveGateway: " + inclusiveGate.getAttribute( "id" ) );
//		diagram.addInclusiveGateway( inclusiveGate.getAttribute("id") );
//	}
	
	private void initDataObject(Element data, BpmnDiagram diagram) {
		writer.println( "dataObject: " + data.getAttribute( "name" ) );
		dataObjects.put(data.getAttribute("name"), data.getAttribute("id"));
		ArrayList<String> dataAttr = new ArrayList<>();
		dataAttr.add(data.getAttribute("name"));
		dataAttr.add( definitions.get(data.getAttribute("itemSubjectRef")) );
		NodeList assoc = data.getElementsByTagName(namespace + "dataInputAssociation");
		for(int i = 0; i < assoc.getLength(); i++) {
			Element e = (Element) assoc.item(i);
			
		}
		data.getElementsByTagName(namespace + "dataOutputAssociation");
		diagram.addDataObject( data.getAttribute("id"), dataAttr );
	}
	
	private void initDataStore(Element data, BpmnDiagram diagram) {
		writer.println( "dataStore: " + data.getAttribute( "name" ) );
		dataStores.put(data.getAttribute("name"), data.getAttribute("id"));
		ArrayList<String> dataAttr = new ArrayList<>();
		dataAttr.add(data.getAttribute("name"));
		dataAttr.add(data.getAttribute("capacity"));
		dataAttr.add( definitions.get(data.getAttribute("itemSubjectRef")) );
		diagram.addDataStore( data.getAttribute("id"), dataAttr );
	}
	
	private void addOutputAssociation(ArrayList<Element> associations, BpmnDiagram diagram) {
		
	}
	
	private void initAssociations(ArrayList<Element> associations, BpmnDiagram diagram) {
		if(associations.isEmpty()) {
        	return;
        }
        
        Element current = null;
        Iterator<Element> iter = associations.iterator();
        while(iter.hasNext()) {
        	current = iter.next();
        	String source = current.getAttribute("sourceRef"), target = current.getAttribute("targetRef");
			writer.println( "sourceRef: " + source + " targetRef: " + target );
			diagram.addSequenceFlow( source, target );
        }
        
        return;
	}
	
	private void initSequenceFlows(ArrayList<Element> sequenceFlows, BpmnDiagram diagram) {
        if(sequenceFlows.isEmpty()) {
        	return;
        }
        
        Element current = null;
        Iterator<Element> iter = sequenceFlows.iterator();
        while(iter.hasNext()) {
        	current = iter.next();
        	String source = current.getAttribute("sourceRef"), target = current.getAttribute("targetRef");
			writer.println( "sourceRef: " + source + " targetRef: " + target );
			diagram.addSequenceFlow( source, target );
        }
        
        return;
	}
	
	@SuppressWarnings("serial")
	public class WrongTypeException extends Exception {
	}
	
	
//	end of class
}






