package xmlConversion;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

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
	
	public BpmnDiagram importXML( String fileName ) {
		
		initExport();
		
		try {
			File inputFile = new File( fileName );
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse( inputFile );
			document.getDocumentElement().normalize();
			
			String namespace = "";
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
	        
	        init(document, process, namespace);
	        
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
	
	
	private void init(Document document, NodeList processList, String namespace) throws WrongTypeException {
//        writer.println(namespace + " version: ");
        if(processList.item(0).getNodeType() != Node.ELEMENT_NODE) {
        	throw new WrongTypeException();
        }
        Element process = (Element) processList.item(0);
        
        String id = process.getAttribute( "id" );
		BpmnDiagram diagram = new BpmnDiagram(id);
		
		initProcess(process, diagram, namespace);
		
		this.diagram = diagram;
	}
	
	private void initProcess(Element process, BpmnDiagram diagram, String namespace) {
		NodeList children = process.getChildNodes();
		ArrayList<Element> flowSequences = new ArrayList<>();
		for(int i = 0; i < children.getLength(); i++) {
			if(children.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element child = (Element) children.item(i);
				String tag = child.getTagName();
//				System.out.println("type: " + tag);
				
				if(tag.equals(namespace + "subProcess")) {
					writer.println();
					writer.println( "subProcess: " + child.getAttribute( "id" ) );
					initProcess(child, diagram.addNormalSubProcess(child.getAttribute("id")), namespace);
					writer.println();
				}
				else if(tag.equals(namespace + "adHocSubProcess")) {
					writer.println();
					writer.println( "subProcess: " + child.getAttribute( "id" ) );
					initProcess(child, diagram.addNormalSubProcess(child.getAttribute("id")), namespace);
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
				else if(tag.equals(namespace + "userTask")) {
					initUserTask(child, diagram);
				}
				else if (tag.equals(namespace + "sendTask")) {
					initSendTask(child, diagram);
				}
				else if (tag.equals(namespace + "receiveTask")) {
					initReceiveTask(child, diagram);
				}
				else if (tag.equals(namespace + "manualTask")) {
					initManualTask(child, diagram);
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
			}
		}
		
		initSequenceFlows(flowSequences, diagram);
		
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
		diagram.addTask( task.getAttribute("id") );
	}
	
	private void initUserTask(Element task, BpmnDiagram diagram) {
		writer.println( "userTask: " + task.getAttribute( "id" ) );
		diagram.addTask( task.getAttribute("id") );
	}
	
	private void initSendTask(Element task, BpmnDiagram diagram) {
		writer.println( "sendTask: " + task.getAttribute( "id" ) );
		diagram.addTask( task.getAttribute("id") );
	}
	
	private void initReceiveTask(Element task, BpmnDiagram diagram) {
		writer.println( "receiveTask: " + task.getAttribute( "id" ) );
		diagram.addTask( task.getAttribute("id") );
	}
	
	private void initManualTask(Element task, BpmnDiagram diagram) {
		writer.println( "manualTask: " + task.getAttribute( "id" ) );
		diagram.addTask( task.getAttribute("id") );
	}
	
	private void initScriptTask(Element task, BpmnDiagram diagram) {
		task.getElementsByTagName("script");
		writer.println( "scriptTask: " + task.getAttribute( "id" ) );
		diagram.addScriptTask( task.getAttribute("id") );
	}
	
	private void initIntermediateEvent(Element intermediateEvent, BpmnDiagram diagram) {
		writer.println( "intermediateEvents: " + intermediateEvent.getAttribute( "id" ) );
		diagram.addIntermediateEvent( intermediateEvent.getAttribute("id") );
	}
	
	private void initExclusiveGate(Element exclusiveGate, BpmnDiagram diagram) {
		writer.println( "exclusiveGateway: " + exclusiveGate.getAttribute( "id" ) );
		diagram.addExclusiveGateway( exclusiveGate.getAttribute("id") );
	}
	
//	private void initInclusiveGate(Element inclusiveGate, BpmnDiagram diagram) {
//		writer.println( "inclusiveGateway: " + inclusiveGate.getAttribute( "id" ) );
//		diagram.addInclusiveGateway( inclusiveGate.getAttribute("id") );
//	}
	
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






