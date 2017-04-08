package bpmnStructure.events;

import visitor.Visitor;

public class BasicEndEvent extends EndEvent {
	public BasicEndEvent(String elementId) {
		super(elementId);
	}

	public void accept(Visitor v) {
		v.Visit(this);

	}

}
