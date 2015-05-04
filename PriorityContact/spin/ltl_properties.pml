bool q = 0;

ltl positiveProperty {
  ([] <> (! np_)) -> (<> q)
}

init {
start:
  do
	:: skip -> 
/* Uncomment lable for failure on a fair cycle */
/*progress_1:*/ q = 0;
	:: skip -> q = 1;
progress_0:	   break;
  od;
  printf("Done!\n");
}