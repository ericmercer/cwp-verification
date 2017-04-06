package bpmnStructure;

import bpmnStructure.dataTypes.PromelaType;
import bpmnStructure.dataTypes.PromelaTypeDef;

public class MessageFlow {

	private PromelaType type;
	private String name;
	private FlowElement startElement;
	private BpmnProcess startProcess;
	private FlowElement endElement;
	private BpmnProcess endProcess;

	public MessageFlow(String messageFlowId, PromelaType messageDataType, BpmnProcess startProcess,
			FlowElement startElement, BpmnProcess endProcess, FlowElement endElement) {
		// TODO Auto-generated constructor stub
		setName(messageFlowId);
		Channels.addChannel(this.getChannelName(), messageDataType);
		type = messageDataType;
		this.startElement = startElement;
		this.startProcess = startProcess;
		this.setEndElement(endElement);
		this.setEndProcess(endProcess);

	}

	public PromelaType getType() {
		// TODO Auto-generated method stub
		return type;
	}

	public String getName() {
		return name;
	}

	public String getChannelName() {
		return "chan_" + name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FlowElement getEndElement() {
		return endElement;
	}

	public void setEndElement(FlowElement endElement) {
		this.endElement = endElement;
	}

	public BpmnProcess getEndProcess() {
		return endProcess;
	}

	public void setEndProcess(BpmnProcess endProcess) {
		this.endProcess = endProcess;
	}

}
