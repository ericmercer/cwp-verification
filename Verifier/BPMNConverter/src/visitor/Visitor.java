package visitor;

import bpmnStructure.FlowElement;
import bpmnStructure.activities.Task;
import bpmnStructure.events.BasicEndEvent;
import bpmnStructure.events.BasicStartEvent;
import bpmnStructure.gateways.ConvergingExclusiveGateway;
import bpmnStructure.gateways.ConvergingParallelGateway;
import bpmnStructure.gateways.DivergingExclusiveGateway;
import bpmnStructure.gateways.DivergingParallelGateway;
import bpmnStructure.gateways.ExclusiveGateway;

public interface Visitor {

	public void Visit(BasicStartEvent f);
	public void Visit(BasicEndEvent f);
	public void Visit(Task f);
	public void Visit(ConvergingExclusiveGateway f);
	public void Visit(DivergingExclusiveGateway f);
	public void Visit(ExclusiveGateway f);
	public void Visit(FlowElement f);
	public void Visit(ConvergingParallelGateway f);
	public void Visit(DivergingParallelGateway f);
}

