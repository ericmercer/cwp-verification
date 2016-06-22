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
  mtype conversationStartDateTime = null;
  mtype conversationScheduledDateTime = null;
  bool activeStatus = true;
}

ContactPlan cp;

/*********************************************************************
 * ContactPlanCWP-9
 *********************************************************************/

// Fix all state witnesses to be: [] !(state)
// Account for Exists eventually -- properties should fail 

// Fix all edges to be [](s0 -> (s0 U (s1 || s2 || ...)))
// Accounts for different paths -- properties should pass

#define iotaS                                   \
    (   cp.priorityCP == null	                \
	 && cp.launchDateTime == null               \
 	 && cp.resolveDateTime == null              \
 	 && cp.cancelDateTime == null               \
 	 && cp.conversationStartDateTime == null    \
	 && cp.conversationScheduledDateTime == null\
	 && cp.activeStatus == true) 
		 
ltl iota {
	[](! iotaS)
}

#define planLaunchedS                           \
    (   cp.priorityCP == null	                \
	 && cp.launchDateTime == nonNull            \
 	 && cp.resolveDateTime == null              \
 	 && cp.cancelDateTime == null               \
 	 && cp.conversationStartDateTime == null    \
 	 && cp.conversationScheduledDateTime == null\
	 && cp.activeStatus == true) 

ltl planLaunched {
	[](! planLaunchedS)
}
	
#define conversationInProgressS                      \
    (   (cp.priorityCP == p1 || cp.priorityCP == p2) \
	 && cp.conversationStartDateTime == nonNull      \
	 && cp.activeStatus == true)
	 
ltl conversationInProgress {
	[](! conversationInProgressS)
}

#define conversationAppointmentScheduledS            \
    (   (cp.priorityCP == p3 || cp.priorityCP == p4) \
	 && cp.conversationScheduledDateTime == nonNull  \
	 && cp.activeStatus == true)

ltl conversationAppointmentScheduled {
	[](! conversationAppointmentScheduledS)
}

#define planCanceledS \
    (cp.activeStatus == false)

ltl planCanceled {
	[](! planCanceledS)
}

#define resolvedS \
	(cp.resolveDateTime == nonNull)

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
	<>(conversationInProgressS -> (conversationInProgressS U resolvedS))
}

ltl conversationAppointmentScheduled_transitions {
	<>(conversationAppointmentScheduledS ->
	  (conversationAppointmentScheduledS U resolvedS))
}

ltl planCanceled_transitions {
	<>(planCanceledS -> (planCanceledS U canceledS))
}

/*********************************************************************
 * PC-to-be-9
 *********************************************************************/

init {
   printf("Start1\n");
}
