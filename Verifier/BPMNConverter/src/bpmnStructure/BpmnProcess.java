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
import bpmnStructure.exceptions.PromelaTypeSizeException;
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

	// TODO: Add method to export structure to BPMN xml format

	private TreeMap<String, FlowElement> elements = new TreeMap<String, FlowElement>();
	private TreeMap<String, TypeDeclaration> processVariables = new TreeMap<String, TypeDeclaration>();

	// TODO: Somehow guarantee the uniqueness of the initial element
	// InitialElement firstElement = new
	// TypeSizePairInitialElement("InitialElement");
	private StartEvent start = null;

	public BpmnProcess(String id) {
		super(id);
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
		NormalSubProcess subProcess = new NormalSubProcess(id);
		addFlowElement(id, subProcess);
		return subProcess;
	}

	// ids must be unique
	public void addStartEvent(String id) {

		setStart(new BasicStartEvent(id));
		addFlowElement(id, getStart());

	}

	public void addStartEvent(String id, String promela) {

		setStart(new BasicStartEvent(id));
		addFlowElement(id, getStart());

	}

	public void addTask(String id) {
		addFlowElement(id, new Task(id, ""));
	}

	public void addTask(String id, String promela) {
		addFlowElement(id, new Task(id, promela));
	}

	public void addScriptTask(String id) {
		addFlowElement(id, new ScriptTask(id, ""));
	}

	public void addScriptTask(String id, String promela) {
		addFlowElement(id, new ScriptTask(id, promela));
	}

	public void addEndEvent(String id) {
		addFlowElement(id, new BasicEndEvent(id));
	}

	public void addEndEvent(String id, String promela) {
		addFlowElement(id, new BasicEndEvent(id));
	}

	public void addExclusiveGateway(String id) {
		addFlowElement(id, new ExclusiveGateway(id));
	}

	public void addParallelGateway(String id) {
		addFlowElement(id, new ParallelGateway(id));
	}

	public void addIntermediateEvent(String id) {
		addFlowElement(id, new IntermediateEvent(id));
	}

	public void addIntermediateEvent(String id, String promela) {
		addFlowElement(id, new IntermediateEvent(id));
	}

	public void addMessageThrowEvent(String id, String dataObjectId) {
		addFlowElement(id, new MessageThrowEvent(id));
		addDataAssociation(id, dataObjectId);
	}

	public void addMessageCatchEvent(String id, String dataObjectId) {
		addFlowElement(id, new MessageCatchEvent(id));
		addDataAssociation(id, dataObjectId);
	}

	public void addDataObject(String name, PromelaType pt, int capacity) throws PromelaTypeSizeException {
		// TODO Auto-generated method stub
		this.processVariables.put(name, new TypeDeclaration(name, pt, capacity));

	}

	public void addMessageStartEvent(String id, String dataObjectId) {
		addFlowElement(id, new MessageStartEvent(id));
		addDataAssociation(id, dataObjectId);
	}

	public void addMessageEndEvent(String id, String dataObjectId) {
		addFlowElement(id, new MessageEndEvent(id));
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
		if (!this.getName().equals(otherElement.getName())) {
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

	public String generateProctype() {

		boolean isNormalStart = true;
		PromelaType messageType = null;
		for (Entry<String, FlowElement> entry : elements.entrySet()) {
			FlowElement f = entry.getValue();
			if (f instanceof MessageStartEvent) {
				isNormalStart = false;
				// TODO: Get message start event
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
		for (Entry<String, FlowElement> entry : elements.entrySet()) {
			FlowElement f = entry.getValue();
			step++;
			for (SequenceFlow sf : f.sequenceFlowIn) {
				proctypeString += sf.getTokenValue();
				if (step < elements.entrySet().size()) {
					proctypeString += ", ";
				} else {
					proctypeString += ";\n";
				}
			}

		}

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
		/* atomic */
		proctypeString += "atomic{\n";
		proctypeString += "do\n";
		for (Entry<String, FlowElement> entry : elements.entrySet()) {
			FlowElement f = entry.getValue();
			String[] options = f.getExecutionOptions();
			for (String option : options) {
				proctypeString += "::" + option;
			}

		}
		proctypeString += "od\n";
		/* end atomic */
		proctypeString += "}\n";
		step = 0;
		proctypeString += "if\n";
		proctypeString += "::(";
		for (Entry<String, FlowElement> entry : elements.entrySet()) {
			FlowElement f = entry.getValue();
			step++;
			for (SequenceFlow sf : f.sequenceFlowIn) {
				proctypeString += sf.getTokenValue() + " == 0 ";

				if (step < elements.entrySet().size()) {
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
		return proctypeString;
		// do
		// :: in_tokens(start) -> /* Task */
		// printf("Task\n")
		// out_tokens(end)
		// :: in_tokens(end) -> /* End Event */
		// printf("End\n")
		// break
		// od
		//
		// /* Check completion: all tokens must be consumed */
		// if
		// :: (start == 0 && end == 0) ->
		// report!normal
		// :: else ->
		// report!abnormal
		// fi
		// od
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
