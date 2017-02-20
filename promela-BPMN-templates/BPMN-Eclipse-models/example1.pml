#include "BPMN.pml"


typedef process_struct/*name defined in xsd*/{
	bool isReady;
	int count;
};

process_struct var[3];/*possibly give this a const value for number*/

proctype subprocess1(chan report; byte token_id){


  byte scriptTask = 1
  byte end = 0
  
 atomic {
	do
	   
	:: in_tokens(scriptTask) -> /* script Task */
	   print(11)
	   /*start script task code*/
		var[token_id].count++;
	   /*end script task code*/
	   out_tokens(end)
	   
	:: in_tokens(end) -> /* End Event */
	   print(12)
	   
	   	/* Check completion: all tokens must be consumed */
		if
		:: (scriptTask == 0 && end == 0) ->
		   report!normal(token_id)
		:: else ->
		   report!abnormal(token_id)
		fi
		break    
	od


  }

}


proctype main(chan report; byte token_id) {
  /*indicates token entering object*/
  byte initScript = 1
  byte gateway1 = 0
  byte gateway2_1 = 0
  byte gateway2_2 = 0
  byte subprocess = 0
  byte parallelFork = 0
  byte parallelJoin1 = 0
  byte task1 = 0
  byte task2 = 0
  byte parallelJoin2 = 0

  byte incrementCount = 0
  byte end = 0

  chan subprocessEnd = [1] of {mtype,byte}
  mtype msg
  byte process_token = 0

  
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
	   
	:: xor_fork(gateway1,3/*Divering Gateway*/,var[token_id].isReady, incrementCount, 4/*test*/, true, gateway2_1,report)   
	   
	:: in_tokens(incrementCount) -> 
	   print(5)
	   /*start script task code*/
		var[token_id].count++;
	   /*end script task code*/
	   out_tokens(gateway2_2)
	   
	:: xor_join(6,gateway2_1, gateway2_2,subprocess)
	   

	:: in_tokens(subprocess) ->
	   print(7)
	   run subprocess1(subprocessEnd,token_id)
	   
	   /*handle abnormal return?*/
	:: subprocessEnd?msg(process_token)->
	   print(8)/*subprocess end*/
	   out_tokens(parallelFork)
	   
	:: parallel_fork(9,parallelFork, task1, task2)

	:: task(task1, 10, parallelJoin1)

	:: task(task2, 11, parallelJoin2)

	:: parallel_join(12, parallelJoin1, parallelJoin2,end)

	:: in_tokens(end) -> /* End Event */
	   print(13)
	   if
	   /* Check completion: all tokens must be consumed */
	   :: (initScript == 0 && gateway1 == 0 &&  gateway2_1 == 0  && gateway2_2 == 0  &&  incrementCount == 0 && end == 0 && parallelFork == 0 && parallelJoin1 == 0 && parallelJoin2 == 0 && task1 == 0 && task2 == 0 && subprocess == 0) ->
		  report!normal(token_id)
	   :: else ->
		  report!abnormal(token_id)
	   fi
	   break
   
	od

	
	
	
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