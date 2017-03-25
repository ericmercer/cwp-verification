package bpmnStructure.dataTypes;

import bpmnStructure.exceptions.PromelaTypeSizeException;

public class PositiveIntType extends PromelaType {

	private int numOfBits;
	private int maxSize;
	private int defaultValue;

	// default maximum length to 256 if not specified, and the default value to
	// zero
	public PositiveIntType() throws PromelaTypeSizeException {
		this(256, 0);
	}

	public PositiveIntType( int maxSize) throws PromelaTypeSizeException {
		this(maxSize, 0);
	}

	public PositiveIntType(int maxSize, int defaultValue) throws PromelaTypeSizeException {
		this(maxSize, defaultValue, 1);
	}

	public PositiveIntType(int maxSize, int defaultValue, int capacity) throws PromelaTypeSizeException {
		// super(name);
		this.maxSize = maxSize;
		this.defaultValue = defaultValue;
		// add one to the number because it needs to include the number
		this.setNumOfBits((int) Math.ceil(Math.log(maxSize + 1) / Math.log(2)));
		// PromelaConstants.addConstant("MAX_" + name.toUpperCase(), maxSize);
	}

	public int getNumOfBits() {
		return numOfBits;
	}

	public void setNumOfBits(int numOfBits) {
		this.numOfBits = numOfBits;
	}

	// @Override
	// public boolean isKey() {
	// return this.typeName.substring(0, 3).equals("key");
	// }

	@Override
	public String generateTypeString() {
		// TODO Auto-generated method stub

		/*
		 * unless I can figure out how to define an array of an unsigned, this
		 * goes away
		 */
		return getTypeName();// + this.typeName;

	}

	public String getTypeName() {
		// TODO Auto-generated method stub

		/*
		 * unless I can figure out how to define an array of an unsigned, this
		 * goes away
		 */

		String typeName = "";
		if (this.maxSize <= 1) {
			typeName = "bit";
		} else if (this.maxSize <= 255) {
			typeName = "byte";
		} else if (this.maxSize <= 32767) {
			typeName = "short";
		} else {
			typeName = "int";
		}
		return typeName;

	}

	public int getDefaultValue() {
		return this.defaultValue;
	}

	public int getMaxSize() {
		return this.maxSize;
	}
}
