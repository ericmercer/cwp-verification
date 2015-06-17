package mathflow.nodes.gate;

import visitor.BpmnVisitor;
import mathflow.nodes.MFControlNode;
import mathflow.nodes.event.MFEnd;
import mathflow.nodes.event.MFIntermediateTimer;
import mathflow.nodes.process.MFProcess;
import mathflow.nodes.process.MFSubprocess;
import mathflow.nodes.task.MFManualTask;
import mathflow.nodes.task.MFServiceTask;
import mathflow.nodes.task.MFUserTask;

public abstract class MFGate extends MFControlNode {

	MFControlNode[] nextNodes;
	String[] nextConditions;
	public MFProcess parentProcess;
	
	public MFGate(String ID) {
		super(ID);
		nextNodes = null;
		nextConditions = null;
	}
	
	public void accept(BpmnVisitor visitor) {
		if (nextNodes != null) {	
			for (MFControlNode node: nextNodes) {
				node.accept(visitor);
			}
		}
	}
	
	public MFControlNode getNextNode(int index) {
		return nextNodes[index];
	}
	
	public String getNextCondition(int index) {
		return nextConditions[index];
	}
	
	public int numberOfEdges() {
		if (nextNodes == null) {
			assert(nextConditions == null);
			return 0;
		} else {
			assert(nextNodes.length == nextConditions.length);
			return nextNodes.length;
		}
	}
	
	public void addEdge(MFControlNode nextNode, String condition) {
		if (nextNodes == null) {
			assert(nextConditions == null);
			nextNodes = new MFControlNode[1];
			nextConditions = new String[1];
			nextNodes[0] = nextNode;
			nextConditions[0] = condition;
		} 
		else {
			MFControlNode[] tempNodes = new MFControlNode[nextNodes.length+1];
			String[] tempStrings = new String[nextConditions.length+1];
			assert(nextNodes.length == nextConditions.length);
			for (int i=0; i<nextNodes.length; i++) {
				tempNodes[i] = nextNodes[i];
				tempStrings[i] = nextConditions[i];
			}
			tempNodes[nextNodes.length] = nextNode;
			tempStrings[nextConditions.length] = condition;
			nextNodes = tempNodes;
			nextConditions = tempStrings;
		}
	}
	
	public String toString() {
		String result = super.toString()+
				"parent process= "+parentProcessToString()+"\n\t"+
				"next nodes= "+nextNodesToString()+"\n\t"+
				"conditions= "+nextConditionsToString()+"\n\t";
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
	
	private String nextConditionsToString() {
		String result = "[";
		if (nextConditions != null) {
			for (int i=0; i<nextConditions.length; i++) {
				result = result + nextConditions[i];
				if (i != nextConditions.length-1) {
					result = result + ", ";
				}
			}
		}
		result = result + "]";
		return result;
	}
	
}
