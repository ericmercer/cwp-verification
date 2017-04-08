package bpmnStructure.events;

import visitor.Visitor;

public class BasicEndEvent extends EndEvent {
	public BasicEndEvent(String elementId, String elementName) {
		super(elementId,elementName);
	}

	public void accept(Visitor v) {
		v.Visit(this);

	}

}
