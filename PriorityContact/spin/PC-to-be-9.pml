mtype = {
  null,
  nonNull,
  p1,
  p2,
  p3,
  p4,
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
/*   :: skip -> printf("Doctor launch PC::arriving priority (XOR)::danger to self or others\n")
              printf("Doctor launch PC::Doctor call police\n");
			  printf("Doctor launch PC::Doctor launch PCEnd21\n");
			  goto end_Doctor_launch_PC;
*/
   :: skip -> printf("Doctor launch PC::arriving priority (XOR)::Doctor launch P1\n");
			  cp.priorityCP = p1;
/*			  
   :: skip -> printf("Doctor launch PC::arriving priority (XOR)::Doctor launch P2\n");
			  cp.priorityCP = p2;
:: skip -> printf("Doctor launch PC::arriving priority (XOR)::Doctor launch P3\n");
			  cp.priorityCP = p3;
   :: skip -> printf("Doctor launch PC::arriving priority (XOR)::Doctor launch P4\n");
			  cp.priorityCP = p4;
*/
   fi;
   cp.launchDateTime = nonNull;
   parent!true;
   
end_Doctor_launch_PC:

}

/*********************************************************************
 * Compelte care and assessement: Doctor monitor contact
 *********************************************************************/
proctype doctor_monitor_contact(chan resolvedByDoctor, resolvedByPC) {
   {
      printf("Doctor monitor contact::delay to check PC\n");
	  printf("Doctor monitor contact::PC filter plans for overdue dates\n");
	  printf("Doctor monitor contact::Doctor review contact plan for overdue\n");
	  if
	  :: skip -> printf("Doctor monitor contact::decide action (XOR)::no action\n");
	  :: skip -> printf("Doctor monitor contact::decide action (XOR)::social worker\n");
	             printf("Doctor request social worker contact patient and mark plan resolved\n");
	  	 	  	 cp.resolveDateTime = nonNull;
				 resolvedByDoctor!true;
	  :: skip -> printf("Doctor monitor contact::decide action (XOR)::cancel\n");
	             printf("Doctor cancel contact plan and remove patient from roster\n");
 	  	 	  	 cp.resolveDateTime = nonNull;
				 resolvedByDoctor!true;
	  fi;

   } unless {
   	  nempty(resolvedByPC);
	  printf("Doctor monitor contact::INTERRUPTED: resolved by PC\n");
   }
   printf("Doctor monitor contact::close-or (XOR)\n");
   resolvedByDoctor!true;
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
 * Compelte care and assessement: Carry out contact plan
 *********************************************************************/
proctype carry_out_contact_plan(chan resolvedByDoctor, resolvedByPC) {
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
      fi;
   } unless {
	  nempty(resolvedByDoctor);
 	  printf("Carry out contact plan::INTERRUPTED: resolved by Doctor\n");
   }
   resolvedByPC!true;
}

/*********************************************************************
 * Complete Care and Assessement Plan
 *********************************************************************/
proctype complete_care_and_assessement_plan(chan parent) {
   chan rv = [1] of {bool};
   chan resolvedByDoctor = [1] of {bool};
   chan resolvedByPC = [1] of {bool};

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
   run doctor_monitor_contact(resolvedByDoctor, resolvedByPC);
   printf("Complete care and asessement plan::and monitor (AND)::Carry out contact plan\n");
   run carry_out_contact_plan(resolvedByDoctor, resolvedByPC);
   resolvedByDoctor?_;
   resolvedByPC?_;
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
