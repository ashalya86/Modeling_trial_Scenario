package trialForTreason;

import java.io.IOException;

import org.jpl7.Atom;
import org.jpl7.Query;
import org.jpl7.Term;


public class Example {
	static String prologPath = "src/trialForTreason/cascadings.pl";
    static String[] salientEvents ={"buildingWalls", "makingPalisades"};
    String [] actions  = {"A", "R","R", "R", "A", "A", "A", "A", "A", "A"};
    Query q1;
	Query q2;
	Query q3;
	Query[] queriesSailentEvents = new Query[salientEvents.length];
	
    
    public Example() {
    	this.q1 = 
			    new Query( 
				"consult", 
				new Term[] {new Atom(prologPath)} 
			    );	
    	// feeding what salient events are
		for (int i = 0; i < salientEvents.length; i++) {
			this.queriesSailentEvents[i] = new Query ("add_PFC((counts_as(" + salientEvents[i] + ", cooperate(secureCity))))");
//			this.queriesSailentEvents[i].hasSolution();
		}
    }
    
    public java.util.Map<String,Term>[] gettingCountAsEvents() {
		this.q1.hasSolution();
		for (int i = 0; i < this.queriesSailentEvents.length; i++) {
			this.queriesSailentEvents[i].hasSolution();
		}
		
		Query queryCountAsEvents = new Query("baseKB:counts_as(A,X).");
		java.util.Map<String,Term>[] solutions = queryCountAsEvents.allSolutions();	
	return 	solutions;
	}

	public static void main(String[] args) throws IOException {
	    System.out.println("Hello from the Java Main Function!");
//		FindingSalientEvent salientEvent = new FindingSalientEvent(prologPath, 2, 2, salientEvents);
//	    System.out.println("Hello from the Java Main Function!");
//		boolean resultSalient = salientEvent.resultOfSalient(prologPath, "randEvent", 2, 3, salientEvents);
//		System.out.println(resultSalient);
	    Example ex = new Example();
		java.util.Map<String,Term>[] solutions = ex.gettingCountAsEvents();
		for ( int i = 0 ; i < solutions.length ; i++ ) { 
			System.out.println( "X = " + solutions[i].get("A")); 
			}
	  }
}
