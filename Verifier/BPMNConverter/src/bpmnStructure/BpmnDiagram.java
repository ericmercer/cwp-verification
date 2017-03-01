package bpmnStructure;

import java.util.ArrayList;

public class BpmnDiagram {

	ArrayList<BpmnProcess> processes = new ArrayList<BpmnProcess>();

	public BpmnProcess addProcess(String processName) {
		BpmnProcess process = new BpmnProcess(processName);
		processes.add(process);
		return process;

	}
	
	public void addDataStore(){
		
	}

	public void addMessageFlow(String process1, String event1, String process2, String event2) {
		// TODO Auto-generated method stub
		
	}

}
