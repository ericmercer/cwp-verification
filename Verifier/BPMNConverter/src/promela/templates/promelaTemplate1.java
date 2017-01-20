package promela.templates;

import java.util.ArrayList;
import java.util.TreeSet;

import bpmnStructure.FlowElement;
import bpmnStructure.SequenceFlow;
import bpmnStructure.activities.Task;

public class promelaTemplate1 {

	public String getProcessChannel(String processName) {
		return "chan processChannel" + processName + " = [1] of {byte};\n";
	}

	public String getProcessRunCommand(FlowElement f) {
		String s = "";
		// TODO: Do I trust that it has the right number of flows in and out?
		s += "run " + f.getProcessTemplateName() + "(";

		s += "processChannel" + f.name + ", ";

		// for (SequenceFlow sf : f.sequenceFlowIn) {
		// //TODO:I'm not sure exactly how there can be a reference, but the
		// name is null
		// s += "processChannel" + sf.start.name + ", ";
		// }
		for (SequenceFlow sf : f.sequenceFlowOut) {
			s += "processChannel" + sf.end.name + ", ";
		}

		s += "done, terminate, taskID);\n";
		return s;

	}

	public String getFoundationalStructure(String processChannels, String processInitializers) {
		String s = "#define N 3\n";
		s += "mtype = {taskID}\n";
		s += "#include split_and_gate_only.pml\n";
		s += "#include task_only.pml\n";
		s += "#include start_only.pml\n";
		s += "#include merge_and_gate_only.pml\n";
		s += "#include end_only.pml\n";
		// s += processProctypes;

		s += "init {\n";
		s += processChannels;

		s += "chan done = [N] of {byte};\n";
		s += "chan terminate = [1] of {bool};\n";

		s += "\n";
		s += processInitializers;
		s += "\n";
		s += "byte x;\n";
		s += "{\n";
		s += "do\n";
		// s += ":: nfull(in) ->\n";

		s += "  :: nfull(processChannelstart) ->\n";
		s += "    atomic {\n";
		s += "      select(x : 1 .. N);\n";
		s += "      printf(\"send(%d)\\n\", x);\n";
		// s += "in!x;\n";
		// replace with more generic solution
		s += "      processChannelstart!x;\n";

		s += "    }\n";
		s += "  :: nfull(done) ->\n";
		s += "    atomic {\n";
		s += "    select(x : 1 .. N);\n";
		s += "    if\n";
		s += "      :: !(done??[eval(x)]) ->\n";
		s += "        done!!x;\n";
		s += "        printf(\"done(%d)\\n\", x)\n";
		s += "      :: else -> skip\n";
		s += "    fi\n";
		s += "    }\n";
		s += "  :: nfull(terminate) ->\n";
		s += "    atomic {\n";
		s += "      printf(\"terminate\\n\")\n";
		s += "      terminate!true;\n";
		s += "    }\n";
		/*
		 * s += ":: out?[_] ->\n"; s += "atomic {\n"; s += "out?x;\n"; s +=
		 * "printf(\"receive(%d)\\n\", x);\n"; s += "}\n";
		 */
		s += "od;\n";
		s += "} unless {\n";
		s += "  if\n";
		s += "    :: terminate?[_] -> printf(\"terminate(%d)\\n\", _pid);\n";
		s += "  fi;\n";
		s += "}\n";
		s += "};\n";
		return s;
	}

	public String getTaskTemplate(String processName) {
		String s = "proctype " + processName + "(chan in, out, done, terminate; mtype id) {\n";
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
		s += " }\n";
		return s;
	}

	public String getStart(String processName) {

		String s = "proctype " + processName + "(chan in,out, done, terminate; mtype id) {\n";
		s += "  byte x = 0, i = 0;\n";
		s += "  bool send1 = false, send2 = false;\n";
		s += "L0:\n";
		s += "  atomic {\n";
		s += "	in?x;\n";
		s += "  out!x;\n";
		s += "	goto L0;\n";
		s += "  } unless {\n";
		s += "	if\n";
		s += "	  :: terminate?[_] -> printf(\"terminate(%d)\\n\", _pid);\n";
		s += "	fi;\n";
		s += "  }\n";
		s += "}\n";
		return s;

	}

	public String getEnd(String processName) {

		String s = "proctype " + processName + "(chan in, done, terminate; mtype id) {\n";
		s += "  byte x = 0, i = 0;\n";
		s += "  bool send1 = false, send2 = false;\n";
		s += "L0:\n";
		s += "  atomic {\n";
		s += "	in?x;\n";
		s += "	goto L0;\n";
		s += "  } unless {\n";
		s += "	if\n";
		s += "	  :: terminate?[_] -> printf(\"terminate(%d)\\n\", _pid);\n";
		s += "	fi;\n";
		s += "  }\n";
		s += "}\n";
		return s;

	}

	public String getDivergingAndGate(String processName) {

		String s = "proctype " + processName + "(chan in, out1, out2, done, terminate; mtype id) {\n";
		s += "  byte x = 0, i = 0;\n";
		s += "  bool send1 = false, send2 = false;\n";
		s += "L0:\n";
		s += "  atomic {\n";
		s += "	in?x;\n";
		s += "	printf(\"%e(%d)\\n\", taskID, x);\n";
		s += "	if\n";
		s += "	  :: !(done??[eval(x)]) ->\n";
		s += "		 send1 = false;\n";
		s += "		 send2 = false;\n";
		s += "		 do\n";
		s += "		   :: nfull(out1) ->\n";
		s += "			  out1!x;\n";
		s += "			  send1 = true;\n";
		s += "		   :: nfull(out2) -> \n";
		s += "			  out2!x;\n";
		s += "			  send2 = true;\n";
		s += "		   :: send1 && send2 ->\n";
		s += "			  break;\n";
		s += "		 od;		 \n";
		s += "		 printf(\"send(%d)\\n\", x);\n";
		s += "	  :: done??[eval(x)] ->\n";
		s += "		 printf(\"done(%d)\\n\", x);\n";
		s += "	fi;\n";
		s += "	goto L0;\n";
		s += "  } unless {\n";
		s += "	if\n";
		s += "	  :: terminate?[_] -> printf(\"terminate(%d)\\n\", _pid);\n";
		s += "	fi;\n";
		s += "  }\n";
		s += "}\n";
		return s;

	}

	public String getConvergingAndGate(String processName) {

		String s = "proctype " + processName + "(chan in, /*in2,*/ out, done, terminate; mtype id) {\n";
		s += "  byte x = 0, i = 0;\n";
		s += "  bool send1 = false, send2 = false;\n";
		s += "L0:\n";
		s += "  atomic {\n";
		s += "	in?x;\n";
		// s += " in2?i;\n";
		s += "  out!x;\n";
		s += "	goto L0;\n";
		s += "  } unless {\n";
		s += "	if\n";
		s += "	  :: terminate?[_] -> printf(\"terminate(%d)\\n\", _pid);\n";
		s += "	fi;\n";
		s += "  }\n";
		s += "}\n";
		return s;

	}

	public String getProcessProcTypeCode(FlowElement f) {
		// TODO Auto-generated method stub

		return "";
	}

	public String getProcessProcTypeCode(Task t) {
		// TODO Auto-generated method stub
		return this.getTaskTemplate("process" + t.name);
	}

}
