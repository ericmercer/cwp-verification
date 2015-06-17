package mathflow.nodes.event;

import visitor.BpmnVisitor;

public class MFEnd extends MFEvent {
	
	public MFEnd(String ID) {
		super(ID);
	}

	public void accept(BpmnVisitor visitor) {
		if (visited == false) {	
			visited = true;
			visitor.preVisit(this);
			visitor.visit(this);
			visitor.endVisit(this);
			visitor.postVisit(this);
		}
	}

	public String toString() {
		return super.toString();
	}
}
