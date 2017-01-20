package bpmnStructure.activities;

import bpmnStructure.Activity;
import visitor.Visitor;

public class Task extends Activity {

	public Task(String name) {
		super(name);
	}

	public void accept(Visitor v) {
		v.Visit(this);

	}

	public String getProcessTemplateName() {
		return "task_only";
	}

}
