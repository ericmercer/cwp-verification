package mathflow.nodes;

public abstract class MFControlNode implements ControlNode {
	
	public String id;
	public String name;
	
	public boolean visited;
	
	public MFControlNode(String ID) {
		id = ID;
		name = null;
		
		visited = false;
	}
	
	public String getID() {
		return id;
	}
	
	public String getName() {
		if (name == null) {
			return "";
		} else {
			return name;
		}
	}
	
	//name can only be set once -> all nodes should be read-only once the data is entered
	public void setName(String Name) {
		if (name == null) {
			name = Name;
		} else {
			return;
		}
	}
	
	public String toString() {
		return "node:\n\tname= "+name+"\n\t";
	}
	
}
