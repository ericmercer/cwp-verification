package bpmnStructure;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

import bpmnStructure.activities.ScriptTask;
import bpmnStructure.activities.Task;
import bpmnStructure.events.BasicEndEvent;
import bpmnStructure.events.BasicStartEvent;
import bpmnStructure.events.IntermediateEvent;
import bpmnStructure.events.StartEvent;
import bpmnStructure.gateways.ExclusiveGateway;
import bpmnStructure.gateways.ParallelGateway;
import bpmnStructure.subProcesses.NormalSubProcess;

public class BpmnDiagram extends FlowElement {
	// this will be used as the interface into creating BPMN structures

	// Assumptions for now:
	// -Only one start
	// -Gates only split into two and converge from two directions

	// TODO: Add method to export structure to BPMN xml format

	private TreeMap<String, FlowElement> elements = new TreeMap<String, FlowElement>();
	// TODO: Somehow guarantee the uniqueness of the initial element
	// InitialElement firstElement = new InitialElement("InitialElement");
	private StartEvent start = null;

	public BpmnDiagram(String id) {
		super(id);
	}

	private void addFlowElement(String id, FlowElement f) {
		if (!elements.containsKey(id)) {
			elements.put(id, f);
		} else {
			// TODO: throw error
		}
	}

	public FlowElement getFlowElement(String id) {
		return elements.get(id);
	}

	/************************************************************************************/
	/*
	 * Methods to generally be used in XML import
	 *****************************************/

	// TODO: Consider how to handler errors if one of the elements does not
	// exist
	public void addSequenceFlow(String idFrom, String idTo) {
		FlowElement f1 = elements.get(idFrom);
		FlowElement f2 = elements.get(idTo);
		f1.addSequenceFlow(f2);
	}

	public BpmnDiagram addNormalSubProcess(String id) {
		NormalSubProcess subProcess = new NormalSubProcess(id);
		addFlowElement(id, subProcess);
		return subProcess;
	}

	// ids must be unique
	public void addStartEvent(String id) {

		setStart(new BasicStartEvent(id));
		addFlowElement(id, getStart());

	}

	public void addTask(String id, ArrayList<String> promelaCode) {
		addFlowElement(id, new Task(id));
	}
	
	public void addScriptTask(String id, ArrayList<String> promelaCode) {
		addFlowElement(id, new ScriptTask(id));
	}

	public void addEndEvent(String id) {
		addFlowElement(id, new BasicEndEvent(id));
	}

	public void addExclusiveGateway(String id) {
		addFlowElement(id, new ExclusiveGateway(id));
	}

	public void addParallelGateway(String id) {
		addFlowElement(id, new ParallelGateway(id));
	}
	
	public void addIntermediateEvent(String id, ArrayList<String> promelaCode) {
		addFlowElement(id, new IntermediateEvent(id));
	}
	
	/**
	 * 
	 * @param id
	 * @param dataAtt this is a list of all of the attributes required: (name, type)
	 */
	public void addDataObject(String id, ArrayList<String> dataAtt) {
		
	}
	
	/**
	 * 
	 * @param id
	 * @param dataAttr dataAtt this is a list of all of the attributes required: (name, capacity, type)
	 */
	public void addDataStore(String id, ArrayList<String> dataAttr) {
		
	}

	/**** End of XML interface methods **************/

	/************************************************************************************/
	/**
	 * This may be useful in testing to determine if two BPMN Diagrams are equal
	 * to one another. At this point the equals method has not been validated
	 * yet.
	 */
	public boolean equals(Object o) {
		// return elements.equals(o);
		BpmnDiagram otherElement = (BpmnDiagram) o;
		if (!this.getName().equals(otherElement.getName())) {
			return false;
		}
		for (Entry<String, FlowElement> entry : elements.entrySet()) {
			FlowElement currentObject = entry.getValue();
			FlowElement otherObject = otherElement.elements.get(entry.getKey());
			if (otherObject == null || !currentObject.equals(otherObject)) {
				return false;
			}
		}
		return true;
	}

	/********************
	 * Works in progress - may change frequently
	 *************************/
	public ArrayList<FlowElement> getFlowelements() {

		ArrayList<FlowElement> returnElements = new ArrayList<FlowElement>();
		for (Entry<String, FlowElement> entry : elements.entrySet()) {

			entry.getValue().splitIntoPieces();
			returnElements.add(entry.getValue());
		}
		// returnElements.add(firstElement);

		return returnElements;
	}



	// add a condition to an existing condition flow
	public void setSequenceFlowCondition(String idFrom, String idTo) {

	}

	// TODO: Improve this, possibly move outside of method
	// Also this will not reach to subprocesses currently
	/* find generic gateways and split into two gateways */
	public void unambiguate() {

		ArrayList<FlowElement> itemsToAdd = new ArrayList<FlowElement>();
		ArrayList<String> keysToRemove = new ArrayList<String>();

		for (Entry<String, FlowElement> entry : elements.entrySet()) {

			ArrayList<FlowElement> newElements = entry.getValue().splitIntoPieces();
			if (newElements != null) {
				itemsToAdd.addAll(newElements);
				keysToRemove.add(entry.getKey());
			}
		}

		for (String key : keysToRemove) {
			elements.remove(key);
		}

		for (FlowElement f : itemsToAdd) {
			elements.put(f.getName(), f);
		}
	}

	public StartEvent getStart() {
		return start;
	}

	public void setStart(StartEvent start) {
		this.start = start;
	}
}
