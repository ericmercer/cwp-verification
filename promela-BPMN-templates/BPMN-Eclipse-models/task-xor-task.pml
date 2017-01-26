#include "BPMN.pml"

chan exception = [0] of {mtype}
/*byte exception = 0*/

proctype start_task_end(chan report; byte start) {
  byte seq2 = 0
  byte seq3 = 0
  byte seq4 = 0
  byte seq5 = 0
  byte seq6 = 0
  byte seq7 = 0
  bool condition = false
  
  atomic {
	do
	:: in_tokens(start) -> /* Task */
	   printf("Provide Medical Records\n")
	   if
	   :: condition = true
	   :: condition = false
	   fi
	   out_tokens(seq2)
	   
	:: in_tokens(seq2) ->
	   if
	   :: (condition == true) ->
		  printf("xor-split (seq3)\n")
		  out_tokens(seq3)
	   :: else -> 
		  if
		  ::(condition == false) ->
			 printf("xor-split (seq4)\n")
			 out_tokens(seq4)
		  :: else ->
			 exception!xor_split_false
			 break
		  fi
	   fi

	:: in_tokens(seq3) ->
	   printf("Reject Application\n")
	   out_tokens(seq5)

	:: in_tokens(seq4)
	   printf("Accept Application\n")
	   out_tokens(seq6)

	:: in_tokens(seq5) ->
	   printf("xor-merge (seq5)\n")
	   out_tokens(seq7)

	:: in_tokens(seq6) ->
	   printf("xor_merge (seq6)\n")
	   out_tokens(seq7)
	   
	:: in_tokens(seq7) -> /* End Event */
	   printf("End\n")
	   break
	od

	/* Check completion: all tokens must be consumed */
	if
	:: (start == 0 &&
		seq2 == 0 &&
		seq3 == 0 &&
		seq4 == 0 &&
		seq5 == 0 &&
		seq6 == 0 &&
		seq7 == 0 ) ->
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
	fi unless {
	  exception?xor_split_false -> printf("Exception\n")
	}
  }
}
#endif