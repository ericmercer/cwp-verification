package bpmnStructure;

import java.util.ArrayList;
import visitor.Visitor;

public class FlowElement {

	public static int flowElementCount = 0;
	public String name;
	public int id;
	public boolean visited = false;
	static int sequenceFlowCount = 1;
	
	public ArrayList<SequenceFlow> sequenceFlowOut = new ArrayList<SequenceFlow>();
	public ArrayList<SequenceFlow> sequenceFlowIn = new ArrayList<SequenceFlow>();

	public FlowElement(String name) {
		this.name = name;
	}

	/*
	 * if there is a way that an element can be split into less ambiguous
	 * pieces, then do it other wise return
	 */
	public ArrayList<FlowElement> splitIntoPieces() {
		return null;

	}

	public void addSequenceFlow(FlowElement f) {
		SequenceFlow connector = new SequenceFlow(this, f,sequenceFlowCount++);
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
			f.end = newElement;
			newElement.sequenceFlowIn.add(f);
		}

	}

	public void RedirectOutboundFlowsTo(FlowElement newElement) {
		// Redirect outbound sequence flows to new element
		for (SequenceFlow f : this.sequenceFlowOut) {
			f.start = newElement;
			newElement.sequenceFlowOut.add(f);
		}

	}

	public FlowElement() {
		id = flowElementCount++;
	}
	
	public String getProcessTemplateName(){
		return "FlowElement";
	}
	

}
