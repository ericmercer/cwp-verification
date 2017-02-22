package bpmnStructure.PrintMessages;

import java.util.TreeMap;

/*Keeps track of Message Numbers and their translation to messages*/

public class PrintMessageManager {

	private static PrintMessageManager instance;

	private TreeMap<Integer, String> messageMapping;
	private int lastNumber;

	private PrintMessageManager() {
		messageMapping = new TreeMap<Integer, String>();
		lastNumber = 0;
	}

	public static synchronized PrintMessageManager getInstance() {
		if (instance == null) {
			instance = new PrintMessageManager();
		}
		return instance;
	}

	public String addMessage(String message) {
		int nextNumber = lastNumber++;
		messageMapping.put(nextNumber, message);
		return nextNumber + "/*" + message + "*/";
	}

	public TreeMap<Integer, String> getMapping() {
		return messageMapping;
	}

}
