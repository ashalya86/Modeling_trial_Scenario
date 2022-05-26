package trialForTreason;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jpl7.Atom;
import org.jpl7.Query;
import org.jpl7.Term;

public class CheckProlog {
	static String prologPath = "src/trialForTreason/cascading_pfc.pl";
	static String result;

	public static void main(String args[]) throws IOException {
		
		int humanCount = 1;
		String event = "rebuildingWalls";
		
		FileWriter exampleFileWriter = new FileWriter(prologPath, true);
		BufferedWriter out = new BufferedWriter(exampleFileWriter);
		out.write(":-add_PFC((prim_action(citizen"+ humanCount + "," +  event + ")))."+ "\n");
//		out.write(":-add_PFC((counts_as(" + event + ", cooperate(secureCity))))."+ "\n");
//		out.write(":-add_PFC((group_member(citizen"+ humanCount+ ", citizens)))." +"\n");
		out.close();
//		Query q2 = new Query ("add_PFC((group_member(citizen"+ humanCount+ ", citizens))).");
//		System.out.println(q2.hasSolution());
		Query q1 = 
			    new Query( 
				"consult", 
				new Term[] {new Atom(prologPath)} 
			    );
		
		System.out.println( "consult " + q1.hasSolution() );
		
		Query q3 = new Query ("add_PFC((group_member(citizen"+ humanCount+ ", citizens))).");
		System.out.println(q3.hasSolution());
		Query q4 = new Query ("add_PFC((counts_as(" + event + ", cooperate(secureCity))))");
		System.out.println(q4.hasSolution());


//		Query query1 = new Query("baseKB:attended_action(assembly, decree(group_goal(citizens, secureCity)))"); 
//		boolean found = false;
//	    System.out.println("..............." + query1.hasSolution());
//
//	    Query query2 = new Query("baseKB:attended_action(X, Y).");
//		   System.out.println("..............." + query2.hasSolution()); 
//		Map<String, Term> solution = new HashMap<String,Term> ();
//		while (query2.hasMoreSolutions() && !found) { // until a good sol is found
//		    solution = query2.nextSolution();  
//		   System.out.println("..............." + solution);    
//		}
		
		String actionQuery = "baseKB:attended_action(citizen" + humanCount + ",cooperate(secureCity)).";
	    Query query3 = new Query (actionQuery);
		   System.out.println("..............." + query3.hasSolution());    


//		
//		public static Query[] returningQueries(String[] events, Query[] queries, int currentTick) {	
//			String[] actions= new String[events.length];
//			for (int i=0; i<events.length; i++) {
//	    		actions[i] = "assert(action(A,"+ events[i]+ "," + currentTick +")).";
//	        	queries[i] = new Query(actions[i]);
//	    	}		
//			 return queries ;
//		}
//		
//		Query salient = 
//				  new Query( 
//				      "baseKB:action", 
//				      new Term[] {new Atom("assembly"), new Atom("decree(Prop)")} 
//				  );
//				if (salient.hasSolution() == true) {
//					 result = "salient";
//				}else {
//					 result = "not salient";
//				}
//
//		Query salient = 
//				  new Query( 
//				      "salient", 
//				      new Term[] {new Atom("A"), new Atom("seeing_monument"), new org.jpl7.Integer(5)} 
//				  );
//				if (salient.hasSolution() == true) {
//					 result = "salient";
//					 System.out.println( "salient ");
//				}else {
//					 result = "not salient";
//				}
		
//		Query salient = 
//				  new Query( 
//				      "baseKB:action", 
//				      new Term[] {new Atom("assembly"), new Atom("decree(Prop)")} 
//				  );
//				if (salient.hasSolution() == true) {
//					 result = "salient";
//				}else {
//					 result = "not salient";
//				}
		   
//			
//			Query salient = 
//					  new Query( 
//					      "salient", 
//					      new Term[] {new Atom("between(1, N, ?), atomic_contact(citizen, N, CitN), assert(member(CitN, citizen))"), new org.jpl7.Integer(humanCount)} 
//					  );
//			
//					  salient.hasSolution();
//			FindingSalientEvent salientEvent = new FindingSalientEvent(this.events, this.prologPath, event, currentTick);

		
	}
}
