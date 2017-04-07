#!/bin/awk -f

BEGIN {}
{
gsub(/message: 1000/, "ReceiveStatus");
gsub(/message: 980/, "shipItem");
gsub(/message: 981/, "prepareItemForShipping");
gsub(/message: 982/, "join2: xor join");
gsub(/message: 983/, "join1: xor join");
gsub(/message: 984/, "cardDeniedMessage");
gsub(/message: 985/, "SendStatus");
gsub(/message: 986/, "ReceiveOrder");
gsub(/message: 987/, "OutOfStockMessage");
gsub(/message: 988/, "CheckInventoryDiverge: xor_fork_default default choice");
gsub(/message: 989/, "xor_fork_default first choice");
gsub(/message: 990/, "xor_fork second choice");
gsub(/message: 991/, "ChargeCreditCard: xor_fork first choice");
gsub(/message: 992/, "parallel fork");
gsub(/message: 993/, "parallel_join");
gsub(/message: 994/, "StartNofications");
gsub(/message: 995/, "EmailUser");
gsub(/message: 996/, "EmailStore");
gsub(/message: 997/, "chooseItem");
gsub(/message: 998/, "StartOrder");
gsub(/message: 999/, "SendOrder");
print;
}
END {}
