package trialForTreason;

import java.util.Map;

import org.jpl7.Atom;
import org.jpl7.Query;
import org.jpl7.Term;

public class ExampleProlog {
	String[] events;
	String prologPath;
	String result;
	String event;
	int currentTick;
	
	public ExampleProlog(String[] events, String prologPath, String event, int currentTick) {
		this.events = events;	
		this.prologPath = prologPath;
		this.event = event;
		this.currentTick = currentTick;
	}
	
	public String resultOfSalient(String prologPath, String event, int currentTick) {
		  Query q1 = 
			    new Query( 
				"consult", 
				new Term[] {new Atom(prologPath)} 
			    );
		System.out.println( "consult " + q1.hasSolution() );
		  Query[] queries = new Query[events.length];
		  queries = returningQueries (events, queries, currentTick); 		  
		  if (queries[0].hasNext() && queries[1].hasNext() && queries[2].hasNext()) {
		  Query salient = 
				  new Query( 
				      "salient", 
				      new Term[] {new Atom("A"), new Atom(event), new org.jpl7.Integer(currentTick)} 
				  );
				if (salient.hasSolution() == true) {
					 result = "salient";
				}else {
					 result = "not salient";
				}
		  }
			return result;
	}
	
	public static Query[] returningQueries(String[] events, Query[] queries, int currentTick) {	
		String[] actions= new String[events.length];
		for (int i=0; i<events.length; i++) {
    		actions[i] = "assert(action(A,"+ events[i]+ "," + currentTick +")).";
        	queries[i] = new Query(actions[i]);
    	}		
		 return queries ;
	}	
}	

