package mathflow.nodes;

import visitor.BpmnVisitor;

public interface ControlNode {
	
	void accept(BpmnVisitor visitor);
	
}
