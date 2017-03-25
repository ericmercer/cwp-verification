package bpmnStructure.exceptions;

public class PromelaTypeSizeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int size;

	public PromelaTypeSizeException(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}
}
