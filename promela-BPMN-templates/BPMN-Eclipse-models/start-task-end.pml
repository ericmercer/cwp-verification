#include "BPMN.pml"

proctype start_task_end(chan report; byte start) {
  byte end = 0
  
  atomic {
	do
	:: in_tokens(start) -> /* Task */
	   printf("Task\n")
	   out_tokens(end)
	:: in_tokens(end) -> /* End Event */
	   printf("End\n")
	   break
	od

	/* Check completion: all tokens must be consumed */
	if
	:: (start == 0 && end == 0) ->
	   report!normal
	:: else ->
	   report!abnormal
	fi
  }
}

#ifdef VERIFY
init {
  atomic{
	chan end = [1] of {mtype}
	printf("Start\n")
	run start_task_end(end,1)
	if
	:: end?normal
	   printf("normal\n")
	:: end?abnormal
	   printf("abnormal\n")
	fi
  }
}
#endif