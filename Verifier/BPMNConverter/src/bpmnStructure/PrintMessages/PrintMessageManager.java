package bpmnStructure.PrintMessages;

import java.util.Map.Entry;
import java.util.TreeMap;

import awk.AWKconverter;

/*Keeps track of Message Numbers and their translation to messages*/

public class PrintMessageManager {

	private static PrintMessageManager instance;

	private TreeMap<Integer, String> messageMapping;
	private int lastNumber;

	private PrintMessageManager() {
		messageMapping = new TreeMap<Integer, String>();
		lastNumber = 1000;
	}

	public static synchronized PrintMessageManager getInstance() {
		if (instance == null) {
			instance = new PrintMessageManager();
		}
		return instance;
	}

	public String addMessage(String message) {
		int nextNumber = lastNumber--;
		messageMapping.put(nextNumber, message);
		return nextNumber + "/*" + message + "*/";
	}

	public TreeMap<Integer, String> getMapping() {
		return messageMapping;
	}

	public String generateAwkScript() {
		AWKconverter converter = new AWKconverter();
		for (Entry<Integer, String> entry : messageMapping.entrySet()) {
			Integer key = entry.getKey();
			String value = entry.getValue();

			
			converter.addKeyPair("message: " + String.valueOf(key), value);
		}

		return converter.toText();
	}

}
