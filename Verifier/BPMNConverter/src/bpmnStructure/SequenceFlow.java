package bpmnStructure;

public class SequenceFlow {

	public FlowElement start;
	public FlowElement end;
	private int idNumber;
	
	public SequenceFlow(FlowElement start, FlowElement end,int idNumber){
		this.start = start;
		this.end = end;
		this.setIdNumber(idNumber);
	}

	public int getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(int idNumber) {
		this.idNumber = idNumber;
	}
	
}
