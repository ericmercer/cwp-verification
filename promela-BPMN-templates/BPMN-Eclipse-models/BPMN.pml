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
	printf("message: %d\n",number)
}

inline xor_option(inseq,messageNumber1,expr1, outseq1, messageNumber2,expr2, outseq2){
	in_tokens(inseq) ->
	if
	:: (expr1) ->
		print(messageNumber1)
		out_tokens(outseq1)
	:: else -> 
		if
		::(expr2) ->
			print(messageNumber2)
			out_tokens(outseq2)
		:: else ->
			exception!xor_split_false
			break
		fi
	fi
}

inline task(inseq, messageNumber,outseq){
	in_tokens(inseq) ->
	   print(messageNumber)
	   out_tokens(outseq)
}

