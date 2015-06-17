package mathflow.nodes.task;

import visitor.BpmnVisitor;
import mathflow.nodes.MFControlNode;
import mathflow.nodes.process.MFProcess;

public abstract class MFTask extends MFControlNode {
	
	MFControlNode[] nextNodes;
	public MFTaskData data;
	public MFProcess parentProcess;
	
	public MFTask(String ID) {
		super(ID);
		nextNodes = null;
		data = new MFTaskData();
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
	
	public int numberOfNextNodes() {
		return nextNodes.length;
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
				"parent process= "+parentProcessToString()+"\n\t"+
				"next nodes= "+nextNodesToString()+"\n\t"+
				"data= "+dataToString()+"\n\t";
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
	
	private String dataToString() {
		if (data == null) {
			return "";
		} else {
			return data.toString();
		}
	}

}
