#include "BPMN.pml"


typedef process_struct/*name defined in xsd*/{
	bool isReady;
	int count;
};

process_struct var[1];/*possibly give this a const value for number*/


proctype main(chan report; byte start, token_id) {
  /*indicates token entering object*/
  byte initScript = 0
  byte gateway1 = 0
  byte gateway2 = 0
  byte incrementCount = 0
  byte end = 0
  
  atomic {
	do
	:: in_tokens(start) -> /* start */
	   print(1)
	   out_tokens(initScript)
	   
	:: in_tokens(initScript) -> 
	   print(2)
	   /*start script task code*/
	   if
	   ::var[token_id].isReady = true;
	   ::var[token_id].isReady = false;
	   fi

	   var[token_id].count = 0;

	   /*end script task code*/
	   out_tokens(gateway1)
	   
	:: xor_option(gateway1,3/*Divering Gateway*/,var[token_id].isReady, incrementCount, 4/*test*/, true, gateway2,report)   
	   
	:: in_tokens(incrementCount) -> 
	   print(5)
	   /*start script task code*/
		var[token_id].count++;
	   /*end script task code*/
	   out_tokens(gateway2)
	   
	:: in_tokens(gateway2) -> 
	   print(6)
	   out_tokens(end)
	   
	:: in_tokens(end) -> /* End Event */
	   print(7)
	   break
   
	od

	/* Check completion: all tokens must be consumed */
	


	if
	:: (start == 0 && initScript == 0 && gateway1 == 0 &&  gateway2 == 0  && incrementCount == 0 && end == 0) ->
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

	run main(end,1,0)
	
	if
	:: end?normal
	   printf("normal\n")
	:: end?abnormal
	   printf("abnormal\n")
	fi

  }
}
#endif