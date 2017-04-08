package bpmnStructure.gateways;

import bpmnStructure.Gateway;
import bpmnStructure.SequenceFlow;
import bpmnStructure.TokenId;
import bpmnStructure.PrintMessages.PrintMessageManager;

public class ExclusiveGateway extends Gateway {

	/* possibly translate normal gateways into two gateways if multiple inputs */
	/* could use a visitor to translate structure */

	public ExclusiveGateway(String elementId) {
		super(elementId);
	}

	@Override
	public String[] getExecutionOptions() {
		String executionString = "";

		if (sequenceFlowOut.size() > 1) {
			// TODO: Set up the guards to the different possible out flows
			// inline xor_fork(inseq,messageNumber1,expr1, outseq1,
			// messageNumber2,
			// expr2,outseq2,exceptionChannel){
			if (sequenceFlowOut.size() != 2) {
				System.err.println("wrong number of out flows for diverging gateway " + this.getElementId() + " "
						+ sequenceFlowOut.size());
			}
			// FlowElement f1 = this.sequenceFlowOut.get(0).getEnd();
			// FlowElement f2 = this.sequenceFlowOut.get(1).getEnd();
			SequenceFlow sf1 = this.sequenceFlowOut.get(0);
			SequenceFlow sf2 = this.sequenceFlowOut.get(1);

			boolean isDefault1 = this.sequenceFlowOut.get(0).isDefault();
			boolean isDefault2 = this.sequenceFlowOut.get(1).isDefault();

			/* no defaults */
			if (!isDefault1 && !isDefault2) {
				executionString += "xor_fork(";
				executionString += this.getDefaultTokenInValue() + ", ";
				executionString += PrintMessageManager.getInstance().addMessage(this.getElementId() + ": xor_fork first choice") + ", ";
				executionString += this.sequenceFlowOut.get(0).getExpression() + ", ";
				executionString += sf1.getTokenValue() + ", ";
				executionString += PrintMessageManager.getInstance().addMessage("xor_fork second choice") + ", ";
				executionString += this.sequenceFlowOut.get(1).getExpression() + ", ";
				executionString += sf2.getTokenValue() + ", ";
				executionString += "reportChannel,";
				executionString += TokenId.getName();
				executionString += ");\n";
			} else {

				SequenceFlow normalSequenceFlow = !isDefault1 ? this.sequenceFlowOut.get(0)
						: this.sequenceFlowOut.get(1);

				SequenceFlow defaultFlow = isDefault1 ? this.sequenceFlowOut.get(0) : this.sequenceFlowOut.get(1);

				executionString += "xor_fork_default(";
				executionString += this.getDefaultTokenInValue() + ", ";
				executionString += PrintMessageManager.getInstance().addMessage("xor_fork_default first choice") + ", ";
				executionString += normalSequenceFlow.getExpression() + ", ";
				executionString += normalSequenceFlow.getTokenValue() + ", ";
				executionString += PrintMessageManager.getInstance()
						.addMessage(this.getElementId() + ": xor_fork_default default choice") + ", ";
				executionString += defaultFlow.getTokenValue();
				executionString += ");\n";
				// inline xor_fork_default(inseq,messageNumber1,expr1, outseq1,
				// messageNumber2,defaultSeq){
			}

		} else {
			// FlowElement f1 = this.sequenceFlowIn.get(0).getStart();
			// FlowElement f2 = this.sequenceFlowIn.get(1).getStart();
			// FlowElement outFlow = this.sequenceFlowOut.get(0).getEnd();

			SequenceFlow sf1 = this.sequenceFlowIn.get(0);
			SequenceFlow sf2 = this.sequenceFlowIn.get(1);
			SequenceFlow soutFlow = this.sequenceFlowOut.get(0);

			PrintMessageManager pm = PrintMessageManager.getInstance();
			String s = "xor_join(";
			s += pm.addMessage(this.getElementId() +": xor join") + ", ";
			s += sf1.getTokenValue() + ", ";
			s += sf2.getTokenValue() + ", ";
			s += soutFlow.getTokenValue() + ")\n";
			executionString = s;
		}

		return new String[] { executionString };

	}

	public String generate_xor_join(String message, String inseq, String inseq2, String outseq) {
		PrintMessageManager pm = PrintMessageManager.getInstance();
		String s = "xor_join(";
		s += pm.addMessage(message) + ", ";
		s += inseq + ", ";
		s += inseq2 + ", ";
		s += outseq + ")\n";

		return s;
	}

}
