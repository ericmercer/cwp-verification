package bpmnStructure.events;

import bpmnStructure.Event;
import bpmnStructure.FlowElement;
import bpmnStructure.SequenceFlow;

public class EndEvent extends Event {
	public EndEvent(String name) {
		super(name);
	}

	// TODO: OVERRIDE THIS IN END EVENT THAT SENDS MESSAGE
	@Override
	public String[] getExecutionOptions() {
		String executionString = "in_tokens(" + this.getDefaultTokenInValue() + ") -> ";

		executionString += "break;\n";
		return new String[] { executionString };


	}
}
