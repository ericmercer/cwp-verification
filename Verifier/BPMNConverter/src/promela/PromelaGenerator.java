package promela;

import java.util.ArrayList;

import bpmnStructure.BpmnDiagram;
import bpmnStructure.BpmnProcess;
import bpmnStructure.dataTypes.*;

import bpmnStructure.subProcesses.SubProcess;

public class PromelaGenerator {

	private  BpmnDiagram diagram;

	public PromelaGenerator(BpmnDiagram diagram) {
		this.diagram = diagram;
	}

	public String generatePromela(int number_of_tokens) {

		String typeDefs = diagram.typeManager.generateTypeDefString() + "\n";
		String constantDefinitions = PromelaConstants.generateConstantString() + "\n";

		String s = "";
		s += "#include \"BPMN.pml\"\n\n";
		s += Mtypes.toPromela();

		s += "/*definitions*/\n";

		s += constantDefinitions;
		s += typeDefs;
		s += this.getGlobalVariables(number_of_tokens);

		s += diagram.getProcTypes();

		s += this.generate_init(diagram, number_of_tokens);

		return s;

	}

	/* all start events live in the init */
	public String generate_init(BpmnDiagram diagram, int number_of_tokens) {

		ArrayList<BpmnProcess> mainProcesses = new ArrayList<BpmnProcess>();
		for (BpmnProcess bp : diagram.getAllProcesses()) {
			if (!bp.isMessageInitiated()&& !(bp instanceof SubProcess) ) {
				mainProcesses.add(bp);
			}
		}
		String s = "";
		s += "init {\n";

		s += "atomic {\n";
		s += "chan end[" + (number_of_tokens * mainProcesses.size()) + "] = [1] of {mtype}\n";

		s += "\n";


		
		for (int pCounter = 0; pCounter < mainProcesses.size();pCounter++) {
			for (int i = 0; i < number_of_tokens; i++) {
				int channelNumber = pCounter * (mainProcesses.size() + 1)+ i;
				s += "run " + mainProcesses.get(pCounter).getProcessName();
				s += "(" + channelNumber + ", end[" + channelNumber + "]);\n";
			}
		}
		s += "\n";
		s += "mtype msg;\n";
		s += "do\n";
		for (int i = 0; i < number_of_tokens * mainProcesses.size(); i++) {
			s += ":: end[" + i + "]?msg -> \n";
			s += "   printf(\"FINISHED %d: %e \\n\"," +i+ ",msg"+ ")\n";
			s += "assert(msg==normal)\n";
		}

		s += ":: timeout ->\n";
		s += "   break\n";
		s += "od\n";
		s += "}\n";
		s += "}\n";
		return s;

	}

	private String getGlobalVariables(int number_of_tokens) {

		return "/*Global Variables*/\n" + diagram.getGlobalVariables(number_of_tokens);
	}

}
