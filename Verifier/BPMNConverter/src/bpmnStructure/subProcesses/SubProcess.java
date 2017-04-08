package bpmnStructure.subProcesses;

import bpmnStructure.BpmnProcess;
import bpmnStructure.FlowElement;
import bpmnStructure.SequenceFlow;
import bpmnStructure.TokenId;
import bpmnStructure.PrintMessages.PrintMessageManager;

public class SubProcess extends BpmnProcess {

	/*
	 * Rules
	 * 
	 * A subprocess must have a report channel to return
	 */
	public SubProcess(String elementId,String elementName) {
		super(elementId, elementName);

	}

	public String getChannelName() {
		return "subprocessReturnChannel_" + this.getElementId();
	}

	@Override
	public String[] getExecutionOptions() {
		String startProcess = "in_tokens(" + this.getDefaultTokenInValue() + ") -> \n";/* \n */
		startProcess += "atomic{\n";
		startProcess += "print("+ PrintMessageManager.getInstance().addMessage(this.getElementInfo()) + ");\n";
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

		
		return new String[] { startProcess, waitProcess };

		
	}

}
