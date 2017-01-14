package bpmnStructure;

import java.util.ArrayList;
import visitor.Visitor;

public class FlowElement {

	public static int flowElementCount = 0;
	public String name;
	public int id;
	public boolean visited = false;

	public ArrayList<FlowElement> sequenceFlow = new ArrayList<FlowElement>();

	public FlowElement(String name) {
		this.name = name;
	}

	public void addSequenceFlow(FlowElement f) {
		sequenceFlow.add(f);
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
