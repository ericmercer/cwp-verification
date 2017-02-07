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
	private NodeList intermediateEvents;
	private NodeList endEvents;
	private NodeList subProcess;
	private NodeList adhocSubProcess;
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
	        if(process.item(0) == null) {
	        	
	        	process = document.getElementsByTagName( "bpmn:process" );
	        	if(process.item(0) != null) {
		        	bpmnVersion(document, process);
		        	return diagram;
	        	}//else:
	        	
        		process = document.getElementsByTagName( "process" );
        		if(process.item(0) == null) {
        			throw new Exception();
        		}
        		blankVersion(document, process);
        		return diagram;
	        }
	        System.out.println("bpmn2 version: ");
	        String id = ( (Element) process.item(0) ).getAttribute( "id" );
			diagram = new BpmnDiagram(id);
			
			subProcess = document.getElementsByTagName( "bpmn2:subProcess" );
			initSubProcess();
			
			adhocSubProcess = document.getElementsByTagName( "bpmn2:adHocSubProcess" );
			initAdHocSubProcess();
			
	        startEvents = document.getElementsByTagName( "bpmn2:startEvent" );
	        initStartEvents();
	        
			tasks = document.getElementsByTagName( "bpmn2:task" );
			initTasks();
	        
			exclusiveGates = document.getElementsByTagName( "bpmn2:exclusiveGateway" );
			initExclusiveGates();
			
			intermediateEvents = document.getElementsByTagName( "bpmn2:intermediateThrowEvent" );
			initIntermediateEvents();
	        
			endEvents = document.getElementsByTagName( "bpmn2:endEvent" );
			initEndEvents();
	        
			sequenceFlows = document.getElementsByTagName("bpmn2:sequenceFlow");
			initSequenceFlows();
	         
	        
		} catch (ParserConfigurationException e) {
			System.out.println( "You have an error in the configuration of your xml file!" );
			e.printStackTrace();
		} catch (SAXException e) {
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
        			System.out.println( "startEvents: " + element.getAttribute( "id" ) );
        			diagram.addNormalSubProcess( element.getAttribute("id") );
        		}
        		
        	}
        }
	}
	
	private void initAdHocSubProcess() {
        if( adhocSubProcess != null ) {
        	for( int i = 0; i < adhocSubProcess.getLength(); i++ ) {
        		if( adhocSubProcess.item(i).getNodeType() == Node.ELEMENT_NODE ) {
        			Element element = (Element) adhocSubProcess.item(i);
        			System.out.println( "startEvents: " + element.getAttribute( "id" ) );
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
        			System.out.println( "startEvents: " + element.getAttribute( "id" ) );
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
        			System.out.println( "endEvent: " + element.getAttribute( "id" ) );
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
        			System.out.println( "task(" + i + "): " + element.getAttribute( "id" ) );
        			diagram.addTask( element.getAttribute("id") );
        		}
        	}
        }
	}
	
	private void initIntermediateEvents() {
        if( intermediateEvents != null ) {
        	for( int i = 0; i < intermediateEvents.getLength(); i++ ) {
        		if( intermediateEvents.item(i).getNodeType() == Node.ELEMENT_NODE ) {
        			Element element = (Element) intermediateEvents.item(i);
        			System.out.println( "intermed.Events(" + i + "): " + element.getAttribute( "id" ) );
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
        			System.out.println( "exclusiveGateway(" + i + "): " + element.getAttribute( "id" ) );
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
        			String one = element.getAttribute("sourceRef"), two = element.getAttribute("targetRef");
        			System.out.println( "sourceRef: " + one + " targetRef: " + two );
        			diagram.addSequenceFlow( one, two );
        		}
        	}
        }
	}
	
	private void bpmnVersion(Document document, NodeList process) {
		System.out.println("bpmn version: ");
        String id = ( (Element) process.item(0) ).getAttribute( "id" );
		diagram = new BpmnDiagram(id);
		
		subProcess = document.getElementsByTagName( "bpmn:subProcess" );
		initSubProcess();
		
		adhocSubProcess = document.getElementsByTagName( "bpmn:adHocSubProcess" );
		initSubProcess();
		
        startEvents = document.getElementsByTagName( "bpmn:startEvent" );
        initStartEvents();
        
		tasks = document.getElementsByTagName( "bpmn:task" );
		initTasks();
        
		exclusiveGates = document.getElementsByTagName( "bpmn:exclusiveGateway" );
		initExclusiveGates();
		
		intermediateEvents = document.getElementsByTagName( "bpmn:intermediateThrowEvent" );
		initIntermediateEvents();
        
		endEvents = document.getElementsByTagName( "bpmn:endEvent" );
		initEndEvents();
        
		sequenceFlows = document.getElementsByTagName("bpmn:sequenceFlow");
		initSequenceFlows();
	}
	
	
	private void blankVersion(Document document, NodeList process) {
		System.out.println("blank version: ");
        String id = ( (Element) process.item(0) ).getAttribute( "id" );
		diagram = new BpmnDiagram(id);
		
		subProcess = document.getElementsByTagName( "subProcess" );
		initSubProcess();
		
		adhocSubProcess = document.getElementsByTagName( "adHocSubProcess" );
		initSubProcess();
		
        startEvents = document.getElementsByTagName( "startEvent" );
        initStartEvents();
        
		tasks = document.getElementsByTagName( "task" );
		initTasks();
        
		exclusiveGates = document.getElementsByTagName( "exclusiveGateway" );
		initExclusiveGates();
		
		intermediateEvents = document.getElementsByTagName( "intermediateThrowEvent" );
		initIntermediateEvents();
        
		endEvents = document.getElementsByTagName( "endEvent" );
		initEndEvents();
        
		sequenceFlows = document.getElementsByTagName("sequenceFlow");
		initSequenceFlows();
	}
	

}
