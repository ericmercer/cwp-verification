package promela;

import bpmnStructure.BpmnDiagram;
import bpmnStructure.BpmnProcess;
import bpmnStructure.PrintMessages.PrintMessageManager;
import bpmnStructure.dataTypes.*;

import bpmnStructure.subProcesses.SubProcess;

public class PromelaGenerator {

	private static BpmnDiagram diagram;

	public PromelaGenerator(BpmnDiagram diagram) {
		PromelaGenerator.diagram = diagram;
	}


	public String generatePromela(int number_of_tokens) {
		PrintMessageManager pm = PrintMessageManager.getInstance();

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
		// init section

		return s;

	}

	/* all start events live in the init */
	public String generate_init(BpmnDiagram diagram, int number_of_tokens) {
		String s = "";
		s += "init {\n";

		s += "atomic {\n";
		s += "chan end[" + number_of_tokens + "] = [1] of {mtype}\n";

		s += "\n";
		s += "mtype msg;\n";

		String main_process = "";
		for (BpmnProcess bp : diagram.getAllProcesses()) {
			if (!bp.isMessageInitiated() && !(bp instanceof SubProcess)) {
				main_process = bp.getProcessName();
			}
		}

		for (int i = 0; i < number_of_tokens; i++) {
			s += "run " + main_process;
			s += "(" + i + ", end[" + i + "]);\n";
		}
		s += "\n";
		s += "do\n";
		for (int i = 0; i < number_of_tokens; i++) {
			s += ":: end[" + i + "]?msg -> \n";
			s += "   printf(\"%e: %d\\n\", msg, " + i + ")\n";
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
