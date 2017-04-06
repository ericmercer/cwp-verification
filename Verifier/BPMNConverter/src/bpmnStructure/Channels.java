package bpmnStructure;

import java.util.ArrayList;

import bpmnStructure.dataTypes.PromelaType;


public class Channels {

	public static ArrayList<Channel> channels = new ArrayList<Channel>();

	public static void addChannel(String name, PromelaType type) {
		channels.add(new Channel(name, type));
	}

}
