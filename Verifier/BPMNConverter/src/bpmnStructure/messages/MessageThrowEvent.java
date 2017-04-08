package bpmnStructure.messages;

import java.util.ArrayList;

import bpmnStructure.Event;
import bpmnStructure.MessageFlow;
import bpmnStructure.TokenId;
import bpmnStructure.PrintMessages.PrintMessageManager;
import bpmnStructure.dataTypes.PromelaType;
import bpmnStructure.dataTypes.TypeDeclaration;

public class MessageThrowEvent extends Event {

	public MessageThrowEvent(String elementId) {
		super(elementId);
	
	}

	@Override
	public String[] getExecutionOptions() {
		MessageFlow mf = this.getMessageFlows().get(0);
		// PromelaType td = mf.getType();

		ArrayList<TypeDeclaration> objects = this.getRelatedDataObjects();

		TypeDeclaration tdec = objects.get(0);
		String variableName = tdec.getVarName();
		String executionString = "in_tokens(" + this.getDefaultTokenInValue() + ") -> \n";/* \n */
		/*send the object via a "message", but really we are just going to spawn a new process here*/
//		executionString += "   " + mf.getChannelName() + "!" + variableName + ";\n";
		executionString += "atomic{\n";
		executionString += "print("+ PrintMessageManager.getInstance().addMessage(this.getElementId()) + ");\n";
		executionString += "run " + mf.getEndProcess().getProcessName();
		executionString += "(" + TokenId.getName() + ", " +"reportChannel" + ", " + variableName + ")\n";
		executionString += "out_tokens(" +  this.sequenceFlowOut.get(0).getTokenValue() + ")\n";
		executionString += "}\n";
		return new String[] { executionString };

	}

}
