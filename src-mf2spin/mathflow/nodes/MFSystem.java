package mathflow.nodes;

import visitor.BpmnVisitor;

public class MFSystem extends MFInformationObject {

	public MFSystem(String ID) {
		super(ID);
		data.scope_class = "Information System";
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

}
