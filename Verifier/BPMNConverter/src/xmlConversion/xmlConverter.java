package xmlConversion;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import bpmnStructure.FlowElement;

public class xmlConverter {
	
	private static NodeList startEvents;
	private static NodeList tasks;
	private static NodeList exclusiveGates;
	private static NodeList endEvents;
//	private static NodeList subProcess;
	private static NodeList sequenceFlows;
	
	private static BpmnDiagram diagram;
	
	public static void main( String[] args ) {
		BpmnDiagram diagram = importXML( args[0] );
		exportToText( diagram );
	}
	
	public static BpmnDiagram importXML( String fileName ) {
		try {
			File inputFile = new File( fileName );
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse( inputFile );
			document.getDocumentElement().normalize();
//	        System.out.println("Root element: " + document.getDocumentElement().getNodeName());
	        NodeList process = document.getElementsByTagName( "bpmn:process" );
	        String id = ( (Element) process.item(0) ).getAttribute( "id" );
			diagram = new BpmnDiagram(id);
			
	        startEvents = document.getElementsByTagName( "bpmn:startEvent" );
	        initStartEvents();
	        
			tasks = document.getElementsByTagName( "bpmn:task" );
			initTasks();
	        
			exclusiveGates = document.getElementsByTagName( "bpmn:exclusiveGateway" );
			initExclusiveGates();
	        
			endEvents = document.getElementsByTagName( "bpmn:endEvent" );
			initEndEvents();
			
//			subProcess = document.getElementsByTagName( "bpmn:subProcess" );
//			initSubProcess();
	        
			sequenceFlows = document.getElementsByTagName("bpmn:sequenceFlow");
			initSequenceFlows();
	         
//	        close(); 
	        
		} catch (ParserConfigurationException e) {
			System.out.println( "You have an error in the configuration of your xml file!" );
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return diagram;
	}
	
	private static void initStartEvents() {
        if( startEvents != null ) {
        	for( int i = 0; i < startEvents.getLength(); i++ ) {
        		if( startEvents.item( i ).getNodeType() == Node.ELEMENT_NODE ) {
        			Element element = (Element) startEvents.item( i );
//        			System.out.println( "startEvents: " + element.getAttribute( "id" ) );
        			diagram.addStartEvent( element.getAttribute( "id" ) );
        		}
        		
        	}
        }
	}
	
	private static void initEndEvents() {
        if( endEvents != null ) {
        	for( int i = 0; i < endEvents.getLength(); i++ ) {
        		if( endEvents.item( i ).getNodeType() == Node.ELEMENT_NODE ) {
        			Element element = (Element) endEvents.item( i );
//        			System.out.println( "endEvent: " + element.getAttribute( "id" ) );
        			diagram.addEndEvent( element.getAttribute( "id" ) );
        		}
        	}
        }
	}
	
	private static void initTasks() {
        if( tasks != null ) {
        	for( int i = 0; i < tasks.getLength(); i++ ) {
        		if( tasks.item( i ).getNodeType() == Node.ELEMENT_NODE ) {
        			Element element = (Element) tasks.item( i );
//        			System.out.println( "task(" + i + "): " + element.getAttribute( "id" ) );
        			diagram.addTask( element.getAttribute( "id" ) );
        		}
        	}
        }
	}
	
	private static void initExclusiveGates() {
        if( exclusiveGates != null ) {
        	for( int i = 0; i < exclusiveGates.getLength(); i++ ) {
        		if( exclusiveGates.item( i ).getNodeType() == Node.ELEMENT_NODE ) {
        			Element element = (Element) exclusiveGates.item( i );
//        			System.out.println( "exclusiveGateway(" + i + "): " + element.getAttribute( "id" ) );
        			diagram.addExclusiveGateway( element.getAttribute( "id" ) );
        		}
        	}
        }
	}
	
	private static void initSequenceFlows() {
        if( sequenceFlows != null ) {
        	for( int i = 0; i < sequenceFlows.getLength(); i++ ) {
        		if( sequenceFlows.item( i ).getNodeType() == Node.ELEMENT_NODE ) {
        			Element element = (Element) sequenceFlows.item( i );
//        			System.out.println( "exclusiveGateway(" + i + "): " + element.getAttribute( "id" ) );
        			diagram.addSequenceFlow( element.getAttribute( "sourceRef" ), element.getAttribute( "targetRef" ) );
        		}
        	}
        }
	}
	
	
	public static void exportToText( BpmnDiagram diagram ) {
//		File export = new File( "testDiagrams/test.txt" );
//		try {
//			PrintWriter writer = new PrintWriter( 
//					new BufferedWriter( 
//							new FileWriter( export ) ) );
//			
//			writer.println( "<bpmn:process id=\"" + diagram.name + "\" isExecutable=\"false\">" );
//			
//			ArrayList<FlowElement> elements = diagram.getFlowelements();
//			for( int i = 0; i < elements.size(); i++ ) {
//				FlowElement f = elements.get(i);
//				StringBuilder s = new StringBuilder( f.toString() );
//				System.out.println( s );
//				int k = s.indexOf( "." );
//				s.replace(0, k + 1, "");
//				System.out.println( s );
//				k = s.indexOf( "." );
//				String ss = s.substring( 0, k );
//				System.out.println( ss );
//				writer.println( "<bpmn:" + ss + " id=\"" + f.name + "\" isExecutable=\"false\">" );
//			}
//			
//			writer.close();
//		} catch (IOException e) {
//			System.out.println( "I couldn't write the file out correctly!" );
//			e.printStackTrace();
//		}
//		
		
	}
	
	
}
