package bpmnStructure.activities;

import bpmnStructure.Activity;
import visitor.Visitor;

public class Task extends Activity {
	String promela = null;

	public Task(String name, String promela) {
		super(name);
		this.promela = promela;
	}

	public void accept(Visitor v) {
		v.Visit(this);

	}

	public String getProcessTemplateName() {
		return "task_only";
	}

}
