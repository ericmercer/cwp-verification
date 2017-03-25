package promela;

import bpmnStructure.BpmnDiagram;
import bpmnStructure.BpmnProcess;
import bpmnStructure.PrintMessages.PrintMessageManager;
import bpmnStructure.dataTypes.*;
import bpmnStructure.exceptions.PromelaTypeSizeException;

public class PromelaGenerator2 {

	private static BpmnDiagram diagram;

	public PromelaGenerator2(BpmnDiagram diagram) {
		PromelaGenerator2.diagram = diagram;
	}

	public static void main(String args[]) throws PromelaTypeSizeException {

		diagram = new BpmnDiagram();

		/******* Define Types ***************/
		//TODO: Figure out how to add these in any order and still 
		//put them in promela in the right order for dependencies to work
		PromelaTypeDef innertype = diagram.addTypeDef("innerType1");
		innertype.addPromelaType("seller",new PositiveIntType( 24, 0));
		PromelaTypeDef cwtype = diagram.addTypeDef("cwpType");
		
		int MAX_SELLERS = 280;
		int MAX_BUYERS = 255;
		int MAX_ITEM = 255;
		int MAX_AMOUNT = 255;
		int MAX_ITEMOWNER = 255;
		int MAX_PAYMENTOWNER = 255;

		cwtype.addPromelaType("seller",new PositiveIntType( MAX_SELLERS, 0));
		cwtype.addPromelaType("buyer",new PositiveIntType( MAX_BUYERS, 0));
		cwtype.addPromelaType("item",new PositiveIntType( MAX_ITEM, 0));
		cwtype.addPromelaType("amount", new PositiveIntType(MAX_AMOUNT, 0));
		cwtype.addPromelaType("itemOwner",new PositiveIntType( MAX_ITEMOWNER, 0));
		cwtype.addPromelaType("paymentOwner",new PositiveIntType( MAX_PAYMENTOWNER, 0));

	
		cwtype.addPromelaType("v7",innertype,4);

		/* how to add an array */
		cwtype.addPromelaType("boolVal", new BoolType(true));

		PromelaTypeDef msgType = diagram.addTypeDef("msgType");
		msgType.addPromelaType("msg",new MtypeType( new String[] { "order", "outOfStock", "shipped" },"shipped"));
		int MAX_ITEM2 = 255;
		int MAX_COST = 255;
		int MAX_BUYERS2 = 255;
		msgType.addPromelaType("item",new PositiveIntType( MAX_ITEM2, 2),3);
		msgType.addPromelaType("cost",new PositiveIntType( MAX_COST, 0));
		msgType.addPromelaType("buyer",new PositiveIntType( MAX_BUYERS2, 0));

		/******* Define Processes ***************/
		BpmnProcess customer = diagram.addProcess("Customer");

	

		customer.addStartEvent("StartOrder");
		customer.addScriptTask("chooseItem",
				"shoppingCart.msg = order select(shoppingCart.item : 0 .. MAX_ITEMS) select(shpppingCart.buyer : 0 .. MAX_BUYERS) select(shoppingCart.cost : 0 .. MAX_COST)");// TODO:Add
																																												// parameter
		// pass
		// in
		// script itself
		customer.addEndEvent("EndOrder");
		customer.addMessageThrowEvent("SendOrder");
		customer.addMessageCatchEvent("ReceiveStatus");
		//customer.addDataObject("shoppingCart", "shoppingCart", 5);
		customer.addDataObject("shoppingCart", cwtype, 5);
		
		customer.addSequenceFlow("StartOrder", "chooseItem");
		customer.addSequenceFlow("chooseItem", "SendOrder");
		customer.addSequenceFlow("SendOrder", "ReceiveStatus");

		BpmnProcess notificationsSubProcess = customer.addNormalSubProcess("NotificationSubProcess");
		notificationsSubProcess.addStartEvent("StartNofications");
		notificationsSubProcess.addParallelGateway("parallelGatewayNotifDiverging");
		notificationsSubProcess.addTask("EmailUser");
		notificationsSubProcess.addTask("EmailStore");
		notificationsSubProcess.addParallelGateway("parallelGatewayNotifConverging");
		notificationsSubProcess.addEndEvent("EndNotifications");

		customer.addSequenceFlow("ReceiveStatus", "NotificationSubProcess");
		customer.addSequenceFlow("NotificationSubProcess", "EndOrder");

		BpmnProcess ss = diagram.addProcess("ShoppingSite");

		ss.addMessageStartEvent("ReceiveOrder");
		ss.addExclusiveGateway("CheckInventoryDiverge");
		ss.addExclusiveGateway("ChargeCreditCard");
		ss.addScriptTask("OutOfStockMessage", "orderStatus.msg = outOfStock");
		ss.addDataObject("orderStatus", msgType, 5);
		ss.addExclusiveGateway("join1");
		ss.addExclusiveGateway("join2");
		ss.addScriptTask("cardDeniedMessage", "orderStatus.msg = cardDenied");
		ss.addScriptTask("prepareItemForShipping",
				"cwpArray[cwpArrayIndex].paymentOwner = cwpArray[cwpArrayIndex].seller");
		ss.addScriptTask("shipItem", "cwpArray[cwpArrayIndex].itemOwner = cwpArray[cwpArrayIndex].buyer");
		ss.addMessageEndEvent("SendStatus");

		diagram.addDataStore("val", cwtype, 4);

		ss.addSequenceFlow("ReceiveOrder", "CheckInventoryDiverge");
		ss.addSequenceFlow("CheckInventoryDiverge", "OutOfStockMessage", "false /*outOfStock*/");
		ss.addDefaultSequenceFlow("CheckInventoryDiverge", "ChargeCreditCard");

		ss.addSequenceFlow("OutOfStockMessage", "join1");
		ss.addSequenceFlow("join1", "SendStatus");
		ss.addSequenceFlow("ChargeCreditCard", "cardDeniedMessage", "false /*cardDenied*/");
		ss.addDefaultSequenceFlow("ChargeCreditCard", "prepareItemForShipping");
		ss.addSequenceFlow("prepareItemForShipping", "shipItem");
		ss.addSequenceFlow("shipItem", "join1");


		// Add Message Flows last

		diagram.addMessageFlow("MessageFlow1", customer, "SendOrder", ss, "ReceiveOrder", msgType);

		diagram.addMessageFlow("MessageFlow2", ss, "SendStatus", customer, "ReceiveStatus", msgType);

		PromelaGenerator2 pg = new PromelaGenerator2(diagram);

		System.out.println(pg.generatePromela());
		PrintMessageManager.getInstance().generateAwkScript();

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


		String typeDefs = diagram.typeManager.generateTypeDefString() + "\n";
		String constantDefinitions =  PromelaConstants.generateConstantString() + "\n";
		
		String s = "";
		s += "#include \"BPMN.pml\"\n\n";
		s += "/*definitions*/\n";
	
		s += constantDefinitions;
		s += typeDefs;
		
		s += diagram.getProcTypes();
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

	/* all start events live in the init */
	public String generate_init(String main_process, int number_of_tokens) {
		String s = "init {\n";
	
		s += this.getGlobalVariables();
		
		s += "atomic {\n";

		for (int i = 0; i < number_of_tokens; i++) {
			s += "chan end" + i + " = [1] of {mtype,byte}\n";
		}
		s += "\n";
		for (int i = 0; i < number_of_tokens; i++) {
			s += "run main(end" + i + "," + i + ");\n";
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

	private String getGlobalVariables() {
		
		return diagram.getGlobalVariables();
	}

}
