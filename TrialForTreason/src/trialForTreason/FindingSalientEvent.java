package trialForTreason;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jpl7.Atom;
import org.jpl7.Query;
import org.jpl7.Term;

public class FindingSalientEvent {
	String[] salientEvents;
	String prologPath;
	boolean resultCitizenAttendedAction;
	boolean result2;
	String event;
	int currentTick;
	int humanCount;
	Query queryConsult;
	List<Query> queriesSailentEvents = new ArrayList<Query>();
	
	public FindingSalientEvent(String prologPath, int currentTick, String[] salientEvents, Query queryConsult) {
		this.prologPath = prologPath;
		this.currentTick = currentTick;
		this.salientEvents = salientEvents;
		this.queryConsult = queryConsult; 
//			    new Query( 
//				"consult", 
//				new Term[] {new Atom(prologPath)} 
//			    );	
		// feeding what salient events are
		for (int i = 0; i < salientEvents.length; i++) {
			this.queriesSailentEvents.add(new Query ("add_PFC((counts_as(" + salientEvents[i] + ", cooperate(secureCity))))"));
		}
	}
	
	public java.util.Map<String,Term>[] gettingCountAsEvents() {
		this.queryConsult.hasSolution();		
		for (int i = 0; i < this.queriesSailentEvents.size(); i++) {
			this.queriesSailentEvents.get(i).hasSolution();
		}
		Query queryCountAsEvents = new Query("baseKB:counts_as(A,X).");
		java.util.Map<String,Term>[] solutions = queryCountAsEvents.allSolutions();	
	return 	solutions;
	}
	
	public boolean resultOfSalient(String prologPath, String event, int currentTick, int humanCount, String[] salientEvents, int jurors_count) throws IOException {
		queryConsult.hasSolution() ;	
		for (int i = 0; i < salientEvents.length; i++) {
				if (salientEvents[i].equals(event)) {
					Query query1 = new Query ("add_PFC((jury"+ jurors_count + ":counts_as(" + salientEvents[i] + ", cooperate(secureCity))))");
					query1.hasSolution();
					System.out.println(query1.hasSolution());
					Query query2 = new Query ("add_PFC((jury"+ jurors_count + ":prim_action(citizen"+ humanCount + "," +  salientEvents[i] + "))).");
					query2.hasSolution();
					Query q4 = new Query ("add_PFC((jury" + jurors_count+ ":group_member(citizen"+ humanCount+ ", citizens))).");
					q4.hasSolution();
					Query citizenAttendedAction = new Query("baseKB:attended_action(citizen" + humanCount + ",cooperate(secureCity)).");
					resultCitizenAttendedAction = citizenAttendedAction.hasSolution();
				}
		}
		Query assemblyAttendedAction = new Query("baseKB:attended_action(assembly, decree(group_goal(citizens, secureCity)))"); 
	    result2 = assemblyAttendedAction.hasSolution();
		return resultCitizenAttendedAction;
	}
	
}	

