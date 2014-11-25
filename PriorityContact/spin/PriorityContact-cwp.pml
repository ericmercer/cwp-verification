/* Priority */
#define  P1LifeThreatening 0
#define  P2LifeChanging    1
#define  P3RoutineChange   2
#define  P4NoChange        3


mtype = {
  VOICE
}

typedef TreatmentPlan{
  bool diseaseDiagnosis = 0;
  bool Severity = 0;
  bool treatmentOptions = 0;
  bool nextAppointment = 0;
}

typedef Phone{
  bool sms = 0;
  bool call = 0;
  bool voiceMail = 0;
};

typedef Conversation {
  bool date = 0;
  bool time = 0;
};

typedef Disease {
  bit symptoms;
}

typedef Patient {
  byte name[20];
  bool inMyCare = 0;
  short VANumber = 0;
  chan mobile = [1] of {mtype};
  Phone home;
  Phone business;
  Phone emergencyContact;
  Disease disease;
}

typedef Doctor {
  byte name[20];
  Phone mobile;
  Phone office;
}

typedef ContactPlan{
  byte cpPriority;
  bool launchDate = 0;
  bool launchTime = 0;
  bool resolveDate = 0;
  bool resolveTime = 0;
  Doctor doctor;
  Patient patient;
  Disease disease;
  TreatmentPlan treatmentPlan;
  Conversation conversation;
}


typedef PhoneType {
  bit sms = 0;
  bit phone = 0;
  bit voiceMail = 0;
}

ContactPlan cpe;
PhoneType phone;

active proctype patientProc() {
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
  
PCAnswersAndGivesInstructions:
{
  printf("PC answers and gives instructions\n");

  printf("Patient enters ID\n");

  printf("PC updates log\n");
}

}

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

  printf("Patient enters ID\n");

  printf("PC updates log\n");
}
goto CarryOutContactPlanEnd;

qCallNowAndPatientAnswersP2:
qPreferredCommunicationModeP3:
qPreferredCommunicationModeP4:


CarryOutContactPlanEnd:
printf("Carry out contact plan end\n");
}
