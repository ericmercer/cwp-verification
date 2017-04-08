package bpmnStructure.events;

import java.util.ArrayList;

import bpmnStructure.MessageFlow;
import bpmnStructure.TokenId;
import bpmnStructure.PrintMessages.PrintMessageManager;
import bpmnStructure.dataTypes.TypeDeclaration;

public class MessageEndEvent extends EndEvent{

	public MessageEndEvent(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public String[] getExecutionOptions() {
		MessageFlow mf = this.getMessageFlows().get(0);
		// PromelaType td = mf.getType();

		ArrayList<TypeDeclaration> objects = this.getRelatedDataObjects();

		TypeDeclaration tdec = objects.get(0);
		String variableName = tdec.getVarName();

		String executionString = "in_tokens(" + this.getDefaultTokenInValue() + ") -> \n";/* \n */
		executionString += "atomic{\n";
		executionString += "print("+ PrintMessageManager.getInstance().addMessage(this.getName()) + ");\n";
		/*send the object via a "message", but really we are just going to spawn a new process here*/
		executionString += "   " + mf.getChannelName() + "!" + TokenId.getName() + ", " + variableName + ";\n";
//		executionString += "run " + mf.getEndProcess().getProcessName();
//		executionString += "(" + TokenId.getName() + ", " + "reportchan" + ", " + variableName + ")\n";
		executionString += "break;\n";
		executionString += "}\n";
		return new String[] { executionString };

	}
}
