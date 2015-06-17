package mathflow.nodes;

import visitor.BpmnVisitor;

public class MFPerformer extends MFResource {

	public MFPerformer(String ID) {
		super(ID);
		data.resourceType = "Human";
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
