/**
 * 
 */
package trialForTreason;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.SimUtilities;

/**
 * @author nick
 * 
 */
public class Jury {

	float propability_of_conviction;
	float propability_of_non_conviction;
	String adoptersEvent;
	ArrayList<String> adoptersEvents = new ArrayList<String>(); 
	
	public Jury() {
		
	}

	public Jury(float propability_of_conviction, float propability_of_non_conviction) {
		this.propability_of_conviction = propability_of_conviction;
		this.propability_of_non_conviction = propability_of_non_conviction;
	}
	
	Logger log = Logger.getLogger(
    		Citizen.class.getName());


	@ScheduledMethod(start = 2, interval = 1)
	public void step() {
			log.info("*************************************************************");
			Double tickcount = RepastEssentials.GetTickCount();
			int currentTick = tickcount.intValue();
			System.out.println("I'm a jury member observing adopters at tick " + (currentTick - 1) + getAdoptersEvent() );			
			adoptersEvents.clear();
//			makedecision(this.propability_of_conviction, this.propability_of_non_conviction);
//			log.info("*************************************************************");
	}
		
	public ArrayList<String> getAdoptersEvent () {
		return adoptersEvents;
	}
	
	public void makedecision(float propability_of_conviction, float propability_of_non_conviction) {
			if ((propability_of_conviction - propability_of_non_conviction) > 0.5) {
				System.out.println("Convict Leocrates");
			}else {
				System.out.println("Do not convict Leocrates");	
				}
			}
	public void setAdoptersEvent(String event) {
		this.adoptersEvent =  event;
		this.adoptersEvents.add(event);
	}
	
}
