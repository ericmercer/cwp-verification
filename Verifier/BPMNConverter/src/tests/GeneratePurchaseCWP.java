package tests;

import bpmnStructure.BpmnDiagram;
import bpmnStructure.BpmnProcess;
import bpmnStructure.dataTypes.BoolType;
import bpmnStructure.dataTypes.MtypeType;
import bpmnStructure.dataTypes.PositiveIntType;
import bpmnStructure.dataTypes.PromelaTypeDef;
import bpmnStructure.exceptions.PromelaTypeSizeException;

public class GeneratePurchaseCWP {

	public static void main(String[] args) throws PromelaTypeSizeException {

		BpmnDiagram diagram = new BpmnDiagram();

		/*******Define Types***************/
		PromelaTypeDef cwtype = diagram.addTypeDef("cwpType");
		int MAX_SELLERS = 255;
		int MAX_BUYERS = 255;
		int MAX_ITEM = 255;
		int MAX_AMOUNT = 255;
		int MAX_ITEMOWNER = 255;
		int MAX_PAYMENTOWNER = 255;
		
		cwtype.addPromelaType(new PositiveIntType("seller",MAX_SELLERS,0));
		cwtype.addPromelaType(new PositiveIntType("buyer",MAX_BUYERS,0));
		cwtype.addPromelaType(new PositiveIntType("item",MAX_ITEM,0));
		cwtype.addPromelaType(new PositiveIntType("amount",MAX_AMOUNT,0));
		cwtype.addPromelaType(new PositiveIntType("itemOwner",MAX_ITEMOWNER,0));
		cwtype.addPromelaType(new PositiveIntType("paymentOwner",MAX_PAYMENTOWNER,0));
		
		/*how to add an array*/
		cwtype.addPromelaType( new BoolType("boolVal",2));

		PromelaTypeDef msgType = diagram.addTypeDef("msgType");
		msgType.addPromelaType(new MtypeType("msg", new String[] { "order", "outOfStock", "shipped" }));
		int MAX_ITEM2 = 255;
		int MAX_COST = 255;
		int MAX_BUYERS2 = 255;
		msgType.addPromelaType(new PositiveIntType("item",MAX_ITEM2,0));
		msgType.addPromelaType(new PositiveIntType("cost",MAX_COST,0));
		msgType.addPromelaType(new PositiveIntType("buyer",MAX_BUYERS2,0));

		/*******Define Processes***************/
		BpmnProcess customer = diagram.addProcess("Customer");

		customer = new BpmnProcess("Customer");

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
		customer.addDataObject("shoppingCart", "shoppingCart", 5);

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
		ss.addDataObject("orderStatus", "orderStatus", 5);
		ss.addExclusiveGateway("join1");
		ss.addExclusiveGateway("join2");
		ss.addScriptTask("cardDeniedMessage", "orderStatus.msg = cardDenied");
		ss.addScriptTask("prepareItemForShipping",
				"cwpArray[cwpArrayIndex].paymentOwner = cwpArray[cwpArrayIndex].seller");
		ss.addScriptTask("shipItem", "cwpArray[cwpArrayIndex].itemOwner = cwpArray[cwpArrayIndex].buyer");
		ss.addMessageEndEvent("SendStatus");

		ss.addDataStore("CWPArray", "CWPArray", 5);

		ss.addSequenceFlow("ReceiveOrder", "CheckInventoryDiverge");
		ss.addSequenceFlow("CheckInventoryDiverge", "OutOfStockMessage", "false /*outOfStock*/");
		ss.addDefaultSequenceFlow("CheckInventoryDiverge", "ChargeCreditCard");

		ss.addSequenceFlow("OutOfStockMessage", "join1");
		ss.addSequenceFlow("join1", "SendStatus");
		ss.addSequenceFlow("ChargeCreditCard", "cardDeniedMessage", "false /*cardDenied*/");
		ss.addDefaultSequenceFlow("ChargeCreditCard", "prepareItemForShipping");
		ss.addSequenceFlow("prepareItemForShipping", "shipItem");
		ss.addSequenceFlow("shipItem", "join1");
		ss.addSequenceFlow("ChargeCreditCard", "cardDeniedMessage");
		ss.addSequenceFlow("ChargeCreditCard", "prepareItemForShipping");

		// Add Message Flows last

		diagram.addMessageFlow("MessageFlow1", customer, "SendOrder", ss, "ReceiveOrder", msgType);

		diagram.addMessageFlow("MessageFlow2", ss, "SendStatus", customer, "ReceiveStatus", msgType);
	}

}
