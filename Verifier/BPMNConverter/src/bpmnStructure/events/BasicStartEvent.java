package bpmnStructure.events;

import visitor.Visitor;

public class BasicStartEvent extends StartEvent {

	public BasicStartEvent(String name) {
		super(name);
	}

	public void accept(Visitor v) {
		v.Visit(this);

	}
	public String getProcessTemplateName(){
		return "start";
	}

}
