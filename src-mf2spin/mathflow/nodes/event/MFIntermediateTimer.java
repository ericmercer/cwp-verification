package mathflow.nodes.event;

import mathflow.nodes.MFControlNode;
import mathflow.nodes.gate.MFAndGate;
import mathflow.nodes.gate.MFOrGate;
import mathflow.nodes.gate.MFXorGate;
import mathflow.nodes.process.MFSubprocess;
import mathflow.nodes.task.MFManualTask;
import mathflow.nodes.task.MFServiceTask;
import mathflow.nodes.task.MFUserTask;
import visitor.BpmnVisitor;

public class MFIntermediateTimer extends MFEvent {

	MFControlNode[] nextNodes;
	MFTimerData data;
	
	public MFIntermediateTimer(String ID) {
		super(ID);
		data = new MFTimerData();
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
	
	public MFTimerData getData() {
		return data;
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
	
	public String toString() {
		String result = super.toString()+
				"next nodes= "+nextNodesToString()+"\n\t"+
				"data= "+dataToString()+"\n\t";
		return result;
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
	
	private String dataToString() {
		if (data == null) {
			return "";
		} else {
			return data.toString();
		}
	}
}
