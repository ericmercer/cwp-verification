mtype = {
  null,
  nonNull,
  p1,
  p2,
  p3,
  p4,
  patientLeaving,
  doctor
}

typedef ContactPlan{
  mtype priorityCP = null;
  mtype launchDateTime = null;
  mtype resolveDateTime = null;
  mtype cancelDateTime = null;
  mtype convStartDateTime = null;
  mtype convSchedDateTime = null;
  mtype appAccptDateTime = null
  bool activeStatus = true;
}

ContactPlan cp;

/*********************************************************************
 * ContactPlanCWP-9
 *********************************************************************/

/*** COMMENT OUT PROPERTIES FOR NOW

#define iotaS                        \
    (   cp.priorityCP == null	     \
	 && cp.launchDateTime == null    \
 	 && cp.resolveDateTime == null   \
 	 && cp.cancelDateTime == null    \
 	 && cp.convStartDateTime == null \
	 && cp.convSchedDateTime == null \
	 && cp.appAccptDateTime == null  \
	 && cp.activeStatus == true) 
		 
ltl iota {
	[](! iotaS)
}

#define planLaunchedS                \
    (   cp.priorityCP == null	     \
	 && cp.launchDateTime == nonNull \
 	 && cp.resolveDateTime == null   \
 	 && cp.cancelDateTime == null    \
 	 && cp.convStartDateTime == null \
 	 && cp.convSchedDateTime == null \
	 && cp.appAccptDateTime == null  \
	 && cp.activeStatus == true) 

ltl planLaunched {
	[](! planLaunchedS)
}
	
#define conversationInProgressS                      \
    (   (cp.priorityCP == p1 || cp.priorityCP == p2) \
	 && cp.convStartDateTime == nonNull              \
	 && cp.activeStatus == true)
	 
ltl conversationInProgress {
	[](! conversationInProgressS)
}

#define conversationAppointmentScheduledS            \
    (   (cp.priorityCP == p3 || cp.priorityCP == p4) \
	 && cp.convSchedDateTime == nonNull              \
	 && cp.activeStatus == true)

ltl conversationAppointmentScheduled {
	[](! conversationAppointmentScheduledS)
}

#define planCanceledS \
    (cp.activeStatus == false)

ltl planCanceled {
	[](! planCanceledS)
}

#define resolvedS                                    \
	(   (cp.resolveDateTime == cp.convStartDateTime) \
	 || (cp.resolveDateTime == cp.appAccptDateTime))

ltl resolved {
	[](! resolvedS)
}

#define canceledS \
	(cp.cancelDateTime == nonNull)

ltl canceled {
	[](! canceledS)
}

ltl iota_transitions {
	[](iotaS -> (iotaS U (planLaunchedS || planCanceledS)))
}

ltl planLaunched_transitions {
	[](planLaunchedS -> (planLaunchedS U (   conversationInProgressS
	                                      || conversationAppointmentScheduledS
										  || planCanceledS)))
}

ltl conversationInProgress_transitions {
	[](conversationInProgressS -> (conversationInProgressS U resolvedS))
}

ltl conversationAppointmentScheduled_transitions {
	[](conversationAppointmentScheduledS ->
	  (conversationAppointmentScheduledS U resolvedS))
}

ltl planCanceled_transitions {
	[](planCanceledS -> (planCanceledS U canceledS))
}
***/

/*********************************************************************
 * PC-to-be-9
 *********************************************************************/

/*********************************************************************
 * Complete care and assessement: Doctor launch PC
 *********************************************************************/
proctype doctor_launch_PC(chan parent) {
   if
   /*
   :: skip -> printf("Doctor launch PC::arriving priority (XOR)::danger to self or others\n")
              printf("Doctor launch PC::Doctor call police\n");
			  printf("Doctor launch PC::Doctor launch PCEnd21\n");
			  goto end_Doctor_launch_PC;
   :: skip -> printf("Doctor launch PC::arriving priority (XOR)::Doctor launch P1\n");
			  cp.priorityCP = p1;
   :: skip -> printf("Doctor launch PC::arriving priority (XOR)::Doctor launch P2\n");
			  cp.priorityCP = p2;
   :: skip -> printf("Doctor launch PC::arriving priority (XOR)::Doctor launch P3\n");
			  cp.priorityCP = p3;
			  */
   :: skip -> printf("Doctor launch PC::arriving priority (XOR)::Doctor launch P4\n");
			  cp.priorityCP = p4;
   fi;
   cp.launchDateTime = nonNull;
   parent!true;
   
end_Doctor_launch_PC:

}

/*********************************************************************
 * Compelte care and assessement: Doctor monitor contact
 *********************************************************************/
proctype doctor_monitor_contact(chan parent, resolvedByDoctor, resolvedByPC) {
   mtype by = null;
   {
      printf("Doctor monitor contact::delay to check PC\n");
	  printf("Doctor monitor contact::PC filter plans for overdue dates\n");
	  printf("Doctor monitor contact::Doctor review contact plan for overdue\n");
	  if
	  :: skip -> printf("Doctor monitor contact::decide action (XOR)::no action\n");
	  :: skip -> printf("Doctor monitor contact::decide action (XOR)::social worker\n");
	             printf("Doctor request social worker contact patient and mark plan resolved\n");
	  	 	  	 cp.resolveDateTime = nonNull;
				 resolvedByDoctor!doctor;
	  :: skip -> printf("Doctor monitor contact::decide action (XOR)::cancel\n");
	             printf("Doctor cancel contact plan and remove patient from roster\n");
 	  	 	  	 cp.resolveDateTime = nonNull;
				 resolvedByDoctor!patientLeaving;
	  fi;

   } unless {
   	  resolvedByPC?by;
	  printf("Doctor monitor contact::INTERRUPTED %e\n", by);
   }
   printf("Doctor monitor contact::close-or (XOR)\n");
   parent!true;
}

/*********************************************************************
 * Carry out contact plan: Doctor send patient to ER
 *********************************************************************/
proctype doctor_send_patient_to_ER(chan parent) {
  parent!true;
}

/*********************************************************************
 * Carry out contact plan: PC carry out contact plan P1
 *********************************************************************/
proctype pc_carry_out_contact_plan_p1(chan parent) {
  parent!true;
}

/*********************************************************************
 * Carry out contact plan: PC carry out contact plan P2
 *********************************************************************/
proctype pc_carry_out_contact_plan_p2(chan parent) {
  parent!true;
}

/*********************************************************************
 * Carry out contact plan: Doctor inform patient of results
 *********************************************************************/
proctype doctor_inform_patient_of_results(chan parent) {
  parent!true;
}

/*********************************************************************
 * Carry out contact plan: Notify POC P2
 *********************************************************************/
proctype notify_poc_p2(chan parent) {
  parent!true;
}

/*********************************************************************
 * Carry out contact plan: Make phone clinic appointment
 *********************************************************************/
proctype make_phone_clinic_appointment(chan parent) {
  parent!true;
}

/*********************************************************************
 * Carry out contact plan: Patient voicemail actions
 *********************************************************************/
proctype patient_voicemail_actions(chan parent) {
   printf("Patient voicemail actions::delay check VM\n");
   printf("Patient voicemail actions::Patient checks voicemail\n");
   printf("Patient voicemail actions::Patient calls PC\n");
   parent!true;
}

proctype p3_routine_change(chan parent) {
   chan rv = [1] of {bool};
   printf("Carry out contact plan::PC autocall P3\n");
loop:
   if
   :: skip -> printf("Carry out contact plan::patient answers P3 (XOR)::yes\n");
      printf("Carry out contact plan::Make phone clinic appointment\n");
	  run make_phone_clinic_appointment(rv);
	  rv?_
   :: skip -> printf("Carry out contact plan::patient answers P3 (XOR)::no\n");
      printf("Carry out contact plan::PC leave voicemail and update log\n");
	  if
	  :: skip -> printf("Carry out contact plan::Patient voicemail actions P3\n");
	     run patient_voicemail_actions(rv);
	     rv?_
		 printf("Carry out contact plan::Make phone clinic apointment\n");
	     run make_phone_clinic_appointment(rv);
	     rv?_
	  :: skip -> goto loop;
	  fi;
   fi;
progress_p3:   
   parent!true;
}

/*********************************************************************
 * Compelte care and assessement: Carry out contact plan
 *********************************************************************/
proctype carry_out_contact_plan(chan parent, resolvedByDoctor, resolvedByPC) {
   mtype by = null;
   {
	  chan rv = [1] of {bool};
	  if
	  :: (cp.priorityCP == p1) -> printf("Carry out contact plan::priority (complex)::P1 life-threatening\n");
		 if
		 :: skip -> printf("Carry out contact plan::call now and patient answers P1 (XOR)::yes\n");
		    printf("Carry out contact plan::Doctor send patient to ER\n");
			run doctor_send_patient_to_ER(rv);
			rv?_;
		 :: skip -> printf("Carry out contact plan::call now and patient answers P1 (XOR)::no\n");
		    printf("Carry out contact plan::PC Carry out contact plan P1\n");
			run pc_carry_out_contact_plan_p1(rv);
			rv?_;
			if
			:: skip -> printf("Carry out contact plan::POC answers P1 (XOR)::yes\n");
 		       printf("Carry out contact plan::Doctor send patient to ER\n");
  			   run doctor_send_patient_to_ER(rv);
			   rv?_;
			:: skip -> printf("Carry out contact plan::POC answers P1 (XOR)::no\n");
			   printf("Carry out contact plan::PC connect Seatle VA ED and update log\n");
  			   printf("Carry out contact plan::ED notify doctor who stops PC\n");
            fi;
         fi;
  		 resolvedByPC!p1;
	  :: (cp.priorityCP == p2) -> printf("Carry out contact plan::priority (complex)::P2 life-changing\n");
	  	 if
		 :: skip -> printf("Carry out contact plan::call now (XOR)::yes\n");
		    printf("Carry out contact plan::Doctor call P P2\n");
			if
			:: skip -> printf("Carry out contact plan::patient answers (XOR)::yes\n");
			   printf("Carry out contact plan::Doctor inform patient of results\n");
			   run doctor_inform_patient_of_results(rv);
   			   rv?_;
			:: skip -> printf("Carry out contact plan::patient answers (XOR)::no\n");
			   printf("Carry out contact plan::PC carry out contact plan P2\n");
			   run pc_carry_out_contact_plan_p2(rv);
   			   rv?_;
         fi;
		 :: skip -> printf("Carry out contact plan::call now (XOR)::no\n");
		    printf("Carry out contact plan::PC carry out contact plan P2\n");
			run pc_carry_out_contact_plan_p2(rv);
 		    rv?_;
            printf("Carry out contact plan::Notify POC P2\n");
			run notify_poc_p2(rv);
			rv?_;
			resolvedByPC!p2;
			printf("Carry out contact plan::Doctor inform patient of results\n");
			run doctor_inform_patient_of_results(rv);
			rv?_;
		 fi;
	  :: (cp.priorityCP == p3) -> printf("Carry out contact plan::priority (complex)::P3 routine change\n");
	     run p3_routine_change(rv);
		 rv?_;
		 resolvedByPC!p3;
  	  :: (cp.priorityCP == p4) -> printf("Carry out contact plan::priority (complex)::P4 no change\n");
         printf("Carry out contact plan::PC autocall P4\n");
         if
         :: skip -> printf("Carry out contact plan::patient answers P4 (XOR)::yes\n");
            printf("Carry out contact plan::Make phone clinic appointment P4\n");
	        run make_phone_clinic_appointment(rv);
	        rv?_
         :: skip -> printf("Carry out contact plan::patient answers P3 (XOR)::no\n");
            printf("Carry out contact plan::PC leave voicemail and update log P4\n");
	        printf("Carry out contact plan::Patient voicemail actions\n");
	        run patient_voicemail_actions(rv);
	        rv?_
		    printf("Carry out contact plan::Make phone clinic apointment\n");			
	        run make_phone_clinic_appointment(rv);
	        rv?_
		 fi;
 		 resolvedByPC!p4;
	  fi;
   fi;
progress_p3:   
	  
      fi;
   } unless {
	  resolvedByDoctor?by;
 	  printf("Carry out contact plan::INTERRUPTED %e\n", by);
   }
   parent!true;
}

/*********************************************************************
 * Complete Care and Assessement Plan
 *********************************************************************/
proctype complete_care_and_assessement_plan(chan parent) {
   chan rv = [1] of {bool};
   chan resolvedByDoctor = [1] of {mtype};
   chan resolvedByPC = [1] of {mtype};
   mtype resolvedBy = null;
   
   printf("Complete care and asessement plan::Confirm diagnosis and severity\n");

   if
      :: skip -> printf("Complete care and asessement plan::Life threatening (XOR)::yes\n");
	  	 	  	 printf("Complete care and asessement plan::Determine appropriate intervention\n");
	  :: skip -> printf("Complete care and asessement plan::Life threatening (XOR)::no\n");
   fi;

   printf("Complete care and asessement plan::Doctor launch PC\n");
   run doctor_launch_PC(rv);
end_doctor_launch_PC:
	rv?_;

   printf("Complete care and asessement plan::and monitor (AND)::Doctor monitor contact\n");
   run doctor_monitor_contact(rv, resolvedByDoctor, resolvedByPC);
   printf("Complete care and asessement plan::and monitor (AND)::Carry out contact plan\n");
   run carry_out_contact_plan(rv, resolvedByDoctor, resolvedByPC);
   rv?_;
   rv?_;
   
   printf("Complete care and asessement plan::and m close (AND)\n");
   parent!true;
}

/*********************************************************************
 * Page-1
 *********************************************************************/
 
init {
   chan rv = [1] of {bool};

   printf("Page-1::Start1\n");
   printf("Page-1::Complete care and asessement plan\n");
   run complete_care_and_assessement_plan(rv);
end_complete_care_and_assessement_plan:
   rv?_;
   printf("Page-1::End25\n");
}
