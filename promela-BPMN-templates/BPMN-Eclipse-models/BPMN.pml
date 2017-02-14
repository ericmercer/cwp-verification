mtype = {
  normal,   /* normal process termination */
  abnormal, /* abnormal process termination (terminating end) */
  xor_split_false
}

inline in_tokens(token_count) {
	(token_count > 0) -> token_count--
}

inline out_tokens(token_count) {
  token_count++
}

inline print(number){
	printf("message: %d, PID: %d\n",number,_pid)
}

inline xor_option(inseq,messageNumber1,expr1, outseq1, messageNumber2, expr2,outseq2,exceptionChannel){
  in_tokens(inseq) ->
  atomic {
	if
	:: (expr1==true) ->
	   print(messageNumber1)
	   out_tokens(outseq1)
	:: else -> 
	   if
	   ::(expr2==true) ->
		  print(messageNumber2)
		  out_tokens(outseq2)
	   :: else ->
		  exceptionChannel!xor_split_false(token_id)
		  break
	   fi
	fi
  }
}

inline task(inseq, messageNumber,outseq){
	in_tokens(inseq) ->
	   print(messageNumber)
	out_tokens(outseq)
}
inline script_task(inseq, messageNumber,outseq){
	in_tokens(inseq) ->
	   print(messageNumber)
	out_tokens(outseq)
}

