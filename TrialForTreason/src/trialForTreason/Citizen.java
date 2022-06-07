/**
 * 
 */
package trialForTreason;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	int salientEventRejectors;
	String cascadingEvent;


	public Citizen(String action, String prologPath, String[] events, int humanCount) {
		this.action = action;
		this.prologPath = prologPath;
		this.events = events;
		this.humanCount = humanCount;
//		salientEventRejectors = 0;
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
//		event = getCascadeAction().get(humanCount);
		event = getCascadeAction();
		salientEvents = getsalientAction();
		System.out.println("I'm informed by a general to follow  " + event);
		FindingSalientEvent salientEvent = new FindingSalientEvent(this.prologPath, this.event, this.currentTick, this.humanCount);
		boolean result = salientEvent.resultOfSalient(this.prologPath, this.event, this.currentTick, this.humanCount, this.salientEvents);
		if (result == true && action == "adopt") {
			System.out.println(event + " event is salient and I'm adopting" );
		}
		else if (result == true && action == "reject") {
			System.out.println(event + " event is salient and I'm rejecting" );
			salientEventRejectors += 1;
			countDownCascade(salientEventRejectors);
		}
		else if (result == false && action == "adopt") {
			System.out.println(event + " event is not salient and I'm adopting");	
		}
		else {
			System.out.println(event + " event is not salient and I'm rejecting");
		}
		System.out.println("salientEventRejectors "+ salientEventRejectors);
		//....
		log.info("........................................................");
		}
	
		
	public void handShake(int humanCount) {
		System.out.println("hello, I am a citizen " + humanCount);
	}
	
	public void setCascadeAction(String cascadingEvent, String [] salientEvents) {
		this.cascadingEvent =  cascadingEvent;
		this.salientEvents = salientEvents;
	}
	
	public String getCascadeAction() {
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
	
	public Boolean seeing_monuments(double signal_accuracy, Map<Double, Double> up_cascade_list) {
		return (up_cascade_list.get(signal_accuracy) > 0.6) ;	
	}
	
	
	public void countDownCascade(int salientEventRejectors) {
		Jury j = new Jury ();
		j.setCountDownCascade(salientEventRejectors);
	}
	
		
	}


