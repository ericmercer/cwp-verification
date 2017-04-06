package bpmnStructure.messages;

import java.util.ArrayList;

import bpmnStructure.Event;
import bpmnStructure.MessageFlow;
import bpmnStructure.TokenId;
import bpmnStructure.PrintMessages.PrintMessageManager;
import bpmnStructure.dataTypes.PromelaType;
import bpmnStructure.dataTypes.PromelaTypeDef;
import bpmnStructure.dataTypes.TypeDeclaration;

public class MessageCatchEvent extends Event {

	private PromelaType td;
	private MessageFlow mf;

	/*
	 * I am assuming that there is only one message flow attached to an
	 * flowelement for now
	 */
	public MessageCatchEvent(String name) {
		super(name);

	}

	public void addMessageFlow(MessageFlow mf) {
		getMessageFlows().add(mf);
		this.td = mf.getType();
		this.mf = mf;
	}

	@Override
	public String[] getExecutionOptions() {

		ArrayList<TypeDeclaration> objects = this.getRelatedDataObjects();

		TypeDeclaration tdec = objects.get(0);

		PromelaType type = tdec.getType();

		String variableName = tdec.getVarName();

		String executionString = this.getDefaultTokenInValue() + " > 0 && ";/* \n */
		// TypeDeclaration payloadDeclaration = new TypeDeclaration("payload",
		// this.td, 0);
		// executionString += payloadDeclaration.generateDeclaration() + ";\n";
		// TODO: Main function
		// TODO: Add keys
		
		
		executionString += mf.getChannelName() + "??[eval(" + TokenId.getName() + ")" + ", ";
		if (type instanceof PromelaTypeDef) {
			PromelaTypeDef ptd = (PromelaTypeDef) type;
			ArrayList<TypeDeclaration> keys = ptd.getKeyVars();
			for (TypeDeclaration key : keys) {
				executionString += "eval("+variableName +"." + key.getVarName() + "), ";
			}
		}
		executionString += variableName + "];\n";
		
		executionString += mf.getChannelName() + "??eval(" + TokenId.getName() + ")" + ", ";
		if (type instanceof PromelaTypeDef) {
			PromelaTypeDef ptd = (PromelaTypeDef) type;
			ArrayList<TypeDeclaration> keys = ptd.getKeyVars();
			for (TypeDeclaration key : keys) {
				executionString += "eval("+variableName +"." + key.getVarName() + "), ";
			}
		}
		executionString += variableName + ";\n";
		
		
		executionString += "decrement_tokens("+this.getDefaultTokenInValue() + ");\n";
		executionString += "print("+ PrintMessageManager.getInstance().addMessage(this.getName()) + ");\n";
		executionString += "out_tokens(" + this.sequenceFlowOut.get(0).getTokenValue() + ")\n";

		return new String[] { executionString };

	}

}
