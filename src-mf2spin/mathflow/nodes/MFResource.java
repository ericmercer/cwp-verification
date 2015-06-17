package mathflow.nodes;

import visitor.BpmnVisitor;

public abstract class MFResource extends MFControlNode {

	public MFResourceData data;
	
	public MFResource(String ID) {
		super(ID);
		data = new MFResourceData();
	}

	@Override
	public void accept(BpmnVisitor visitor) {
		// TODO Auto-generated method stub

	}
	
	public String toString() {
		return super.toString()+
				data.toString()+"\n\t";
	}
}
