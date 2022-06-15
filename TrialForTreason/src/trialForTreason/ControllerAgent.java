package trialForTreason;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.jpl7.Atom;
import org.jpl7.Query;
import org.jpl7.Term;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.util.ContextUtils;
import repast.simphony.engine.schedule.ScheduleParameters;


public class ControllerAgent {
	public int actionNo;
	public String action;
	public float propability_of_non_conviction;
	int humanCount;
	ArrayList<String> cascading_events;
	String prologPath;
	String[] salientEvents;
	Query [] primitiveActionsQueries;

	public ControllerAgent(int human_count, String[] salientEvents, String prologPath) {
		this.cascading_events = creatingAgentsEvents(human_count, salientEvents);	
		this.prologPath = prologPath;
		this.salientEvents = salientEvents;
	}


	public static ArrayList<String> creatingAgentsEvents(int humanCount, String[] salientEvents){
		ArrayList<String> list = new ArrayList<String>();  
		Random randy = new Random();
		for (int i = 0; i < humanCount; i++) {
//			int actionNo = randy.nextInt(humanCount);
//			if (actionNo < humanCount/2) {
//				int salientNo = randy.nextInt(salientEvents.length);
//				list.add(salientEvents[salientNo]);
//			}
//			else {
				list.add("randEvent" + i);	
//			}
		}
		return list;
	}
    
   @ScheduledMethod(start = 1, interval = 1)
	public void step() {
		System.out.println("Random events, "+ cascading_events);
//		setting_prolog_variables(this.salientEvents, this.prologPath);
		cascadeAction(this.cascading_events);
		
	}
	  
	public void cascadeAction(ArrayList<String> cascading_events) {
		Context context = ContextUtils.getContext(this);
		Iterable<Citizen> i = context.getAgentLayer(Citizen.class);
	      Iterator<Citizen> it = i.iterator();
	      int cascadeIndex = 0; 
	      while (it.hasNext())
	      {
	            Citizen c = it.next();  
	            c.setRandomAction(cascading_events.get(cascadeIndex), this.salientEvents);
	            cascadeIndex++;
	      }      
	}
	
	public Query [] setting_prolog_variables(String [] salientEvents, String prologPath) {
		primitiveActionsQueries = null;
		Query q1 = 
			    new Query( 
				"consult", 
				new Term[] {new Atom(prologPath)} 
			    );
		System.out.println( "consult " + q1.hasSolution() );
		
		Query q3 = new Query ("add_PFC((group_member(citizen"+ humanCount+ ", citizens))).");
		primitiveActionsQueries[0] = q3;
		System.out.println(q3.hasSolution());
		for (int i = 0; i < salientEvents.length; i++) {
			Query query = new Query ("add_PFC((counts_as(" + salientEvents[i] + ", cooperate(secureCity))))");
			System.out.println(query.hasSolution());
			primitiveActionsQueries[i] = query;
		}
		 
		Citizen c = new Citizen ();
		c.setPremitiveAction(primitiveActionsQueries);
		return primitiveActionsQueries;
	}
}
