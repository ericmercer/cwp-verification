package bpmnStructure;

import java.util.ArrayList;

import bpmnStructure.dataTypes.PromelaType;
import bpmnStructure.dataTypes.PromelaTypeDef;
import bpmnStructure.dataTypes.TypeDefManager;
import bpmnStructure.dataTypes.TypeDeclaration;
import bpmnStructure.exceptions.PromelaTypeSizeException;

public class BpmnDiagram {

	ArrayList<BpmnProcess> mainProcesses = new ArrayList<BpmnProcess>();
	ArrayList<MessageFlow> messageFlows = new ArrayList<MessageFlow>();
	ArrayList<TypeDeclaration> globalVariables = new ArrayList<TypeDeclaration>();

	public TypeDefManager typeManager = new TypeDefManager();

	public PromelaTypeDef addTypeDef(String name) {
		return typeManager.addTypeDef(name);
	}

	public BpmnProcess addProcess(String processName) {
		BpmnProcess process = new BpmnProcess(processName);
		mainProcesses.add(process);

		return process;

	}

	public void addDataStore(String name, PromelaType td, int capacity)  {
		globalVariables.add(new TypeDeclaration(name, td, capacity));
		// TODO: Assumes zero to one data stores will be used
		TokenId.setName(name + "Index");
	}

	public void addMessageFlow(String messageFlowId, BpmnProcess process1, String flowElement1Name,
			BpmnProcess process2, String flowElement2Name, PromelaTypeDef messageDataType) {

		FlowElement startElement = process1.getFlowElement(flowElement1Name);
		FlowElement endElement = process2.getFlowElement(flowElement2Name);

		MessageFlow mf = new MessageFlow(messageFlowId, messageDataType, process1, startElement,process2, endElement);

		startElement.addMessageFlow(mf);
		endElement.addMessageFlow(mf);

		messageFlows.add(mf);
	}

	public boolean equals(Object o) {
		if (o.getClass() != this.getClass()) {
			return false;
		}
		BpmnDiagram temp = (BpmnDiagram) o;
		for (int i = 0; i < this.mainProcesses.size(); i++) {
			if (!this.mainProcesses.get(i).equals(temp.mainProcesses.get(i))) {
				return false;
			}
		}
		return true;
	}

	public String getGlobalVariables(int number_of_tokens) {
		String out = "";
		
		out += "chan end[" + number_of_tokens + "] = [1] of {mtype};\n";
		
		for (TypeDeclaration var : globalVariables) {
			out += var.generateDeclaration() + ";\n";
		}

		for (Channel ch : Channels.channels) {
			out += "chan " + ch.getName() + " =  [1] of {";
			out += /* tokenid */"byte" + ", ";
			PromelaType type = ch.getType();
			if (type instanceof PromelaTypeDef) {
				PromelaTypeDef ptd = (PromelaTypeDef) type;
				ArrayList<TypeDeclaration> keys = ptd.getKeyVars();
				for (TypeDeclaration key : keys) {
					out += key.getType().getTypeName() + ", ";
				}

			}

			out += ch.getType().getTypeName() + "};\n";
		}

		return out;
	}

	public ArrayList<BpmnProcess> getAllProcesses() {
		ArrayList<BpmnProcess> processes = new ArrayList<BpmnProcess>();
		for (BpmnProcess process : mainProcesses) {
			processes.add(process);
			processes.addAll(process.getAllSubProcesses());
		}

		return processes;

	}

	public String getProcTypes() {
		String out = "";
		for (BpmnProcess process : this.getAllProcesses()) {

			out += process.generateProctype();
		}
		return out;
	}

}
