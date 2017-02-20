package awk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

public class AWKconverter {
	
	private TreeMap<String, String> dictionary;
	
	public static void main(String args[]) {
		AWKconverter convert = new AWKconverter("awks/dictionary.txt");
		convert.toText();
		return;
	}
	
	public AWKconverter(String fileName) {
		dictionary = new TreeMap<>();
		importDictionary(fileName);
	}
	
	private void importDictionary(String fileName) {
		
		try {
			Scanner scanner = new Scanner( new BufferedReader( new FileReader(fileName) ) );
			String key = "", value = "";
			while(scanner.hasNext()) {
				key = scanner.nextLine();
				value = scanner.nextLine();
				dictionary.put(key, value);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void toText() {
		try {
			PrintWriter writer = new PrintWriter( new BufferedWriter( new FileWriter("awks/awkScript.txt") ) );
			
			writer.println("#!/bin/awk -f");
			writer.println();
			writer.println("BEGIN { print \"START\" }");
			
			for(Entry<String, String> entry: dictionary.entrySet()) {
				writer.print("\t/");
				writer.print(entry.getKey());
				writer.print("/ { print \"");
				writer.print(entry.getValue());
				writer.println("\" }");
			}
			
			writer.println("END { print \"STOP\" }");
			writer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
