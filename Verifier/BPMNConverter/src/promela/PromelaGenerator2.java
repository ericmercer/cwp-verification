package promela;

import bpmnStructure.BpmnDiagram;
import bpmnStructure.BpmnProcess;
import bpmnStructure.PrintMessages.PrintMessageManager;
import bpmnStructure.dataTypes.*;
import bpmnStructure.exceptions.PromelaTypeSizeException;
import bpmnStructure.subProcesses.SubProcess;

public class PromelaGenerator2 {

	private static BpmnDiagram diagram;

	public PromelaGenerator2(BpmnDiagram diagram) {
		PromelaGenerator2.diagram = diagram;
	}

	public static void main(String args[]) throws PromelaTypeSizeException {

		diagram = new BpmnDiagram();

		/******* Define Types ***************/
		// TODO: Figure out how to add these in any order and still
		// put them in promela in the right order for dependencies to work

		PromelaTypeDef cwtype = diagram.addTypeDef("cwpType");

		int MAX_SELLERS = 280;
		int MAX_BUYERS = 255;
		int MAX_ITEM = 255;
		int MAX_AMOUNT = 255;
		int MAX_ITEMOWNER = 255;
		int MAX_PAYMENTOWNER = 255;

		cwtype.addPromelaType("seller", new PositiveIntType(MAX_SELLERS, 0));
		cwtype.addPromelaType("buyer", new PositiveIntType(MAX_BUYERS, 0));
		cwtype.addPromelaType("item", new PositiveIntType(MAX_ITEM, 0));
		cwtype.addPromelaType("amount", new PositiveIntType(MAX_AMOUNT, 0));
		cwtype.addPromelaType("itemOwner", new PositiveIntType(MAX_ITEMOWNER, 0));
		cwtype.addPromelaType("paymentOwner", new PositiveIntType(MAX_PAYMENTOWNER, 0));

		// cwtype.addPromelaType("v7", innertype, 4);

		/* how to add an array */
		cwtype.addPromelaType("boolVal", new BoolType(true));

		PromelaTypeDef msgType = diagram.addTypeDef("msgType");
		msgType.addPromelaType("msg",
				new MtypeType(new String[] { "order", "outOfStock", "shipped", "cardDenied" }, "shipped"));
		int MAX_ITEM2 = 255;
		int MAX_COST = 255;
		int MAX_BUYERS2 = 255;
		msgType.addPromelaType("item", new PositiveIntType(MAX_ITEM2), 0);
		msgType.addPromelaType("cost", new PositiveIntType(MAX_COST, 0));
		msgType.addPromelaType("buyer", new PositiveIntType(MAX_BUYERS2, 0));

		/******* Define Processes ***************/
		BpmnProcess customer = diagram.addProcess("Customer");
		customer.addDataObject("shoppingCart", msgType, 0);

		customer.addStartEvent("StartOrder");
		String code1 = "shoppingCart.msg = order;\n";
		code1 += "byte temp;\n";
		code1 += "select(temp : 0 .. MAX_ITEM);\n";
		code1 += "shoppingCart.item = temp;\n";
		code1 += "select(temp : 0 .. MAX_BUYER);\n";
		code1 += "shoppingCart.item = temp;\n";
		code1 += "select(temp : 0 .. MAX_COST);\n";
		code1 += "shoppingCart.item = temp;\n";
		customer.addScriptTask("chooseItem", code1);
		// parameter
		// pass
		// in
		// script itself
		customer.addEndEvent("EndOrder");
		customer.addMessageThrowEvent("SendOrder", "shoppingCart");
		customer.addMessageCatchEvent("ReceiveStatus", "shoppingCart");

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

		notificationsSubProcess.addSequenceFlow("StartNofications", "parallelGatewayNotifDiverging");
		notificationsSubProcess.addSequenceFlow("parallelGatewayNotifDiverging", "EmailUser");
		notificationsSubProcess.addSequenceFlow("parallelGatewayNotifDiverging", "EmailStore");
		notificationsSubProcess.addSequenceFlow("EmailUser", "parallelGatewayNotifConverging");
		notificationsSubProcess.addSequenceFlow("EmailStore", "parallelGatewayNotifConverging");
		notificationsSubProcess.addSequenceFlow("parallelGatewayNotifConverging", "EndNotifications");

		customer.addSequenceFlow("ReceiveStatus", "NotificationSubProcess");
		customer.addSequenceFlow("NotificationSubProcess", "EndOrder");

		BpmnProcess ss = diagram.addProcess("ShoppingSite");
		ss.addDataObject("orderStatus", msgType, 0);

		ss.addMessageStartEvent("ReceiveOrder", "orderStatus");
		ss.addExclusiveGateway("CheckInventoryDiverge");
		ss.addExclusiveGateway("ChargeCreditCard");
		ss.addScriptTask("OutOfStockMessage", "orderStatus.msg = outOfStock;");

		ss.addExclusiveGateway("join1");
		ss.addExclusiveGateway("join2");
		ss.addScriptTask("cardDeniedMessage", "orderStatus.msg = cardDenied;");
		ss.addScriptTask("prepareItemForShipping",
				"cwpArray[cwpArrayIndex].paymentOwner = cwpArray[cwpArrayIndex].seller;");
		ss.addScriptTask("shipItem", "cwpArray[cwpArrayIndex].itemOwner = cwpArray[cwpArrayIndex].buyer;");
		ss.addMessageEndEvent("SendStatus", "orderStatus");

		diagram.addDataStore("cwpArray", cwtype, 4);

		ss.addSequenceFlow("ReceiveOrder", "CheckInventoryDiverge");
		ss.addSequenceFlow("CheckInventoryDiverge", "OutOfStockMessage", "false /*outOfStock*/");
		ss.addDefaultSequenceFlow("CheckInventoryDiverge", "ChargeCreditCard");

		ss.addSequenceFlow("OutOfStockMessage", "join2");
		ss.addSequenceFlow("join1", "join2");

		ss.addSequenceFlow("cardDeniedMessage", "join1");
		ss.addSequenceFlow("shipItem", "join1");

		ss.addSequenceFlow("join2", "SendStatus");
		ss.addSequenceFlow("ChargeCreditCard", "cardDeniedMessage");
		ss.addSequenceFlow("ChargeCreditCard", "prepareItemForShipping");
		ss.addSequenceFlow("prepareItemForShipping", "shipItem");

		// Add Message Flows last

		diagram.addMessageFlow("MessageFlow1", customer, "SendOrder", ss, "ReceiveOrder", msgType);

		diagram.addMessageFlow("MessageFlow2", ss, "SendStatus", customer, "ReceiveStatus", msgType);

		PromelaGenerator2 pg = new PromelaGenerator2(diagram);

		System.out.println(pg.generatePromela(1));
		PrintMessageManager.getInstance().generateAwkScript();

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
		// Moving this to be global
		// s += "chan end[" + number_of_tokens + "] = [1] of {mtype}\n";

		s += "\n";
		s += "mtype msg;\n";
		
		String main_process = "";
		for (BpmnProcess bp: diagram.getAllProcesses()){
			if (!bp.isMessageInitiated() && !(bp instanceof SubProcess)){
				main_process = bp.getProcessName();
			}
		}
		
		for (int i = 0; i < number_of_tokens; i++) {
			s += "run "+main_process;
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
