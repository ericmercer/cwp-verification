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
		   :: out1!x ->
			  send1 = true;
		   :: out2!x -> 
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
<<<<<<< HEAD
}
=======
}
>>>>>>> 2cbee965206f1b7857e4ec85a4a231b38dc049f5
