package mathflow.nodes.task;

import mathflow.nodes.MFPerformer;

public class MFTaskData {
	
	public String delay;
	public String fixedCost;
	public String value;
	public String priority;
	public String[] inputNames;
	public String[] inputAttrs;
	public String[] inputValues;
	public String[] outputNames;
	public String[] outputAttrs;
	public String[] outputValues;
	public String earliestStartTime;
	public String latestStartTime;
	public boolean useDistributionFormula;
	public boolean useTimeValue;
	public String duration;
	public String timeUnit;
	public String distributionType;
	public int distributionMin;
	public int distributionMean;
	public int distributionMax;
	public MFPerformer[] performers;
	public String[] urlDescriptions;
	public String[] urlAddrs;
	
	public void addPerformer(MFPerformer performer) {
		if (performers == null) {
			performers = new MFPerformer[1];
			performers[0] = performer;
		} else {
			MFPerformer[] temp = new MFPerformer[performers.length+1];
			for (int i=0; i<performers.length; i++) {
				temp[i] = performers[i];
			}
			temp[performers.length] = performer;
			performers = temp;
		}
	}
	
	public String toString() {
		return "{delay= "+delay+",\n\t\t"+
				"fixedCost= "+fixedCost+"\n\t\t"+
				"value= "+value+"\n\t\t"+
				"priority= "+priority+"\n\t\t"+
				"inputs= "+arrayToString(inputNames)+"\n\t\t"+
				"outputs= "+arrayToString(outputNames)+"\n\t\t"+
				"earliestStartTime= "+earliestStartTime+"\n\t\t"+
				"latestStartTimes= "+latestStartTime+"\n\t\t"+
				"useDistributionFormula= "+useDistributionFormula+"\n\t\t"+
				"useTimeValue= "+useTimeValue+"\n\t\t"+
				"duration= "+duration+"\n\t\t"+
				"timeUnit= "+timeUnit+"\n\t\t"+
				"distribution = ["+distributionMin+", "+distributionMean+", "+distributionMax+"]\n\t\t"+
				"performers= "+arrayToString(performers)+"\n\t\t"+
				"urls= "+arrayToString(urlDescriptions)+"}";
	}
	
	private String arrayToString(String[] array) {
		if (array == null) {
			return "[]";
		} else {
			String result = "[";
			for (int i=0; i<array.length; i++) {
				result += array[i];
				if (i != array.length-1) {
					result += ", ";
				}
			}
			result += "]";
			return result;
		}
	}
	
	private String arrayToString(MFPerformer[] array) {
		if (array == null) {
			return "[]";
		} else {
			String result = "[";
			for (int i=0; i<array.length; i++) {
				result += array[i].name;
				if (i != array.length-1) {
					result += ", ";
				}
			}
			result += "]";
			return result;
		}
	}
}
