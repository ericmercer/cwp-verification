mtype = {
  /* Priority */
  P1LifeThreatening,
  P2LifeChanging,
  P3RoutineChange,
  P4NoChange,
  /* States */
  Needed,
  Launched,
  ConversationInProgress,
  Resolved,
  AppointmentScheduled,
  NoLongerNeeded
}

typedef ContactPlanEntity{
  /* Contact Plan */
  mtype cpPriority;
  mtype state = Needed;
}

typedef Phone {
  bit sms = 0;
  bit phone = 0;
  bit voiceMail = 0;
}


ContactPlanEntity cpe;
Phone phone;

ltl alwaysResolve {<> (cpe.state == Resolved)}

/* Properties should generate error because failing means the
 * state occurs in at least one path. */
ltl pNeeded {[] !(cpe.state == Needed)}
ltl pLaunched {[] !(cpe.state == Launched)}
ltl pConversationInProgress {[] !(cpe.state == ConversationInProgress)}
ltl pAppointmentScheduled {[] !(cpe.state == AppointmentScheduled)}
ltl pResolved {[] !(cpe.state == Resolved)}
ltl pNoLongerNeeded {[] !(cpe.state == NoLongerNeeded)}

/* Propreties should not generate an error because they
 * check transitions on the work product */
ltl pNeededSucc {[] ((cpe.state == Needed) ->
					 ((cpe.state == Needed) U (cpe.state == Launched)))}
ltl pLaunchedSucc {[] ((cpe.state == Launched) ->
					   ((cpe.state == Launched) U ((cpe.state == ConversationInProgress) ||
					                               (cpe.state == AppointmentScheduled) ||
					                               (cpe.state == NoLongerNeeded))))}
ltl pConversationInProgressSucc {[] ((cpe.state == ConversationInProgress) ->
									 ((cpe.state == ConversationInProgress) U (cpe.state == Resolved)))}
ltl pAppointmentScheduledSucc {[] ((cpe.state == AppointmentScheduled) ->
								   ((cpe.state == AppointmentScheduled) U ((cpe.state == Resolved) ||
																		   (cpe.state == NoLongerNeeded))))}
ltl pResolvedSucc {[] ((cpe.state == Resolved) ->
					   ((cpe.state == Resolved) U (cpe.state == NoLongerNeeded)))}
ltl pNoLongerNeededSucc  {[] ((cpe.state == NoLongerNeeded) ->
							  ([](cpe.state == NoLongerNeeded)))}

/*
I can answer some of the questions right now:
   * What should it look like when the process begins? 
        The user (clinician) launches a contact plan, with information
        attributes that are spec'd in the workflow model. The first
        state is spec'd in the state diagram of the CWP.
   * What should it look like when the process ends?
        The contact plan reaches its "resolved" state. The path depends
        on the priority.
   * Are there any invariants that should be monitored?
       (not sure what this means?)
   * Is there sequencing in how it evolves that is important to check?
        The state diagram must be satisfied by the workflow for each
        priority.
   * Are there things that should never happen?
       Yes, in a slightly more complex version of the workflow there
       are places that should not happen but might. For example, the
       more complex workflow throws an error if the patient answers a
       call but then doesn't carry out the instructions. The error
       reports are part of the user interface for tracking contact
       plans. However, for the first check of a model I think we
       should assume error-less execution by all human and machine
       agents. We want to put the safety rules in the Context Model
       that constrains the workflow (the implementation of the
       top-level algorithm).

Do these answers, except for the one about invariants, make sense?
*/

active proctype PriorityContact() {
  printf("Confirm diagnosis and severity\n");
  if
	:: cpe.cpPriority = P1LifeThreatening
	   /*:: cpe.cpPriority = P2LifeChanging*/
	   /*:: cpe.cpPriority = P3RoutineChange*/
	   /*:: cpe.cpPriority = P4NoChange*/
  fi;
  
  printf("Query: life threatening?\n");
  if
	:: cpe.cpPriority == P1LifeThreatening -> goto InitiatePriorityContact
  fi;

  printf("Determine appropriate intervention\n");
  
InitiatePriorityContact:
{
  printf("Initiate priority contact\n");
  cpe.state = Launched;
  
  printf("Carry out contact plan\n");
  printf("Query: priority?\n");
  if
	:: cpe.cpPriority == P1LifeThreatening -> goto qCallNowAndPatientAnswersP1;
	:: cpe.cpPriority == P2LifeChanging -> goto qCallNowAndPatientAnswersP2;
	:: cpe.cpPriority == P3RoutineChange -> goto qPreferredCommunicationModeP3;
	:: cpe.cpPriority == P4NoChange -> goto qPreferredCommunicationModeP4;
  fi;
}

qCallNowAndPatientAnswersP1:
{
  printf("Query: call now and patient answers P1?\n");
  if
	:: skip -> goto DoctorSendsPatientToER;
	:: skip -> goto PCCarryOutContactPlanP1;
  fi;
}

DoctorSendsPatientToER:
{
  printf("Doctor sends patient to ER\n");
  printf("Discuss why patient must immediately get life-saving care\n");
  
  cpe.state = ConversationInProgress;
  
  printf("Query: include in call P1?\n");
  
  if
	:: printf("Doctor adds emergency contact\n");
	:: skip;
  fi;
  
  if
	:: printf("Doctor adds 911\n");
	:: skip;
  fi;
  
  printf("Doctor send patient to ER gateway 21\n");

  printf("Doctor resolves contact P1\n");
  cpe.state = Resolved;

  goto CarryOutContactPlanEnd;
}

PCCarryOutContactPlanP1:
{
  printf("PC send text and update log\n"); phone.sms = 1;
  printf("PC autocall all tels and update log\n"); phone.phone = 1;
}

PatientCheck:
{
  if
	:: phone.sms -> printf("Patient checks sms\n");
					printf("Patient calls PC P1t\n");
					goto PCAnswersAndGivesInstructions;
	:: phone.sms -> skip;
	:: phone.phone -> goto PCAnswersAndGivesInstructions;
	:: phone.phone -> phone.voiceMail = 1; phone.phone = 0;
	:: phone.voiceMail -> printf("Patient checks voice mail\n");
						  printf("Patient calls PC P1v\n");
						  goto PCAnswersAndGivesInstructions;
	:: phone.voiceMail == 1-> skip;
  fi;
  
  if
	:: skip -> printf("PC send text and update log\n"); phone.sms = 1;
	:: skip -> printf("PC autocall all tels and update log\n"); phone.phone = 1;
	:: skip;
  fi;
  
  goto PatientCheck;
}

PCAnswersAndGivesInstructions:
{
  printf("PC answers and gives instructions\n");
  cpe.state = ConversationInProgress;

  printf("Patient enters ID\n");

  printf("PC updates log\n");
  cpe.state = Resolved;
}
goto CarryOutContactPlanEnd;

qCallNowAndPatientAnswersP2:
qPreferredCommunicationModeP3:
qPreferredCommunicationModeP4:


CarryOutContactPlanEnd:
printf("Carry out contact plan end\n");
}
