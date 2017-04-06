package bpmnStructure;

public class SequenceFlow {

	private FlowElement start;
	private FlowElement end;
	private int idNumber;
	private String expression;
	private boolean isDefault = false;

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public SequenceFlow(FlowElement start, FlowElement end, int idNumber, String expression, boolean isDefault) {
		this.setStart(start);
		this.setEnd(end);
		this.setIdNumber(idNumber);
		this.expression = expression;
		this.setDefault(isDefault);
	}
	
	public String getTokenValue(){
		return this.getEnd().getTokenName() +"_"+ idNumber;
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

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public boolean equals(Object o) {
		SequenceFlow otherElement = (SequenceFlow) o;
		return this.getStart().equals(otherElement.getStart()) && this.getEnd().equals(otherElement.getEnd());
		
	}

}
