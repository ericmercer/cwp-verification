package bpmnStructure;

import java.util.ArrayList;

import bpmnStructure.dataTypes.PromelaTypeDef;

public class Channels {

	public static ArrayList<Channel> channels = new ArrayList<Channel>();

	public static void addChannel(String name, PromelaTypeDef type) {
		channels.add(new Channel(name, type));
	}

}
