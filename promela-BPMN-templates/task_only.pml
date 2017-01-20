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