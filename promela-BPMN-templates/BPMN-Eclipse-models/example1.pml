#include "BPMN.pml"


typdef struct/*name defined in xsd*/{
	bool isReady;
	int count;
};

struct var[4];/*possibly give this a const value for number*/

proctype subprocess1(chan report; byte start, byte token_id){


  byte scriptTask = 0
  byte end = 0
  
 atomic {
	do
	:: in_tokens(start) -> /* start */
	   printf("Start\n")
	   out_tokens(scriptTask)
	   
	:: in_tokens(scriptTask) -> /* script Task */
	   printf("Task\n")
	   /*start script task code*/
		var[token_index].count++;
	   /*end script task code*/
	   out_tokens(end)
	   
	:: in_tokens(end) -> /* End Event */
	   printf("End\n")
	   break
	::    
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

proctype main(chan report; byte start, byte token_id) {
  /*indicates token entering object*/
  byte initScript = 0
  byte gateway1 = 0
  byte gateway2 = 0
  byte incrementCount = 0
  byte subprocess = 0
  byte end = 0
  
  atomic {
	do
	:: in_tokens(start) -> /* start */
	   printf("Start\n")
	   out_tokens(initScript)
	   
	:: in_tokens(initScript) -> 
	   printf("Script Task\n")
	   /*start script task code*/
	   var[token_id].count = 10;
	   var[token_id].isReady = false;
	   /*end script task code*/
	   out_tokens(gateway1)
	   
	:: inline xor_option(gateway1,3/*Divering Gateway*/,var[token_id].isReady, incrementCount, messageNumber2,true, gateway2)   
	   
	:: in_tokens(incrementCount) -> 
	   printf("Script Task\n")
	   /*start script task code*/
		var[token_index].count++;
	   /*end script task code*/
	   out_tokens(gateway2)
	   
	:: in_tokens(gateway2) -> 
	   printf("Task\n")
	   out_tokens(subprocess)
	   
	:: in_tokens(subprocess) -> 
		run subprocess1()
		
	:: in_tokens(end) -> /* End Event */
	   printf("End\n")
	   break
	::    
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
	run main(end,1,1)
	if
	:: end?normal
	   printf("normal\n")
	:: end?abnormal
	   printf("abnormal\n")
	fi
  }
}
#endif