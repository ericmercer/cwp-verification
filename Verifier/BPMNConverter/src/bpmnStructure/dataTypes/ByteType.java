package bpmnStructure.dataTypes;

public class ByteType extends PromelaVariable implements PromelaNumber {

	
	private int maxNumber;

	public ByteType(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public ByteType(String name, int maxNumber) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setMaxNumber(int maxNumber) {
		this.maxNumber = maxNumber;
	}

}
