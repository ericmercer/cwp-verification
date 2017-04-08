package bpmnStructure;

import java.util.ArrayList;

import bpmnStructure.PrintMessages.PrintMessageManager;
import bpmnStructure.dataTypes.TypeDeclaration;
import visitor.Visitor;

public abstract class FlowElement {

	public static int flowElementCount = 0;
	private String name;
	// public int id;
	// public boolean visited = false;
	static int sequenceFlowCount = 1;

	public ArrayList<SequenceFlow> sequenceFlowOut = new ArrayList<SequenceFlow>();
	public ArrayList<SequenceFlow> sequenceFlowIn = new ArrayList<SequenceFlow>();

	private ArrayList<MessageFlow> messageFlows = new ArrayList<MessageFlow>();

	private ArrayList<TypeDeclaration> relatedDataObjects = new ArrayList<TypeDeclaration>();

	public FlowElement(String name) {
		this.setName(name);
	}

	public String getScript() {
		return null;
	}

	/*
	 * if there is a way that an element can be split into less ambiguous
	 * pieces, then do it other wise return
	 */
	public ArrayList<FlowElement> splitIntoPieces() {
		return null;

	}

	/* returns info to execute */
	/* guard -> script; next flows */

	public String[] getExecutionOptions() {
		/* default assumes only one flow in */
		String executionString = "in_tokens(/*def*/" + this.getDefaultTokenInValue() + ") -> ";/* \n */
		executionString += "atomic{\n";
		executionString += "print("+ PrintMessageManager.getInstance().addMessage(this.getName()) + ");\n";
		for (SequenceFlow outFlow : this.sequenceFlowOut) {
			// FlowElement out = outFlow.getEnd();
			executionString += "out_tokens(" + outFlow.getTokenValue() + ")\n";

		}
		executionString += "}\n";
		return new String[] { executionString };

	}

	public String getDefaultTokenInValue() {
		if (this.sequenceFlowIn.size() > 0) {
			return this.sequenceFlowIn.get(0).getTokenValue();
		} else {
			return this.getTokenName();
		}

	}
	
	public String getDefaultTokenOutValue(){
		if (this.sequenceFlowOut.size() > 0) {
			return this.sequenceFlowOut.get(0).getTokenValue();
		} else {
			return this.getTokenName();
		}
	}

	public void addDefaultSequenceFlow(FlowElement f) {
		this.addSequenceFlow(f, "", true);

	}

	public void addSequenceFlow(FlowElement f, boolean isDefault) {
		this.addSequenceFlow(f, "", isDefault);
	}

	public void addSequenceFlow(FlowElement f, String expression, boolean isDefault) {
		SequenceFlow connector = new SequenceFlow(this, f, sequenceFlowCount++, expression, isDefault);
		sequenceFlowOut.add(connector);
		f.sequenceFlowIn.add(connector);

	}

	public void addMessageFlow(MessageFlow mf) {
		getMessageFlows().add(mf);
	}

	public void accept(Visitor v) {
		v.Visit(this);
		// visited = true;
		// System.out.println("flowElement: " + this.name);
		// for (FlowElement f: sequenceFlow){
		// if (!f.visited){
		// f.accept(v);
		// }
		// }

	}

	public void RedirectInboundFlowsTo(FlowElement newElement) {
		// Redirect incoming sequence flows to new element
		for (SequenceFlow f : this.sequenceFlowIn) {
			f.setEnd(newElement);
			newElement.sequenceFlowIn.add(f);
		}

	}

	public void RedirectOutboundFlowsTo(FlowElement newElement) {
		// Redirect outbound sequence flows to new element
		for (SequenceFlow f : this.sequenceFlowOut) {
			f.setStart(newElement);
			newElement.sequenceFlowOut.add(f);
		}

	}

	public FlowElement() {
		// id = flowElementCount++;
	}

	public String getProcessTemplateName() {
		return "FlowElement";
	}

	public boolean equals(Object o) {
		FlowElement otherElement = (FlowElement) o;
		if (!otherElement.getName().equals(this.getName())) {
			System.out.println("this.name: " + this.getName() + " other.name: " + otherElement.getName());
			return false;
		}
		if (otherElement.getClass() != this.getClass()) {
			System.out.println("classes: " + this.getClass() + ", " + otherElement.getClass());
			return false;
		}
		for (SequenceFlow f : this.sequenceFlowOut) {
			boolean found = false;
			for (SequenceFlow f1 : otherElement.sequenceFlowOut) {
				if (f1.getEnd().getName().equals(f.getEnd().getName())) {
					found = true;
					break;
				}
			}
			if (!found) {
				System.out.println("false");
				return false;
			}

		}

		for (SequenceFlow f : this.sequenceFlowIn) {
			boolean found = false;
			for (SequenceFlow f1 : otherElement.sequenceFlowIn) {
				if (f1.getStart().getName().equals(f.getStart().getName())) {
					found = true;
					break;
				}
			}
			if (!found) {
				System.out.println("false");
				return false;
			}

		}

		return true;
	}

	public String getName() {
		return name;
	}

	public String getProcessName() {
		return "process_" + this.getName();
	}

	public String getTokenName() {
		return "token_" + this.getName();
	}

	public String getChannelName() {
		return "channel_" + this.getName();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append("\tname: " + this.name);
		output.append("\n\t\tclass: " + this.getClass());
		output.append("\n\t\tinFLows: " + this.sequenceFlowIn.size());
		output.append("\n\t\toutFlows: " + this.sequenceFlowOut.size());
		return output.toString();
	}

	public ArrayList<MessageFlow> getMessageFlows() {
		return messageFlows;
	}

	public void setMessageFlows(ArrayList<MessageFlow> messageFlows) {
		this.messageFlows = messageFlows;
	}

	public void addAssociatedDataObject(TypeDeclaration td) {
		getRelatedDataObjects().add(td);

	}

	public ArrayList<TypeDeclaration> getRelatedDataObjects() {
		return relatedDataObjects;
	}

	public void setRelatedDataObjects(ArrayList<TypeDeclaration> relatedDataObjects) {
		this.relatedDataObjects = relatedDataObjects;
	}

}
