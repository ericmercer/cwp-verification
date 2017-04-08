package bpmnStructure.events;

import visitor.Visitor;

public class BasicStartEvent extends StartEvent {

	public BasicStartEvent(String elementId) {
		super(elementId);
	}

	public void accept(Visitor v) {
		v.Visit(this);

	}

}
