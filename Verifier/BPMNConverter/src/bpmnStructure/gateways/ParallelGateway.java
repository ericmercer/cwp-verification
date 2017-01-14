package bpmnStructure.gateways;

import bpmnStructure.FlowElement;
import bpmnStructure.Gateway;
import bpmnStructure.SequenceFlow;

public class ParallelGateway extends Gateway {

	/* possibly translate normal gateways into two gatways if multiple inputs */
	/* coud use a visitor to translate structure */
	public ParallelGateway(String name) {
		super(name);
	}

	public void splitIntoPieces() {

		Gateway convergingGateway = null;
		Gateway divergingGateway = null;
		/* if diverging, converging, or both */
		// Converging
		if (this.sequenceFlowIn.size() > 1) {
//			return;
			convergingGateway = new ConvergingParallelGateway("Converging" + this.name);
		}

		// Diverging
		if (this.sequenceFlowOut.size() > 1) {

			divergingGateway = new DivergingParallelGateway("Diverging" + this.name);
		}

		if (convergingGateway != null && divergingGateway != null) {
			convergingGateway.addSequenceFlow(divergingGateway);
		}

		if (convergingGateway == null) {
//			System.out.println("here1" + this.name);
			convergingGateway = divergingGateway;
		}

		if (divergingGateway == null) {
//			System.out.println("here2" + this.name);
			divergingGateway = convergingGateway;
		}

		// Redirect incoming sequence flows to new convergingGateway
		for (SequenceFlow f : this.sequenceFlowIn) {
//			System.out.println("inflows for " + this.name + " " + f.start.name + " -> " + f.end.name);
//			System.out.println("replace " + f.start.name + " -> " + divergingGateway.name);
			f.end = divergingGateway;
			divergingGateway.sequenceFlowIn.add(f);
		}

		for (SequenceFlow f : this.sequenceFlowOut) {
//			System.out.println("outFlows for " + this.name + " " + f.start.name + " -> " + f.end.name);
//			System.out.println("replace " + f.start.name + " with " + convergingGateway.name);
			f.start = convergingGateway;
			convergingGateway.sequenceFlowOut.add(f);
		}

	}
}