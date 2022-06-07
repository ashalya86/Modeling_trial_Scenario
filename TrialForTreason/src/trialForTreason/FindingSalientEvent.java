package trialForTreason;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.jpl7.Atom;
import org.jpl7.Query;
import org.jpl7.Term;

public class FindingSalientEvent {
	String[] events;
	String prologPath;
	boolean result1;
	boolean result2;
	String event;
	int currentTick;
	int humanCount;
	
	public FindingSalientEvent(String prologPath, String event, int currentTick, int humanCount) {
		this.prologPath = prologPath;
		this.event = event;
		this.currentTick = currentTick;
		this.humanCount = humanCount;
	}
	
	public boolean resultOfSalient(String prologPath, String event, int currentTick, int humanCount, String[] salientEvents) throws IOException {
		Query q1 = 
			    new Query( 
				"consult", 
				new Term[] {new Atom(prologPath)} 
			    );
				q1.hasSolution() ;
		
		for (int i = 0; i < salientEvents.length; i++) {
			if (event == salientEvents[i]) {
				Query query1 = new Query ("add_PFC((counts_as(" + event + ", cooperate(secureCity))))");
				query1.hasSolution();
				Query query2 = new Query ("add_PFC((prim_action(citizen"+ humanCount + "," +  event + "))).");
				query2.hasSolution();
			}	
		}
		
		Query q4 = new Query ("add_PFC((group_member(citizen"+ humanCount+ ", citizens))).");
		q4.hasSolution(); 
		
		Query citizenAttendedAction = new Query("baseKB:attended_action(citizen" + humanCount + ",cooperate(secureCity)).");
		Query assemblyAttendedAction = new Query("baseKB:attended_action(assembly, decree(group_goal(citizens, secureCity)))"); 
		result1 = citizenAttendedAction.hasSolution();
	    result2 = assemblyAttendedAction.hasSolution();
		return result1;
	}
	
}	

