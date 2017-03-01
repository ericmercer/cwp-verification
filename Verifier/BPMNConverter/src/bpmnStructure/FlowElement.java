package bpmnStructure;

import java.util.ArrayList;
import visitor.Visitor;

public class FlowElement {

	public static int flowElementCount = 0;
	private String name;
	// public int id;
	// public boolean visited = false;
	static int sequenceFlowCount = 1;

	public ArrayList<SequenceFlow> sequenceFlowOut = new ArrayList<SequenceFlow>();
	public ArrayList<SequenceFlow> sequenceFlowIn = new ArrayList<SequenceFlow>();

	public FlowElement(String name) {
		this.setName(name);
	}

	/*
	 * if there is a way that an element can be split into less ambiguous
	 * pieces, then do it other wise return
	 */
	public ArrayList<FlowElement> splitIntoPieces() {
		return null;

	}

	public void addDefaultSequenceFlow(FlowElement f) {
		this.addSequenceFlow(f, "true");

	}

	public void addSequenceFlow(FlowElement f, String expression) {
		SequenceFlow connector = new SequenceFlow(this, f, sequenceFlowCount++, expression);
		sequenceFlowOut.add(connector);
		f.sequenceFlowIn.add(connector);

	}

	public void addMessageFlow(FlowElement f) {
		// TODO: implement
	}

	public void addAssociation(FlowElement f) {
		// TODO: implement
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

	public void setName(String name) {
		this.name = name;
	}

}
