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

public class ConvertToBpmn {
	
	private BpmnDiagram diagram;
	private PrintWriter writer;
	
	private String namespace;
	private HashMap<String, String> definitions;
	
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
//        writer.println(namespace + " version: ");
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
        
		list = document.getElementsByTagName(namespace + "dataStore");
		for(int i = 0; i < list.getLength(); i++) {
			if(list.item(i) != null) {
				e = (Element) list.item(i);
				initDataStore(e, diagram);
			}
		}
		
		Element process = null;
		for(int i = 0; i < processList.getLength(); i++) {
			process = (Element) processList.item(i);
			String id = process.getAttribute( "id" );
			initProcess(process, diagram.addProcess(id));
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
					initIntermediateThrowEvent(child, diagram);
				}
				else if(tag.equals(namespace + "intermediateCatchEvent")) {
					initIntermediateCatchEvent(child, diagram);
				}
				else if(tag.equals(namespace + "sequenceFlow")) {
					flowSequences.add(child);
				}
				else if(tag.equals(namespace + "dataObject")) {
					initDataObject(child, diagram);
				}
//				else if(tag.equals(namespace + "association")) {
//					associations.add(child);
//				}
			}
		}
		
		initSequenceFlows(flowSequences, diagram);
//		initAssociations(associations, diagram);
		
		return;
	}
	
	private void initExport() {
		try {
			writer = new PrintWriter( new BufferedWriter( new FileWriter("../output.txt") ) );
//			writer.print("worked?");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return;
	}
	
	private void initStartEvent(Element startEvent, BpmnProcess process) {
		writer.println( "startEvent: " + startEvent.getAttribute( "id" ) );
		NodeList message = startEvent.getElementsByTagName(namespace + "messageEventDefinition");
		NodeList list = startEvent.getElementsByTagName(namespace + "documentation");
		String code = null;
		if(list != null && list.getLength() != 0) {
			Element doc = (Element) list.item(0);
			code = getCode("<PROMELA>", "</PROMELA>", doc);
		}
		if(message != null && message.getLength() == 1) {
			if(code == null || code.length() == 0) {
				process.addMessageStartEvent(startEvent.getAttribute("id"));
			}else {
				process.addMessageStartEvent(startEvent.getAttribute("id"), code);
			}
			return;
		}
		if(code == null || code.length() == 0) {
			process.addStartEvent(startEvent.getAttribute("id"));
		}else {
			process.addStartEvent(startEvent.getAttribute("id"), code);
		}
	}
	
	private void initEndEvent(Element endEvent, BpmnProcess process) {
		writer.println( "endEvent: " + endEvent.getAttribute( "id" ) );
		NodeList message = endEvent.getElementsByTagName(namespace + "messageEventDefinition");
		NodeList list = endEvent.getElementsByTagName(namespace + "documentation");
		String code = null;
		if(list != null && list.getLength() != 0) {
			Element doc = (Element) list.item(0);
			code = getCode("<PROMELA>", "</PROMELA>", doc);
		}
		if(message != null && message.getLength() == 1) {
			if(code == null || code.length() == 0) {
				process.addMessageEndEvent(endEvent.getAttribute("id"));
			}else {
				process.addMessageEndEvent(endEvent.getAttribute("id"), code);
			}
			return;
		}
		if(code == null || code.length() == 0) {
			process.addEndEvent(endEvent.getAttribute("id"));
		}else {
			process.addEndEvent(endEvent.getAttribute("id"), code);
		}
	}
	
	private void initTask(Element task, BpmnProcess process) {
		writer.println( "task: " + task.getAttribute( "id" ) );
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
		if(code == null || code.size() == 0) {
			process.addScriptTask( task.getAttribute("id") );
		}else {
			process.addScriptTask( task.getAttribute("id"), code.toString() );
		}
	}
	
	private void initIntermediateCatchEvent(Element intermediateEvent, BpmnProcess process) {
		writer.println( "intermediateEvents: " + intermediateEvent.getAttribute( "id" ) );
		NodeList list = intermediateEvent.getElementsByTagName(namespace + "messageEventDefinition");
		if(list != null && list.getLength() == 1) {
			process.addMessageCatchEvent(intermediateEvent.getAttribute("id"));
			return;
		}
		process.addIntermediateEvent( intermediateEvent.getAttribute("id") );
	}
	
	private void initIntermediateThrowEvent(Element intermediateEvent, BpmnProcess process) {
		writer.println( "intermediateEvents: " + intermediateEvent.getAttribute( "id" ) );
		NodeList list = intermediateEvent.getElementsByTagName(namespace + "messageEventDefinition");
		if(list != null && list.getLength() == 1) {
			process.addMessageThrowEvent(intermediateEvent.getAttribute("id"));
			return;
		}
		process.addIntermediateEvent( intermediateEvent.getAttribute("id") );
	}
	
	private void initExclusiveGate(Element exclusiveGate, BpmnProcess process) {
		writer.println( "exclusiveGateway: " + exclusiveGate.getAttribute( "id" ) );
		process.addExclusiveGateway( exclusiveGate.getAttribute("id") );
	}
	
//	private void initInclusiveGate(Element inclusiveGate, BpmnDiagram diagram) {
//		writer.println( "inclusiveGateway: " + inclusiveGate.getAttribute( "id" ) );
//		diagram.addInclusiveGateway( inclusiveGate.getAttribute("id") );
//	}
	
	private void initDataObject(Element data, BpmnProcess process) {
		writer.println( "dataObject: " + data.getAttribute( "name" ) );
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
		writer.println( "dataStore: " + data.getAttribute( "name" ) );
		String cap = data.getAttribute("capacity");
		int capacity = Integer.parseInt(cap);
		diagram.addDataStore( data.getAttribute("id"), data.getAttribute("name"), capacity );
	}
	
	private void initSequenceFlows(ArrayList<Element> sequenceFlows, BpmnProcess diagram) {
        if(sequenceFlows.isEmpty()) {
        	return;
        }
        
        Element current = null;
        NodeList temp = null;
        Iterator<Element> iter = sequenceFlows.iterator();
        while(iter.hasNext()) {
        	current = iter.next();
        	String source = current.getAttribute("sourceRef"), target = current.getAttribute("targetRef");
			writer.println( "sourceRef: " + source + " targetRef: " + target );
			temp = current.getElementsByTagName(namespace + "conditionExpression");
			if(temp != null && temp.item(0) != null) {
				current = (Element) temp.item(0);
//				System.out.println("condition: " + current.getTextContent());
				diagram.addSequenceFlow( source, target, current.getTextContent() );
			}else {
				diagram.addSequenceFlow( source, target );
			}
        }
        
        return;
	}
	
	private void initMessageFlow(ArrayList<Element> messageFlows, BpmnDiagram diagram) {
		if(messageFlows.isEmpty()) {
			return;
		}
		Element current = null;
		NodeList temp = null;
		Iterator<Element> iter = messageFlows.iterator();
		while(iter.hasNext()) {
			current = iter.next();
			String source = current.getAttribute("sourceRef"), target = current.getAttribute("targetRef");
			writer.println( "sourceRef: " + source + " targetRef: " + target );
			String ref = definitions.get(current.getAttribute("messageRef"));
			diagram.addMessageFlow(current.getAttribute("id"), null, source, null, target, diagram.addTypeDef(ref));
		}
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
			}else if (current.equals(stopTag)) {
				take = false;
			}
			if(take) {
				code.add(current);
			}
		}
		scan.close();
		writer.println(code.toString());
		if(code.size() == 1 || !code.get(code.size() - 1).equals(stopTag)) {
//			throw an error because we never read in the stop tag, just the start tag
			return null;
		}else if(code.size() == 0) {
			return null; // there is no code we care about in the documentation
		}
		code.remove(0);
		code.remove(code.size() - 1);
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




