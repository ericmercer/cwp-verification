package bpmnStructure.activities;

import bpmnStructure.PrintMessages.PrintMessageManager;

public class ScriptTask extends Task {
	String promela = null;

	public ScriptTask(String elementId, String elementName, String promela) {
		super(elementId, elementName);
		this.promela = promela;
		System.out.println("id, promela: " + elementId + ", " + promela);
	}

	@Override
	public String[] getExecutionOptions() {
		String executionString = "in_tokens(" + this.getDefaultTokenInValue() + ") -> \n";/* \n */
		executionString += "atomic{\n";
		executionString += "print(" + PrintMessageManager.getInstance().addMessage(this.getElementInfo()) + ");\n";
		executionString += "   " + promela + "\n";
		executionString += "out_tokens(" + this.getDefaultTokenOutValue() + ")\n";
		executionString += "}\n";
		return new String[] { executionString };

	}

}
