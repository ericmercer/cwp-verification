package xml.parse;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import mathflow.nodes.MFControlNode;
import mathflow.nodes.MFInfoObjData;
import mathflow.nodes.MFInformationObject;
import mathflow.nodes.MFPerformer;
import mathflow.nodes.MFPerson;
import mathflow.nodes.MFResource;
import mathflow.nodes.MFResourceData;
import mathflow.nodes.MFSystem;
import mathflow.nodes.process.*;
import mathflow.nodes.event.*;
import mathflow.nodes.gate.*;
import mathflow.nodes.task.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import visitor.experimentalPmlVisitor;

public class BpmnParser {
	private ControlFlowStructure ctrlFlow;
	String firstProcessID;
	
	public BpmnParser() {
		ctrlFlow = new ControlFlowStructure();
		firstProcessID = null;
	}
	
	public void run(String[] args) {
	
		try {
			
			File bpmnFile;
			if (args.length > 0) {
				bpmnFile = new File(args[0]);
			} else {
				System.out.println("Please enter a filepath");
				return;
			}
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(bpmnFile);
			
			doc.getDocumentElement().normalize();
			
			iterateDOMTree(doc);
			System.out.println("number of nodes stored= "+ctrlFlow.numberOfNodes());
			//System.out.println("Control Nodes: \n"+ctrlFlow.toString());
			
			experimentalPmlVisitor visitor = new experimentalPmlVisitor();
			if (firstProcessID != null) {
				ctrlFlow.getNode(firstProcessID).accept(visitor);
				System.out.println("********BEGIN FILE HERE:********\n****\n"+visitor.pmlFileToString());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void iterateDOMTree(Document doc) {
		Element docRoot = doc.getDocumentElement();
		
		//find initial process
		NodeList allProcessNodes = docRoot.getElementsByTagName("model:process");
		Element firstProcessElement = (Element) allProcessNodes.item(0);
		firstProcessID = firstProcessElement.getAttribute("id");
		//for test purposes
		//assert(firstProcessElement.getAttribute("name").matches(".*[Pp]age.*1.*"));
		
		//add first process info
		MFFirstProcess firstProcess = new MFFirstProcess(firstProcessElement.getAttribute("id"));
		firstProcess.name = firstProcessElement.getAttribute("name");
		//find start node
		NodeList firstStartNodes = firstProcessElement.getElementsByTagName("model:startEvent");
		assert(firstStartNodes.getLength() == 1);
		Element firstStartElement = (Element) firstStartNodes.item(0);
		firstProcess.start = new MFStart(firstStartElement.getAttribute("id"));
		firstProcess.start.name = firstStartElement.getAttribute("name");
		firstProcess.start.parentProcess = firstProcess;
		ctrlFlow.addNode(firstProcess.start);
		//find end nodes and connect them to the process
		NodeList firstEndNodes = firstProcessElement.getElementsByTagName("model:endEvent");
		for (int i=0; i<firstEndNodes.getLength(); i++) {
			Element firstEndElement = (Element) firstEndNodes.item(i);
			MFEnd curFirstEnd = new MFEnd(firstEndElement.getAttribute("id"));
			curFirstEnd.name = firstEndElement.getAttribute("name");
			curFirstEnd.parentProcess = firstProcess;
			ctrlFlow.addNode(curFirstEnd);
		}
		
		//add person objects
		NodeList firstPersonNodes = firstProcessElement.getElementsByTagName("model:person");
		for (int i=0; i<firstPersonNodes.getLength(); i++) {
			if (firstPersonNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element personElement = (Element) firstPersonNodes.item(i);
				MFPerson newPerson = new MFPerson(personElement.getAttribute("id"));
				newPerson.name = personElement.getAttribute("name");
				ctrlFlow.addNode(newPerson);
				firstProcess.addPerson(newPerson);
			}
		}
		
		//add system objects
		NodeList firstSystemNodes = firstProcessElement.getElementsByTagName("model:dataObject");
		for (int i=0; i<firstSystemNodes.getLength(); i++) {
			if (firstSystemNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element systemElement = (Element) firstSystemNodes.item(i);
				MFSystem newSystem = new MFSystem(systemElement.getAttribute("id"));
				newSystem.name = systemElement.getAttribute("name");
				ctrlFlow.addNode(newSystem);
				firstProcess.addSystem(newSystem);
			}
		}
		
		ctrlFlow.addNode(firstProcess);
		
		String firstNodeID = firstProcess.id;
		
		
		NodeList allElementNodes = docRoot.getElementsByTagName("*");
		
		//store DOM info into MFControlNodes
		//int numProcessNodes = 0;
		//int numTaskNodes = 0;
		//int numSubprocessNodes = 0;
		for (int i=0; i<allElementNodes.getLength(); i++) {
			Element curElement = (Element) allElementNodes.item(i);
			String curElementID = curElement.getAttribute("id");
			String curElementTagName = curElement.getTagName();
			
			//System.out.println("current element id= "+curElementID);
			switch (curElementTagName) {
			
				case "model:process": 
					//numProcessNodes++;
					//id, type, & name are defined for all nodes
					//id & type are minimal info provided by DOM elements
					MFProcess newProcess;
					if (ctrlFlow.getNode(curElementID) == null) {	
						newProcess = new MFProcess(curElementID);
						ctrlFlow.addNode(newProcess);
					}
					if (ctrlFlow.getNode(curElementID) instanceof MFFirstProcess) {
						break;
					}
					newProcess = (MFProcess) ctrlFlow.getNode(curElementID);
					newProcess.name = curElement.getAttribute("name");
					
					//get the start node of the process
					NodeList startNodes = curElement.getElementsByTagName("model:startEvent");
					assert (startNodes.getLength() == 1);
					Element startElement = (Element) startNodes.item(0);
					MFStart newStart;
					newStart = new MFStart(startElement.getAttribute("id"));
					newStart.name = startElement.getAttribute("name");
					newStart.parentProcess = newProcess;
					//add start node
					newProcess.start = newStart;
					ctrlFlow.addNode(newStart);
					
					//get the end nodes of the process
					NodeList endNodes = curElement.getElementsByTagName("model:endEvent");
					for (int j=0; j<endNodes.getLength(); j++) {
						Element curEndElement = (Element) endNodes.item(j);
						MFEnd curEnd = new MFEnd(curEndElement.getAttribute("id"));
						curEnd.name = curEndElement.getAttribute("name");
						curEnd.parentProcess = newProcess;
						ctrlFlow.addNode(curEnd);
					}
					
					//check for systems in the process and add them
					NodeList systemNodes = curElement.getElementsByTagName("model:dataObject");
					if (systemNodes.getLength() != 0) {
						for (int j=0; j<systemNodes.getLength(); j++) {
							if (systemNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
								Element systemElement = (Element) systemNodes.item(j);
								MFSystem curSystem = new MFSystem(systemElement.getAttribute("id"));
								curSystem.name = systemElement.getAttribute("name");
								ctrlFlow.addNode(curSystem);
								newProcess.addSystem(curSystem);
							}
						}
					}
					
					//check for persons in the process and add them
					NodeList personNodes = curElement.getElementsByTagName("model:person");
					if (personNodes.getLength() != 0) {
						for (int j=0; j<personNodes.getLength(); j++) {
							if (personNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
								Element personElement = (Element) personNodes.item(j);
								MFPerson curPerson = new MFPerson(personElement.getAttribute("id"));
								curPerson.name = personElement.getAttribute("name");
								ctrlFlow.addNode(curPerson);
								newProcess.addPerson(curPerson);
							}
						}
					}
					break;
					
				case "model:callActivity":
					//numSubprocessNodes++;
					//id, type, name
					MFSubprocess newSubprocess;
					if (ctrlFlow.getNode(curElementID) == null) {
						newSubprocess = new MFSubprocess(curElementID);
						ctrlFlow.addNode(newSubprocess);
					}
					newSubprocess = (MFSubprocess) ctrlFlow.getNode(curElementID);
					newSubprocess.name = curElement.getAttribute("name");
					
					//get process pointed to by subprocess
					//name is not known by this DOM element
					if (ctrlFlow.getNode(curElement.getAttribute("calledElement")) == null) {
						newProcess = new MFProcess(curElement.getAttribute("calledElement"));
						ctrlFlow.addNode(newProcess);
					}
					newProcess = (MFProcess) ctrlFlow.getNode(curElement.getAttribute("calledElement"));
					newSubprocess.setCalledProcess(newProcess);
					
					//get parent process that contains subprocess
					Element parentElement = (Element) curElement.getParentNode();
					assert(parentElement.getTagName().equals("model:process"));
					if (ctrlFlow.getNode(parentElement.getAttribute("id")) == null) {
						newSubprocess.parentProcess = new MFProcess(parentElement.getAttribute("id"));
						newSubprocess.parentProcess.name = parentElement.getAttribute("name");
						ctrlFlow.addNode(newSubprocess.parentProcess);
					}
					newSubprocess.parentProcess = (MFProcess) ctrlFlow.getNode(parentElement.getAttribute("id"));
					
					break;
					
				case "model:manualTask":
					//numTaskNodes++;
					MFManualTask newManualTask;
					if (ctrlFlow.getNode(curElementID) == null) {
						newManualTask = new MFManualTask(curElementID);
						ctrlFlow.addNode(newManualTask);
					}
					newManualTask = (MFManualTask) ctrlFlow.getNode(curElementID);
					newManualTask.name = curElement.getAttribute("name");
					
					//get parent process
					parentElement = (Element) curElement.getParentNode();
					assert(parentElement.getTagName().equals("model:process"));
					if (ctrlFlow.getNode(parentElement.getAttribute("id")) == null) {
						newManualTask.parentProcess = new MFProcess(parentElement.getAttribute("id"));
						newManualTask.parentProcess.name = parentElement.getAttribute("name");
						ctrlFlow.addNode(newManualTask.parentProcess);
					}
					newManualTask.parentProcess = (MFProcess) ctrlFlow.getNode(parentElement.getAttribute("id"));
					break;
					
				case "model:userTask":
					//numTaskNodes++;
					MFUserTask newUserTask;
					if (ctrlFlow.getNode(curElementID) == null) {
						newUserTask = new MFUserTask(curElementID);
						ctrlFlow.addNode(newUserTask);
					}
					newUserTask = (MFUserTask) ctrlFlow.getNode(curElementID);
					newUserTask.name = curElement.getAttribute("name");
					
					//get performer data (if present) and store in TaskData
					if (curElement.getElementsByTagName("model:performer").getLength() != 0) {
						Element performerElement = (Element) curElement.getElementsByTagName("model:performer").item(0);
						NodeList resourceNodes = performerElement.getElementsByTagName("model:resourceRef");
						for (int j=0; j<resourceNodes.getLength(); j++) {
							if (resourceNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
								Element resourceElement = (Element) resourceNodes.item(j);
								MFPerformer newPerformer;
								if (ctrlFlow.getNode(resourceElement.getTextContent()) == null) {
									newPerformer = new MFPerformer(resourceElement.getTextContent());
									ctrlFlow.addNode(newPerformer);
								}
								newPerformer = (MFPerformer) ctrlFlow.getNode(resourceElement.getTextContent());
								newUserTask.data.addPerformer(newPerformer);
							}
						}
					}
					
					//get parent process
					parentElement = (Element) curElement.getParentNode();
					assert(parentElement.getTagName().equals("model:process"));
					if (ctrlFlow.getNode(parentElement.getAttribute("id")) == null) {
						newUserTask.parentProcess = new MFProcess(parentElement.getAttribute("id"));
						newUserTask.parentProcess.name = parentElement.getAttribute("name");
						ctrlFlow.addNode(newUserTask.parentProcess);
					}
					newUserTask.parentProcess = (MFProcess) ctrlFlow.getNode(parentElement.getAttribute("id"));
					break;
					
				case "model:serviceTask":
					//numTaskNodes++;
					MFServiceTask newServiceTask;
					if (ctrlFlow.getNode(curElementID) == null) {
						newServiceTask = new MFServiceTask(curElementID);
						ctrlFlow.addNode(newServiceTask);
					}
					newServiceTask = (MFServiceTask) ctrlFlow.getNode(curElementID);
					newServiceTask.name = curElement.getAttribute("name");
					
					//get parent process
					parentElement = (Element) curElement.getParentNode();
					assert(parentElement.getTagName().equals("model:process"));
					if (ctrlFlow.getNode(parentElement.getAttribute("id")) == null) {
						newServiceTask.parentProcess = new MFProcess(parentElement.getAttribute("id"));

						newServiceTask.parentProcess.name = parentElement.getAttribute("name");
						ctrlFlow.addNode(newServiceTask.parentProcess);
					}
					newServiceTask.parentProcess = (MFProcess) ctrlFlow.getNode(parentElement.getAttribute("id"));
					break;
					
				case "model:exclusiveGateway":
					
					MFXorGate newXorGate;
					if (ctrlFlow.getNode(curElementID) == null) {
						newXorGate = new MFXorGate(curElementID);
						ctrlFlow.addNode(newXorGate);
					}
					newXorGate = (MFXorGate) ctrlFlow.getNode(curElementID);
					newXorGate.name = curElement.getAttribute("name");
					
					//get parent process
					parentElement = (Element) curElement.getParentNode();
					assert(parentElement.getTagName().equals("model:process"));
					if (ctrlFlow.getNode(parentElement.getAttribute("id")) == null) {
						newXorGate.parentProcess = new MFProcess(parentElement.getAttribute("id"));
						newXorGate.parentProcess.name = parentElement.getAttribute("name");
						ctrlFlow.addNode(newXorGate.parentProcess);
					}
					newXorGate.parentProcess = (MFProcess) ctrlFlow.getNode(parentElement.getAttribute("id"));
					break;
					
				case "model:inclusiveGateway":
					
					MFOrGate newOrGate;
					if (ctrlFlow.getNode(curElementID) == null) {
						newOrGate = new MFOrGate(curElementID);
						ctrlFlow.addNode(newOrGate);
					}
					newOrGate = (MFOrGate) ctrlFlow.getNode(curElementID);
					newOrGate.name = curElement.getAttribute("name");
					
					//get parent process
					parentElement = (Element) curElement.getParentNode();
					assert(parentElement.getTagName().equals("model:process"));
					if (ctrlFlow.getNode(parentElement.getAttribute("id")) == null) {
						newOrGate.parentProcess = new MFProcess(parentElement.getAttribute("id"));
						newOrGate.parentProcess.name = parentElement.getAttribute("name");
						ctrlFlow.addNode(newOrGate.parentProcess);
					}
					newOrGate.parentProcess = (MFProcess) ctrlFlow.getNode(parentElement.getAttribute("id"));
					break;
					
				case "model:parallelGateway":
					
					MFAndGate newAndGate;
					if (ctrlFlow.getNode(curElementID) == null) {
						newAndGate = new MFAndGate(curElementID);
						ctrlFlow.addNode(newAndGate);
					}
					newAndGate = (MFAndGate) ctrlFlow.getNode(curElementID);
					newAndGate.name = curElement.getAttribute("name");
					
					//get parent process
					parentElement = (Element) curElement.getParentNode();
					assert(parentElement.getTagName().equals("model:process"));
					if (ctrlFlow.getNode(parentElement.getAttribute("id")) == null) {
						newAndGate.parentProcess = new MFProcess(parentElement.getAttribute("id"));
						newAndGate.parentProcess.name = parentElement.getAttribute("name");
						ctrlFlow.addNode(newAndGate.parentProcess);
					}
					newAndGate.parentProcess = (MFProcess) ctrlFlow.getNode(parentElement.getAttribute("id"));
					break;
					
				case "model:intermediateCatchEvent":
					
					MFIntermediateTimer newTimer;
					if (ctrlFlow.getNode(curElementID) == null) {
						newTimer = new MFIntermediateTimer(curElementID);
						ctrlFlow.addNode(newTimer);
					}
					newTimer = (MFIntermediateTimer) ctrlFlow.getNode(curElementID);
					newTimer.name = curElement.getAttribute("name");
					
					//get time duration info
					NodeList timeDurationNodes = curElement.getElementsByTagName("model:timeDuration");
					assert(timeDurationNodes.getLength() == 1);
					Element timeDurationElement = (Element) timeDurationNodes.item(0);
					MFTimerData newTimerData = newTimer.getData();
					newTimerData.timeDuration = timeDurationElement.getTextContent();
					
					//get parent process
					parentElement = (Element) curElement.getParentNode();
					assert(parentElement.getTagName().equals("model:process"));
					if (ctrlFlow.getNode(parentElement.getAttribute("id")) == null) {
						newTimer.parentProcess = new MFProcess(parentElement.getAttribute("id"));
						newTimer.parentProcess.name = parentElement.getAttribute("name");
						ctrlFlow.addNode(newTimer.parentProcess);
					}
					newTimer.parentProcess = (MFProcess) ctrlFlow.getNode(parentElement.getAttribute("id"));
					break;	
					
				case "model:resource":
					
					MFPerformer newPerformer;
					if (ctrlFlow.getNode(curElementID) == null) {
						newPerformer = new MFPerformer(curElementID);
						ctrlFlow.addNode(newPerformer);
					}
					newPerformer = (MFPerformer) ctrlFlow.getNode(curElementID);
					firstProcess = (MFFirstProcess) ctrlFlow.getNode(firstNodeID);
					newPerformer.name = curElement.getAttribute("name");
					firstProcess.addPerformer(newPerformer);
					
					break;
			}	
		}
		//System.out.println("number of process nodes= "+numProcessNodes);
		//System.out.println("number of task nodes= "+numTaskNodes);
		//System.out.println("number of subprocess nodes= "+numSubprocessNodes);
		
		//establish edges between nodes and add data from simInfo DOM elements
		//this is easier once types have been established for each node
		for (int i=0; i<allElementNodes.getLength(); i++) {
			Element curElement = (Element) allElementNodes.item(i);
			String sourceID;
			String targetID;
			String nextCondition;
			String referenceID;
			//System.out.println("current element id= "+curElement.getAttribute("id"));
			
			//flow arrow connections: nextIDs and nextConditions in node
			if (curElement.getTagName().equals("model:sequenceFlow")) {
				sourceID = curElement.getAttribute("sourceRef");
				targetID = curElement.getAttribute("targetRef");
				nextCondition = curElement.getAttribute("name");
				
				MFControlNode sourceNode = ctrlFlow.getNode(sourceID);
				MFControlNode targetNode = ctrlFlow.getNode(targetID);
				
				if (sourceNode instanceof MFUserTask) {
					
					MFUserTask sourceTask = (MFUserTask) sourceNode;
					sourceTask.addNextNode(targetNode);	
				} 
				else if (sourceNode instanceof MFManualTask) {
					
					MFManualTask sourceTask = (MFManualTask) sourceNode;
					sourceTask.addNextNode(targetNode);	
				} 
				else if (sourceNode instanceof MFServiceTask) {
					
					MFServiceTask sourceTask = (MFServiceTask) sourceNode;
					sourceTask.addNextNode(targetNode);	
				} 
				else if (sourceNode instanceof MFAndGate) {
					
					MFAndGate sourceGate = (MFAndGate) sourceNode;
					sourceGate.addEdge(targetNode, nextCondition);
				} 
				else if (sourceNode instanceof MFOrGate) {
					
					MFOrGate sourceGate = (MFOrGate) sourceNode;
					sourceGate.addEdge(targetNode, nextCondition);
				} 
				else if (sourceNode instanceof MFXorGate) {
					
					MFXorGate sourceGate = (MFXorGate) sourceNode;
					sourceGate.addEdge(targetNode, nextCondition);
				} 
				else if (sourceNode instanceof MFProcess) {
					assert(false);  //should never happen
				} 
				else if (sourceNode instanceof MFSubprocess) {
					
					MFSubprocess sourceProcess = (MFSubprocess) sourceNode;
					sourceProcess.addNextNode(targetNode);
				} 
				else if (sourceNode instanceof MFStart) {
					
					MFStart sourceStart = (MFStart) sourceNode;
					sourceStart.addNextNode(targetNode);
				} 
				else if (sourceNode instanceof MFEnd) {
					assert(false);  //should never happen
				} 
				else if (sourceNode instanceof MFIntermediateTimer) {
					
					MFIntermediateTimer sourceTimer = (MFIntermediateTimer) sourceNode;
					sourceTimer.addNextNode(targetNode);
				}
			} 
			//simulation info for each control element: simInfo element in node
			else if (curElement.getTagName().equals("sim:info")) {
				//sim info for flows include probabilities and other info we don't need for Promela
				if (curElement.getAttribute("id").matches(".*_[Ff]low.?.*")) {
					break;
				}
				referenceID = curElement.getAttribute("ref");
				MFControlNode referenceNode = ctrlFlow.getNode(referenceID);
				
				if (referenceNode instanceof MFIntermediateTimer) {
					
					MFIntermediateTimer curTimer = (MFIntermediateTimer) referenceNode;
					MFTimerData timerData = curTimer.getData();
					
					//timerData.delayType = "";
					//timerData.constantDelayTime = "";
					//timerData.distributionType = "";
					//timerData.distributionMin = "";
					//timerData.distributionMean = "";
					//timerData.distributionMax = "";
					//timerData.performers = null;
					//timerData.inputNames = null;
					//timerData.inputAttrs = null;
					//timerData.inputValues = null;
					//timerData.outputNames = null;
					//timerData.outputAttrs = null;
					//timerData.outputValues = null;
				} 
				else if (referenceNode instanceof MFManualTask ||
						 referenceNode instanceof MFUserTask ||
						 referenceNode instanceof MFServiceTask) {
					
					MFTask curTask = (MFTask) referenceNode;
					MFTaskData taskData = curTask.data;
					
					taskData.delay = curElement.getAttribute("delay");
					taskData.fixedCost = curElement.getAttribute("cost");
					//taskData.value = "";
					//taskData.priority = "";
					taskData.inputNames = curElement.getAttribute("inputNames").split("\\s*;\\s*");
					taskData.inputAttrs = curElement.getAttribute("inputAttrs").split("\\s*;\\s*");
					taskData.inputValues = curElement.getAttribute("inputValues").split("\\s*;\\s*");
					taskData.outputNames = curElement.getAttribute("outputNames").split("\\s*;\\s*");
					taskData.outputAttrs = curElement.getAttribute("outputAttrs").split("\\s*;\\s*");
					taskData.outputValues = curElement.getAttribute("outputValues").split("\\s*;\\s*");
					//taskData.earliestStartTime = "";
					//taskData.latestStartTime = "";
					//taskData.useDistributionFormula = false;
					//taskData.useTimeValue = false;
					taskData.duration = curElement.getAttribute("duration");
					//taskData.timeUnit = "";
					//taskData.distributionType = "";
					//taskData.distributionMin = 0;
					//taskData.distributionMean = 0;
					//taskData.distributionMax = 0;
					//taskData.performers = null;
					//taskData.urlDescriptions = null;
					//taskData.urlAddrs = null;
				}
				else if (referenceNode instanceof MFPerformer) {
					
					MFResource curResource = (MFResource) referenceNode;
					MFResourceData rData = curResource.data;
					
					rData.name = referenceNode.name;
					rData.capacity = Double.parseDouble(curElement.getAttribute("capacity"));
					rData.capacityType = "Fixed Capacity";
					rData.assocClassName = "none";
					rData.costPerHourWhenBusy = Double.parseDouble(curElement.getAttribute("costPerHourBusy"));
					rData.costPerUse = Double.parseDouble(curElement.getAttribute("costPerUse"));
					rData.costPerHourWhenIdle = Double.parseDouble(curElement.getAttribute("costPerHourIdle"));
				}
				else if (referenceNode instanceof MFPerson ||
						 referenceNode instanceof MFSystem) {
					
					MFInformationObject curObj = (MFInformationObject) referenceNode;
					MFInfoObjData objData = curObj.data;
					
					objData.name = referenceNode.name;
					//objData.type = "something known only to the user";
					objData.state = curElement.getAttribute("state");
					objData.description = "";  //probably not needed?
					objData.attrNames = curElement.getAttribute("infoAttrs").split("\\s*;\\s*");
					objData.attrValues = curElement.getAttribute("infoValues").split("\\s*;\\s*");
				}
			}
		}
		System.out.println("total nodes= "+allElementNodes.getLength());
	}
}
