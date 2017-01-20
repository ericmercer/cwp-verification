package bpmnStructure.events;

import visitor.Visitor;

public class BasicEndEvent extends EndEvent {
	public BasicEndEvent(String name) {
		super(name);
	}
	public void accept(Visitor v) {
		v.Visit(this);
		

	}
	public String getProcessTemplateName(){
		return "end";
	}
}
