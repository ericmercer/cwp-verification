package bpmnStructure;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

import bpmnStructure.activities.Task;
import bpmnStructure.events.BasicEndEvent;
import bpmnStructure.events.BasicStartEvent;
import bpmnStructure.gateways.ExclusiveGateway;
import bpmnStructure.gateways.ParallelGateway;
import promela.templates.promelaTemplate1;
import visitor.ProcessCodeVisitor;

public class BpmnDiagram extends FlowElement {
	// this will be used as the interface into creating BPMN structures

	// TODO: Add method to export structure to BPMN xml format

	TreeMap<String, FlowElement> elements = new TreeMap<String, FlowElement>();
	// TODO: Somehow guarantee the uniqueness of the initial element
	InitialElement firstElement = new InitialElement("InitialElement");

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

	// TODO: Consider how to handler errors if one of the elements does not
	// exist
	public void addSequenceFlow(String idFrom, String idTo) {
		FlowElement f1 = elements.get(idFrom);
		FlowElement f2 = elements.get(idTo);
		f1.addSequenceFlow(f2);
	}

	public BpmnDiagram addSubProcess(String id) {
		BpmnDiagram subProcess = new BpmnDiagram(id);
		addFlowElement(id, subProcess);
		return subProcess;
	}

	// ids must be unique
	public void addStartEvent(String id) {
		addFlowElement(id, new BasicStartEvent(id));

		firstElement.addSequenceFlow(this.getFlowElement(id));
	}

	public void addTask(String id) {
		addFlowElement(id, new Task(id));
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
	// TODO: Implement
	// Add structure to keep track of variable values
	// Will need variable name, type, and initial value
	// probably want String, integer, or boolean types

	// Maybe FlowElements should have an optional reference to a
	// data source?
	public void addDataState() {

	}

	// add a condition to an existing condition flow
	public void setSequenceFlowCondition(String idFrom, String idTo) {

	}

	/* find generic gateways and split into two gateways */
	// public void splitIntoPieces() {
	//
	// ArrayList<FlowElement> itemsToAdd = new ArrayList<FlowElement>();
	// ArrayList<String> keysToRemove = new ArrayList<String>();
	//
	// for (Entry<String, FlowElement> entry : elements.entrySet()) {
	//
	// ArrayList<FlowElement> newElements = entry.getValue().splitIntoPieces();
	// if (newElements != null) {
	// itemsToAdd.addAll(newElements);
	// keysToRemove.add(entry.getKey());
	// }
	// }
	//
	// for (String key : keysToRemove) {
	// elements.remove(key);
	// }
	//
	// for (FlowElement f : itemsToAdd) {
	// elements.put(f.name, f);
	// }
	// }

	public ArrayList<FlowElement> getFlowelements() {

		ArrayList<FlowElement> returnElements = new ArrayList<FlowElement>();
		for (Entry<String, FlowElement> entry : elements.entrySet()) {

			entry.getValue().splitIntoPieces();
			returnElements.add(entry.getValue());
		}
		// returnElements.add(firstElement);

		return returnElements;
	}

}
