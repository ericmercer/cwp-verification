#define N 3

mtype = {taskID};

#include "split_and_gate_only.pml"

init {
  chan in = [1] of {byte};
  chan out1 = [1] of {byte};
  chan out2 = [1] of {byte};
  chan done = [N] of {byte};
  chan terminate = [1] of {bool};
  run split_and_gate(in, out1, out2, done, terminate, taskID);
  
  byte x;
  {
	do
      :: nfull(in) ->
         atomic {
		   select(x : 1 .. N);
		   printf("send(%d)\n", x);
		   in!x;
		 }
      :: nfull(done) ->
	     atomic {
		   select(x : 1 .. N);
		   if
			 :: !(done??[eval(x)]) -> 
				done!!x;
				printf("done(%d)\n", x);
			 :: else -> skip;
		   fi;
		 }
      :: nfull(terminate) ->
	     atomic {
		   printf("terminate\n");
		   terminate!true;
	     }
      :: out1?[_] ->
	     atomic {
		   out1?x;
		   printf("receive(%d)\n", x);
		 }
	  :: out2?[_] ->
	     atomic {
		   out2?x;
		   printf("receive(%d)\n", x);
		 }
	od;
  } unless {
	if
	  :: terminate?[_] -> printf("terminate(%d)\n", _pid);
	fi;
  }
}