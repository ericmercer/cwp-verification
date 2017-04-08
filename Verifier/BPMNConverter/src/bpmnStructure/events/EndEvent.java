package bpmnStructure.events;

import bpmnStructure.Event;


public class EndEvent extends Event {
	public EndEvent(String elementId) {
		super(elementId);
	}

	@Override
	public String[] getExecutionOptions() {
		String executionString = "in_tokens(" + this.getDefaultTokenInValue() + ") -> ";

		executionString += "break;\n";
		return new String[] { executionString };


	}
}
