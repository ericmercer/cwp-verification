mtype = {
  null,
  nonNull,
  P1LifeThreatening,
  P2LifeChanging,
  P3RoutineChange,
  P4NoChange,
  mail,
  myHealthEvt,
  phone
}

/* Input */

/* Severity ranges over P1-P4 defined above in mtype */
mtype severity = null;
mtype preferredCommunicationMode = null;

/* End-of-Input */

/* Events */
bool End25 = 0;
bool CarryOutContactPlanEnd33 = 0;
bool CarryOutContactPlanEnd56 = 0;
bool CarryOutContactPlanEnd146 = 0;
bool CarryOutContactPlanEnd175 = 0;
/* End-of-Events */

typedef PriorityContact{
  mtype contactPriority = null;
  mtype launchDate = null;
  mtype launchTime = null;
  mtype resolveDate = null;
  mtype resolveTime = null;
  mtype confirmedAppointmentDateTime = null;
  /* Only part of the log but hoisted up also makes more sense since
  the appointment information is here. Ditto with inMyCare. */
  mtype conversDate = null; 
  mtype conversTime = null;
  bool inMyCare = 1;
  /* Stuff that is defined, but not referenced anywhere.
   * 
  mtype patientName = null;
  mtype patientIDNumber = null;
  mtype patientLogin = null;
  mtype PriorityContactInstructions = null;
  mtype doctorOpenScheduleDateTime1 = null;
  mtype doctorOpenScheduleDateTime2 = null;
  mtype voiceMenuSelection = null;
   */
}


PriorityContact pc;

proctype CompleteCareAndAssessmentPlan() {
  printf("Process: complete care and assessement plan\n");
  /* Complete care and asessement plan: initializes the contact plan entity */
ConfirmDiagnosisAndSeverity:
  printf("Task: confirm diagnosis and severity\n");
  goto GatewayLifeThreatening;
  
GatewayLifeThreatening:
  printf("Gateway: life threatening? ");
  if
	:: severity == P1LifeThreatening -> printf(" Yes.\n"); goto InitiatePriorityContact;
	:: else -> printf(" No.\n"); goto DetermineAppropriateIntervention;
  fi;
  
DetermineAppropriateIntervention:
  printf("Task: determine appropriate intervention\n");
  goto InitiatePriorityContact;
  
InitiatePriorityContact:
  printf("Task: initiate priority contact (Physician MD-1)\n");
  atomic {
	/* This task also initializes patient information, but that is
	ommitted from the model since it is not used in any control
	flow. */
	pc.contactPriority = severity;
	pc.launchDate = nonNull;
	pc.launchTime = nonNull;
  };
  run CarryOutContactPlan();
  /* Process will not terminate until all of its children finish */
}  

proctype P1() {
  printf("P1\n");
end_CarryOutContactPlanEnd33:
  CarryOutContactPlanEnd33 = 1;
}

proctype P2() {
  printf("P2\n");
end_CarryOutContactPlanEnd56:
  CarryOutContactPlanEnd56 = 1;
}

proctype P3() {
  printf("P3\n");
end_CarryOutContactPlanEnd146:
  CarryOutContactPlanEnd146 = 1;
}

proctype discussNoChange(chan p) {
  printf("Process: discussNoChange\n");
  printf("Task: Discuss no changeTask8 (Physician MD-1)");
  p!1;
}

proctype makePhoneClinicAppointment(chan p) {
  printf("Process: makePhoneClinicAppointment\n");
  printf("Task: Patient listens to instructions (paitent-performer)\n");
  printf("Task: Patient enters ID\n");
  printf("Task:");
  p!1;
}

proctype P4() {
  chan sync = [1] of {bool};
  printf("Gateway: perferred communication mode?");
  if
	:: (preferredCommunicationMode == mail) ->
	   printf("mail\n");
	   goto tmail;
	:: (preferredCommunicationMode == myHealthEvt) ->
	   printf("myHealthEvt\n");
	   goto tmyHealthEvt;
	:: (preferredCommunicationMode == phone) ->
	   printf("phone\n");
	   goto tphone;
  fi;
  assert(0);
  
tmail:
  printf("Task: mail P4 (Physican MD-1)\n");
  goto exit;

tmyHealthEvt:
  printf("Task: myHealthEvt P4 (Physican MD-1)\n");
  goto exit;

tphone:
  printf("Gateway: call now and patient answers?");
  if
/*	:: skip -> printf("Yes\n");
			   goto pDiscussNoChange;
 */
	:: skip -> printf("No\n");
			   goto PCAutocallP4;
  fi;

PCAutocallP4:
  printf("Task: PC autocall P4\n");
  goto PatientAnswersP4;

PatientAnswersP4:
  printf("Gateway: Patient answers P4?");
  if
	:: skip -> printf("Yes\n");
			   goto pMakePhoneClinicAppointment;
  fi;

pMakePhoneClinicAppointment:
  run makePhoneClinicAppointment(sync);
  sync?_;
  goto pDiscussNoChange;
  
pDiscussNoChange:
  run discussNoChange(sync);
  sync?_;
  goto end_CarryOutContactPlanEnd146;
  
end_CarryOutContactPlanEnd146:
  CarryOutContactPlanEnd175 = 1;
exit:
}

proctype CarryOutContactPlan() {
  printf("Process: carry out contact plan\n");
  printf("Gateway: priority?");
  if
	:: (pc.contactPriority == P1LifeThreatening) ->
	   printf("P1LifeThreatening\n"); run P1();
	:: (pc.contactPriority == P2LifeChanging) ->
	   printf("P2LifeChanging\n"); run P2();
	:: (pc.contactPriority == P3RoutineChange) ->
	   printf("P3RoutineChange\n"); run P3();
	:: (pc.contactPriority == P4NoChange) ->
	   printf("P4NoChange\n"); run P4();
  fi;
  (CarryOutContactPlanEnd33==1 || CarryOutContactPlanEnd56 || CarryOutContactPlanEnd146 || CarryOutContactPlanEnd175);
}

init {
  /* Initialize the input to the model (things that do not change over
  the life of the model) */
  if
/*	:: skip -> severity = P1LifeThreatening; printf("severity: P1 life threatening\n");
	:: skip -> severity = P2LifeChanging; printf("severity: P2 life changing\n");
	:: skip -> severity = P3RoutineChange; printf("severity: P3 routine change\n");
 */
	:: skip -> severity = P4NoChange; printf("severity: P4 no change\n");
  fi;
  if
/*	:: skip -> preferredCommunicationMode = mail;
			   printf("preferredCommunicationMode: mail\n");
	:: skip -> preferredCommunicationMode = myHealthEvt;
			   printf("preferredCommunicationMode: myHealthEvt\n");
 */
	:: skip -> preferredCommunicationMode = phone;
			   printf("preferredCommunicationMode: phone\n");
  fi;
  
  printf("Event: Start1\n");
  run CompleteCareAndAssessmentPlan();
  /* It is OK to not terminate the initial process */
end_End25:
  (End25 == 1)
}

