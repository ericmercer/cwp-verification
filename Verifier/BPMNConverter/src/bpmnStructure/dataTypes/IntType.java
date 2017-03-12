package bpmnStructure.dataTypes;

public class IntType extends PromelaVariable implements PromelaNumber {

	private int maxNumber;
	private int numberOfBits;

	public IntType(String name, int maxNumber) {
		super(name);
		this.setMaxNumber(maxNumber);
	}

	@Override
	public void setMaxNumber(int maxNumber) {
		this.maxNumber = maxNumber;
		int numberOfBits = (int) Math.ceil(Math.log(this.maxNumber)/Math.log(2));
		
	}

}
