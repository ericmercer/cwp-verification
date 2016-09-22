#define N 3

mtype = {taskID};

proctype split_and_gate(chan in, out1, out2, done, terminate; mtype id) {
  byte x = 0, i = 0;
  bool send1 = false, send2 = false;
L0:
  atomic {
	in?x;
	printf("%e(%d)\n", taskID, x);
	if
	  :: !(done??[eval(x)]) ->
		 send1 = false;
		 send2 = false;
		 do
		   :: nfull(out1) ->
			  out1!x;
			  send1 = true;
		   :: nfull(out2) -> 
			  out2!x;
			  send2 = true;
		   :: send1 && send2 ->
			  break;
		 od;		 
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