/* Priority */
#define  P1LifeThreatening 0
#define  P2LifeChanging    1
#define  P3RoutineChange   2
#define  P4NoChange        3


mtype = {
  VOICE
}


typedef Conversation {
  bool date = 0;
  bool time = 0;
};

typedef Patient {
  /*byte name[20];*/
  bool name;
  bool inMyCare = 0;
  short VANumber = 0;
  chan mobile = [0] of {mtype};
  chan home = [0] of {mtype};
  chan business = [0] of {mtype};
  chan emergencyContact = [0] of {mtype}
}

typedef Doctor {
  /*byte name[20];*/
  chan mobile = [0] of {mtype};
  chan office = [0] of {mtype};
}

typedef TreatmentPlan{
  bool diseaseDiagnosis = 0;
  bool Severity = 0;
  bool treatmentOptions = 0;
  bool nextAppointment = 0;
}

typedef ContactPlan{
  byte cpPriority;
  bool launchDate = 0;
  bool launchTime = 0;
  bool resolveDate = 0;
  bool resolveTime = 0;
  Doctor doctor;
  Patient patient;
  TreatmentPlan treatmentPlan;
  Conversation conversation;
}

ContactPlan cpe;

proctype patientProc() {
  printf ("Patient stuff...");
}

proctype doctorProc() {
  printf ("Patient stuff...");
}

proctype p1LifeThreateningProc(chan p) {
  p!1;
}

proctype p2LifeChangingProc(chan p) {
  p!1;
}

proctype p3RoutineChangeProc(chan p) {
  p!1;
}

proctype p4NoChangeProc(chan p) {
  p!1;
}

proctype carryOutContactPlanProc() {
  chan child = [0] of {bool};
  bool result = 0;
  
  printf("Carry out contact plan\n");
  printf("Query: priority?\n");
  if
	:: cpe.cpPriority == P1LifeThreatening -> run p1LifeThreateningProc(child);
	:: cpe.cpPriority == P2LifeChanging -> run p2LifeChangingProc(child);
	:: cpe.cpPriority == P3RoutineChange -> run p3RoutineChangeProc(child);
	:: cpe.cpPriority == P4NoChange -> run p4NoChangeProc(child);
  fi;
  child?result;
}

proctype cpeMonitor() {
  printf ("Monitor the cpe");
end:
  assert(cpe.resolveDate && cpe.resolveTime);
}

init {
  
  /* Complete care and asessement plan: initializes the contact plan entity */
ConfirmDiagnosisAndSeverity:
  printf("Confirm diagnosis and severity:");
  if
	:: skip -> cpe.cpPriority = P1LifeThreatening; printf("P1 life threatening\n");
	:: skip -> cpe.cpPriority = P2LifeChanging; printf("P2 life changing\n");
	:: skip -> cpe.cpPriority = P3RoutineChange; printf("P3 routine change\n");
	:: skip -> cpe.cpPriority = P4NoChange; printf("P4 no change\n");
  fi;
  goto QueryLifeThreatening;
  
QueryLifeThreatening:
  printf("Query: life threatening?\n");
  if
	:: cpe.cpPriority == P1LifeThreatening -> goto LaunchPriorityContact;
	:: cpe.cpPriority != P2LifeChanging -> goto DetermineAppropriateIntervention;
  fi;

DetermineAppropriateIntervention:
  /* ??: Is there any interaction with the CWP in this step? */
  printf("Determine appropriate intervention\n");
  goto LaunchPriorityContact;

LaunchPriorityContact:
  printf("Initiate priority contact\n");
  cpe.patient.name = 1;
  cpe.patient.VANumber = 1;
  cpe.launchDate = 1;
  cpe.launchTime = 1;
  goto CarryOutContactPlan;

CarryOutContactPlan:
  run cpeMonitor();
  run patientProc();
  run doctorProc();
  run carryOutContactPlanProc();
}