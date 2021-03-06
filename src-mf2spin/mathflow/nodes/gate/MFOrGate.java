package mathflow.nodes.gate;

import visitor.BpmnVisitor;

public class MFOrGate extends MFGate {

	public MFOrGate(String ID) {
		super(ID);
	}

	public void accept(BpmnVisitor visitor) {
		if (visited == false) {	
			visited = true;
			visitor.preVisit(this);
			if (visitor.visit(this) == true) {
				//visit child nodes if visit() returns true
				super.accept(visitor);
			}
			visitor.endVisit(this);
			visitor.postVisit(this);
		}
	}

}
