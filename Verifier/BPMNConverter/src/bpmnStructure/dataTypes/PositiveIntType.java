package bpmnStructure.dataTypes;

public class PositiveIntType extends PromelaType {

	private int numOfBits;



	public PositiveIntType(String name, int maxSize) {
		super(name);

		this.numOfBits = (int) Math.ceil(Math.log(maxSize+1) / Math.log(2));
	}

}
