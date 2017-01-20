merge_and_gate(chan in, in2, out, done, terminate; mtype id) {
  byte x = 0, i = 0;
  //bool send1 = false, send2 = false;
L0:
  atomic {
  //I know this isn' right, but it works for now
	in?x;
    in2?i;
  out!x;
	goto L0;
  } unless {
	if
	  :: terminate?[_] -> printf(\"terminate(%d)\\n\", _pid);
	fi;
  }
}