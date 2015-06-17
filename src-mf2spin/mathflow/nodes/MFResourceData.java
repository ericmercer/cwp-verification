package mathflow.nodes;

public class MFResourceData {

	public String resourceType;
	
	public String name;
	public double capacity;
	public String capacityType;
	public String assocClassName;
	
	public double costPerHourWhenBusy;
	public double costPerUse;
	public double costPerHourWhenIdle;
	
	public String toString() {
		return "{resource type= "+resourceType+"\n\t"+
				"name= "+name+"\n\t"+
				"capacity= "+capacity+"\n\t"+
				"capacity type= "+capacityType+"\n\t"+
				"associated class name= "+assocClassName+"\n\t"+
				"cost per hour when busy= "+costPerHourWhenBusy+"\n\t"+
				"cost per use= "+costPerUse+"\n\t"+
				"costPerHourWhenIdle= "+costPerHourWhenIdle+"}";
	}
}
