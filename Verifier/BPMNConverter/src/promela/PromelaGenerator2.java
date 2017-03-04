package promela;

import bpmnStructure.BpmnDiagram;
import bpmnStructure.BpmnProcess;
import bpmnStructure.PrintMessages.PrintMessageManager;

public class PromelaGenerator2 {

	private static BpmnDiagram diagram;

	public PromelaGenerator2(BpmnDiagram diagram) {
		PromelaGenerator2.diagram = diagram;
	}

	public static void main(String args[]) {

		diagram = new BpmnDiagram();
		diagram.addMessageFlow("Customer", "SendOrder", "ShoppingSite", "ReceiveOrder");

		diagram.addMessageFlow("ShoppingSite", "SendStatus", "Customer", "ReceiveStatus");

		BpmnProcess customer = diagram.addProcess("Customer");

		customer = new BpmnProcess("Customer");

		customer.addStartEvent("StartOrder");
		customer.addScriptTask("chooseItem",
				"shoppingCart.msg = order select(shoppingCart.item : 0 .. MAX_ITEMS) select(shpppingCart.buyer : 0 .. MAX_BUYERS) select(shoppingCart.cost : 0 .. MAX_COST)");// TODO:Add
																																												// parameter
																																												// to
																																												// pass
																																												// in
		// script itself
		customer.addEndEvent("EndOrder");
		customer.addMessageThrowEvent("SendOrder");
		customer.addMessageCatchEvent("ReceiveStatus");
		customer.addDataObject("shoppingCart","shoppingCart", 5);

		customer.addSequenceFlow("StartOrder", "chooseItem");
		customer.addSequenceFlow("chooseItem", "SendOrder");
		customer.addSequenceFlow("SendOrder", "ReceiveStatus");
		customer.addSequenceFlow("ReceiveStatus", "EndOrder");

		BpmnProcess ss = diagram.addProcess("ShoppingSite");

		ss.addMessageStartEvent("ReceiveOrder");
		ss.addExclusiveGateway("CheckInventoryDiverge");
		ss.addExclusiveGateway("ChargeCreditCard");
		ss.addScriptTask("OutOfStockMessage", "orderStatus.msg = outOfStock");
		ss.addDataObject("orderStatus","orderStatus", 5);
		ss.addExclusiveGateway("join1");
		ss.addExclusiveGateway("join2");
		ss.addScriptTask("cardDeniedMessage", "orderStatus.msg = cardDenied");
		ss.addScriptTask("prepareItemForShipping",
				"cwpArray[cwpArrayIndex].paymentOwner = cwpArray[cwpArrayIndex].seller");
		ss.addScriptTask("shipItem", "cwpArray[cwpArrayIndex].itemOwner = cwpArray[cwpArrayIndex].buyer");
		ss.addMessageEndEvent("SendStatus");

		ss.addDataStore("CWPArray","CWPArray", 5);

		ss.addSequenceFlow("ReceiveOrder", "CheckInventoryDiverge");
		ss.addSequenceFlow("CheckInventoryDiverge", "OutOfStockMessage");
		ss.addSequenceFlow("CheckInventoryDiverge", "ChargeCreditCard");
		ss.addSequenceFlow("OutOfStockMessage", "join1");
		ss.addSequenceFlow("join1", "SendStatus");
		ss.addSequenceFlow("ChargeCreditCard", "cardDeniedMessage");
		ss.addSequenceFlow("ChargeCreditCard", "prepareItemForShipping");
		ss.addSequenceFlow("prepareItemForShipping", "shipItem");
		ss.addSequenceFlow("shipItem", "join1");
		ss.addSequenceFlow("ChargeCreditCard", "cardDeniedMessage");
		ss.addSequenceFlow("ChargeCreditCard", "prepareItemForShipping");

		PromelaGenerator2 pg = new PromelaGenerator2(diagram);
		System.out.println(pg.generatePromela());
	}

	public String generate_xor_fork(String inseq, String message, String expr1, String outseq1, String message2,
			String expr2, String outseq2, String reportChannel) {
		PrintMessageManager pm = PrintMessageManager.getInstance();
		String s = "";
		s += "xor_fork(";
		s += inseq + ",";
		s += pm.addMessage(message) + ", ";
		s += expr1 + ", ";
		s += outseq1 + ", ";
		s += pm.addMessage(message2) + ", ";
		s += expr2 + ", ";
		s += outseq2 + ", ";
		s += reportChannel + ")\n";
		return s;
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

	public String generate_parallel_fork(String message, String inseq, String outseq1, String outseq2) {
		PrintMessageManager pm = PrintMessageManager.getInstance();

		String s = "parallel_fork(";
		s += pm.addMessage(message) + ", ";
		s += inseq + ", ";
		s += outseq1 + ", ";
		s += outseq2 + ")\n";

		return s;
	}

	public String generate_end_activity(String message, String inseq, String reportChannel) {
		PrintMessageManager pm = PrintMessageManager.getInstance();
		String s = "/*End*/  in_tokens(" + inseq + ") -> \n";
		s += "   " + "print(" + pm.addMessage(message) + ")\n";
		s += "   " + "if\n";
		s += "   " + ":: " + "true"/* get all sequences counts */ + " -> \n";
		s += "      " + reportChannel + "!normal(token_id)\n";
		s += "   " + ":: else ->\n";
		s += "      " + reportChannel + "!abnormal(token_id)\n";
		s += "   " + "fi\n";
		s += "   " + "break\n";
		return s;
	}

	public String generatePromela() {
		PrintMessageManager pm = PrintMessageManager.getInstance();

		String s = "";
		s += "#include \"BPMN.pml\"\n";

		// typdefs

		// global variables (Data Stores) - needs bound
		// String s = "print(" +
		// PrintMessageManager.getInstance().addMessage("hello") + ");\n";
		// do section
		s += "do\n";
		/*
		 * s += ":: " + this.generate_xor_fork("gateway1", "Diverging Gatway",
		 * "var[token_id].isReady", "incrementCount", "test", "true",
		 * "gateway2_1", "report"); s += ":: " + this.generate_xor_join(
		 * "xor join", "gateway2_1", "gateway2_2", "subprocess");
		 * 
		 * s += ":: " + this.generate_parallel_fork("parallel fork",
		 * "parallelFork", "task1", "task2");
		 * 
		 * s += ":: " + this.generate_parallel_join("parallel join",
		 * "parallelJoin1", "parallelJoin2", "end");
		 * 
		 * s += ":: " + this.generate_end_activity("end", "end", "report");
		 * 
		 * s += ":: " + this.generate_subprocess_start("subprocess",
		 * "subprocess", "subprocess"); s += "\n"; s += ":: " +
		 * this.generate_subprocess_end("end subprocess", "subprocessEnd",
		 * "processToken", "parallelFork");
		 */

		s += "od\n";
		s += this.generate_init("main", 3);
		// init section

		return s;

	}

	// TODO: Figure out subprocess parameters
	public String generate_subprocess_start(String message, String inseq, String subprocess) {
		PrintMessageManager pm = PrintMessageManager.getInstance();

		String s = "/*start subprocess*/ in_tokens(" + inseq + ") -> \n";
		s += "   " + "print(" + pm.addMessage(message) + ")\n";
		s += "   " + "run " + subprocess + "(" + "param" + ")\n";
		return s;
	}

	public String generate_subprocess_end(String message, String subProcessReportChannel, String processToken,
			String outseq) {
		PrintMessageManager pm = PrintMessageManager.getInstance();
		String s = "/*End subprocess*/ " + subProcessReportChannel + "?msg(" + processToken + ") -> \n";
		s += "   " + "print(" + pm.addMessage(message) + ")\n";
		s += "   " + "out_tokens(" + outseq + ")\n";
		return s;
	}

	public String generate_parallel_join(String message, String inseq1, String inseq2, String outseq) {
		// TODO Auto-generated method stub
		PrintMessageManager pm = PrintMessageManager.getInstance();

		String s = "parallel_join(";
		s += pm.addMessage("parallel join") + ", ";
		s += inseq1 + ", ";
		s += inseq2 + ", ";
		s += outseq + ")\n";
		return s;
	}

	public String generate_init(String main_process, int number_of_tokens) {
		String s = "init {\n";
		s += "mytpe msg\n";
		s += "byte token_id = 0\n";
		s += "\n";
		s += "atomic {\n";

		for (int i = 0; i < number_of_tokens; i++) {
			s += "chan end" + i + " = [1] of {mtype,byte}\n";
		}
		s += "\n";
		for (int i = 0; i < number_of_tokens; i++) {
			s += "run main(end" + i + "," + i + ")\n";
		}
		s += "\n";
		s += "do\n";
		for (int i = 0; i < number_of_tokens; i++) {
			s += ":: end" + i + "?msg(token_id) -> \n";
			s += "   printf(\"%e: %d\\n\", msg, token_id)\n";
		}

		s += ":: timeout ->\n";
		s += "   break\n";
		s += "od\n";
		s += "}\n";
		return s;

	}

}
