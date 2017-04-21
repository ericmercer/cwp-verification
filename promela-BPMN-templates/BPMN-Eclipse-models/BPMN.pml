mtype = {
  normal,   /* normal process termination */
  abnormal, /* abnormal process termination (terminating end) */
  xor_split_false
}

inline has_token(token_count){
	(token_count > 0)
}

inline decrement_tokens(token_count){

	token_count--;
}

inline in_tokens(token_count) {
	(token_count > 0) -> token_count--;
}

inline double_in_tokens(token_count1,token_count2) {
	(token_count1 > 0 && token_count2 > 0) -> 
	token_count1--;
	token_count2--;
}

inline out_tokens(token_count) {
  token_count++;
}

inline print(number){
	/*printf("message: %d, PID: %d, token: %d\n",number,_pid,token_id)*/
	printf("message: %d\n",number)
}


inline xor_fork(inseq,messageNumber1,expr1, outseq1, messageNumber2, expr2,outseq2,reportChannel,token_id){
  in_tokens(inseq) ->
  atomic {
	if
	::(expr1) ->
	   print(messageNumber1)
	   out_tokens(outseq1)
	::(expr2) ->
	   print(messageNumber2)
	   out_tokens(outseq2)
	:: else ->
	   printf("xorsplit exception");
	   reportChannel!xor_split_false;
	   break;
	fi
  }
}

inline xor_fork_default(inseq,messageNumber1,expr1, outseq1, messageNumber2,defaultSeq){
  in_tokens(inseq) ->
  atomic {
	if
	::(expr1) ->
	   print(messageNumber1)
	   out_tokens(outseq1)
	:: else ->
	   print(messageNumber2)
	   out_tokens(defaultSeq)
	fi
  }
}

inline xor_join(messageNumber, inseq,inseq2, outseq){
	/*(has_token(inseq) || has_token(inseq2) ) ->*/
	((inseq > 0) || (inseq2 > 0) ) ->
	atomic {
	print(messageNumber)
	if 
	:: in_tokens(inseq) -> out_tokens(outseq)
	:: in_tokens(inseq2) -> out_tokens(outseq)
	fi
	}
}



inline parallel_fork(inseq,messageNumber, outseq1,outseq2){
  in_tokens(inseq) ->
 
  atomic {
   print(messageNumber)
	out_tokens(outseq1)
	out_tokens(outseq2)
  }
}

inline parallel_join(messageNumber, inseq1,inseq2,outseq){
  	double_in_tokens(inseq1,inseq2)-> 
	atomic {
	print(messageNumber)
 	out_tokens(outseq)
	}
}





