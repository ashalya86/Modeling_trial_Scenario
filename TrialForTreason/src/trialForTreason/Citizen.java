/**
 * 
 */
package trialForTreason;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.jpl7.Atom;
import org.jpl7.Query;
import org.jpl7.Term;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.SimUtilities;
import java.util.logging.Logger;
import java.util.Iterator;

/**
 * @author nick
 *
 */
public class Citizen {
	
	String event;
	String[] events;
	String prologPath;
	int currentTick;
	int cascadeIndex;
	String action;
	int humanCount;
	ArrayList<String> cascading_events;
	Query [] primitiveActionsQueries;
	String[] salientEvents;
	int salientEventAdopters;
	String cascadingEvent;
	int Count;
	int noOfAgents;

	public Citizen(String action, String prologPath, String[] salientEvents, int humanCount) {
		this.action = action;
		this.prologPath = prologPath;
		this.salientEvents = salientEvents;
		this.humanCount = humanCount;
		this.salientEventAdopters = 0;
		this.noOfAgents = noOfAgents ;
	}
	
	public Citizen () {
		
	}
	
	Logger log = Logger.getLogger(
    		Citizen.class.getName());
	
	@ScheduledMethod(start = 1, interval = 1, priority=ScheduleParameters.LAST_PRIORITY)
	public void step() throws IOException { 
		log.info("........................................................");
		Double tickcount = RepastEssentials.GetTickCount();
		currentTick = tickcount.intValue();
		System.out.println("currentTick " + currentTick);
		handShake(humanCount);
		FindingSalientEvent salientEvent = new FindingSalientEvent(this.prologPath, this.currentTick, this.humanCount, this.salientEvents);
		if (currentTick == (humanCount + 1)) {
			System.out.println("I'm adopting an action");
//			boolean result = salientEvent.resultOfSalient(this.prologPath, this.event, this.currentTick, this.humanCount, this.salientEvents);
			java.util.Map<String,Term>[] solutions = salientEvent.gettingCountAsEvents();
			for ( int i=0 ; i < solutions.length ; i++ ) { 
				  System.out.println( "X = " + solutions[i].get("A")); 
				}
			Random randy = new Random();
			int actionNo = randy.nextInt(solutions.length);
			System.out.println( "I'm choosing " + solutions[actionNo].get("A"));
			passAdoptersEvent(solutions[actionNo].get("A").toString());
		}else {
			System.out.println("I'm rejecting an action");
			event = getRandomAction();
			System.out.println("I'm choosing an action " + event);

		}
		log.info("........................................................");
		}
	
		
	public void passAdoptersEvent(String event) {
		Context context = ContextUtils.getContext(this);
		Iterable<Jury> i = context.getAgentLayer(Jury.class);
	      Iterator<Jury> it = i.iterator();
	      int index = 0; 
	      while (it.hasNext())
	      {
	    	  Jury j = it.next();  
	          j.setAdoptersEvent(event);
	          index++;
	      }      
	}
	
	public void handShake(int humanCount) {
		System.out.println("hello, I am a citizen " + (humanCount+1));
	}
	
	public void setRandomAction(String cascadingEvent, String [] salientEvents) {
		this.cascadingEvent =  cascadingEvent;
//		this.salientEvents = salientEvents;
	}
	
	public String getRandomAction() {
		return cascadingEvent;
	}
	
	public String[] getsalientAction() {
		return salientEvents ;
	}
	
	
	public void setPremitiveAction(Query [] primitiveActionsQueries) {
		this.primitiveActionsQueries = primitiveActionsQueries;
	}
	
	public Query []  getPremitiveAction () {
		return primitiveActionsQueries;
	}	
	}


