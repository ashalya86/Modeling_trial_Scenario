package trialForTreason;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.jpl7.Atom;
import org.jpl7.Query;
import org.jpl7.Term;

public class FindingSalientEvent {
	String[] salientEvents;
	String prologPath;
	boolean result1;
	boolean result2;
	String event;
	int currentTick;
	int humanCount;
	Query q1;
	Query q2;
	Query q3;
	
	public FindingSalientEvent(String prologPath, int currentTick, int humanCount, String[] salientEvents) {
		this.prologPath = prologPath;
//		this.event = event;
		this.currentTick = currentTick;
		this.humanCount = humanCount;
		this.salientEvents = salientEvents;
		
		this.q1 = 
			    new Query( 
				"consult", 
				new Term[] {new Atom(prologPath)} 
			    );	
	}
	
	
	public java.util.Map<String,Term>[] gettingCountAsEvents() {
		this.q1.hasSolution() ;

		for (int i = 0; i < salientEvents.length; i++) {
			this.q2 = new Query ("add_PFC((counts_as(" + salientEvents[i] + ", cooperate(secureCity))))");
			q2.hasSolution();
		}
		Query queryCountAsEvents = new Query("baseKB:counts_as(A,X).");
		java.util.Map<String,Term>[] solutions = queryCountAsEvents.allSolutions();
		
	return 	solutions;
	}
	
	public boolean resultOfSalient(String prologPath, String event, int currentTick, int humanCount, String[] salientEvents) throws IOException {
	 Query consult = 
			    new Query( 
				"consult", 
				new Term[] {new Atom(prologPath)} 
			    );
	 consult.hasSolution() ;
		
		for (int i = 0; i < salientEvents.length; i++) {
				if (salientEvents[i].equals(event)) {
					Query query1 = new Query ("add_PFC((counts_as(" + salientEvents[i] + ", cooperate(secureCity))))");
					query1.hasSolution();
					System.out.println(query1.hasSolution());
					Query query2 = new Query ("add_PFC((prim_action(citizen"+ humanCount + "," +  salientEvents[i] + "))).");
					query2.hasSolution();
					Query q4 = new Query ("add_PFC((group_member(citizen"+ humanCount+ ", citizens))).");
					q4.hasSolution();
					Query citizenAttendedAction = new Query("baseKB:attended_action(citizen" + humanCount + ",cooperate(secureCity)).");
					result1 = citizenAttendedAction.hasSolution();
				}
		}
		Query assemblyAttendedAction = new Query("baseKB:attended_action(assembly, decree(group_goal(citizens, secureCity)))"); 
	    result2 = assemblyAttendedAction.hasSolution();
		return result1;
	}
	
}	

