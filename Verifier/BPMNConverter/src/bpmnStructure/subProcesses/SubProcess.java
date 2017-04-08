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
		String startProcess = "in_tokens(" + this.getDefaultTokenInValue() + ") -> \n";/* \n */
		startProcess += "atomic{\n";
		startProcess += "run " + this.getProcessName() + "(" + TokenId.getName() + ", " + this.getChannelName() + ")"
				+ "; \n";
		startProcess += "}\n";
		/*
		 * this might be different if there is exception handling in the future
		 */

		String waitProcess = "len(" + this.getChannelName() + ") > 0 ->\n";
		waitProcess += "atomic{\n";
		waitProcess += "mtype tempS;\n";
		waitProcess += this.getChannelName() + "?tempS;\n";
		waitProcess += "if\n";
		waitProcess += "::tempS==normal;\n";
		waitProcess += "::else -> reportChannel!tempS;\n";
		waitProcess += "fi\n";

		// FlowElement out = this.sequenceFlowOut.get(0).getEnd();
		SequenceFlow sout = this.sequenceFlowOut.get(0);
		waitProcess += "out_tokens(" + sout.getTokenValue() + ")\n";
		waitProcess += "}\n";

		/*
		 * ::in_tokens(token_NotificationSubProcess_10) -> atomic{ run
		 * process_NotificationSubProcess(cwpArrayIndex,
		 * subprocessReturnChannel_NotificationSubProcess); }
		 * 
		 * 
		 * ::len(subprocessReturnChannel_NotificationSubProcess) > 0 -> atomic{
		 * mtype tempS; subprocessReturnChannel_NotificationSubProcess?tempS; if
		 * ::tempS == normal; ::else -> reportChannel!tempS fi
		 * out_tokens(token_EndOrder_11) }
		 */
		return new String[] { startProcess, waitProcess };

		// public String[] getExecutionOptions() {
		// String startProcess = "in_tokens(" + this.getDefaultTokenInValue() +
		// ") -> ";/* \n */
		// startProcess += "atomic{\n";
		// startProcess += "run " + this.getProcessName() + "(" +
		// TokenId.getName() + ", " + this.getChannelName() + ")"
		// + "; \n";
		// startProcess += "/*wait for subprocess to return*/\n";
		// /*
		// * this might be different if there is exception handling in the
		// future
		// */
		// startProcess += "if\n";
		// startProcess += "::" + this.getChannelName() + "??" + "normal" +
		// ";\n";
		// startProcess += "::" + this.getChannelName() + "??" + "abnormal" +
		// ";\n";
		// startProcess += "reportChannel!abnormal\n";
		// startProcess += "break;\n";
		// startProcess += "::" + this.getChannelName() + "??" +
		// "xor_split_false" + ";\n";
		// startProcess += "reportChannel!xor_split_false\n";
		// startProcess += "break;\n";
		//
		// startProcess += "fi\n";
		//
		//// FlowElement out = this.sequenceFlowOut.get(0).getEnd();
		// SequenceFlow sout = this.sequenceFlowOut.get(0);
		// startProcess += "out_tokens(" + sout.getTokenValue() + ")\n";
		// startProcess += "}\n";
	}

}
