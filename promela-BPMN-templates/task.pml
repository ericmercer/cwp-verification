#define N 3

mtype = {taskID};

/* TODO: the done channel needs messages to include scope:
 unsigned id
 unsigned level
 The token is dropped if the id matches and level is greater-than-equal the current level
 Top level is 0
 The task needs to have another parameter, level, so it knows what level it is started out
*/

/* TODO: need interation counter on gates to handle loops
*/

/* TODO: add assertion to be sure iteration counters do not roll over. Make the iteration count a parameter
   so the assertion is tied to the parameter  
*/
proctype task(chan in, out, done, terminate; mtype id) {
  byte x = 0;
L0:
  atomic {
	in?x;
	printf("%e(%d)\n", taskID, x);
	if
	  :: !(done??[eval(x)]) ->
		 out!x;
		 printf("send(%d)\n", x);
	  :: done??[eval(x)] ->
		 printf("done(%d)\n", x);
	fi;
	goto L0;
  } unless {
	if
	  :: terminate?[_] -> printf("terminate(%d)\n", _pid);
	fi;
  }
}

init {
  chan in = [1] of {byte};
  chan out = [1] of {byte};
  chan done = [N] of {byte};
  chan terminate = [1] of {bool};
  run task(in, out, done, terminate, taskID);
  
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
      :: out?[_] ->
	     atomic {
		   out?x;
		   printf("receive(%d)\n", x);
		 }
	od;
  } unless {
	if
	  :: terminate?[_] -> printf("terminate(%d)\n", _pid);
	fi;
  }
}