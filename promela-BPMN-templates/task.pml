#define N 1

mtype = {taskID};

proctype task(chan in, out, done, terminate; mtype id) {
   byte x = 0;
L0:   
   atomic {
	  in?x;
	  printf("%e(%d) -->", taskID, x);
	  if
	  :: !(done??[eval(x)]) ->
  			out!x;
	        printf("send(%d)\n", x);
	  :: else ->
	        printf("done(%d)\n", x);
	  fi;
	  goto L0;
   } unless {
      if
	  :: terminate?[_] -> printf("terminate(%d)\n", _pid);
  	  :: false -> skip;
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
	  :: false -> skip;
	  fi;
   }
}