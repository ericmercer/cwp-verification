#include "BPMN.pml"


typedef process_struct/*name defined in xsd*/{
	bool isReady;
	int count;
};

process_struct var[3];/*possibly give this a const value for number*/


proctype main(chan report; byte token_id) {
  /*indicates token entering object*/
  byte initScript = 1
  byte gateway1 = 0
  byte gateway2 = 0
  byte incrementCount = 0
  byte end = 0

#ifdef ATOMIC
  atomic {
#endif
	
	do
	   
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
	   if
	   :: (initScript == 0 && gateway1 == 0 &&  gateway2 == 0  && incrementCount == 0 && end == 0) ->
		  report!normal(token_id)
	   :: else ->
		  report!abnormal(token_id)
	   fi
	   break
   
	od

	/* Check completion: all tokens must be consumed */
	
	
#ifdef ATOMIC
  }
#endif

}

#ifdef VERIFY
init {
  atomic{
	chan end0 = [1] of {mtype,byte}
	chan end1 = [1] of {mtype,byte}
	chan end2 = [1] of {mtype,byte}

	mtype msg
	byte token_id = 0

	run main(end0,0)
	run main(end1,1)
	run main(end2,2)

	do
	:: end0?msg(token_id)
	   printf("%e: %d\n", msg, token_id)
	:: end1?msg(token_id)
	   printf("%e: %d\n", msg, token_id)
	:: end2?msg(token_id)
	   printf("%e: %d\n", msg, token_id)
	:: timeout ->
	   break
	od

  }
}
#endif