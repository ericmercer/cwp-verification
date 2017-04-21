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
		convert.toFile("awks/awkScript.txt");
		return;
	}

	public void addKeyPair(String key, String value) {
		dictionary.put(key, value);
	}

	public AWKconverter(String fileName) {
		dictionary = new TreeMap<>();
		importDictionary(fileName);
	}

	public AWKconverter() {
		dictionary = new TreeMap<>();

	}

	private void importDictionary(String fileName) {

		try {
			Scanner scanner = new Scanner(new BufferedReader(new FileReader(fileName)));
			String key = "", value = "";
			while (scanner.hasNext()) {
				key = scanner.nextLine();
				value = scanner.nextLine();
				dictionary.put(key, value);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void toFile(String path) {
		try {
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(path)));

			writer.print(this.toText());	
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String toText() {

		StringBuilder builder = new StringBuilder();
		String newline = "\n";

		builder.append("#!/bin/awk -f").append(newline);
		builder.append(newline);
		builder.append("BEGIN {}").append(newline);
		;
		builder.append("{").append(newline);
		;

		for (Entry<String, String> entry : dictionary.entrySet()) {

			builder.append("gsub(/" + entry.getKey() + "/, \"" + entry.getValue() + "\");").append(newline);
			;

		}
		builder.append("print;").append(newline);
		builder.append("}").append(newline);
		builder.append("END {}").append(newline);

		return builder.toString();
	}
	// This code didn't quite work for me
	// public void toText(String path) {
	// try {
	// PrintWriter writer = new PrintWriter( new BufferedWriter( new
	// FileWriter(path) ) );
	//
	// writer.println("#!/bin/awk -f");
	// writer.println();
	// writer.println("BEGIN { print \"START\" }");
	//
	// for(Entry<String, String> entry: dictionary.entrySet()) {
	// writer.print("\t/");
	// writer.print(entry.getKey());
	// writer.print("/ { print \"");
	// writer.print(entry.getValue());
	// writer.println("\" }");
	// }
	//
	// writer.println("END { print \"STOP\" }");
	// writer.close();
	//
	// } catch (IOException e) {
	//
	// e.printStackTrace();
	// }
	// }

}
