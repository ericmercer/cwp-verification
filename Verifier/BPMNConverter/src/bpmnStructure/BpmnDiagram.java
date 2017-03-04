package bpmnStructure;

import java.util.ArrayList;

public class BpmnDiagram {

	ArrayList<BpmnProcess> processes = new ArrayList<BpmnProcess>();

	public BpmnProcess addProcess(String processName) {
		BpmnProcess process = new BpmnProcess(processName);
		processes.add(process);
		return process;

	}
	
	public void addDataStore(String id, String name, int capacity){
		
	}

	public void addMessageFlow(String process1, String event1, String process2, String event2) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean equals(Object o) {
		if(o.getClass() != this.getClass()) {
			return false;
		}
		BpmnDiagram temp = (BpmnDiagram) o;
		for(int i = 0; i < this.processes.size(); i++) {
			if( !this.processes.get(i).equals(temp.processes.get(i)) ) {
				return false;
			}
		}
		return true;
	}

}
