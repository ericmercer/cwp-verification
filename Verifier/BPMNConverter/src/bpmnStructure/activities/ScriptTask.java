package bpmnStructure.activities;

import bpmnStructure.FlowElement;
import bpmnStructure.SequenceFlow;
import bpmnStructure.PrintMessages.PrintMessageManager;

public class ScriptTask extends Task {

	public ScriptTask(String name, String promela) {
		super(name, promela);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] getExecutionOptions() {
		String executionString = "in_tokens(" + this.getDefaultTokenInValue() + ") -> \n";/* \n */
		executionString += "atomic{\n";
		executionString += "print("+ PrintMessageManager.getInstance().addMessage(this.getName()) + ");\n";
		executionString += "   " + promela + "\n";
		executionString += "out_tokens(" + this.getDefaultTokenOutValue() + ")\n";
		executionString += "}\n";
		return new String[]{executionString};

	}

}
