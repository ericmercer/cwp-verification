package bpmnStructure.events;

import visitor.Visitor;

public class BasicStartEvent extends StartEvent {

	public BasicStartEvent(String elementId, String elementName) {
		super(elementId,elementName);
	}

	public void accept(Visitor v) {
		v.Visit(this);

	}

}
