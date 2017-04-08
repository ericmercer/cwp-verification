package bpmnStructure;

import bpmnStructure.dataTypes.PromelaType;
import bpmnStructure.dataTypes.PromelaTypeDef;

public class MessageFlow {

	private PromelaType type;
	private String messageFlowId;
	private FlowElement startElement;
	private BpmnProcess startProcess;
	private FlowElement endElement;
	private BpmnProcess endProcess;

	public MessageFlow(String messageFlowId, PromelaType messageDataType, BpmnProcess startProcess,
			FlowElement startElement, BpmnProcess endProcess, FlowElement endElement) {
		setMessageFlowId(messageFlowId);
		Channels.addChannel(this.getChannelName(), messageDataType);
		type = messageDataType;
		this.startElement = startElement;
		this.startProcess = startProcess;
		this.setEndElement(endElement);
		this.setEndProcess(endProcess);

	}

	public FlowElement getStartElement() {
		return startElement;
	}

	public void setStartElement(FlowElement startElement) {
		this.startElement = startElement;
	}

	public BpmnProcess getStartProcess() {
		return startProcess;
	}

	public void setStartProcess(BpmnProcess startProcess) {
		this.startProcess = startProcess;
	}

	public PromelaType getType() {
		return type;
	}

	public String getChannelName() {
		return "chan_" + messageFlowId;
	}

	public String getMessageFlowId() {
		return messageFlowId;
	}

	public void setMessageFlowId(String messageFlowId) {
		this.messageFlowId = messageFlowId;
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
