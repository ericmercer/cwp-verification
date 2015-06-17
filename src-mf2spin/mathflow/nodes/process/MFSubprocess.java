package mathflow.nodes.process;

import mathflow.nodes.MFControlNode;
import mathflow.nodes.event.MFEnd;
import mathflow.nodes.event.MFIntermediateTimer;
import mathflow.nodes.gate.MFAndGate;
import mathflow.nodes.gate.MFOrGate;
import mathflow.nodes.gate.MFXorGate;
import mathflow.nodes.task.MFManualTask;
import mathflow.nodes.task.MFServiceTask;
import mathflow.nodes.task.MFUserTask;
import visitor.BpmnVisitor;

public class MFSubprocess extends MFControlNode {

	MFControlNode[] nextNodes;
	MFProcess calledProcess;
	public MFProcess parentProcess;
	
	public MFSubprocess(String ID) {
		super(ID);
		nextNodes = null;
		calledProcess = null;
	}

	public void accept(BpmnVisitor visitor) {
		if (visited == false) {
			visited = true;
			visitor.preVisit(this);
			if (visitor.visit(this) == true) {
				//visit child nodes if visit() returns true
				if (nextNodes != null) {	
					for (MFControlNode node: nextNodes) {
						node.accept(visitor);
					}
				}
			}
			visitor.endVisit(this);
			visitor.postVisit(this);
		}
	}
	
	public MFControlNode getNextNode(int index) {
		return nextNodes[index];
	}
	
	public int numberOfNextNodes() {
		return nextNodes.length;
	}
	
	public MFProcess getCalledProcess() {
		return calledProcess;
	}
	
	public void addNextNode(MFControlNode nextNode) {
		if (nextNodes == null) {
			nextNodes = new MFControlNode[1];
			nextNodes[0] = nextNode;
		} 
		else {
			MFControlNode[] temp = new MFControlNode[nextNodes.length+1];
			for (int i=0; i<nextNodes.length; i++) {
				temp[i] = nextNodes[i];
			}
			temp[nextNodes.length] = nextNode;
			nextNodes = temp;
		}
	}
	
	public void setCalledProcess(MFProcess newProcess) {
		if (calledProcess == null) {
			calledProcess = newProcess;
		} else {
			return;
		}
	}
	
	public String toString() {
		String result = super.toString()+
				"parent process= "+parentProcessToString()+"\n\t"+
				"next nodes= "+nextNodesToString()+"\n\t";
		return result;
	}
	
	private String parentProcessToString() {
		if (parentProcess == null) {
			return "";
		} else {
			return parentProcess.name;
		}
	}
	
	private String nextNodesToString() {
		String result = "[";
		if (nextNodes != null) {	
			for (int i=0; i<nextNodes.length; i++) {
				result = result + nextNodes[i].name;
				if (i != nextNodes.length-1) {
					result = result + ",\n\t\t";
				}
			}
		}	
		result = result + "]";
		return result;
	}

}
