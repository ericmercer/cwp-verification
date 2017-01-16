package promela.templates;

import java.util.ArrayList;
import java.util.TreeSet;


public class promelaTemplate1 {

	// /*
	// * -Definitions -proctypes -init +channels +run commands +main body
	// */
	//
	// TreeSet<SequenceFlow> sequences = new TreeSet<SequenceFlow>();
	// ArrayList<String> defines = new ArrayList<String>();
	// ArrayList<String> mtypes = new ArrayList<String>();
	// ArrayList<String> proctypes = new ArrayList<String>();
	// ArrayList<String> andGateChannels = new ArrayList<String>();
	// public int andGateChannelCount = 0;
	//
	// public void addChannel() {
	//
	// }
	//
	// public void addProcType(String proctype) {
	// proctypes.add(proctype);
	// }
	//
	// public void addDefine(String variable, String value) {
	// defines.add("#define " + variable + " " + value + "\n");
	//
	// }
	//
	// public void addMtype(String enumeratedType) {
	// mtypes.add(enumeratedType);
	//
	// }
	//
	// public String getProcTypes() {
	// String out = "";
	// for (String s : proctypes) {
	// out += s;
	// }
	// return out;
	// }
	//
	// public String getDefinitions() {
	// String out = "";
	// for (String s : defines) {
	// out += s;
	// }
	// return out;
	// }
	//
	// public String getMtypes() {
	// String out = "mtype = {";
	// for (String s : mtypes) {
	// out += s;
	// }
	// out += "};\n";
	// return out;
	// }
	//
	// public String getChannelsString() {
	// String out = "";
	// for (SequenceFlow s : sequences) {
	// out += " chan " + s.channelName + " = [1] of {byte};\n";
	// }
	// out += " chan done = [N] of {byte};\n";
	// out += " chan terminate = [1] of {bool};\n";
	// return out;
	// }
	//
	// private void addSequenceFlows(ArrayList<SequenceFlow> flows) {
	// for (SequenceFlow f : flows) {
	// this.addSequenceFlow(f);
	// }
	// }
	//
	// private void addSequenceFlow(SequenceFlow s) {
	// sequences.add(s);
	// }
	//
	// /*
	// * add a process that gets spawned at the onset and listens for changes to
	// * channels
	// */
	// public void addProcessToInitialStart() {
	//
	// }
	//
	// public void addManualTaskString(String nodeName /* maybe id instead? */,
	// ArrayList<SequenceFlow> inflows, ArrayList<SequenceFlow> outflows) {
	// this.addSequenceFlows(inflows);
	// this.addSequenceFlows(outflows);
	// }
	//
	// /* it will need something to indicate the channels involved */
	// public void addAndGate(String nodeName /* maybe id instead? */,
	// ArrayList<SequenceFlow> inflows, ArrayList<SequenceFlow> outflows) {
	// if (outflows.size() > 1) {
	// this.addSequenceFlows(inflows);
	// this.addSequenceFlows(outflows);
	// String proctype = "proctype " + nodeName + "_AndGate";
	// proctype += this.getStandardMethodSignature(inflows.size(),
	// outflows.size());
	// proctype += getAndGateBody(outflows);
	//
	// proctype += "}\n";
	//
	// this.addProcType(proctype);
	// } else {
	// /* merge and */
	// }
	//
	// }
	//
	// public String createAndGateChannel() {
	// String channel = "addGateChannel" + andGateChannelCount++;
	// andGateChannels.add(channel);
	// return channel;
	// }
	//
	// public String getAndGateBody(ArrayList<SequenceFlow> outflows) {
	// String out = " byte x = 0, i = 0;\n";
	// ArrayList<String> groupChannels = new ArrayList<String>();
	// for (int x = 0; x < outflows.size(); x++) {
	// groupChannels.add(createAndGateChannel());
	// }
	//
	// /*
	// * run (g0,1,g1)run (g1,2,g2)run (g2,3,g3)run (g3,4,g4)run (g4,5,6)
	// * g0!x;
	// */
	// /* TODO: add parameters for done, terminate and id */
	// for (int x = 0; x < groupChannels.size() - 2; x++) {
	// out += " run split_and_gate(" + groupChannels.get(x) + ", "
	// + outflows.get(x).channelName + ", "
	// + groupChannels.get(x + 1) + ");\n";
	// }
	// out += " run split_and_gate("
	// + groupChannels.get(groupChannels.size() - 2) + ", ";
	// out += outflows.get(outflows.size() - 2).channelName + ", ";
	// out += outflows.get(outflows.size() - 1).channelName + ");\n";
	// /* from 1 to n -1 do one template, and at N do the other two */
	//
	// out += " " + groupChannels.get(0) + "?x;\n";
	// return out;
	// }
	
	 public String getTaskTemplate() {
	 String s = "proctype task(chan in, out, done, terminate; mtype id) {\n";
	 s += " byte x = 0;\n";
	 s += " L0:\n";
	 s += " atomic {\n";
	 s += " in?x;\n";
	 s += " printf(\"%e(%d)\\n\", taskID, x);\n";
	 s += " if\n";
	 s += " :: !(done??[eval(x)]) ->\n";
	 s += " out!x;\n";
	 s += " printf(\"send(%d)\\n\", x);\n";
	 s += " :: done??[eval(x)] ->\n";
	 s += " printf(\"done(%d)\\n\", x);\n";
	 s += " fi;\n";
	 s += " goto L0;\n";
	 s += " } unless {\n";
	 s += " if\n";
	 s += " :: terminate?[_] -> printf(\"terminate(%d)\\n\", _pid);\n";
	 s += " fi;\n";
	 s += " }\n";
	 return s;
	 }
	
	 public String getSplitAndGate() {
	 String s = "proctype split_and_gate(chan in, out1, out2, done, terminate;	 mtype id) {\n"
	 + " byte x = 0, i = 0;\n"
	 + " bool send1 = false, send2 = false;\n"
	 + "L0:\n"
	 + " atomic {\n"
	 + " in?x;\n"
	 + " printf(\"%e(%d)\\n\", taskID, x);\n"
	 + " if\n"
	 + " :: !(done??[eval(x)]) ->\n"
	 + " send1 = false;\n"
	 + " send2 = false;\n"
	 + " do\n"
	 + " :: nfull(out1) ->\n"
	 + " out1!x;\n"
	 + " send1 = true;\n"
	 + " :: nfull(out2) -> \n"
	 + " out2!x;\n"
	 + " send2 = true;\n"
	 + " :: send1 && send2 ->\n"
	 + " break;\n"
	 + " od; \n"
	 + " printf(\"send(%d)\\n\", x);\n"
	 + " :: done??[eval(x)] ->\n"
	 + " printf(\"done(%d)\\n\", x);\n"
	 + " fi;\n"
	 + " goto L0;\n"
	 + " } unless {\n"
	 + " if\n"
	 + " :: terminate?[_] -> printf(\"terminate(%d)\\n\", _pid);\n"
	 + " fi;\n" + " }\n" + "}\n";
	 return s;
	 }
	//
	// public String getStandardMethodSignature(int channelsIn, int channelsOut)
	// {
	// String out = "";
	// out += "(chan ";
	// for (int inCounter = 0; inCounter < channelsIn; inCounter++) {
	// out += "in" + inCounter;
	// if (inCounter < channelsIn - 1 || channelsOut > 0) {
	// out += ", ";
	// }
	// }
	// for (int outCounter = 0; outCounter < channelsOut; outCounter++) {
	// out += "out" + outCounter;
	// if (outCounter < channelsOut - 1) {
	// out += ", ";
	// }
	// }
	//
	// out += ", done, terminate; mtype id){\n";
	// return out;
	// }
	//
	// public String initializeProcessesString() {
	//
	// return "run ...\n";
	// }
	//
	// public void createMainBody() {
	//
	// }
	//
	// public String toString() {
	//
	// /* add values not dependent on flow */
	// this.addDefine("N", "3");
	// this.addMtype("taskID");
	// this.addProcType(getSplitAndGate());
	// this.addProcType(getTaskTemplate());
	//
	// String out = this.getDefinitions();
	// out += this.getMtypes();
	// out += this.getProcTypes();
	// out += "init {\n";
	// out += this.getChannelsString();
	// out += this.initializeProcessesString();
	// out += "}\n";
	// return out;
	// }

}
