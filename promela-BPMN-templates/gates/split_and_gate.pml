proctype split_and_gate(chan in, out1, out2, terminated_tokens, shutdown) {
  byte token = 0;
  bool send1 = false, send2 = false;
loop:
  atomic {
	in?token;
	printf("gate%d.receive(%d)\n", _pid, token);
	if
	  :: !(terminated_tokens??[eval(token)]) ->
		 /* Assumes order does not matter since receiving processes */
		 /* are able to schedule in any order.                      */
		 out1!token; 
		 out2!token; 
		 printf("gate%d.send(%d)\n", _pid, token);
	  :: terminated_tokens??[eval(token)] ->
		 printf("gate%d.terminating(%d)\n", _pid, token)
	fi;
	goto loop
  } unless {
	atomic {
	  (shutdown?[_]);
	  printf("gate%d.shutdown\n", _pid)
	}
  }
}

#ifdef _VERIFY_GATE_

#define N 3

init {
  chan in = [1] of {byte};
  chan out1 = [1] of {byte};
  chan out2 = [1] of {byte};
  chan terminated_tokens = [N] of {byte};
  chan shutdown = [1] of {bool};

  run split_and_gate(in, out1, out2, terminated_tokens, shutdown);
  byte token;
  do
	:: nfull(in) ->
	   atomic {
		 select(token : 1 .. N);
		 printf("init.send_in(%d)\n", token);
		 in!token;
	   }
	:: nfull(terminated_tokens) ->
	   atomic {
		 select(token : 1 .. N);
		 if
		   :: !(terminated_tokens??[eval(token)]) -> 
			  terminated_tokens!!token;
			  printf("init.terminated_tokens(%d)\n", token);
		   :: else -> skip;
		 fi;
	   }
	:: nfull(shutdown) ->
	   atomic {
		 printf("shutdown\n");
		 shutdown!true;
	   }
	:: out1?[_] ->
	   atomic {
		 out1?token;
		 printf("init.receive_out1(%d)\n", token);
	   }
	:: out2?[_] ->
	   atomic {
		 out2?token;
		 printf("init.receive_out2(%d)\n", token);
	   }
  od unless {
	atomic {
	  (shutdown?[_]) ;
	  printf("init.shutdown\n");
	}
  }
}

#endif
