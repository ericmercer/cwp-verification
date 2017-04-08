package promela;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import bpmnStructure.BpmnDiagram;
import bpmnStructure.BpmnProcess;
import bpmnStructure.PrintMessages.PrintMessageManager;
import bpmnStructure.dataTypes.BoolType;
import bpmnStructure.dataTypes.MtypeType;
import bpmnStructure.dataTypes.PositiveIntType;
import bpmnStructure.dataTypes.PromelaTypeDef;

public class BpmnToPromela {

	public static void main(String[] args) {
		// directory to write to
		// get BpmnDiagram / give files needed for BPMN import
		// ask for number of instances you want to run it for
		
		/*number of instances to run if no datastore is set*/
		int default_number_of_instances = 1;
		

		String beginningPath = "C:\\Users\\jvisker\\Documents\\";
		BpmnDiagram bpmnMemoryStructure = getManualBpmn();
		PromelaGenerator pg = new PromelaGenerator(bpmnMemoryStructure);
		
		
		int dataStoreSize = bpmnMemoryStructure.getDataStoreSize();

		String promelaString = pg.generatePromela(dataStoreSize>0?dataStoreSize:default_number_of_instances);
		String awkScript = PrintMessageManager.getInstance().generateAwkScript();

		writeStringToFile(promelaString, beginningPath + "generatedPromela.pml");
		writeStringToFile(awkScript, beginningPath + "messageTranslation.awk");

	}

	public static void writeStringToFile(String fileString, String path) {

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(path));
			out.write(fileString); // Replace with the string
									// you are trying to write
			out.close();
		} catch (IOException e) {
			System.out.println("Exception ");

		}
	}

	public static BpmnDiagram getManualBpmn() {
		BpmnDiagram diagram = new BpmnDiagram();

		/******* Define Types ***************/
		// TODO: Figure out how to add these in any order and still
		// put them in promela in the right order for dependencies to work

		PromelaTypeDef cwtype = diagram.addTypeDef("cwpType");

		int maxValue = 0;
		
		int MAX_SELLERS = maxValue;
		int MAX_BUYERS = maxValue;
		int MAX_ITEM = maxValue;
		int MAX_AMOUNT = maxValue;
		int MAX_ITEMOWNER = maxValue;
		int MAX_PAYMENTOWNER = maxValue;
		int MAX_ITEM2 = maxValue;
		int MAX_COST = maxValue;
		int MAX_BUYERS2 = maxValue;
		
		
		cwtype.addPromelaType("seller", new PositiveIntType(MAX_SELLERS, 0));
		cwtype.addPromelaType("buyer", new PositiveIntType(MAX_BUYERS, 0));
		cwtype.addPromelaType("item", new PositiveIntType(MAX_ITEM, 0));
		cwtype.addPromelaType("amount", new PositiveIntType(MAX_AMOUNT, 0));
		cwtype.addPromelaType("itemOwner", new PositiveIntType(MAX_ITEMOWNER, 0));
		cwtype.addPromelaType("paymentOwner", new PositiveIntType(MAX_PAYMENTOWNER, 0));
		cwtype.addPromelaType("boolVal", new BoolType(true));

		
		
		diagram.addDataStore("cwpArray", cwtype, 3);
		
		PromelaTypeDef msgType = diagram.addTypeDef("msgType");
		msgType.addPromelaType("msg",
				new MtypeType(new String[] { "order", "outOfStock", "shipped", "cardDenied" }, "shipped"));

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


		return diagram;
	}

}
