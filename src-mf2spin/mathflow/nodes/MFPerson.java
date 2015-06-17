package mathflow.nodes;

import visitor.BpmnVisitor;

public class MFPerson extends MFInformationObject {

	public MFPerson(String ID) {
		super(ID);
		data.scope_class = "Person";
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
