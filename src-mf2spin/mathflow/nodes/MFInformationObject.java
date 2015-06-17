package mathflow.nodes;

public abstract class MFInformationObject extends MFControlNode {

	public MFInfoObjData data;
	
	public MFInformationObject(String ID) {
		super(ID);
		data = new MFInfoObjData();
	}
	
	public String toString() {
		return super.toString()+
				data.toString()+"\n\t";
	}
}
