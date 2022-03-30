/**
 * 
 */
package trialForTreason;


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
	
	Map<Double, Double> up_cascade_list;
	double signal_accuracy;
	String event;
	String[] events;
	String prologPath;
	int currentTick;
	int cascadeIndex;
	String action;

	public Citizen(Map<Double, Double> up_cascade_list, double signal_accuracy, String prologPath, String[] events) {
		this.up_cascade_list = up_cascade_list;
		this.signal_accuracy = signal_accuracy;
		this.prologPath = prologPath;
		this.events = events;
	}
	
	Logger log = Logger.getLogger(
    		Citizen.class.getName());
	
	@ScheduledMethod(start = 2, interval = 1, priority=ScheduleParameters.LAST_PRIORITY)
	public void step() { 
		Double tickcount = RepastEssentials.GetTickCount();
		currentTick = tickcount.intValue();
		System.out.println("currentTick " + currentTick);
		System.out.println("Now I'm entering in to public space");
		log.info("........................................................");
		handShake();
//		entering_public_space(this.signal_accuracy, this.up_cascade_list, currentTick);	
		System.out.println("cascading action " + getCascadeAction());
		}
	
	public void entering_public_space(double signal_accuracy, Map<Double, Double> up_cascade_list, int currentTick) {
		System.out.println("Now I'm entering in to public space");
		int eventNumber = RandomHelper.nextIntFromTo(4, 10);
		if (eventNumber == 5) {
			event = "birds_flying";
			System.out.println("I'm seeing birds_flying");
		}else if(eventNumber == 6) {
			event = "attending_rituals";
			System.out.println("I'm  attending rituals");
		}else if(eventNumber == 7) {
			event = "seeing_monument";
			System.out.println("I'm seeing traitor monuments");
		}
		else {
			event = "people_walking";
			System.out.println("I'm seeing people are walking around me");
		}
		
//    	System.out.println( "consult " + q1.hasSolution() );
		SalientEventsProlog salientEvent = new SalientEventsProlog(this.events, this.prologPath, event, currentTick);
		String result = salientEvent.resultOfSalient(this.prologPath, event, currentTick);
		System.out.println( "event " + event + " is " + result);

  			if (result == "salient" && event == "seeing_monument" && seeing_monuments(signal_accuracy, up_cascade_list)) {
  				
  		    	System.out.println( "************I'll secure city*********" );  				
  			}
	}
	
	public void handShake() {
		System.out.println("hello, I am a citizen of Athens");
	}
	
	
	public void setCascadeAction(int cascadeIndex, String action) {
		this.cascadeIndex = cascadeIndex;
		this.action = action;
		System.out.println("cascade event "+ action);
	}
	
	public String getCascadeAction() {
		return action;
	}
	
	public Boolean seeing_monuments(double signal_accuracy, Map<Double, Double> up_cascade_list) {
		return (up_cascade_list.get(signal_accuracy) > 0.6) ;	
	}
		
	}


