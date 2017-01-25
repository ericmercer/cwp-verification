package bpmnStructure;

public class SequenceFlow {

	private FlowElement start;
	private FlowElement end;
	private int idNumber;
	
	public SequenceFlow(FlowElement start, FlowElement end,int idNumber){
		this.setStart(start);
		this.setEnd(end);
		this.setIdNumber(idNumber);
	}

	public int getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(int idNumber) {
		this.idNumber = idNumber;
	}

	public FlowElement getStart() {
		return start;
	}

	public void setStart(FlowElement start) {
		this.start = start;
	}

	public FlowElement getEnd() {
		return end;
	}

	public void setEnd(FlowElement end) {
		this.end = end;
	}
	
}
