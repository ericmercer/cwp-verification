package bpmnStructure;

import java.util.ArrayList;

import bpmnStructure.dataTypes.PromelaType;
import bpmnStructure.dataTypes.PromelaTypeDef;
import bpmnStructure.dataTypes.TypeDefManager;
import bpmnStructure.dataTypes.TypeDeclaration;
import bpmnStructure.exceptions.PromelaTypeSizeException;

public class BpmnDiagram {

	ArrayList<BpmnProcess> processes = new ArrayList<BpmnProcess>();
	ArrayList<MessageFlow> messageFlows = new ArrayList<MessageFlow>();
	ArrayList<TypeDeclaration> globalVariables = new ArrayList<TypeDeclaration>();

	public TypeDefManager typeManager = new TypeDefManager();

	public PromelaTypeDef addTypeDef(String name) {
		return typeManager.addTypeDef(name);
	}

	public BpmnProcess addProcess(String processName) {
		BpmnProcess process = new BpmnProcess(processName);
		processes.add(process);
		return process;

	}

	public void addDataStore(String name, PromelaType td, int capacity) throws PromelaTypeSizeException {
		globalVariables.add(new TypeDeclaration(name, td, capacity));
	}

	public void addMessageFlow(String messageFlowId, BpmnProcess process1, String event1, BpmnProcess process2,
			String event2, PromelaTypeDef messageDataType) {

		messageFlows.add(new MessageFlow(messageFlowId, process1, event1, process2, event2, messageDataType));
	}

	public boolean equals(Object o) {
		if (o.getClass() != this.getClass()) {
			return false;
		}
		BpmnDiagram temp = (BpmnDiagram) o;
		for (int i = 0; i < this.processes.size(); i++) {
			if (!this.processes.get(i).equals(temp.processes.get(i))) {
				return false;
			}
		}
		return true;
	}

	public String getGlobalVariables() {
		String out = "";
		for (TypeDeclaration var : globalVariables) {
			out += var.generateDeclaration() + ";\n";
		}
		return out;
	}

	public String getProcTypes() {
		String out = "";
		for (BpmnProcess process : processes) {
			out += process.generateProctype();
		}
		return out;
	}

}
