package bpmnStructure.gateways;

import java.util.ArrayList;

import bpmnStructure.FlowElement;
import bpmnStructure.Gateway;

public class ParallelGateway extends Gateway {

	/* possibly translate normal gateways into two gatways if multiple inputs */
	/* coud use a visitor to translate structure */
	public ParallelGateway(String name) {
		super(name);
	}

	public ArrayList<FlowElement> splitIntoPieces() {

		Gateway convergingGateway = null;
		Gateway divergingGateway = null;
		/* if diverging, converging, or both */
		// Converging
		if (this.sequenceFlowIn.size() > 1) {
			// return;
			convergingGateway = new ConvergingParallelGateway("Converging" + this.getName());
		}

		// Diverging
		if (this.sequenceFlowOut.size() > 1) {

			divergingGateway = new DivergingParallelGateway("Diverging" + this.getName());
		}

		if (convergingGateway != null && divergingGateway != null) {
			convergingGateway.addSequenceFlow(divergingGateway);
		}

		ArrayList<FlowElement> newElements = new ArrayList<FlowElement>();
		if (convergingGateway == null) {
			convergingGateway = divergingGateway;
		} else {
			newElements.add(convergingGateway);
		}

		if (divergingGateway == null) {
			divergingGateway = convergingGateway;
		} else {
			newElements.add(divergingGateway);
		}

		// TODO: Make sure this actually works when the gateway as multiple
		// incoming and outbound flows
		this.RedirectInboundFlowsTo(divergingGateway);
		this.RedirectOutboundFlowsTo(convergingGateway);

		return newElements;

	}
	
	public String getProcessTemplateName(){
		return "generic_parallel_gateway";
	}
}