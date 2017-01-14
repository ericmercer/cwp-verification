package bpmnStructure;

import java.util.ArrayList;
import visitor.Visitor;

public class FlowElement {

	public static int flowElementCount = 0;
	public String name;
	public int id;
	public boolean visited = false;

	public ArrayList<SequenceFlow> sequenceFlowOut = new ArrayList<SequenceFlow>();
	public ArrayList<SequenceFlow> sequenceFlowIn = new ArrayList<SequenceFlow>();

	public FlowElement(String name) {
		this.name = name;
	}

	/*
	 * if there is a way that an element can be split into less ambiguous
	 * pieces, then do it other wise return
	 */
	public void splitIntoPieces() {

	}

	public void addSequenceFlow(FlowElement f) {
		SequenceFlow connector = new SequenceFlow(this, f);
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

	public FlowElement() {
		id = flowElementCount++;
	}

}
