package bpmnStructure.gateways;


import bpmnStructure.Gateway;
import bpmnStructure.SequenceFlow;
import bpmnStructure.PrintMessages.PrintMessageManager;

public class ParallelGateway extends Gateway {

	/* possibly translate normal gateways into two gatways if multiple inputs */
	/* coud use a visitor to translate structure */
	public ParallelGateway(String elementId,String elementName) {
		super(elementId, elementName);
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

			/* no defaults */

			executionString += "parallel_fork(";
			executionString += this.getDefaultTokenInValue() + ", ";
			executionString += PrintMessageManager.getInstance().addMessage("parallel fork") + ", ";
			executionString += sf1.getTokenValue() + ", ";
			executionString += sf2.getTokenValue();

			executionString += ");\n";

		} else {
			// FlowElement f1 = this.sequenceFlowIn.get(0).getStart();
			// FlowElement f2 = this.sequenceFlowIn.get(1).getStart();
			// FlowElement outFlow = this.sequenceFlowOut.get(0).getEnd();
			//
			SequenceFlow sf1 = this.sequenceFlowIn.get(0);
			SequenceFlow sf2 = this.sequenceFlowIn.get(1);
			SequenceFlow soutFlow = this.sequenceFlowOut.get(0);

			PrintMessageManager pm = PrintMessageManager.getInstance();
			String s = "parallel_join(";
			s += pm.addMessage("parallel_join") + ", ";
			s += sf1.getTokenValue() + ", ";
			s += sf2.getTokenValue() + ", ";
			s += soutFlow.getTokenValue() + ")\n";
			executionString = s;
		}

		return new String[] { executionString };

	}

	
	
	// TODO: Future Work - Preprocessing
//	public ArrayList<FlowElement> splitIntoPieces() {
//
//		Gateway convergingGateway = null;
//		Gateway divergingGateway = null;
//		/* if diverging, converging, or both */
//		// Converging
//		if (this.sequenceFlowIn.size() > 1) {
//			// return;
//			convergingGateway = new ConvergingParallelGateway("Converging" + this.getName());
//		}
//
//		// Diverging
//		if (this.sequenceFlowOut.size() > 1) {
//
//			divergingGateway = new DivergingParallelGateway("Diverging" + this.getName());
//		}
//
//		if (convergingGateway != null && divergingGateway != null) {
//			convergingGateway.addDefaultSequenceFlow(divergingGateway);
//		}
//
//		ArrayList<FlowElement> newElements = new ArrayList<FlowElement>();
//		if (convergingGateway == null) {
//			convergingGateway = divergingGateway;
//		} else {
//			newElements.add(convergingGateway);
//		}
//
//		if (divergingGateway == null) {
//			divergingGateway = convergingGateway;
//		} else {
//			newElements.add(divergingGateway);
//		}
//
//		
//		// incoming and outbound flows
//		this.RedirectInboundFlowsTo(divergingGateway);
//		this.RedirectOutboundFlowsTo(convergingGateway);
//
//		return newElements;
//
//	}
	
	
}