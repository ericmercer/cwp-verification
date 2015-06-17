package xml.parse;
import java.util.HashMap;

import mathflow.nodes.MFControlNode;

import org.w3c.dom.Element;

public class ControlFlowStructure {
	HashMap<String, MFControlNode> controlNodes;
	
	public ControlFlowStructure() {
		controlNodes = new HashMap<String, MFControlNode>();
	}
	
	public MFControlNode getNode(String nodeID) {
		return controlNodes.get(nodeID);
	}
	
	public int numberOfNodes() {
		return controlNodes.size();
	}
	
	public void addNode(MFControlNode node) {
		String nodeID = node.getID();
		controlNodes.put(nodeID, node);
	}
	
	/*public void updateNode(MFControlNode node) {
		controlNodes.remove(node.getID());
		addNode(node);
	}*/
	
	/*public void setNodeElement(String nodeID, Element curElement) {
		controlNodes.get(nodeID).setElement(curElement);;
	}
	
	public void addToNodeNextLinks(String nodeID, String nextID, String nextCondition) {
		controlNodes.get(nodeID).addNextID(nextID);
		controlNodes.get(nodeID).addNextCondition(nextCondition);
	}
	
	public void setNodeCalledID(String nodeID, String calledID) {
		controlNodes.get(nodeID).setCalledID(calledID);
	}
	
	public void setNodeSimInfo(String nodeID, Element newInfo) {
		controlNodes.get(nodeID).setSimInfo(newInfo);
	}
	
	public void markNodeAsStored(String nodeID) {
		controlNodes.get(nodeID).markAsStored();
	}*/
	
	public String toString() {
		return controlNodes.toString();
	}
}
