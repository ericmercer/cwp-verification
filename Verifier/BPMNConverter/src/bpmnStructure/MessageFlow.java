package bpmnStructure;

import bpmnStructure.dataTypes.PromelaTypeDef;

public class MessageFlow {

	public MessageFlow(String messageFlowId, BpmnProcess process1, String event1, BpmnProcess process2, String event2,
			PromelaTypeDef messageDataType) {
		// TODO Auto-generated constructor stub
		Channels.addChannel(messageFlowId, messageDataType);

	}

}
