package bpmnStructure.events;

import bpmnStructure.Event;
import bpmnStructure.PrintMessages.PrintMessageManager;


public class EndEvent extends Event {
	public EndEvent(String elementId, String elementName) {
		super(elementId,elementName);
	}

	@Override
	public String[] getExecutionOptions() {
		String executionString = "in_tokens(" + this.getDefaultTokenInValue() + ") -> \n";
		executionString += "atomic{\n";
		executionString += "print("+ PrintMessageManager.getInstance().addMessage(this.getElementInfo()) + ");\n";
		executionString += "break;\n";
		executionString += "}\n";
		return new String[] { executionString };


	}
}
