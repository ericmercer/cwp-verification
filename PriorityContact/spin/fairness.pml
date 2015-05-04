bool q = 0;

/* ltl NPEventuallyQ {([]<> !np_) && (!<> q)}*/
/* Does not error as expected */
never  {    /* ([]<> !np_) && (!<> q) */
T0_init:
	do
	:: (! ((np_)) && ! ((q))) -> goto accept_S9
	:: (! ((q))) -> goto T0_init
	od;
accept_S9:
	do
	:: (! ((q))) -> goto T0_init
	od;
}

init {
start:
  do
	:: skip -> 
/* Uncomment lable for failure on a fair cycle */
/*progress_1:*/  q = 0;
	:: skip -> q = 1;
progress_0:	   break;
  od;
  printf("Done!\n");
}