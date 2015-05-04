mtype = {
  null,
  nonNull,
  P1LifeThreatening,
  P2LifeChanging,
  P3RoutineChange,
  P4NoChange,
  mail
}

/* Input */

/* Severity ranges over P1-P4 defined above in mtype */
mtype gSeverity = null;

/* P4 globals */
bool gCallNowAndPatientAnswersP4 = 0;
bool gPatientAnswersP4 = 0;
bool gScheduleAnswersPhoneP4 = 0;
bool gSelectDateTime1P4 = 0;
bool gSelectDateTime2P4 = 0;

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

proctype ConfirmDiagnosisAndSeverity(chan rvChan) {
  printf("Task: confirm diagnosis and severity\n");
  rvChan!1;
}

proctype DetermineAppropriateIntervention(chan rvChan) {
  printf("Task: determine appropriate intervention\n");
  rvChan!1;
}

proctype InitiatePriorityContact(chan rvChan) {
  printf("Task: initiate priority contact (Physician MD-1)\n");
  atomic {
	/* This task also initializes patient information, but that is
	ommitted from the model since it is not used in any control
	flow. */
	pc.contactPriority = gSeverity;
	pc.launchDate = nonNull;
	pc.launchTime = nonNull;
  };
  rvChan!1;
}

proctype CompleteCareAndAssessmentPlan(chan rvChan) {
  chan rv = [1] of {bool};
  printf("Process: complete care and assessement plan\n");
  /* Complete care and asessement plan: initializes the contact plan entity */
  run ConfirmDiagnosisAndSeverity(rv);
  rv?_;

  printf("Gateway (XOR->2): life threatening? ");
  if
	:: gSeverity == P1LifeThreatening -> printf(" Yes.\n");
										 goto locInitiatePriorityContact;
	:: else -> printf(" No.\n");
			   goto locDetermineAppropriateIntervention;
  fi;

locDetermineAppropriateIntervention:
  run DetermineAppropriateIntervention(rv);
  rv?_;
  goto locInitiatePriorityContact;
  
locInitiatePriorityContact:
  run InitiatePriorityContact(rv);
  rv?_;
  goto locCarryOutContactPlan;

locCarryOutContactPlan:
  /* CarryOutContactPlan() does not start anything, so there is no
  need for CompleteCareAndAssessmentPlan to wait. */
  run CarryOutContactPlan(rv);
  
  rvChan!1;
}  

proctype P1(chan rvChan) {
  printf("P1\n");
end_CarryOutContactPlanEnd33:
  CarryOutContactPlanEnd33 = 1;
  goto exit;
exit:
  rvChan!1;
}

proctype P2(chan rvChan) {
  printf("P2\n");
end_CarryOutContactPlanEnd56:
  CarryOutContactPlanEnd56 = 1;
  goto exit;
exit:
  rvChan!1;
}

proctype P3(chan rvChan) {
  printf("P3\n");
end_CarryOutContactPlanEnd146:
  CarryOutContactPlanEnd146 = 1;
  goto exit;
exit:
  rvChan!1;

}

proctype discussNoChangeTask8(chan rvChan) {
  printf("Task: Discuss no changeTask8 (Physician MD-1)\n");
  rvChan!1;
}

proctype discussNoChange(chan rvChan) {
  chan rv = [1] of {bool};
  printf("Process: Discuss no change\n");
  run discussNoChangeTask8(rv);
  rv?_;
  
  rvChan!1;
}

proctype PatientListensToInstructionsP4(chan rvChan) {
  printf("Task: Patient listens to instructions (patient-performer)\n");
  rvChan!1;
}

proctype PatientEntersIDP4(chan rvChan){
  printf("Task: Patient enters ID\n");
  rvChan!1;
}

proctype PCOffersTimeDate1P4(chan rvChan) {
  printf("Task: PC offers timeDate1 P4\n");
  rvChan!1;
}

proctype PCOffersTimeDate2P4(chan rvChan) {
  printf("Task: PC offers timeDate2 P4\n");
  rvChan!1;
}

proctype PatientMakesSelectionP4_1(chan rvChan) {
  printf("Task: Patient makes selection P4-1\n");
  rvChan!gSelectDateTime1P4;
}

proctype PatientMakesSelectionP4_2(chan rvChan) {
  printf("Task: Patient makes selection P4-2\n");
  rvChan!gSelectDateTime2P4;
}

proctype PCWriteAppointmentTimeDateToDoctorSchedule(chan rvChan) {
  printf("Task: PC write appointment timeDate to doctor schedule\n");
  pc.confirmedAppointmentDateTime = nonNull;
  rvChan!1;
}

proctype PatientLeavesVMRequest(chan rvChan) {
  printf("Task: Patient leaves VM request\n");
  rvChan!1;
}

proctype SchedulerCallsPatient(chan rvChan) {
  printf("Task: scheduler calls patient\n");
  rvChan!1;
}

proctype SelectCallClinicTimeDate(chan rvChan) {
  printf("Task: select call clinic timeDate\n");
  pc.confirmedAppointmentDateTime = nonNull;
  rvChan!1;
}

proctype PCCallClinicSchedulerP4(chan rvChan) {
  chan rv = [1] of {bool};
  printf("Process: PC call clinic scheduler P4\n");

  printf("Gateway (XOR->2): scheduler answers phone P4?");
  if
	:: gScheduleAnswersPhoneP4 -> printf("yes\n");
				goto locPatientLeavesVMRequest;
	:: !gScheduleAnswersPhoneP4 -> printf("no\n");
				goto locSelectCallClinicTimeDate;
  fi;

locPatientLeavesVMRequest:
  run PatientLeavesVMRequest(rv);
  rv?_;
  goto locSchedulerCallsPatient;

locSchedulerCallsPatient:
  run SchedulerCallsPatient(rv);
  rv?_;
  
locSelectCallClinicTimeDate:
  run SelectCallClinicTimeDate(rv);
  rv?_;
  
  rvChan!1;
}

proctype MakePhoneClinicAppointmentP4(chan rvChan) {
  chan rv = [1] of {bool};
  bool yes;
  
  printf("Process: make phone clinic appointment P4\n");
  
  run PatientListensToInstructionsP4(rv);
  rv?_;

  run PatientEntersIDP4(rv);
  rv?_;

  run PCOffersTimeDate1P4(rv);
  rv?_;

  run PatientMakesSelectionP4_1(rv);
  rv?yes;

  printf("Gateway (XOR->2): select timeDate1?");
  if
	:: yes -> printf("Yes\n");
			  goto locPCWriteAppointmentTimeDateToDoctorSchedule;
	:: !yes -> printf("No\n");
			   goto locPCOffersTimeDate2P4;
  fi;

locPCWriteAppointmentTimeDateToDoctorSchedule:
  run PCWriteAppointmentTimeDateToDoctorSchedule(rv);
  rv?_;
  goto locDelayToApp;

locPCOffersTimeDate2P4:
  run PCOffersTimeDate2P4(rv);
  rv?_;

  run PatientMakesSelectionP4_2(rv);
  rv?yes;

  printf("Gateway (XOR->2): select timeDate2?");
  if
	:: yes -> printf("Yes\n");
			  goto locPCWriteAppointmentTimeDateToDoctorSchedule;
	:: !yes -> printf("No\n");
			   goto locPCCallClinicSchedulerP4;
  fi;
  
locPCCallClinicSchedulerP4:
  run PCCallClinicSchedulerP4(rv);
  rv?_;
  goto locDelayToApp;

locDelayToApp:
  printf("Event: delay to app P4\n");
  if
	:: skip -> goto locDelayToApp;
	:: skip -> goto progress_exit;
  fi;
  
progress_exit:
  rvChan!1;
}

proctype PCAutoCallP4(chan rvChan) {
  printf("Task: PC autocall P4\n");
  rvChan!1;
}

proctype PCLeaveVoicemailAndUpdateLogP4(chan rvChan) {
  printf("Task: PC leave voicmail and update log P4\n");
  rvChan!1;
}

proctype PatientChecksVoicemailP4(chan rvChan){
  printf("Task: Patient checks voicemail P4\n");
  rvChan!1;
}

proctype PatientCallsPCP4(chan rvChan) {
  printf("Task: Patient calls PC P4\n");
  rvChan!1;
}

proctype PatientVoicemailActionsP4(chan rvChan){
  chan rv = [1] of {bool};

  printf("Process: Patient voicmail actions P4\n");

locDelayCheckVMP4:
  printf("Event: delay check VM P4\n");
  if
	:: skip -> goto locDelayCheckVMP4;
	:: skip -> goto progress_locPatientChecksVoicemailP4;
  fi;

progress_locPatientChecksVoicemailP4:
  run PatientChecksVoicemailP4(rv);
  rv?_;

  run PatientCallsPCP4(rv);
  rv?_;

  rvChan!1;
}

proctype P4(chan rvChan) {
  chan rv = [1] of {bool};

  printf("Process: P4\n");
  printf("Gateway (XOR->2): call now and patient answers P4?");
  if
	:: gCallNowAndPatientAnswersP4 ->
	   printf("Yes\n");
	   goto locDiscussNoChange;
	:: !gCallNowAndPatientAnswersP4 ->
	   printf("No\n");
	   goto locPCAutoCallP4;
  fi;

locPCAutoCallP4:
  run PCAutoCallP4(rv);
  rv?_;
  goto PatientAnswersP4;

PatientAnswersP4:
  printf("Gateway: Patient answers P4?");
  if
	:: gPatientAnswersP4 -> printf("Yes\n");
							goto locMakePhoneClinicAppointment;
	:: !gPatientAnswersP4 -> printf("No\n");
							 goto locPCLeaveVoicemailAndUpdateLogP4;
							

  fi;

locPCLeaveVoicemailAndUpdateLogP4:
  run PCLeaveVoicemailAndUpdateLogP4(rv);
  rv?_;
  goto locPatientVoicemailActionsP4;

locPatientVoicemailActionsP4:
  run PatientVoicemailActionsP4(rv);
  rv?_;
  goto locMakePhoneClinicAppointment;

locMakePhoneClinicAppointment:
  run MakePhoneClinicAppointmentP4(rv);
  rv?_;
  goto locDiscussNoChange;
  
locDiscussNoChange:
  run discussNoChange(rv);
  rv?_;
  goto end_locCarryOutContactPlanEnd146;
  
end_locCarryOutContactPlanEnd146:
  /* The event "throws" */
  printf("Event: CarryOutContactPlanEnd175\n");
  CarryOutContactPlanEnd175 = 1;
  goto exit;
  
exit:
  rvChan!1;
}

proctype CarryOutContactPlan(chan rvChan) {
  chan rv = [1] of {bool};
  printf("Process: carry out contact plan\n");
  printf("Gateway (XOR->4)): priority?");
  if
	:: (pc.contactPriority == P1LifeThreatening) ->
	   printf("P1LifeThreatening\n");
	   goto locP1;
	:: (pc.contactPriority == P2LifeChanging) ->
	   printf("P2LifeChanging\n");
	   goto locP2;
	:: (pc.contactPriority == P3RoutineChange) ->
	   printf("P3RoutineChange\n");
	   goto locP3;
	:: (pc.contactPriority == P4NoChange) ->
	   printf("P4NoChange\n");
	   goto locP4;
  fi;

locP1:
  run P1(rv);
  rv?_;
  goto exit;

locP2:
  run P2(rv);
  rv?_;
  goto exit;

locP3:
  run P3(rv);
  rv?_;
  goto exit;

locP4:
  run P4(rv);
  rv?_;
  goto exit;

exit:
  (CarryOutContactPlanEnd33==1 || CarryOutContactPlanEnd56 || CarryOutContactPlanEnd146 || CarryOutContactPlanEnd175);
  rvChan!1;
}

init {
  chan rv = [1] of {bool};
  
  /* Initialize the input to the model (things that do not change over
  the life of the model) */

  /* Priority Setting */
  if
/*	:: skip -> gSeverity = P1LifeThreatening; printf("severity: P1 life threatening\n");
	:: skip -> gSeverity = P2LifeChanging; printf("severity: P2 life changing\n");
	:: skip -> gSeverity = P3RoutineChange; printf("severity: P3 routine change\n");
 */
	:: skip -> gSeverity = P4NoChange; printf("severity: P4 no change\n");
  fi;

  /* P4 Input */
  if
	:: skip -> gCallNowAndPatientAnswersP4 = 0;
			   printf("gCallNowAndPatientAnswersP4: No\n");
	:: skip -> gCallNowAndPatientAnswersP4 = 1;
			   printf("gCallNowAndPatientAnswersP4: Yes\n");
  fi;

  
  if
	:: skip -> gPatientAnswersP4 = 0;
			   printf("gPatientAnswersP4: No\n");
	:: skip -> gPatientAnswersP4 = 1;
			   printf("gPatientAnswersP4: Yes\n");
  fi;

  if
	:: skip -> gScheduleAnswersPhoneP4 = 0;
			   printf("gScheduleAnswersPhoneP4: No\n");
	:: skip -> gScheduleAnswersPhoneP4 = 1;
			   printf("gScheduleAnswersPhoneP4: Yes\n");
  fi;

  if
	:: skip -> gSelectDateTime1P4 = 0;
			   printf("gSelectDateTime1P4: No\n");
	:: skip -> gSelectDateTime1P4 = 1;
			   printf("gSelectDateTime1P4: Yes\n");
  fi;

  if
	:: skip -> gSelectDateTime2P4 = 0;
			   printf("gSelectDateTime2P4: No\n");			   
	:: skip -> gSelectDateTime2P4 = 1;
			   printf("gSelectDateTime2P4: Yes\n");
  fi;

  printf("Event: Start1\n");
  run CompleteCareAndAssessmentPlan(rv);
  rv?_;
end_End25:
  (End25 == 1);
  printf("End25\n");
}
