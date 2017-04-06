package bpmnStructure.subProcesses;

import bpmnStructure.BpmnProcess;
import bpmnStructure.FlowElement;
import bpmnStructure.SequenceFlow;
import bpmnStructure.TokenId;

public class SubProcess extends BpmnProcess {

	/*
	 * Rules
	 * 
	 * A subprocess must have a report channel to return
	 */
	public SubProcess(String id) {
		super(id);

	}

	public String getChannelName() {
		return "subprocessReturnChannel_" + this.getName();
	}

	@Override
	public String[] getExecutionOptions() {
		String startProcess = "in_tokens(" + this.getDefaultTokenInValue() + ") -> ";/* \n */

		startProcess += "run " + this.getProcessName() + "(" + TokenId.getName() + ", " + this.getChannelName() + ")"
				+ "; \n";
		startProcess += "/*wait for subprocess to return*/\n";
		/*
		 * this might be different if there is exception handling in the future
		 */
		startProcess += "if\n";
		startProcess += "::" + this.getChannelName() + "??" + "normal" + ";\n";
		startProcess += "::" + this.getChannelName() + "??" + "abnormal" + ";\n";
		startProcess += "reportChannel!abnormal\n";
		startProcess += "break;\n";
		startProcess += "::" + this.getChannelName() + "??" + "xor_split_false" + ";\n";
		startProcess += "reportChannel!xor_split_false\n";
		startProcess += "break;\n";

		startProcess += "fi\n";

//		FlowElement out = this.sequenceFlowOut.get(0).getEnd();
		SequenceFlow sout = this.sequenceFlowOut.get(0);
		startProcess += "out_tokens(" + sout.getTokenValue() + ")\n";

		return new String[] { startProcess };

	}

}
