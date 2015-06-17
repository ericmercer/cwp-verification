package mathflow.nodes.event;

import mathflow.nodes.MFPerformer;

public class MFTimerData {
	
	public String timeDuration;
	
	public String delayType;
	public String constantDelayTime;
	public String distributionType;
	public String distributionMin;
	public String distributionMean;
	public String distributionMax;
	public MFPerformer[] performers;
	public String[] inputNames;
	public String[] inputAttrs;
	public String[] inputValues;
	public String[] outputNames;
	public String[] outputAttrs;
	public String[] outputValues;
	
	public MFTimerData() {
		// TODO Auto-generated constructor stub
	}
	
	public String toString() {
		return "{timeDuration= "+timeDuration+"\n\t\t"+
				"delayType= "+delayType+"\n\t\t"+
				"distributionType= "+distributionType+"\n\t\t"+
				"distribution= ["+distributionMin+", "+distributionMean+", "+distributionMax+"]\n\t\t"+
				"performers= "+arrayToString(performers)+"\n\t\t"+
				"inputs= "+arrayToString(inputNames)+"\n\t\t"+
				"outputs= "+arrayToString(outputNames)+"}";

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
