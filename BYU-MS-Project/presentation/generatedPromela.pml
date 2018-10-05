#include "BPMN.pml"

mtype = {cardDenied, order, outOfStock, shipped};
/*definitions*/
#define MAX_AMOUNT 1
#define MAX_BUYER 1
#define MAX_COST 1
#define MAX_ITEM 1
#define MAX_ITEMOWNER 1
#define MAX_MSG 0
#define MAX_PAYMENTOWNER 1
#define MAX_SELLER 1

typedef cwpType{
bit seller;
bit buyer;
bit item;
bit amount;
bit itemOwner;
bit paymentOwner;
mtype msg;
};
typedef msgType{
mtype msg;
bit item;
bit cost;
bit buyer;
};

/*Global Variables*/
cwpType cwpArray[1];
chan chan_MessageFlow_8 =  [1] of {byte, msgType};
chan chan_MessageFlow_9 =  [1] of {byte, msgType};
proctype process_Process_1(byte cwpArrayIndex; chan reportChannel){
byte token_EndEvent_7_11, token_IntermediateCatchEvent_2_9, token_IntermediateThrowEvent_1_8, token_SubProcess_1_10, token_UserTask_1_7;
chan subprocessReturnChannel_SubProcess_1= [1] of {mtype};
msgType shoppingCart;
byte token_StartEvent_1 = 1;
/*process transition do loop*/
do
::in_tokens(token_EndEvent_7_11) -> 
atomic{
print(1000/*EndOrder-EndEvent_7*/);
break;
}
::token_IntermediateCatchEvent_2_9 > 0 && chan_MessageFlow_9??[eval(cwpArrayIndex), shoppingCart]->
atomic{
chan_MessageFlow_9??eval(cwpArrayIndex), shoppingCart;
decrement_tokens(token_IntermediateCatchEvent_2_9);
print(999/*ReceiveStatus-IntermediateCatchEvent_2*/);
out_tokens(token_SubProcess_1_10)
}
::in_tokens(token_IntermediateThrowEvent_1_8) -> 
atomic{
print(998/*SendOrder-IntermediateThrowEvent_1*/);
run process_Process_2(cwpArrayIndex, reportChannel, shoppingCart)
out_tokens(token_IntermediateCatchEvent_2_9)
}
::in_tokens(token_StartEvent_1) -> atomic{
print(997/*StartOrder-StartEvent_1*/);
out_tokens(token_UserTask_1_7)
}
::in_tokens(token_SubProcess_1_10) -> 
atomic{
print(996/*SubProcess_1*/);
run process_SubProcess_1(cwpArrayIndex, subprocessReturnChannel_SubProcess_1); 
}
::len(subprocessReturnChannel_SubProcess_1) > 0 ->
atomic{
mtype tempS;
subprocessReturnChannel_SubProcess_1?tempS;
if
::tempS==normal;
::else -> reportChannel!tempS;
fi
out_tokens(token_EndEvent_7_11)
}
::in_tokens(token_UserTask_1_7) -> 
atomic{
print(995/*Choose Item-UserTask_1*/);
   		shoppingCart.msg = order;
		byte temp;
		select(temp : 0 .. MAX_ITEM);
		shoppingCart.item = temp;
		select(temp : 0 .. MAX_BUYER);
		shoppingCart.item = temp;
		select(temp : 0 .. MAX_COST);
		shoppingCart.item = temp;

out_tokens(token_IntermediateThrowEvent_1_8)
}
od
atomic{
if
::(token_EndEvent_7_11 == 0 && token_IntermediateCatchEvent_2_9 == 0 && token_IntermediateThrowEvent_1_8 == 0 && token_SubProcess_1_10 == 0 && token_UserTask_1_7 == 0 )->
reportChannel!normal
::else->
printf("token_EndEvent_7_11 %d\n", token_EndEvent_7_11);
printf("token_IntermediateCatchEvent_2_9 %d\n", token_IntermediateCatchEvent_2_9);
printf("token_IntermediateThrowEvent_1_8 %d\n", token_IntermediateThrowEvent_1_8);
printf("token_SubProcess_1_10 %d\n", token_SubProcess_1_10);
printf("token_UserTask_1_7 %d\n", token_UserTask_1_7);
reportChannel!abnormal
fi
}
}
proctype process_SubProcess_1(byte cwpArrayIndex; chan reportChannel){
byte token_EndEvent_1_6, token_ParallelGateway_1_5, token_ParallelGateway_2_3, token_ParallelGateway_2_4, token_Task_1_1, token_Task_2_2;
byte token_StartEvent_3 = 1;
/*process transition do loop*/
do
::in_tokens(token_EndEvent_1_6) -> 
atomic{
print(994/*End Event 1-EndEvent_1*/);
break;
}
::parallel_fork(token_ParallelGateway_1_5, 993/*parallel fork*/, token_Task_1_1, token_Task_2_2);
::parallel_join(992/*parallel_join*/, token_ParallelGateway_2_3, token_ParallelGateway_2_4, token_EndEvent_1_6)
::in_tokens(token_StartEvent_3) -> atomic{
print(991/*Start Event 3-StartEvent_3*/);
out_tokens(token_ParallelGateway_1_5)
}
::in_tokens(token_Task_1_1) -> atomic{
print(990/*EmailUser-Task_1*/);
out_tokens(token_ParallelGateway_2_3)
}
::in_tokens(token_Task_2_2) -> atomic{
print(989/*EmailStore-Task_2*/);
out_tokens(token_ParallelGateway_2_4)
}
od
atomic{
if
::(token_EndEvent_1_6 == 0 && token_ParallelGateway_1_5 == 0 && token_ParallelGateway_2_3 == 0 && token_ParallelGateway_2_4 == 0 && token_Task_1_1 == 0 && token_Task_2_2 == 0 )->
reportChannel!normal
::else->
printf("token_EndEvent_1_6 %d\n", token_EndEvent_1_6);
printf("token_ParallelGateway_1_5 %d\n", token_ParallelGateway_1_5);
printf("token_ParallelGateway_2_3 %d\n", token_ParallelGateway_2_3);
printf("token_ParallelGateway_2_4 %d\n", token_ParallelGateway_2_4);
printf("token_Task_1_1 %d\n", token_Task_1_1);
printf("token_Task_2_2 %d\n", token_Task_2_2);
reportChannel!abnormal
fi
}
}
proctype process_Process_2(byte cwpArrayIndex; chan reportChannel; msgType message){
byte token_EndEvent_8_22, token_ExclusiveGateway_1_16, token_ExclusiveGateway_2_13, token_ExclusiveGateway_5_18, token_ExclusiveGateway_5_19, token_ParallelGateway_4_20, token_ParallelGateway_4_21, token_ScriptTask_1_12, token_ScriptTask_3_14, token_UserTask_4_17, token_UserTask_5_15;
msgType orderStatus;
byte token_StartEvent_2 = 1;
/*process transition do loop*/
do
::in_tokens(token_EndEvent_8_22) -> 
atomic{
print(988/*SendStatus-EndEvent_8*/);
   chan_MessageFlow_9!cwpArrayIndex, orderStatus;
break;
}
::xor_fork_default(token_ExclusiveGateway_1_16, 987/*xor_fork_default first choice*/, true, token_ScriptTask_1_12, 986/*ExclusiveGateway_1: xor_fork_default default choice*/, token_ExclusiveGateway_2_13);
::xor_fork(token_ExclusiveGateway_2_13, 985/*ExclusiveGateway_2: xor_fork first choice*/, true, token_ScriptTask_3_14, 984/*xor_fork second choice*/, true, token_UserTask_4_17, reportChannel,cwpArrayIndex);
::xor_join(983/*ExclusiveGateway_5: xor join*/, token_ExclusiveGateway_5_18, token_ExclusiveGateway_5_19, token_ParallelGateway_4_21)
::parallel_join(982/*parallel_join*/, token_ParallelGateway_4_20, token_ParallelGateway_4_21, token_EndEvent_8_22)
::in_tokens(token_ScriptTask_1_12) -> 
atomic{
print(981/*orderStatus-ScriptTask_1*/);
   orderStatus.msg = outOfStock
out_tokens(token_ParallelGateway_4_20)
}
::in_tokens(token_ScriptTask_3_14) -> 
atomic{
print(980/*orderStatus-ScriptTask_3*/);
   orderStatus.msg = cardDenied
out_tokens(token_ExclusiveGateway_5_19)
}
::in_tokens(token_StartEvent_2) -> atomic{
print(979/*ReceiveOrder-StartEvent_2*/);
out_tokens(token_ExclusiveGateway_1_16)
}
::in_tokens(token_UserTask_4_17) -> 
atomic{
print(978/*Prepare Item for Shipping-UserTask_4*/);
   cwpArray[cwpArrayIndex].paymentOwner = cwpArray[cwpArrayIndex].seller

out_tokens(token_UserTask_5_15)
}
::in_tokens(token_UserTask_5_15) -> 
atomic{
print(977/*Ship Item-UserTask_5*/);
   cwpArray[cwpArrayIndex].itemOwner = cwpArray[cwpArrayIndex].buyer

out_tokens(token_ExclusiveGateway_5_18)
}
od
atomic{
if
::(token_EndEvent_8_22 == 0 && token_ExclusiveGateway_1_16 == 0 && token_ExclusiveGateway_2_13 == 0 && token_ExclusiveGateway_5_18 == 0 && token_ExclusiveGateway_5_19 == 0 && token_ParallelGateway_4_20 == 0 && token_ParallelGateway_4_21 == 0 && token_ScriptTask_1_12 == 0 && token_ScriptTask_3_14 == 0 && token_UserTask_4_17 == 0 && token_UserTask_5_15 == 0 )->
reportChannel!normal
::else->
printf("token_EndEvent_8_22 %d\n", token_EndEvent_8_22);
printf("token_ExclusiveGateway_1_16 %d\n", token_ExclusiveGateway_1_16);
printf("token_ExclusiveGateway_2_13 %d\n", token_ExclusiveGateway_2_13);
printf("token_ExclusiveGateway_5_18 %d\n", token_ExclusiveGateway_5_18);
printf("token_ExclusiveGateway_5_19 %d\n", token_ExclusiveGateway_5_19);
printf("token_ParallelGateway_4_20 %d\n", token_ParallelGateway_4_20);
printf("token_ParallelGateway_4_21 %d\n", token_ParallelGateway_4_21);
printf("token_ScriptTask_1_12 %d\n", token_ScriptTask_1_12);
printf("token_ScriptTask_3_14 %d\n", token_ScriptTask_3_14);
printf("token_UserTask_4_17 %d\n", token_UserTask_4_17);
printf("token_UserTask_5_15 %d\n", token_UserTask_5_15);
reportChannel!abnormal
fi
}
}
init {
atomic {
chan end[1] = [1] of {mtype}

run process_Process_1(0, end[0]);

mtype msg;
do
:: end[0]?msg -> 
   printf("FINISHED %d: %e \n",0,msg)
assert(msg==normal)
:: timeout ->
   break
od
}
}
