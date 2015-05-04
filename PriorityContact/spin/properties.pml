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


/*
/* ltl EventuallyQ {(!<> q)}*/
/* Errors as expected */
never  {    /* (!<> q) */
accept_init:
T0_init:
	do
	:: (! ((q))) -> goto T0_init
	od;
}

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
*/
