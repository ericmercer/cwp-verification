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
import bpmnStructure.BpmnProcess;
import bpmnStructure.dataTypes.PromelaType;
import bpmnStructure.dataTypes.PromelaTypeDef;

public class XMLConverter {
	
	private BpmnDiagram diagram;
	private PrintWriter writer;
	
	private String namespace;
	private HashMap<String, String> definitions;
	private HashMap<String, PromelaTypeDef> messageDefs;
	private HashMap<String, BpmnProcess> messageEvents;
	private HashMap<String, PromelaType> variables;
	private HashMap<String, String> dataObjects;
	
	public XMLConverter() {
	}
	
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
	        NodeList processList = document.getElementsByTagName( namespace + "process" );
	        if(processList.item(0) == null) {
	        	throw new Exception();
	        }
//	        System.out.println(processList.item(0).getNodeType());
	        init(document, processList);
	        
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
		} catch (NullPointerException e) {
			System.err.println("There was no import of data objects/structures from an outside xsd file! "
					+ "\nIf you use dataObjects or messages in the BPMN diagram, "
					+ "\nyou must have a corresponding xsd file to describe the data structures");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Not a valid input file!");
			e.printStackTrace();
		}
		writer.close();
		return diagram;
	}
	
	
	private void init(Document document, NodeList processList) throws WrongTypeException {
		writer.println( "Diagram:\n" );
		if(processList.item(0).getNodeType() != Node.ELEMENT_NODE) {
        	throw new WrongTypeException();
        }
//        itemDefinition
        NodeList list = null;
        Element e = null;
        BpmnDiagram diagram = new BpmnDiagram();
        definitions = new HashMap<>();
        dataObjects = new HashMap<>();
        
        list = document.getElementsByTagName(namespace + "import");
		for(int i = 0; i < list.getLength(); i++) {
			if(list.item(i) != null && list.item(i).getNodeType() == Node.ELEMENT_NODE) {
				e = (Element) list.item(i);
				String location = e.getAttribute("location");
				String[] parts = location.split("/");
				XSDConverter xsd = new XSDConverter();
//				System.out.println(parts[parts.length - 2] + "/" + parts[parts.length - 1]);
				variables = xsd.importXSD(parts[parts.length - 2] + "/" + parts[parts.length - 1], diagram);
			}
		}
        
        list = document.getElementsByTagName(namespace + "itemDefinition");
        String structRef = null;
		for(int i = 0; i < list.getLength(); i++) {
			if(list.item(i) != null) {
				e = (Element) list.item(i);
				structRef = e.getAttribute("structureRef");
				if (structRef.contains(":")) {
					structRef = structRef.substring( structRef.indexOf(":") + 1 );
				}
				definitions.put(e.getAttribute("id"), structRef);
			}
		}
        
		messageDefs = new HashMap<>();
		list = document.getElementsByTagName(namespace + "message");
		String id = null, name = null;
		for(int i = 0; i < list.getLength(); i++) {
			if(list.item(i) != null) {
				e = (Element) list.item(i);
				id = e.getAttribute("id");
				name = e.getAttribute("name");
				if (!variables.containsKey(name)) {
					System.err.println("The message found in the bpmn diagram did "
							+ "not match any data elements in the corresponding xsd");
				}
				messageDefs.put(id, (PromelaTypeDef) variables.get(name));
			}
		}
		
		list = document.getElementsByTagName(namespace + "dataStore");
		for(int i = 0; i < list.getLength(); i++) {
			if(list.item(i) != null) {
				e = (Element) list.item(i);
				try {
					initDataStore(e, diagram);
				} catch (InvalidDataTypeException e2) {
					System.err.println("The data store found in the bpmn diagram "
							+ "did not match any data elements in the corresponding xsd");
					e2.printStackTrace();
				}
			}
		}
		
		messageEvents = new HashMap<>();
		Element process = null;
		writer.println( "ProcessList.size:\t" + processList.getLength() );
		for(int i = 0; i < processList.getLength(); i++) {
			process = (Element) processList.item(i);
			id = process.getAttribute( "id" );
			writer.println( "Process:\t" + id + "\n" );
			initProcess(process, diagram.addProcess(id));
		}
		
		list = document.getElementsByTagName(namespace + "messageFlow");
		for(int i = 0; i < list.getLength(); i++) {
			if(list.item(i) != null) {
				e = (Element) list.item(i);
				try {
					initMessageFlow(e, diagram);
				} catch (InvalidDataTypeException e1) {
					e1.printStackTrace();
					System.err.println("The message flow tried to reference a message that did not exist");
					e1.printStackTrace();
				}
			}
		}
		
		this.diagram = diagram;
	}
	
	private void initProcess(Element process, BpmnProcess diagram) {
		NodeList children = process.getChildNodes();
		ArrayList<Element> flowSequences = new ArrayList<>();
//		ArrayList<Element> associations = new ArrayList<>();
		for(int i = 0; i < children.getLength(); i++) {
			if(children.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element child = (Element) children.item(i);
				String tag = child.getTagName();
				if(tag.equals(namespace + "dataObject")) {
					try {
						initDataObject(child, diagram);
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (InvalidDataTypeException e) {
						System.err.println("The data object found in the bpmn diagram did not "
								+ "match any data elements in the corresponding xsd");
						e.printStackTrace();
					}
				}
			}
		}
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
				else if(tag.equals(namespace + "intermediateThrowEvent")) {
					initIntermediateThrowEvent(child, diagram);
				}
				else if(tag.equals(namespace + "intermediateCatchEvent")) {
					initIntermediateCatchEvent(child, diagram);
				}
				else if(tag.equals(namespace + "task") || tag.equals(namespace + "userTask") || 
						tag.equals(namespace + "manualTask") || tag.equals(namespace + "sendTask") || 
						tag.equals(namespace + "receiveTask")) {
					initTask(child, diagram);
				}
				else if(tag.equals(namespace + "scriptTask")) {
					initScriptTask(child, diagram);
				}
				else if(tag.equals(namespace + "exclusiveGateway")) {
					initExclusiveGate(child, diagram);
				}
				else if(tag.equals(namespace + "parallelGateway")) {
					initParallelGate(child, diagram);
				}
				else if(tag.equals(namespace + "sequenceFlow")) {
					flowSequences.add(child);
				}
			}
		}
		
		initSequenceFlows(flowSequences, diagram);
		return;
	}
	
	private void initExport() {
		try {
			writer = new PrintWriter( new BufferedWriter( new FileWriter("./output.txt") ) );
//			writer.print("worked?");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return;
	}
	
	private void initStartEvent(Element startEvent, BpmnProcess process) {
		String id = startEvent.getAttribute("id");
		writer.print( "startEvent:\t" + startEvent.getAttribute( "id" ) );
		NodeList message = startEvent.getElementsByTagName(namespace + "messageEventDefinition");
		if(message != null && message.getLength() == 1) {
//			if the event has a messageEventDefinition then we add a messageStartEvent instead of 
//			a regular start event and we add that event id and the process it belongs to to a hashmap
//			so that we can easily add them to the messageFlows later
			messageEvents.put(id, process);
			writer.print( "\tmessage" );
			String dataName = getDataName(startEvent);
//			System.out.println(id + ":" + dataName);
			process.addMessageStartEvent(id, startEvent.getAttribute("name"), dataName);
			return;
		}
		process.addStartEvent(id, startEvent.getAttribute("name"));
		writer.println();
	}
	
	private String getDataName(Element element) {
		NodeList list = element.getElementsByTagName(namespace + "dataInputAssociation");
		Element child = null;
		NodeList childList = null;
		String dataObjectName = null;
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
				child = (Element) list.item(i);
				childList = child.getElementsByTagName(namespace + "sourceRef");
				for (int j = 0; j < list.getLength(); j++) {
					if (childList.item(j).getNodeType() == Node.ELEMENT_NODE) {
						child = (Element) childList.item(j);
						dataObjectName = child.getTextContent();
//						System.out.println(dataObjects.get(dataObjectName));
						return dataObjects.get(dataObjectName);
					}
				}
				return null;
			}
		}
		list = element.getElementsByTagName(namespace + "dataOutputAssociation");
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
				child = (Element) list.item(i);
				childList = child.getElementsByTagName(namespace + "targetRef");
				for (int j = 0; j < list.getLength(); j++) {
					if (childList.item(j).getNodeType() == Node.ELEMENT_NODE) {
						child = (Element) childList.item(j);
						dataObjectName = child.getTextContent();
//						System.out.println(dataObjects.get(dataObjectName));
						return dataObjects.get(dataObjectName);
					}
				}
			}
		}
		return null;
	}
	
	private void initEndEvent(Element endEvent, BpmnProcess process) {
		String id = endEvent.getAttribute("id");
		writer.println( "endEvent:\t" + endEvent.getAttribute( "id" ) );
		NodeList message = endEvent.getElementsByTagName(namespace + "messageEventDefinition");
		if(message != null && message.getLength() == 1) {
			messageEvents.put(id, process);
			String dataName = getDataName(endEvent);
//			System.out.println(id + ":" + dataName);
			process.addMessageEndEvent(id, endEvent.getAttribute("name"), dataName);
			return;
		}
		process.addEndEvent(id, endEvent.getAttribute("name"));
	}
	
	private void initIntermediateCatchEvent(Element intermediateEvent, BpmnProcess process) {
		String id = intermediateEvent.getAttribute("id");
		writer.println( "intermediateEvents:\t" + intermediateEvent.getAttribute( "id" ) );
		NodeList message = intermediateEvent.getElementsByTagName(namespace + "messageEventDefinition");
		if(message != null && message.getLength() == 1) {
			messageEvents.put(id, process);
			String dataName = getDataName(intermediateEvent);
//			System.out.println(id + ":" + dataName);
			process.addMessageCatchEvent(id, intermediateEvent.getAttribute("name"), dataName);
			return;
		}
		process.addIntermediateEvent(id, intermediateEvent.getAttribute("name"));
	}
	
	private void initIntermediateThrowEvent(Element intermediateEvent, BpmnProcess process) {
		String id = intermediateEvent.getAttribute("id");
		writer.println( "intermediateEvents:\t" + intermediateEvent.getAttribute( "id" ) );
		NodeList message = intermediateEvent.getElementsByTagName(namespace + "messageEventDefinition");
		if(message != null && message.getLength() == 1) {
			messageEvents.put(id, process);
			String dataName = getDataName(intermediateEvent);
//			System.out.println(id + ":" + dataName);
			process.addMessageThrowEvent(id, intermediateEvent.getAttribute("name"), dataName);
			return;
		}
		process.addIntermediateEvent(id, intermediateEvent.getAttribute("name"));
	}
	
	private void initTask(Element task, BpmnProcess process) {
		writer.println( "task:\t" + task.getAttribute( "id" ) );
		NodeList list = task.getElementsByTagName(namespace + "documentation");
		String code = null;
		if(list != null && list.getLength() != 0) {
			Element doc = (Element) list.item(0);
			code = getCode("<PROMELA>", "</PROMELA>", doc);
		}
		if(code == null) {
			process.addTask( task.getAttribute("id"), task.getAttribute("name") );
		}else {
			process.addScriptTask( task.getAttribute("id"), task.getAttribute("name"), code );
		}
	}
	
	private void initScriptTask(Element task, BpmnProcess process) {
		task.getElementsByTagName("script");
		writer.println( "scriptTask:\t" + task.getAttribute( "id" ) );
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
		String dataName = getDataName(task);
//		System.out.println("scriptTask: " + dataName);
		if(code == null || code.size() == 0) {
			process.addScriptTask( task.getAttribute("id"), code.toString() );
		}else {
			process.addScriptTask( task.getAttribute("id"), code.toString(), dataName );
		}
	}
	
	private void initExclusiveGate(Element exclusiveGate, BpmnProcess process) {
		writer.println( "exclusiveGateway:\t" + exclusiveGate.getAttribute( "id" ) );
		process.addExclusiveGateway( exclusiveGate.getAttribute("id") );
	}
	
	private void initParallelGate(Element parellelGate, BpmnProcess process) {
		writer.println( "parellelGateway:\t" + parellelGate.getAttribute( "id" ) );
		process.addParallelGateway( parellelGate.getAttribute("id") );
	}
	
	private void initDataObject(Element data, BpmnProcess process) throws NumberFormatException, InvalidDataTypeException {
		writer.println( "dataObject:\t" + data.getAttribute("name") );
		dataObjects.put(data.getAttribute("id"), data.getAttribute("name"));
		NodeList list = data.getElementsByTagName(namespace + "documentation");
		String code = null;
		if(list != null && list.getLength() != 0) {
			Element doc = (Element) list.item(0);
			code = getCode("<CAPACITY>", "</CAPACITY>", doc);
		}
		String name = data.getAttribute("name");
		if (!variables.containsKey(name)) {
			throw new InvalidDataTypeException();
		}
		if(code == null) {
			process.addDataObject( data.getAttribute("name"), variables.get(name), 1 );
		}else {
			process.addDataObject( data.getAttribute("name"), variables.get(name), Integer.parseInt(code) );
		}
	}
	
	private void initDataStore(Element data, BpmnDiagram diagram) throws InvalidDataTypeException {
		writer.print( "dataStore:\t" + data.getAttribute( "name" ) );
		String name = data.getAttribute("name"), cap = data.getAttribute("capacity");
		int capacity = Integer.parseInt(cap);
		if (!variables.containsKey(name)) {
			for (String key : variables.keySet()) {
				System.out.print(key + "\t");
			}
			System.out.println("\n" + name);
			throw new InvalidDataTypeException();
		}
		writer.println("\ttype: " + variables.get(name));
		diagram.addDataStore( name, variables.get(name), capacity );
	}
	
	private void initSequenceFlows(ArrayList<Element> sequenceFlows, BpmnProcess process) {
        if(sequenceFlows.isEmpty()) {
        	return;
        }
        
        Element current = null;
        NodeList condition = null;
        Iterator<Element> iter = sequenceFlows.iterator();
        while(iter.hasNext()) {
        	current = iter.next();
        	String source = current.getAttribute("sourceRef"), target = current.getAttribute("targetRef");
			writer.println( "sourceRef:\t" + source + "\ttargetRef:\t" + target );
			condition = current.getElementsByTagName(namespace + "conditionExpression");
			if(condition != null && condition.item(0) != null) {
				current = (Element) condition.item(0);
//				System.out.println("condition: " + current.getTextContent());
				process.addSequenceFlow( source, target, current.getTextContent() );
			}else {
				try {
					process.addSequenceFlow( source, target );
				} catch (NullPointerException e) {
					System.out.println("Source: " + source + " Target: " + target);
				}
				
			}
        }
        
        return;
	}
	
	private void initMessageFlow(Element current, BpmnDiagram diagram) throws InvalidDataTypeException {
		if(current == null) {
			return;
		}
		String id = current.getAttribute("id"), source = current.getAttribute("sourceRef"), 
				target = current.getAttribute("targetRef"),
				ref = current.getAttribute("messageRef");
		if (messageDefs.get(ref) == null) {
			throw new InvalidDataTypeException();
		}
		writer.println( "messageFlow:\t" + "sourceRef:\t" + source + "\ttargetRef:\t" + target );
		diagram.addMessageFlow(id, messageEvents.get(source), source, 
				messageEvents.get(target), target, messageDefs.get(ref));
/**		
		messageEvents holds the process to which the source and target events belong, which means that just by 
		using the the id of the source or target reference as a key, we can get the process to which it belongs
		messageDefs is defined when reading in the message objects as a map between the message id and 
		the typeDef defined in the xsd
 */
	}
	
	/**
	 * reads the documentation element given and finds the code desired by the identifiers (inditifier and stopper).
	 * @param identifier the string that identifies where to start intaking the code
	 * @param stopper the String indentifying where to stop intaking the code
	 * @param doc the document you want to read from
	 * @return the code taken from the document
	 */
	private String getCode(String startTag, String stopTag, Element doc) {
		Scanner scan = new Scanner(doc.getTextContent());
		ArrayList<String> code = new ArrayList<>();
		boolean take = false;
		String current = null;
		while(scan.hasNextLine()) {
			current = scan.nextLine();
			if(current.equals(startTag)) {
				take = true;
			}
			if(take) {
				code.add(current);
			}
			if (current.equals(stopTag)) {
				take = false;
			}
		}
		scan.close();
//		System.out.println(code.toString());
		if(code.size() == 1 || !code.get(code.size() - 1).equals(stopTag)) {
//			throw an error because we never read in the stop tag, just the start tag
			System.err.println("No Stop tag!");
			return null;
		}else if(code.size() == 0) {
			System.err.println("No documentation!");
			return null; // there is no code we care about in the documentation
		}
		code.remove(0);
		code.remove(code.size() - 1);
//		System.out.println(code.toString());
		writer.println(code.toString());
		return code.toString();
	}
	
	public HashMap<String, PromelaType> getTypes() {
		return variables;
	}
	
	@SuppressWarnings("serial")
	public class WrongTypeException extends Exception {
	}
	
	@SuppressWarnings("serial")
	public class InvalidDataTypeException extends Exception {
	}
	
	public static void main(String[] args) {
		XMLConverter converter = new XMLConverter();
		BpmnDiagram diagram = null;
		diagram = converter.importXML("diagrams/online_purchase2.bpmn");
		System.out.println(diagram.getGlobalVariables(0));
	}
	
} //	end of class



