package bpmnStructure;

public class SequenceFlow {

	public FlowElement start;
	public FlowElement end;
	
	public SequenceFlow(FlowElement start, FlowElement end){
		this.start = start;
		this.end = end;
	}
	
}
