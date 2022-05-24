package trialForTreason;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.jpl7.Atom;
import org.jpl7.Query;
import org.jpl7.Term;

import trialForTreason.HisSaver;

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
	
	public boolean resultOfSalient(String prologPath, String event, int currentTick, int humanCount) throws IOException {
		HisSaver hs = HisSaver.getInstance(prologPath);
		hs.saveWord(":-add_PFC((prim_action(citizen"+ humanCount + "," +  event + "))).");
		hs.saveWord(":-add_PFC((counts_as(" + event + ", cooperate(secureCity)))).");
		hs.saveWord(":-add_PFC((group_member(citizen"+ humanCount+ ", citizens))).");

		Query q1 = 
			    new Query( 
				"consult", 
				new Term[] {new Atom(prologPath)} 
			    );
		System.out.println( "consult " + q1.hasSolution() );

		Query citizenAttendedAction = new Query("baseKB:attended_action(citizen" + humanCount + ",cooperate(secureCity)).");
		Query assemblyAttendedAction = new Query("baseKB:attended_action(assembly, decree(group_goal(citizens, secureCity)))"); 
		result1 = citizenAttendedAction.hasSolution();
	    result2 = assemblyAttendedAction.hasSolution();
	    System.out.println("..............." + assemblyAttendedAction.hasSolution());
		return result2;
	}
	
//	public static Query[] returningQueries(String[] events, Query[] queries, int currentTick) {	
//		String[] actions= new String[events.length];
//		for (int i=0; i<events.length; i++) {
//    		actions[i] = "assert(action(A,"+ events[i]+ "," + currentTick +")).";
//        	queries[i] = new Query(actions[i]);
//    	}		
//		 return queries ;
//	}	
}	

