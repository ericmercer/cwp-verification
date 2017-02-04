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

import bpmnStructure.BpmnDiagram;

public class ConvertToBpmn {
	
	private NodeList startEvents;
	private NodeList tasks;
	private NodeList exclusiveGates;
	private NodeList endEvents;
	private NodeList subProcess;
	private NodeList sequenceFlows;
	
	private BpmnDiagram diagram;
	
	public BpmnDiagram importXML( String fileName ) {
		
		try {
			File inputFile = new File( fileName );
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse( inputFile );
			document.getDocumentElement().normalize();
//	        System.out.println("Root element: " + document.getDocumentElement().getNodeName());
	        NodeList process = document.getElementsByTagName( "bpmn2:process" );
	        if(process == null) {
	        	process = document.getElementsByTagName( "bpmn:process" );
	        	if(process == null) {
	        		process = document.getElementsByTagName( "process" );
	        		if(process == null) {
	        			throw new Exception();
	        		}
	        		blankVersion(document, process);
	        		return diagram;
	        	}
	        	bpmnVersion(document, process);
	        	return diagram;
	        }
	        String id = ( (Element) process.item(0) ).getAttribute( "id" );
			diagram = new BpmnDiagram(id);
			
			subProcess = document.getElementsByTagName( "bpmn2:subProcess" );
			initSubProcess();
			
	        startEvents = document.getElementsByTagName( "bpmn2:startEvent" );
	        initStartEvents();
	        
			tasks = document.getElementsByTagName( "bpmn2:task" );
			initTasks();
	        
			exclusiveGates = document.getElementsByTagName( "bpmn2:exclusiveGateway" );
			initExclusiveGates();
	        
			endEvents = document.getElementsByTagName( "bpmn2:endEvent" );
			initEndEvents();
	        
			sequenceFlows = document.getElementsByTagName("bpmn2:sequenceFlow");
			initSequenceFlows();
	         
//	        close(); 
	        
		} catch (ParserConfigurationException e) {
			System.out.println( "You have an error in the configuration of your xml file!" );
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("The file doesn't exist!");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Not a valid input file!");
			e.printStackTrace();
		}
		
		return diagram;
	}
	
	
	
	private void initSubProcess() {
        if( subProcess != null ) {
        	for( int i = 0; i < subProcess.getLength(); i++ ) {
        		if( subProcess.item(i).getNodeType() == Node.ELEMENT_NODE ) {
        			Element element = (Element) subProcess.item(i);
//        			System.out.println( "startEvents: " + element.getAttribute( "id" ) );
        			diagram.addNormalSubProcess( element.getAttribute("id") );
        		}
        		
        	}
        }
	}
	
	private void initStartEvents() {
        if( startEvents != null ) {
        	for( int i = 0; i < startEvents.getLength(); i++ ) {
        		if( startEvents.item(i).getNodeType() == Node.ELEMENT_NODE ) {
        			Element element = (Element) startEvents.item(i);
//        			System.out.println( "startEvents: " + element.getAttribute( "id" ) );
        			diagram.addStartEvent( element.getAttribute("id") );
        		}
        		
        	}
        }
	}
	
	private void initEndEvents() {
        if( endEvents != null ) {
        	for( int i = 0; i < endEvents.getLength(); i++ ) {
        		if( endEvents.item(i).getNodeType() == Node.ELEMENT_NODE ) {
        			Element element = (Element) endEvents.item(i);
//        			System.out.println( "endEvent: " + element.getAttribute( "id" ) );
        			diagram.addEndEvent( element.getAttribute("id") );
        		}
        	}
        }
	}
	
	private void initTasks() {
        if( tasks != null ) {
        	for( int i = 0; i < tasks.getLength(); i++ ) {
        		if( tasks.item(i).getNodeType() == Node.ELEMENT_NODE ) {
        			Element element = (Element) tasks.item(i);
//        			System.out.println( "task(" + i + "): " + element.getAttribute( "id" ) );
        			diagram.addTask( element.getAttribute("id") );
        		}
        	}
        }
	}
	
	private void initExclusiveGates() {
        if( exclusiveGates != null ) {
        	for( int i = 0; i < exclusiveGates.getLength(); i++ ) {
        		if( exclusiveGates.item(i).getNodeType() == Node.ELEMENT_NODE ) {
        			Element element = (Element) exclusiveGates.item(i);
//        			System.out.println( "exclusiveGateway(" + i + "): " + element.getAttribute( "id" ) );
        			diagram.addExclusiveGateway( element.getAttribute("id") );
        		}
        	}
        }
	}
	
	private void initSequenceFlows() {
        if( sequenceFlows != null ) {
        	for( int i = 0; i < sequenceFlows.getLength(); i++ ) {
        		if( sequenceFlows.item(i).getNodeType() == Node.ELEMENT_NODE ) {
        			Element element = (Element) sequenceFlows.item(i);
//        			System.out.println( "exclusiveGateway(" + i + "): " + element.getAttribute( "id" ) );
        			diagram.addSequenceFlow( element.getAttribute("sourceRef"), element.getAttribute("targetRef") );
        		}
        	}
        }
	}
	
	private void bpmnVersion(Document document, NodeList process) {
        String id = ( (Element) process.item(0) ).getAttribute( "id" );
		diagram = new BpmnDiagram(id);
		
		subProcess = document.getElementsByTagName( "bpmn:subProcess" );
		initSubProcess();
		
        startEvents = document.getElementsByTagName( "bpmn:startEvent" );
        initStartEvents();
        
		tasks = document.getElementsByTagName( "bpmn:task" );
		initTasks();
        
		exclusiveGates = document.getElementsByTagName( "bpmn:exclusiveGateway" );
		initExclusiveGates();
        
		endEvents = document.getElementsByTagName( "bpmn:endEvent" );
		initEndEvents();
        
		sequenceFlows = document.getElementsByTagName("bpmn:sequenceFlow");
		initSequenceFlows();
	}
	
	
	private void blankVersion(Document document, NodeList process) {
        String id = ( (Element) process.item(0) ).getAttribute( "id" );
		diagram = new BpmnDiagram(id);
		
		subProcess = document.getElementsByTagName( "subProcess" );
		initSubProcess();
		
        startEvents = document.getElementsByTagName( "startEvent" );
        initStartEvents();
        
		tasks = document.getElementsByTagName( "task" );
		initTasks();
        
		exclusiveGates = document.getElementsByTagName( "exclusiveGateway" );
		initExclusiveGates();
        
		endEvents = document.getElementsByTagName( "endEvent" );
		initEndEvents();
        
		sequenceFlows = document.getElementsByTagName("sequenceFlow");
		initSequenceFlows();
	}

}
