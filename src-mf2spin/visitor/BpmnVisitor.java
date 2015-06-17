package visitor;

import mathflow.nodes.*;
import mathflow.nodes.event.*;
import mathflow.nodes.gate.*;
import mathflow.nodes.process.*;
import mathflow.nodes.task.*;

public interface BpmnVisitor {
	
	boolean visit(MFEnd node);
	boolean visit(MFIntermediateTimer node);
	boolean visit(MFStart node);
	boolean visit(MFAndGate node);
	boolean visit(MFOrGate node);
	boolean visit(MFXorGate node);
	boolean visit(MFProcess node);
	boolean visit(MFSubprocess node);
	boolean visit(MFFirstProcess node);
	boolean visit(MFManualTask node);
	boolean visit(MFServiceTask node);
	boolean visit(MFUserTask node);
	boolean visit(MFSystem node);
	boolean visit(MFPerformer node);
	boolean visit(MFPerson node);
	
	void endVisit(MFEnd node);
	void endVisit(MFIntermediateTimer node);
	void endVisit(MFStart node);
	void endVisit(MFAndGate node);
	void endVisit(MFOrGate node);
	void endVisit(MFXorGate node);
	void endVisit(MFProcess node);
	void endVisit(MFSubprocess node);
	void endVisit(MFFirstProcess node);
	void endVisit(MFManualTask node);
	void endVisit(MFServiceTask node);
	void endVisit(MFUserTask node);
	void endVisit(MFSystem node);
	void endVisit(MFPerformer node);
	void endVisit(MFPerson node);
	
	void preVisit(MFControlNode node);
	void postVisit(MFControlNode node);
	
}
