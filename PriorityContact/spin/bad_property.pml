bool q = 0;

never  {    /* ([]<> !np_) -> (!<> q) */
T0_init:
	do
	:: (! ((q))) -> goto accept_S2
	:: ((np_)) -> goto accept_S8
	:: (1) -> goto T0_S5
	od;
accept_S2:
	do
	:: (! ((q))) -> goto accept_S2
	od;
accept_S8:
	do
	:: ((np_)) -> goto accept_S8
	od;
T0_S5:
	do
	:: ((np_)) -> goto accept_S8
	:: (1) -> goto T0_S5
	od;
}

init {
start:
  do
	:: skip -> 
/* Uncomment label for failure on a fair cycle */
/* progress_1: */ q = 0;
	:: skip -> q = 1;
progress_0:	   break;
  od;
  printf("Done!\n");
}