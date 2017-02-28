package bpmnStructure;

import java.util.ArrayList;

public class BpmnDiagram {

	ArrayList<BpmnProcess> processes = new ArrayList<BpmnProcess>();

	public BpmnProcess addProcess(String processName) {
		BpmnProcess process = new BpmnProcess(processName);
		processes.add(process);
		return process;

	}

}
