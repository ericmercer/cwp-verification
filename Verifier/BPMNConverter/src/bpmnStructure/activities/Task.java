package bpmnStructure.activities;

import bpmnStructure.Activity;
import visitor.Visitor;

public class Task extends Activity {
	

	public Task(String elementId, String elementName) {
		super(elementId,elementName);
	
	}


	
	public void accept(Visitor v) {
		v.Visit(this);

	}


}
