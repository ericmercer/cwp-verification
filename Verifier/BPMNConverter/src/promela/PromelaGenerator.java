package promela;

import bpmnStructure.BpmnDiagram;
import bpmnStructure.FlowElement;
import promela.templates.promelaTemplate1;
import visitor.ProcessCodeVisitor;

public class PromelaGenerator {

	BpmnDiagram diagram;

	public PromelaGenerator(BpmnDiagram diagram) {
		this.diagram = diagram;
	}

	public String generatePromela() {
		// Generate the PROMELA code as a string

		promelaTemplate1 pt = new promelaTemplate1();
		String channels = "";
		String runCommands = "";
		// String proctypeFunctions = "";
		for (FlowElement f : diagram.getFlowelements()) {
			channels += pt.getProcessChannel(f);
			runCommands += pt.getProcessRunCommand(f);
			ProcessCodeVisitor codeVisitor = new ProcessCodeVisitor();
			f.accept(codeVisitor);

			// proctypeFunctions += codeVisitor.code;

		}

		return pt.getFoundationalStructure(channels, runCommands, "processChannel" + diagram.getStart().getName());
	}

}
