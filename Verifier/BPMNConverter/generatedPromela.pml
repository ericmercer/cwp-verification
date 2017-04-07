#include "BPMN.pml"

mtype = {cardDenied, order, outOfStock, shipped};
/*definitions*/
#define MAX_AMOUNT 255
#define MAX_BUYER 255
#define MAX_COST 255
#define MAX_ITEM 255
#define MAX_ITEMOWNER 255
#define MAX_PAYMENTOWNER 255
#define MAX_SELLER 280

typedef cwpType{
short seller;
byte buyer;
byte item;
byte amount;
byte itemOwner;
byte paymentOwner;
bool boolVal = true;
};
typedef msgType{
mtype msg = shipped;
byte item;
byte cost;
byte buyer;
};

/*Global Variables*/
chan end[1] = [1] of {mtype};
cwpType cwpArray[4];
chan chan_MessageFlow1 =  [1] of {byte, msgType};
chan chan_MessageFlow2 =  [1] of {byte, msgType};
proctype process_Customer(byte cwpArrayIndex; chan reportChannel){
byte token_EndOrder_11, token_NotificationSubProcess_10, token_ReceiveStatus_3, token_SendOrder_2, token_chooseItem_1;
chan subprocessReturnChannel_NotificationSubProcess= [1] of {mtype};
msgType shoppingCart;
byte token_StartOrder = 1;
/*process transition do loop*/
atomic{
do
::in_tokens(token_EndOrder_11) -> break;
::in_tokens(token_NotificationSubProcess_10) -> run process_NotificationSubProcess(cwpArrayIndex, subprocessReturnChannel_NotificationSubProcess); 
/*wait for subprocess to return*/
if
::subprocessReturnChannel_NotificationSubProcess??normal;
::subprocessReturnChannel_NotificationSubProcess??abnormal;
reportChannel!abnormal
break;
::subprocessReturnChannel_NotificationSubProcess??xor_split_false;
reportChannel!xor_split_false
break;
fi
out_tokens(token_EndOrder_11)
::token_ReceiveStatus_3 > 0 && chan_MessageFlow2??[eval(cwpArrayIndex), shoppingCart];
chan_MessageFlow2??eval(cwpArrayIndex), shoppingCart;
decrement_tokens(token_ReceiveStatus_3);
print(1000/*ReceiveStatus*/);
out_tokens(token_NotificationSubProcess_10)
::in_tokens(token_SendOrder_2) -> 
print(999/*SendOrder*/);
run process_ShoppingSite(cwpArrayIndex, end[cwpArrayIndex], shoppingCart)
out_tokens(token_ReceiveStatus_3)
::in_tokens(/*def*/token_StartOrder) -> print(998/*StartOrder*/);
out_tokens(token_chooseItem_1)
::in_tokens(token_chooseItem_1) -> 
print(997/*chooseItem*/);
   shoppingCart.msg = order;
byte temp;
select(temp : 0 .. MAX_ITEM);
shoppingCart.item = temp;
select(temp : 0 .. MAX_BUYER);
shoppingCart.item = temp;
select(temp : 0 .. MAX_COST);
shoppingCart.item = temp;

out_tokens(token_SendOrder_2)
od
}
if
::(token_EndOrder_11 == 0 && token_NotificationSubProcess_10 == 0 && token_ReceiveStatus_3 == 0 && token_SendOrder_2 == 0 && token_chooseItem_1 == 0 )->
reportChannel!normal
::else->
printf("token_EndOrder_11 %d\n", token_EndOrder_11);
printf("token_NotificationSubProcess_10 %d\n", token_NotificationSubProcess_10);
printf("token_ReceiveStatus_3 %d\n", token_ReceiveStatus_3);
printf("token_SendOrder_2 %d\n", token_SendOrder_2);
printf("token_chooseItem_1 %d\n", token_chooseItem_1);
reportChannel!abnormal
fi
}
proctype process_NotificationSubProcess(byte cwpArrayIndex; chan reportChannel){
byte token_EmailStore_6, token_EmailUser_5, token_EndNotifications_9, token_parallelGatewayNotifConverging_7, token_parallelGatewayNotifConverging_8, token_parallelGatewayNotifDiverging_4;
byte token_StartNofications = 1;
/*process transition do loop*/
atomic{
do
::in_tokens(/*def*/token_EmailStore_6) -> print(996/*EmailStore*/);
out_tokens(token_parallelGatewayNotifConverging_8)
::in_tokens(/*def*/token_EmailUser_5) -> print(995/*EmailUser*/);
out_tokens(token_parallelGatewayNotifConverging_7)
::in_tokens(token_EndNotifications_9) -> break;
::in_tokens(/*def*/token_StartNofications) -> print(994/*StartNofications*/);
out_tokens(token_parallelGatewayNotifDiverging_4)
::parallel_join(993/*parallel_join*/, token_parallelGatewayNotifConverging_7, token_parallelGatewayNotifConverging_8, token_EndNotifications_9)
::parallel_fork(token_parallelGatewayNotifDiverging_4, 992/*parallel fork*/, token_EmailUser_5, token_EmailStore_6);
od
}
if
::(token_EmailStore_6 == 0 && token_EmailUser_5 == 0 && token_EndNotifications_9 == 0 && token_parallelGatewayNotifConverging_7 == 0 && token_parallelGatewayNotifConverging_8 == 0 && token_parallelGatewayNotifDiverging_4 == 0 )->
reportChannel!normal
::else->
printf("token_EmailStore_6 %d\n", token_EmailStore_6);
printf("token_EmailUser_5 %d\n", token_EmailUser_5);
printf("token_EndNotifications_9 %d\n", token_EndNotifications_9);
printf("token_parallelGatewayNotifConverging_7 %d\n", token_parallelGatewayNotifConverging_7);
printf("token_parallelGatewayNotifConverging_8 %d\n", token_parallelGatewayNotifConverging_8);
printf("token_parallelGatewayNotifDiverging_4 %d\n", token_parallelGatewayNotifDiverging_4);
reportChannel!abnormal
fi
}
proctype process_ShoppingSite(byte cwpArrayIndex; chan reportChannel; msgType message){
byte token_ChargeCreditCard_14, token_CheckInventoryDiverge_12, token_OutOfStockMessage_13, token_SendStatus_19, token_cardDeniedMessage_20, token_join1_17, token_join1_18, token_join2_15, token_join2_16, token_prepareItemForShipping_21, token_shipItem_22;
msgType orderStatus;
byte token_ReceiveOrder = 1;
/*process transition do loop*/
atomic{
do
::xor_fork(token_ChargeCreditCard_14, 991/*ChargeCreditCard: xor_fork first choice*/, true, token_cardDeniedMessage_20, 990/*xor_fork second choice*/, true, token_prepareItemForShipping_21, reportChannel,cwpArrayIndex);
::xor_fork_default(token_CheckInventoryDiverge_12, 989/*xor_fork_default first choice*/, false /*outOfStock*/, token_OutOfStockMessage_13, 988/*CheckInventoryDiverge: xor_fork_default default choice*/, token_ChargeCreditCard_14);
::in_tokens(token_OutOfStockMessage_13) -> 
print(987/*OutOfStockMessage*/);
   orderStatus.msg = outOfStock;
out_tokens(token_join2_15)
::in_tokens(/*def*/token_ReceiveOrder) -> print(986/*ReceiveOrder*/);
out_tokens(token_CheckInventoryDiverge_12)
::in_tokens(token_SendStatus_19) -> 
print(985/*SendStatus*/);
   chan_MessageFlow2!cwpArrayIndex, orderStatus;
break;
::in_tokens(token_cardDeniedMessage_20) -> 
print(984/*cardDeniedMessage*/);
   orderStatus.msg = cardDenied;
out_tokens(token_join1_17)
::xor_join(983/*join1: xor join*/, token_join1_17, token_join1_18, token_join2_16)
::xor_join(982/*join2: xor join*/, token_join2_15, token_join2_16, token_SendStatus_19)
::in_tokens(token_prepareItemForShipping_21) -> 
print(981/*prepareItemForShipping*/);
   cwpArray[cwpArrayIndex].paymentOwner = cwpArray[cwpArrayIndex].seller;
out_tokens(token_shipItem_22)
::in_tokens(token_shipItem_22) -> 
print(980/*shipItem*/);
   cwpArray[cwpArrayIndex].itemOwner = cwpArray[cwpArrayIndex].buyer;
out_tokens(token_join1_18)
od
}
if
::(token_ChargeCreditCard_14 == 0 && token_CheckInventoryDiverge_12 == 0 && token_OutOfStockMessage_13 == 0 && token_SendStatus_19 == 0 && token_cardDeniedMessage_20 == 0 && token_join1_17 == 0 && token_join1_18 == 0 && token_join2_15 == 0 && token_join2_16 == 0 && token_prepareItemForShipping_21 == 0 && token_shipItem_22 == 0 )->
reportChannel!normal
::else->
printf("token_ChargeCreditCard_14 %d\n", token_ChargeCreditCard_14);
printf("token_CheckInventoryDiverge_12 %d\n", token_CheckInventoryDiverge_12);
printf("token_OutOfStockMessage_13 %d\n", token_OutOfStockMessage_13);
printf("token_SendStatus_19 %d\n", token_SendStatus_19);
printf("token_cardDeniedMessage_20 %d\n", token_cardDeniedMessage_20);
printf("token_join1_17 %d\n", token_join1_17);
printf("token_join1_18 %d\n", token_join1_18);
printf("token_join2_15 %d\n", token_join2_15);
printf("token_join2_16 %d\n", token_join2_16);
printf("token_prepareItemForShipping_21 %d\n", token_prepareItemForShipping_21);
printf("token_shipItem_22 %d\n", token_shipItem_22);
reportChannel!abnormal
fi
}
init {
atomic {

mtype msg;
run process_Customer(0, end[0]);

do
:: end[0]?msg -> 
   printf("%e: %d\n", msg, 0)
assert(msg==normal)
:: timeout ->
   break
od
}
}
