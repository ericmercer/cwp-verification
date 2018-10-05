#!/bin/awk -f

BEGIN {}
{
gsub(/message: 1000/, "EndOrder-EndEvent_7");
gsub(/message: 977/, "Ship Item-UserTask_5");
gsub(/message: 978/, "Prepare Item for Shipping-UserTask_4");
gsub(/message: 979/, "ReceiveOrder-StartEvent_2");
gsub(/message: 980/, "orderStatus-ScriptTask_3");
gsub(/message: 981/, "orderStatus-ScriptTask_1");
gsub(/message: 982/, "parallel_join");
gsub(/message: 983/, "ExclusiveGateway_5: xor join");
gsub(/message: 984/, "xor_fork second choice");
gsub(/message: 985/, "ExclusiveGateway_2: xor_fork first choice");
gsub(/message: 986/, "ExclusiveGateway_1: xor_fork_default default choice");
gsub(/message: 987/, "xor_fork_default first choice");
gsub(/message: 988/, "SendStatus-EndEvent_8");
gsub(/message: 989/, "EmailStore-Task_2");
gsub(/message: 990/, "EmailUser-Task_1");
gsub(/message: 991/, "Start Event 3-StartEvent_3");
gsub(/message: 992/, "parallel_join");
gsub(/message: 993/, "parallel fork");
gsub(/message: 994/, "End Event 1-EndEvent_1");
gsub(/message: 995/, "Choose Item-UserTask_1");
gsub(/message: 996/, "SubProcess_1");
gsub(/message: 997/, "StartOrder-StartEvent_1");
gsub(/message: 998/, "SendOrder-IntermediateThrowEvent_1");
gsub(/message: 999/, "ReceiveStatus-IntermediateCatchEvent_2");
print;
}
END {}
