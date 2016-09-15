bool apptMade = false;

proctype PC_autocall(chan in1, in2, out) {
end:
   if
   :: in1?_ -> goto L0;
   :: in2?_ -> goto L0;
   fi;
L0:
   atomic{ 
      printf("PC autocall\n");
      out!true;
	  goto end;
   }
}

proctype Xor(chan in, out1, out2) {
end:
   in?_;
   atomic {
     if
        :: skip -> printf("Xor: pt answers 70\n");
		           out1!true;
		:: skip -> printf("Xor: no 30\n");
		           out2!true;
	 fi;
	 goto end;
   }
}

proctype Pt_makes_appt_with_PC(chan in1, in2, out) {
end:
   if
   :: in1?_ -> goto L0;
   :: in2?_ -> goto L0;
   fi;
L0:
   atomic{
      printf("Pt makes appt with PC\n");
	  assert(!apptMade);
      apptMade = true;
	  out!true;
	  goto end;
   }
}

proctype PC_leave_VM(chan in, out) {
end:
   in?_;
   atomic{
      printf("PC leave VM\n");
	  out!true;
	  goto end;
   }
}

proctype and(chan in, out1, out2) {
end:
   in?_;	
   atomic{
      printf("and\n");
	  out1!true;
	  out2!true;
      goto end;
   }
}

proctype minutes_patient_delay(chan in, out) {
end:
   in?_;
   atomic{

      printf("minutes patient delay\n");
      out!true;
	  goto end;
   }
}

proctype seconds_wait_60_min(chan in, out) {
end:
   in?_;
   atomic{
      printf("seconds_wait_60_min\n");
      out!true;
	  goto end;
   }
}

proctype was_appt_made(chan in, out1, out2) {
end:
   in?_;
   atomic{
      printf("was appt made *\n");
	  if
	  :: apptMade -> printf("stop calling 186\n");
 	                 out1!true;
	  :: !apptMade -> printf("call again 12\n");
	                  out2!true;
	  fi;
	  goto end;
  }
}

proctype close_and(chan in1, in2, out) {
end:
   in1?_;
end2:
   in2?_;
   atomic{
      printf("close and *\n");
 	  out!true;
	  goto end;
   }
}

proctype pt_VM_procedures(chan in, out) {
end:
   in?_;
   atomic {
      printf("pt VM procedures\n");
end1:	  out!true;
	  goto end;
   }
}

proctype vm_count(chan in, out1, out2) {
end:
   in?_;
   atomic{
      printf("vm count *\n");
      if
	  :: apptMade -> printf("yes215\n");
end2:	                 out1!true;
	  :: !apptMade -> printf("no\n");
                      out2!true;
      fi;
	  goto end;
   }
}

proctype pt_delete_redundant_VMs(chan in, out) {
end:
   in?_;
   atomic {
      printf("pt delete redundant VMs\n");
end1:	  out!true;
	  goto end;
   }
}

init {
   chan start_to_PC_autocall = [1] of {bool};
   chan PC_autocall_to_Xor = [1] of {bool};
   chan Xor_to_Pt_makes_appt_with_pc = [1] of {bool};
   chan Xor_to_PC_leave_VM = [1] of {bool};
   chan Pt_makes_appt_with_PC_to_end26 = [1] of {bool};
   chan Vm_count_to_Pt_makes_appt_with_PC = [1] of {bool};
   chan Vm_count_to_Pt_delete_redundant_VMs = [1] of {bool};
   chan PC_leave_VM_to_and = [1] of {bool};
   chan and_to_minutes_patient_delay = [1] of {bool};
   chan and_to_seconds_wait_60_min = [1] of {bool};
   chan minutes_patient_delay_to_pt_VM_procedures = [1] of {bool};
   chan seconds_wait_60_min_was_appt_made = [1] of {bool};
   chan was_appt_made_to_close_and = [1] of {bool};
   chan was_appt_made_to_PC_autocall = [1] of {bool};
   chan Pt_delete_rundundant_VMs_to_close_and = [1] of {bool};
   chan close_and_to_end_26 = [1] of {bool};
   chan pt_VM_procedures_to_vm_count = [1] of {bool};

   run PC_autocall(start_to_PC_autocall, was_appt_made_to_PC_autocall, PC_autocall_to_Xor);
   run Xor(PC_autocall_to_Xor, Xor_to_Pt_makes_appt_with_pc, Xor_to_PC_leave_VM);
   run Pt_makes_appt_with_PC(Xor_to_Pt_makes_appt_with_pc, Vm_count_to_Pt_makes_appt_with_PC,
                             Pt_makes_appt_with_PC_to_end26);
   run PC_leave_VM(Xor_to_PC_leave_VM, PC_leave_VM_to_and);
   run and(PC_leave_VM_to_and, and_to_minutes_patient_delay, and_to_seconds_wait_60_min)
   run minutes_patient_delay(and_to_minutes_patient_delay, minutes_patient_delay_to_pt_VM_procedures);
   run seconds_wait_60_min(and_to_seconds_wait_60_min, seconds_wait_60_min_was_appt_made);
   run was_appt_made(seconds_wait_60_min_was_appt_made, was_appt_made_to_close_and, was_appt_made_to_PC_autocall);
   run close_and(was_appt_made_to_close_and, Pt_delete_rundundant_VMs_to_close_and, close_and_to_end_26);
   run pt_VM_procedures(minutes_patient_delay_to_pt_VM_procedures, pt_VM_procedures_to_vm_count);
   run vm_count(pt_VM_procedures_to_vm_count, Vm_count_to_Pt_delete_redundant_VMs, Vm_count_to_Pt_makes_appt_with_PC);
   run pt_delete_redundant_VMs(Vm_count_to_Pt_delete_redundant_VMs, Pt_delete_rundundant_VMs_to_close_and);
   
   printf("Start\n");
   start_to_PC_autocall!true;
 end:  if 
   :: Pt_makes_appt_with_PC_to_end26?_ -> goto L0;
   :: close_and_to_end_26?_ -> goto L0;
   fi;
L0:
   printf("End26\n");
   assert(apptMade);
   goto end;
}

