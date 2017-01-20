start(chan in,out, done, terminate; mtype id) {
  byte x = 0, i = 0;
  bool send1 = false, send2 = false;
L0:
  atomic {
	in?x;
  out!x;
	goto L0;
  } unless {
	if
	  :: terminate?[_] -> printf(\"terminate(%d)\\n\", _pid);
	fi;
  }
}