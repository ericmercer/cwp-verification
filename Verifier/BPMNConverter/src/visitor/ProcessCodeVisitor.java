package visitor;

import java.util.ArrayList;

import bpmnStructure.FlowElement;
import bpmnStructure.activities.Task;
import bpmnStructure.events.BasicEndEvent;
import bpmnStructure.events.BasicStartEvent;
import bpmnStructure.gateways.ConvergingExclusiveGateway;
import bpmnStructure.gateways.ConvergingParallelGateway;
import bpmnStructure.gateways.DivergingExclusiveGateway;
import bpmnStructure.gateways.DivergingParallelGateway;
import bpmnStructure.gateways.ExclusiveGateway;
import promela.PromelaProcess;
import promela.templates.promelaTemplate1;


public class ProcessCodeVisitor implements Visitor{

	/* Instantiate a PROMELA template class that */
	/* handles building the PROMELA file */
	public String code = "";
	private promelaTemplate1 pt = new promelaTemplate1();

	


	public void Visit(FlowElement f) {
		// TODO Auto-generated method stub
		

	}

	@Override
	public void Visit(BasicStartEvent f) {
		// TODO Auto-generated method stub
		code = pt.getStart("process"+f.getName());
		
	}

	@Override
	public void Visit(BasicEndEvent f) {
		// TODO Auto-generated method stub
		code = pt.getEnd("process"+f.getName());
	}

	@Override
	public void Visit(Task f) {
		// TODO Auto-generated method stub
		code = pt.getTaskTemplate("process"+ f.getName());
		
	}

	@Override
	public void Visit(ConvergingExclusiveGateway f) {
		// TODO Auto-generated method stub

		
	}

	@Override
	public void Visit(DivergingExclusiveGateway f) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Visit(ExclusiveGateway f) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void Visit(ConvergingParallelGateway f) {
		// TODO Auto-generated method stub
		code = pt.getConvergingAndGate("process"+f.getName());
	}
	//TODO: Currently this assumes the parallel gateway only has two branches, will need to modify here
	// or in BpmnDiagram code so that it breaks it down into multiple binary splits
	@Override
	public void Visit(DivergingParallelGateway f) {
		
		code = pt.getDivergingAndGate("process"+ f.getName());
		
	}

}
