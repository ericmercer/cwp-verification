package mathflow.nodes.process;

import visitor.BpmnVisitor;
import mathflow.nodes.MFControlNode;
import mathflow.nodes.MFPerson;
import mathflow.nodes.MFSystem;
import mathflow.nodes.event.MFStart;

public class MFProcess extends MFControlNode {
	
	public MFStart start;
	public MFSubprocess callingProcess;
	MFSystem[] systems;
	MFPerson[] persons;
	
	public MFProcess(String ID) {
		super(ID);
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
	
	public MFSystem getSystem(int index) {
		if (systems != null) {
			return systems[index];
		} else {
			return null;
		}
	}
	
	public MFPerson getPerson(int index) {
		if (persons != null) {
			return persons[index];
		} else {
			return null;
		}
	}
	
	public void addSystem(MFSystem newSystem) {
		if (systems == null) {
			systems = new MFSystem[1];
			systems[0] = newSystem;
		} else {
			MFSystem[] temp = new MFSystem[systems.length+1];
			for (int i=0; i<systems.length; i++) {
				temp[i] = systems[i];
			}
			temp[systems.length] = newSystem;
			systems = temp;
		}
	}
	
	public void addPerson(MFPerson newPerson) {
		if (persons == null) {
			persons = new MFPerson[1];
			persons[0] = newPerson;
		} else {
			MFPerson[] temp = new MFPerson[persons.length+1];
			for (int i=0; i<persons.length; i++) {
				temp[i] = persons[i];
			}
			temp[persons.length] = newPerson;
			persons = temp;
		}
	}
	
	public String toString() {
		return super.toString() +
				"start node= "+startToString()+"\n\t"+
				"systems= "+systemsToString()+"\n\t"+
				"persons= "+personsToString()+"\n\t";
	}
	
	private String startToString() {
		if (start == null) {
			return "";
		} else {
			return start.name;
		}
	}
	
	private String systemsToString() {
		String result = "[";
		if (systems != null) {
			for (int i=0; i<systems.length; i++) {
				result += systems[i].name;
				if (i != systems.length-1) {
					result += ",\n\t\t";
				}
			}
		}
		result += "]";
		return result;
	}
	
	private String personsToString() {
		String result = "[";
		if (persons != null) {
			for (int i=0; i<persons.length; i++) {
				result = result+persons[i].name;
				if (i != persons.length-1) {
					result = result+",\n\t\t";
				}
			}
		}
		result = result+"]";
		return result;
	}
}
