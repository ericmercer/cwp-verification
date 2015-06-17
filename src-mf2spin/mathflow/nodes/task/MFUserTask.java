package mathflow.nodes.task;

import visitor.BpmnVisitor;

public class MFUserTask extends MFTask {
	
	public MFUserTask(String ID) {
		super(ID);
		nextNodes = null;
	}

	public void accept(BpmnVisitor visitor) {
		if (visited == false) {
			visited = true;
			visitor.preVisit(this);
			if (visitor.visit(this) == true) {
				//visit child nodes if visit() returns true
				//super class contains next nodes (i.e. child nodes)
				super.accept(visitor);
			}
			visitor.endVisit(this);
			visitor.postVisit(this);
		}
	}

}
