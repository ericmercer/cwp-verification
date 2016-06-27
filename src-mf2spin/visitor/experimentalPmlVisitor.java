package visitor;

import java.util.Stack;

import mathflow.nodes.MFControlNode;
import mathflow.nodes.MFPerformer;
import mathflow.nodes.MFPerson;
import mathflow.nodes.MFSystem;
import mathflow.nodes.event.MFEnd;
import mathflow.nodes.event.MFIntermediateTimer;
import mathflow.nodes.event.MFStart;
import mathflow.nodes.gate.MFAndGate;
import mathflow.nodes.gate.MFOrGate;
import mathflow.nodes.gate.MFXorGate;
import mathflow.nodes.process.MFFirstProcess;
import mathflow.nodes.process.MFProcess;
import mathflow.nodes.process.MFSubprocess;
import mathflow.nodes.task.MFManualTask;
import mathflow.nodes.task.MFServiceTask;
import mathflow.nodes.task.MFUserTask;

public class experimentalPmlVisitor implements BpmnVisitor {
	
	PromelaFileStructure pmlFS;
	Stack<String> procTypeCodeSegments;
	Stack<String> procTypeCodeIDs;
	String curProcessID;
	String curProcTypeCodeSegment;
	boolean visitChildren;
	int numProcTypesAdded;
	String firstProcessID;
	
	public experimentalPmlVisitor() {
		pmlFS = new PromelaFileStructure();
		procTypeCodeSegments = new Stack<String>();
		procTypeCodeIDs = new Stack<String>();
		curProcessID = "";
		curProcTypeCodeSegment = "";
		visitChildren = true;
		numProcTypesAdded = 0;
		firstProcessID = "";
	}

	@Override
	public boolean visit(MFEnd node) {
		//System.out.println(":END "+"\""+node.name+"\" node");
		if (node.parentProcess.id.equals(curProcessID) == false) {
			changeToNewCodeSegment(node.parentProcess.id);
		}
		curProcTypeCodeSegment += "end_"+changeToUpperCamelCase(node.name)+":\n"+
								  "  goto exit;\n"+
								  "exit:\n"+
								  "  rvChan!1;\n";
		if (curProcessID == firstProcessID) {
			//System.out.println(":END: \""+node.name+"\" node");
			curProcTypeCodeSegment += "}";
			numProcTypesAdded++;
			pmlFS.addToProcTypes(Integer.toString(numProcTypesAdded), curProcTypeCodeSegment);
		}
		return visitChildren;
	}

	@Override
	public boolean visit(MFIntermediateTimer node) {
		//System.out.println(":TIMER "+"\""+node.name+"\" node");
		if (node.parentProcess.id.equals(curProcessID) == false) {
			changeToNewCodeSegment(node.parentProcess.id);
		}
		curProcTypeCodeSegment += ":delay:\n";
		return visitChildren;
	}

	@Override
	public boolean visit(MFStart node) {
		//System.out.println(":START "+"\""+node.name+"\" node");
		if (node.parentProcess.id.equals(curProcessID) == false) {
			assert(false);
		}
		curProcTypeCodeSegment += ":start:\n";
		return visitChildren;
	}

	@Override
	public boolean visit(MFAndGate node) {
		//System.out.println(":AND "+"\""+node.name+"\" node");
		if (node.parentProcess.id.equals(curProcessID) == false) {
			changeToNewCodeSegment(node.parentProcess.id);
		}
		String line = ":AND Gate: \n[";
		for (int i=0; i<node.numberOfEdges(); i++) {
			line += node.getNextNode(i).name;
			line += " (C="+node.getNextCondition(i)+")";
			if (i != node.numberOfEdges()-1) {
				line += ", \n";
			}
		}
		line += "]";
		curProcTypeCodeSegment += line+"\n";
		return visitChildren;
	}

	@Override
	public boolean visit(MFOrGate node) {
		//System.out.println(":OR "+"\""+node.name+"\" node");
		if (node.parentProcess.id.equals(curProcessID) == false) {
			changeToNewCodeSegment(node.parentProcess.id);
		}
		String line = ":OR Gate: \n[";
		for (int i=0; i<node.numberOfEdges(); i++) {
			line += node.getNextNode(i).name;
			line += " (C="+node.getNextCondition(i)+")";
			if (i != node.numberOfEdges()-1) {
				line += ", \n";
			}
		}
		line += "]";
		curProcTypeCodeSegment += line+"\n";
		return visitChildren;
	}

	@Override
	public boolean visit(MFXorGate node) {
		//System.out.println(":XOR "+"\""+node.name+"\" node");
		if (node.parentProcess.id.equals(curProcessID) == false) {
			changeToNewCodeSegment(node.parentProcess.id);
		}
		String line = ":XOR Gate: \n[";
		for (int i=0; i<node.numberOfEdges(); i++) {
			line += node.getNextNode(i).name;
			line += " (C="+node.getNextCondition(i)+")";
			if (i != node.numberOfEdges()-1) {
				line += ", \n";
			}
		}
		line += "]";
		curProcTypeCodeSegment += line+"\n";
		return visitChildren;
	}

	@Override
	public boolean visit(MFProcess node) {
		//System.out.println(":PROCESS "+"\""+node.name+"\" node");
		assert(node.id.equals(curProcessID) == false);
		curProcTypeCodeSegment = "";
		curProcessID = node.id;
		curProcTypeCodeSegment += changeToUpperCamelCase(node.name)+"() {\n";
		return visitChildren;
	}

	@Override
	public boolean visit(MFSubprocess node) {
		//System.out.println(":SUBPROCESS "+"\""+node.name+"\" node");
		if (node.parentProcess.id.equals(curProcessID) == false) {
			changeToNewCodeSegment(node.parentProcess.id);
		}
		curProcTypeCodeSegment += ":Subprocess:\n";
		//call process assoc w/ this subprocess
		procTypeCodeIDs.push(node.parentProcess.id);
		procTypeCodeSegments.push(curProcTypeCodeSegment);
		node.getCalledProcess().accept(this);
		return visitChildren;
	}

	@Override
	public boolean visit(MFFirstProcess node) {
		//System.out.println(":1ST_PROCESS "+"\""+node.name+"\" node");
		assert(node.id.equals(curProcessID) == false);
		curProcessID = node.id;
		firstProcessID = node.id;
		//procTypeCodeSegments.push("");
		curProcTypeCodeSegment += "init {\n";
		return visitChildren;
	}

	@Override
	public boolean visit(MFManualTask node) {
		System.out.println("ManualTask");
		System.out.println(node.toString());
		//System.out.println(":MANUAL_TASK "+"\""+node.name+"\" node");
		if (node.parentProcess.id.equals(curProcessID) == false) {
			changeToNewCodeSegment(node.parentProcess.id);
		}
		curProcTypeCodeSegment += ":manual task:\n";
		return visitChildren;
	}

	@Override
	public boolean visit(MFServiceTask node) {
		System.out.println("ServiceTask");
		System.out.println(node.toString());
		//System.out.println(":SERVICE_TASK "+"\""+node.name+"\" node");
		if (node.parentProcess.id.equals(curProcessID) == false) {
			changeToNewCodeSegment(node.parentProcess.id);
		}
		curProcTypeCodeSegment += ":service task:\n";
		return visitChildren;
	}

	@Override
	public boolean visit(MFUserTask node) {
		System.out.println("UserTask");
		System.out.println(node.toString());
		//System.out.println(":USER_TASK "+"\""+node.name+"\" node");
		if (node.parentProcess.id.equals(curProcessID) == false) {
			changeToNewCodeSegment(node.parentProcess.id);
		}
		curProcTypeCodeSegment += ":user task:\n";
		return visitChildren;
	}

	@Override
	public boolean visit(MFSystem node) {
		//System.out.println(":SYSTEM "+"\""+node.name+"\" node");
		pmlFS.addToGlobalVars(node.id, "system= "+node.name);
		return visitChildren;
	}

	@Override
	public boolean visit(MFPerformer node) {
		//System.out.println(":PERFORMER "+"\""+node.name+"\" node");
		pmlFS.addToGlobalVars(node.id, "performer= "+node.name);
		return visitChildren;
	}

	@Override
	public boolean visit(MFPerson node) {
		//System.out.println(":PERSON "+"\""+node.name+"\" node");
		pmlFS.addToGlobalVars(node.id, "person= "+node.name);
		return visitChildren;
	}

	@Override
	public void endVisit(MFEnd node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endVisit(MFIntermediateTimer node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endVisit(MFStart node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endVisit(MFAndGate node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endVisit(MFOrGate node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endVisit(MFXorGate node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endVisit(MFProcess node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endVisit(MFSubprocess node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endVisit(MFFirstProcess node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endVisit(MFManualTask node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endVisit(MFServiceTask node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endVisit(MFUserTask node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endVisit(MFSystem node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endVisit(MFPerformer node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endVisit(MFPerson node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preVisit(MFControlNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postVisit(MFControlNode node) {
		// TODO Auto-generated method stub

	}
	
	public String pmlFileToString() {
		return pmlFS.toString();
	}
	
	private void changeToNewCodeSegment(String nodeParentProcessID) {
		//System.out.println("Process \""+curProcessID+"\" complete: return to \""+nodeParentProcessID+"\"");
		curProcTypeCodeSegment += "}\n";
		numProcTypesAdded++;
		pmlFS.addToProcTypes(Integer.toString(numProcTypesAdded), curProcTypeCodeSegment);
		
		
		while (nodeParentProcessID.equals(procTypeCodeIDs.peek()) == false) {
			curProcessID = procTypeCodeIDs.pop();
			curProcTypeCodeSegment = procTypeCodeSegments.pop();
			curProcTypeCodeSegment += "}\n";
			numProcTypesAdded++;
			pmlFS.addToProcTypes(Integer.toString(numProcTypesAdded), curProcTypeCodeSegment);
		}
		
		curProcessID = procTypeCodeIDs.pop();
		curProcTypeCodeSegment = procTypeCodeSegments.pop();
		
	}
	
	private String changeToUpperCamelCase(String line) {
		String result = "";
		String[] words = line.split("\\s+");
		StringBuilder builder = new StringBuilder();
		for (int i=0; i<words.length; i++) {
			builder.append(Character.toUpperCase(words[i].charAt(0)));
			if (words[i].length() > 1) {
				builder.append(words[i].substring(1));
			}
		}
		result = builder.toString();
		return result;
	}
}
