package mathflow.nodes.process;

import visitor.BpmnVisitor;
import mathflow.nodes.MFPerformer;
import mathflow.nodes.MFPerson;
import mathflow.nodes.MFSystem;

public class MFFirstProcess extends MFProcess {

	MFPerformer[] performers;
	
	public MFFirstProcess(String ID) {
		super(ID);
		callingProcess = null;
	}
	
	public void accept(BpmnVisitor visitor) {
		if (visited == false) {
			visited = true;
			visitor.preVisit(this);
			if (visitor.visit(this) == true) {
				//visit child nodes if visit() returns true
				if (start != null) {
					start.accept(visitor);
				}
			}
			visitor.endVisit(this);
			visitor.postVisit(this);
		}
	}
	
	public MFPerformer getPerformer(int index) {
		if (performers != null) {
			return performers[index];
		} else {
			return null;
		}
	}
	
	public void addPerformer(MFPerformer newPerformer) {
		if (performers == null) {
			performers = new MFPerformer[1];
			performers[0] = newPerformer;
		} else {
			MFPerformer[] temp = new MFPerformer[performers.length+1];
			for (int i=0; i<performers.length; i++) {
				temp[i] = performers[i];
			}
			temp[performers.length] = newPerformer;
			performers = temp;
		}
	}

	public String toString() {
		String result = super.toString()+
						"performers= "+performersToString()+"\n\t";
		return result;
	}
	
	private String performersToString() {
		String result = "[";
		if (performers != null) {	
			for (int i=0; i<performers.length; i++) {
				result = result + performers[i].name;
				if (i != performers.length-1) {
					result = result + ",\n\t\t";
				}
			}
		}
		result = result + "]";
		return result;
	}
}
