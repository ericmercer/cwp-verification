never {
  assert(pc.contactPriority == null &&
		 pc.launchDate == null && pc.launchTime == null &&
		 pc.resolveDate == null && pc.resolveTime == null &&
		 pc.confirmedAppointmentDateTime == null &&
		 pc.conversDate == null && pc.conversTime == null &&
         pc.inMyCare == 1);
start:
  if
	:: (pc.launchDate == nonNull && pc.launchTime == nonNull &&
		pc.resolveDate == null && pc.resolveTime == null &&
		pc.confirmedAppointmentDateTime == null &&
		pc.conversDate == null && pc.conversTime == null &&
		pc.inMyCare == 1) -> goto accept_launched;
	:: (pc.launchDate == null && pc.launchTime == null &&
		pc.resolveDate == null && pc.resolveTime == null &&
		pc.confirmedAppointmentDateTime == null &&
		pc.conversDate == null && pc.conversTime == null &&
		pc.inMyCare == 1) -> goto start;
	:: else -> goto accept;
  fi;

accept_launched:
  if
	:: (pc.conversDate != null && pc.conversTime != null) -> goto accept_conversationInProgress;
	:: (pc.confirmedAppointmentDateTime != null) -> goto accept_appointmentScheduled;
	:: (pc.inMyCare == 0) -> goto noLongerNeeded;
	:: else -> goto accept_launched;
  fi;

accept_conversationInProgress:
  if 
	:: (pc.resolveDate != null && pc.resolveTime != null) -> goto resolved;
	:: (pc.inMyCare == 0) -> goto noLongerNeeded;
	:: else -> goto accept_conversationInProgress;
  fi;

accept_appointmentScheduled:
  if
	:: (pc.resolveDate != null && pc.resolveTime != null) -> goto resolved;
	:: (pc.inMyCare == 0) -> goto noLongerNeeded;
	:: else -> goto accept_appointmentScheduled;
  fi;

resolved:
  goto noLongerNeeded;
  
accept:
  do
	:: 1
  od;

noLongerNeeded:
  (0);
}
