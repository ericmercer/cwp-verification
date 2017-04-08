package bpmnStructure;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

import bpmnStructure.activities.ScriptTask;
import bpmnStructure.activities.Task;
import bpmnStructure.dataTypes.BoolType;
import bpmnStructure.dataTypes.PromelaType;
import bpmnStructure.dataTypes.PromelaTypeDef;

import bpmnStructure.dataTypes.TypeDeclaration;
import bpmnStructure.events.BasicEndEvent;
import bpmnStructure.events.BasicStartEvent;
import bpmnStructure.events.IntermediateEvent;
import bpmnStructure.events.MessageEndEvent;
import bpmnStructure.events.MessageStartEvent;
import bpmnStructure.events.StartEvent;
import bpmnStructure.gateways.ExclusiveGateway;
import bpmnStructure.gateways.ParallelGateway;
import bpmnStructure.messages.MessageCatchEvent;
import bpmnStructure.messages.MessageThrowEvent;
import bpmnStructure.subProcesses.NormalSubProcess;
import bpmnStructure.subProcesses.SubProcess;

public class BpmnProcess extends FlowElement {
	// this will be used as the interface into creating BPMN structures

	// Assumptions for now:
	// -Only one start
	// -Gates only split into two and converge from two directions

	private TreeMap<String, FlowElement> elements = new TreeMap<String, FlowElement>();
	private TreeMap<String, TypeDeclaration> processVariables = new TreeMap<String, TypeDeclaration>();

	private StartEvent start = null;

	public BpmnProcess(String id, String elementName) {
		super(id, elementName);
	}

	public boolean isMessageInitiated() {
		for (Entry<String, FlowElement> entry : elements.entrySet()) {
			FlowElement f = entry.getValue();
			if (f instanceof MessageStartEvent) {
				return true;

			}
		}
		return false;
	}

	private void addFlowElement(String id, FlowElement f) {
		if (!elements.containsKey(id)) {
			elements.put(id, f);
		} else {
			System.err.println("Duplicate Flow Element: " + id);
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
		/*
		 * there is an implicit true guard on normal flows - it will not always
		 * be needed in the promela though
		 */
		this.addSequenceFlow(idFrom, idTo, "true", false);
	}

	public void addDefaultSequenceFlow(String idFrom, String idTo) {
		this.addSequenceFlow(idFrom, idTo, "", true);
	}

	public void addSequenceFlow(String idFrom, String idTo, String expression) {
		this.addSequenceFlow(idFrom, idTo, expression, false);
	}

	public void addSequenceFlow(String idFrom, String idTo, String expression, boolean isDefault) {
		FlowElement f1 = elements.get(idFrom);
		FlowElement f2 = elements.get(idTo);
		f1.addSequenceFlow(f2, expression, isDefault);
	}

	public BpmnProcess addNormalSubProcess(String id) {
		return addNormalSubProcess(id, id);
	}

	public BpmnProcess addNormalSubProcess(String id, String name) {
		NormalSubProcess subProcess = new NormalSubProcess(id, id);
		addFlowElement(id, subProcess);
		return subProcess;
	}

	// ids must be unique
	public void addStartEvent(String id) {
		addStartEvent(id, id);
	}

	public void addStartEvent(String id, String name) {
		setStart(new BasicStartEvent(id, name));
		addFlowElement(id, getStart());
	}

	public void addTask(String id) {
		addFlowElement(id, new Task(id, id));
	}

	public void addTask(String id, String name) {
		addFlowElement(id, new Task(id, name));
	}

	public void addScriptTask(String id, String promela) {
		addScriptTask(id, promela, id);
	}

	public void addScriptTask(String id, String promela, String name) {
		addFlowElement(id, new ScriptTask(id, name, promela));
	}

	public void addEndEvent(String id) {
		addEndEvent(id, id);
	}

	public void addEndEvent(String id, String name) {
		addFlowElement(id, new BasicEndEvent(id, name));
	}

	public void addExclusiveGateway(String id) {
		addExclusiveGateway(id, id);
	}

	public void addExclusiveGateway(String id, String name) {
		addFlowElement(id, new ExclusiveGateway(id, name));
	}

	public void addParallelGateway(String id) {
		addParallelGateway(id, id);
	}

	public void addParallelGateway(String id, String name) {
		addFlowElement(id, new ParallelGateway(id, name));
	}

	public void addIntermediateEvent(String id) {
		addIntermediateEvent(id, id);
	}

	public void addIntermediateEvent(String id, String name) {
		addFlowElement(id, new IntermediateEvent(id, name));
	}

	public void addMessageThrowEvent(String id, String dataObjectId) {
		addMessageThrowEvent(id, id, dataObjectId);
	}

	public void addMessageThrowEvent(String id, String name, String dataObjectId) {
		addFlowElement(id, new MessageThrowEvent(id, name));
		addDataAssociation(id, dataObjectId);
	}

	public void addMessageCatchEvent(String id, String dataObjectId) {
		addMessageCatchEvent(id, id, dataObjectId);
	}

	public void addMessageCatchEvent(String id, String name, String dataObjectId) {
		addFlowElement(id, new MessageCatchEvent(id, name));
		addDataAssociation(id, dataObjectId);
	}

	public void addDataObject(String name, PromelaType pt, int capacity) {
		this.processVariables.put(name, new TypeDeclaration(name, pt, capacity));
	}

	public void addMessageStartEvent(String id, String dataObjectId) {
		addMessageStartEvent(id, id, dataObjectId);
	}

	public void addMessageStartEvent(String id, String name, String dataObjectId) {
		addFlowElement(id, new MessageStartEvent(id, name));
		addDataAssociation(id, dataObjectId);
	}

	public void addMessageEndEvent(String id, String dataObjectId) {
		addMessageEndEvent(id, id, dataObjectId);
	}

	public void addMessageEndEvent(String id, String name, String dataObjectId) {
		addFlowElement(id, new MessageEndEvent(id, name));
		addDataAssociation(id, dataObjectId);
	}

	public void addDataAssociation(String flowElementId, String dataObjectId) {
		TypeDeclaration td = processVariables.get(dataObjectId);
		if (td == null) {
			System.err.println("no data object foudnd for: " + dataObjectId);
		}
		elements.get(flowElementId).addAssociatedDataObject(td);
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
		BpmnProcess otherElement = (BpmnProcess) o;
		if (!this.getElementId().equals(otherElement.getElementId())) {
			return false;
		}
		for (Entry<String, FlowElement> entry : elements.entrySet()) {
			FlowElement currentObject = entry.getValue();
			FlowElement otherObject = otherElement.elements.get(entry.getKey());
			if (otherObject == null || !currentObject.equals(otherObject)) {
				System.out.println("this.object: " + currentObject + "\nother.object: " + otherObject);
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

	// TODO: Future Work - Improve this, possibly move outside of method
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
			elements.put(f.getElementId(), f);
		}
	}

	public StartEvent getStart() {
		return start;
	}

	public void setStart(StartEvent start) {
		this.start = start;
	}

	public String generateProctype() {

		boolean isNormalStart = true;
		PromelaType messageType = null;
		for (Entry<String, FlowElement> entry : elements.entrySet()) {
			FlowElement f = entry.getValue();
			if (f instanceof MessageStartEvent) {
				isNormalStart = false;

				MessageStartEvent mst = (MessageStartEvent) f;
				messageType = mst.getRelatedDataObjects().get(0).getType();
			}
		}

		String proctypeString = "proctype " + this.getProcessName() + "(" + "byte " + TokenId.getName() + ";"
				+ " chan reportChannel";
		if (!isNormalStart) {
			proctypeString += "; " + messageType.getTypeName() + " message";
		}
		proctypeString += ")" + "{\n";

		proctypeString += "byte ";
		int step = 0;
		ArrayList<FlowElement> declarationElements = getAllElementsWitInboundSequenceFlow();
		for (FlowElement f : declarationElements) {

			step++;

			for (SequenceFlow sf : f.sequenceFlowIn) {
				proctypeString += sf.getTokenValue();
				if (step < declarationElements.size()) {
					proctypeString += ", ";
				} 
			}

		}
		proctypeString += ";\n";
		
		for (Entry<String, FlowElement> entry : elements.entrySet()) {
			FlowElement f = entry.getValue();
			if (f instanceof SubProcess) {
				proctypeString += "chan " + f.getChannelName() + "= [1] of {mtype}" + ";\n";

			}
		}

		for (Entry<String, TypeDeclaration> entry : processVariables.entrySet()) {
			proctypeString += entry.getValue().generateDeclaration() + ";\n";
		}

		for (Entry<String, FlowElement> entry : elements.entrySet()) {
			FlowElement f = entry.getValue();
			if (f instanceof StartEvent) {

				proctypeString += "byte " + f.getDefaultTokenInValue() + " = 1;\n";

			}

		}

		proctypeString += "/*process transition do loop*/\n";
		// TODO: Possibly make sure there are more than zero transitions

		proctypeString += "do\n";
		for (Entry<String, FlowElement> entry : elements.entrySet()) {
			FlowElement f = entry.getValue();
			String[] options = f.getExecutionOptions();
			for (String option : options) {
				proctypeString += "::" + option;
			}

		}
		proctypeString += "od\n";

		step = 0;
		proctypeString += "atomic{\n";
		proctypeString += "if\n";
		proctypeString += "::(";
		for (FlowElement f : declarationElements) {
			
			step++;
			for (SequenceFlow sf : f.sequenceFlowIn) {
				proctypeString += sf.getTokenValue() + " == 0 ";

				if (step < declarationElements.size()) {
					proctypeString += "&& ";
				}
			}

		}
		proctypeString += ")->\n";
		proctypeString += "reportChannel!normal\n";
		proctypeString += "::else->\n";
		for (Entry<String, FlowElement> entry : elements.entrySet()) {
			FlowElement f = entry.getValue();
			for (SequenceFlow sf : f.sequenceFlowIn) {
				proctypeString += "printf(\"" + sf.getTokenValue() + " %d\\n\", " + sf.getTokenValue() + ");\n";
			}
		}
		proctypeString += "reportChannel!abnormal\n";
		proctypeString += "fi\n";

		proctypeString += "}\n";
		proctypeString += "}\n";
		return proctypeString;

	}

	public ArrayList<FlowElement> getAllElementsWitInboundSequenceFlow() {
		ArrayList<FlowElement> inElements = new ArrayList<FlowElement>();
		for (Entry<String, FlowElement> entry : elements.entrySet()) {

			FlowElement f = entry.getValue();
			if (f.sequenceFlowIn.size() > 0) {
				inElements.add(f);
			}
		}
		return inElements;
	}

	public ArrayList<SubProcess> getAllSubProcesses() {
		ArrayList<SubProcess> subProcesses = new ArrayList<SubProcess>();

		for (Entry<String, FlowElement> entry : elements.entrySet()) {
			FlowElement f = entry.getValue();
			if (f instanceof SubProcess) {
				SubProcess s = (SubProcess) f;
				subProcesses.add(s);
				subProcesses.addAll(s.getAllSubProcesses());
			}

		}
		return subProcesses;
	}

}
