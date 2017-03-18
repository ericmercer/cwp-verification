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

public class XMLConverter {
	
	private BpmnDiagram diagram;
	private PrintWriter writer;
	
	private String namespace;
	private HashMap<String, String> definitions;
	private HashMap<String, String> messageDefs;
	private HashMap<String, BpmnProcess> messageEvents;
	
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
	        System.out.println(processList.item(0).getNodeType());
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
        list = document.getElementsByTagName(namespace + "itemDefinition");
		for(int i = 0; i < list.getLength(); i++) {
			if(list.item(i) != null) {
				e = (Element) list.item(i);
				definitions.put(e.getAttribute("id"), e.getAttribute("structureRef"));
			}
		}
        
		messageDefs = new HashMap<>();
		list = document.getElementsByTagName(namespace + "message");
		String temp = null, key = null, value = null;
		for(int i = 0; i < list.getLength(); i++) {
			if(list.item(i) != null) {
				e = (Element) list.item(i);
				key = e.getAttribute("id");
				temp = e.getAttribute("itemRef");
				value = definitions.get(temp);
				messageDefs.put(key, value);
			}
		}
		
		list = document.getElementsByTagName(namespace + "dataStore");
		for(int i = 0; i < list.getLength(); i++) {
			if(list.item(i) != null) {
				e = (Element) list.item(i);
				initDataStore(e, diagram);
			}
		}
		
		messageEvents = new HashMap<>();
		Element process = null;
		writer.println( "ProcessList.size:\t" + processList.getLength() );
		for(int i = 0; i < processList.getLength(); i++) {
			process = (Element) processList.item(i);
			String id = process.getAttribute( "id" );
			writer.println( "Process:\t" + id + "\n" );
			initProcess(process, diagram.addProcess(id));
		}
		
		list = document.getElementsByTagName(namespace + "messageFlow");
		for(int i = 0; i < list.getLength(); i++) {
			if(list.item(i) != null) {
				e = (Element) list.item(i);
				initMessageFlow(e, diagram);
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
				else if(tag.equals(namespace + "task") || tag.equals(namespace + "userTask") || tag.equals(namespace + "manualTask") || 
						tag.equals(namespace + "sendTask") || tag.equals(namespace + "receiveTask")) {
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
				else if(tag.equals(namespace + "dataObject")) {
					initDataObject(child, diagram);
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
		writer.println( "startEvent:\t" + startEvent.getAttribute( "id" ) );
		NodeList message = startEvent.getElementsByTagName(namespace + "messageEventDefinition");
		NodeList list = startEvent.getElementsByTagName(namespace + "documentation");
		String code = null;
		if(list != null && list.getLength() != 0) {
			Element doc = (Element) list.item(0);
			code = getCode("<PROMELA>", "</PROMELA>", doc);
		}
		if(message != null && message.getLength() == 1) {
//			if the event has a messageEventDefinition then we add a messageStartEvent instead of 
//			a regular start event and we add that event id and the process it belongs to to a hashmap
//			so that we can easily add them to the messageFlows later
			messageEvents.put(id, process);
			if(code == null || code.length() == 0) {
				process.addMessageStartEvent(id);
			}else {
				process.addMessageStartEvent(id, code);
			}
			return;
		}
		if(code == null || code.length() == 0) {
			process.addStartEvent(id);
		}else {
			process.addStartEvent(id, code);
		}
	}
	
	private void initEndEvent(Element endEvent, BpmnProcess process) {
		String id = endEvent.getAttribute("id");
		writer.println( "endEvent:\t" + endEvent.getAttribute( "id" ) );
		NodeList message = endEvent.getElementsByTagName(namespace + "messageEventDefinition");
		NodeList list = endEvent.getElementsByTagName(namespace + "documentation");
		String code = null;
		if(list != null && list.getLength() != 0) {
			Element doc = (Element) list.item(0);
			code = getCode("<PROMELA>", "</PROMELA>", doc);
		}
		if(message != null && message.getLength() == 1) {
			messageEvents.put(id, process);
			if(code == null || code.length() == 0) {
				process.addMessageEndEvent(id);
			}else {
				process.addMessageEndEvent(id, code);
			}
			return;
		}
		if(code == null || code.length() == 0) {
			process.addEndEvent(id);
		}else {
			process.addEndEvent(id, code);
		}
	}
	
	private void initIntermediateCatchEvent(Element intermediateEvent, BpmnProcess process) {
		String id = intermediateEvent.getAttribute("id");
		writer.println( "intermediateEvents:\t" + intermediateEvent.getAttribute( "id" ) );
		NodeList message = intermediateEvent.getElementsByTagName(namespace + "messageEventDefinition");
		NodeList list = intermediateEvent.getElementsByTagName(namespace + "documentation");
		String code = null;
		if(list != null && list.getLength() != 0) {
			Element doc = (Element) list.item(0);
			code = getCode("<PROMELA>", "</PROMELA>", doc);
		}
		if(message != null && message.getLength() == 1) {
			messageEvents.put(id, process);
			if(code == null || code.length() == 0) {
				process.addMessageCatchEvent(id);
			}else {
				process.addMessageCatchEvent(id, code);
			}
			return;
		}
		if(code == null || code.length() == 0) {
			process.addIntermediateEvent(id);
		}else {
			process.addIntermediateEvent(id, code);
		}
	}
	
	private void initIntermediateThrowEvent(Element intermediateEvent, BpmnProcess process) {
		String id = intermediateEvent.getAttribute("id");
		writer.println( "intermediateEvents:\t" + intermediateEvent.getAttribute( "id" ) );
		NodeList message = intermediateEvent.getElementsByTagName(namespace + "messageEventDefinition");
		NodeList list = intermediateEvent.getElementsByTagName(namespace + "documentation");
		String code = null;
		if(list != null && list.getLength() != 0) {
			Element doc = (Element) list.item(0);
			code = getCode("<PROMELA>", "</PROMELA>", doc);
		}
		if(message != null && message.getLength() == 1) {
			messageEvents.put(id, process);
			if(code == null || code.length() == 0) {
				process.addMessageThrowEvent(id);
			}else {
				process.addMessageThrowEvent(id, code);
			}
			return;
		}
		if(code == null || code.length() == 0) {
			process.addIntermediateEvent(id);
		}else {
			process.addIntermediateEvent(id, code);
		}
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
			process.addTask( task.getAttribute("id") );
		}else {
			process.addTask( task.getAttribute("id"), code );
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
		if(code == null || code.size() == 0) {
			process.addScriptTask( task.getAttribute("id") );
		}else {
			process.addScriptTask( task.getAttribute("id"), code.toString() );
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
	
	private void initDataObject(Element data, BpmnProcess process) {
		writer.println( "dataObject:\t" + data.getAttribute( "name" ) );
		NodeList list = data.getElementsByTagName(namespace + "documentation");
		String code = null;
		if(list != null && list.getLength() != 0) {
			Element doc = (Element) list.item(0);
			code = getCode("<CAPACITY>", "</CAPACITY>", doc);
		}
		if(code == null) {
			process.addDataObject( data.getAttribute("id"), data.getAttribute("name"), 1 );
		}else {
			process.addDataObject( data.getAttribute("id"), data.getAttribute("name"), Integer.parseInt(code) );
		}
		
	}
	
	private void initDataStore(Element data, BpmnDiagram diagram) {
		writer.println( "dataStore:\t" + data.getAttribute( "name" ) );
		String id = data.getAttribute("id"), name = data.getAttribute("name"), cap = data.getAttribute("capacity");
		int capacity = Integer.parseInt(cap);
		diagram.addDataStore( id, name, capacity );
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
	
	private void initMessageFlow(Element current, BpmnDiagram diagram) {
		if(current == null) {
			return;
		}
		String id = current.getAttribute("id"), source = current.getAttribute("sourceRef"), target = current.getAttribute("targetRef"),
				ref = definitions.get(current.getAttribute("messageRef"));
		writer.println( "messageFlow:\t" + "sourceRef:\t" + source + "\ttargetRef:\t" + target );
		diagram.addMessageFlow(id, messageEvents.get(source), source, messageEvents.get(target), target, diagram.addTypeDef(ref));
//		messageEvents holds the process to which the source and target events belong, which means that just by 
//		using the the id of the source or target reference as a key, we can get the process to which it belongs
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
	
	@SuppressWarnings("serial")
	public class WrongTypeException extends Exception {
	}
	
	
//	end of class
}

//private void addOutputAssociation(ArrayList<Element> associations, BpmnProcess diagram) {
//	
//}
//
//private void initAssociations(ArrayList<Element> associations, BpmnProcess diagram) {
//	if(associations.isEmpty()) {
//    	return;
//    }
//    
//    Element current = null;
//    Iterator<Element> iter = associations.iterator();
//    while(iter.hasNext()) {
//    	current = iter.next();
//    	String source = current.getAttribute("sourceRef"), target = current.getAttribute("targetRef");
//		writer.println( "sourceRef: " + source + " targetRef: " + target );
//		diagram.addSequenceFlow( source, target );
//    }
//    
//    return;
//}




