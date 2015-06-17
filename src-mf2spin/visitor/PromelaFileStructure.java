package visitor;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Stack;

public class PromelaFileStructure {
	private HashMap<String, String> globalVars;
	private HashMap<String, String> typeDefs;
	private HashMap<String, String> procTypes;
	private String init;
	
	private PriorityQueue<String> globalVarsOrder;
	private PriorityQueue<String> typeDefsOrder;
	private PriorityQueue<String> procTypesOrder;
	
	public PromelaFileStructure() {
		globalVars = new HashMap<String, String>();
		typeDefs = new HashMap<String, String>();
		procTypes = new HashMap<String, String>();
		init = "";
		
		globalVarsOrder = new PriorityQueue<String>();
		typeDefsOrder = new PriorityQueue<String>();
		procTypesOrder = new PriorityQueue<String>();
	}
	
	public HashMap<String, String> getGlobalVars() {
		return globalVars;
	}
	
	public HashMap<String, String> getTypeDefs() {
		return typeDefs;
	}
	
	public HashMap<String, String> getProcTypes() {
		return procTypes;
	}
	
	public String getInit() {
		return init;
	}
	
	public void addToGlobalVars(String nodeID, String codeSegment) {
		globalVars.put(nodeID, codeSegment);
		queueGlobalVars(nodeID);
	}
	
	public void addToTypeDefs(String nodeID, String codeSegment) {
		typeDefs.put(nodeID, codeSegment);
		queueTypeDefs(nodeID);
	}
	
	public void addToProcTypes(String nodeID, String codeSegment) {
		procTypes.put(nodeID, codeSegment);
		queueProcTypes(nodeID);
	}
	
	public void addAfterInit(String newString) {
		init = init.concat(newString);
	}
	
	public void addBeforeInit(String newString) {
		init = newString.concat("init");
	}
	
	public boolean queueGlobalVars(String nodeID) {
		if (globalVars.containsKey(nodeID)) {
			globalVarsOrder.add(nodeID);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean queueTypeDefs(String nodeID) {
		if (typeDefs.containsKey(nodeID)) {
			typeDefsOrder.add(nodeID);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean queueProcTypes(String nodeID) {
		if (procTypes.containsKey(nodeID)) {
			procTypesOrder.add(nodeID);
			return true;
		} else {
			return false;
		}
	}
	
	public String toString() {
		String completeFileString = globalVarsToString()+
									typeDefsToString()+
									procTypesToString()+
									"\n"+init+"\n";
		return completeFileString;
	}
	
	private String globalVarsToString() {
		String globalVarsString = "";
		PriorityQueue<String> localOrderQ = new PriorityQueue<String>(globalVarsOrder);
		for (int i=0; i<globalVarsOrder.size(); i++) {
			String curKey = localOrderQ.poll();
			String curString = globalVars.get(curKey);
			globalVarsString = globalVarsString.concat("\n"+curString+"\n");
		}
		return globalVarsString;
	}
	
	private String typeDefsToString() {
		String typeDefsString = "";
		PriorityQueue<String> localOrderQ = new PriorityQueue<String>(typeDefsOrder);
		for (int i=0; i<typeDefsOrder.size(); i++) {
			String curKey = localOrderQ.poll();
			String curString = typeDefs.get(curKey);
			typeDefsString = typeDefsString.concat("\n"+curString+"\n");
		}
		return typeDefsString;
	}
	
	private String procTypesToString() {
		String procTypesString = "";
		PriorityQueue<String> localOrderQ = new PriorityQueue<String>(procTypesOrder);
		for (int i=0; i<procTypesOrder.size(); i++) {
			String curKey = localOrderQ.poll();
			String curString = procTypes.get(curKey);
			procTypesString = procTypesString.concat("\n"+curString+"\n");
		}
		return procTypesString;
	}
}
