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
