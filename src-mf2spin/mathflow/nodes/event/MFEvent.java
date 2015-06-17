package mathflow.nodes.event;

import mathflow.nodes.MFControlNode;
import mathflow.nodes.process.MFProcess;

public abstract class MFEvent extends MFControlNode {
	
	public MFProcess parentProcess;
	
	public MFEvent(String ID) {
		super(ID);
	}
	
	public String toString() {
		return super.toString()+
				"parent process= "+parentProcessToString()+"\n\t";
	}
	
	private String parentProcessToString() {
		if (parentProcess == null) {
			return "";
		} else {
			return parentProcess.name;
		}
	}
}
