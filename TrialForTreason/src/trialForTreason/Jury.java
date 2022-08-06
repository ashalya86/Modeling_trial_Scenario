/**
 * 
 */
package trialForTreason;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.jpl7.Query;

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
	ArrayList<String> publicSquareAttenders = new ArrayList<String>(); 

	String prologPath;
	int humanCount;
	String[] salientEvents;
	int cascadingCount;
	int noCitizens  = 10;
	int jurors_count;
	Query queryConsult;
	
	ScheduleParameters params = ScheduleParameters.createRepeating(11, 1);

	
	public Jury(String prologPath, int humanCount, String[] salientEvents, int jurors_count, Query queryConsult) {
		this.prologPath = prologPath;
		this.humanCount = humanCount;
		this.salientEvents = salientEvents;	
		this.jurors_count = jurors_count; 
		this.queryConsult = queryConsult;
	}
	
	Logger log = Logger.getLogger(
    		Citizen.class.getName());


	@ScheduledMethod(start = 2, interval = 1)
	public void step() throws IOException {
			log.info("*************************************************************");
			Double tickcount = RepastEssentials.GetTickCount();
			int currentTick = tickcount.intValue();
			if (currentTick < (noCitizens + 1)) {
				observingSalientEvents(this.prologPath, this.adoptersEvent, currentTick, this.humanCount, this.salientEvents, this.jurors_count);
			} else{
				passingPublicSquare(currentTick);
			}
			publicSquareAttenders.clear();
	}
	
	public void observingSalientEvents(String prologPath, String adoptersEvent, int currentTick, int humanCount, String [] salientEvents, int jurors_count) throws IOException {
		System.out.println("I'm a jury member observing any salient events at tick " + (currentTick - 1));
		FindingSalientEvent salientEvent = new FindingSalientEvent(prologPath, currentTick, salientEvents, queryConsult);
		boolean resultSalient = salientEvent.resultOfSalient(prologPath, adoptersEvent, currentTick, humanCount+1, salientEvents, jurors_count);
		if (resultSalient == true) {
			System.out.println("There is a salient event at tick " + (currentTick - 1) + " which is " + this.adoptersEvent);
		}else {
			System.out.println("There is no salient event at tick " + (currentTick - 1));
		}
		if (currentTick == this.noCitizens) {
			System.out.println("I'm a jury member observing cascade of " +  this.cascadingCount + " agents at tick " + currentTick);
			System.out.println("I'm a jury member observing cascade of " + adoptersEvents );
		}

	}
	
//	@ScheduledMethod(start = 11, interval = 1)
	public void passingPublicSquare(int currentTick) {
		Random randy = new Random();
		if (randy.nextInt(2) == 1) {
			System.out.println("*************************************************************");
			System.out.println("I'm a jury member passing the public Square at tick " + (currentTick - 1));
			System.out.println("I'm observing" + publicSquareAttenders);
		}
	}
		
	public void makedecision(float propability_of_conviction, float propability_of_non_conviction) {
			if ((propability_of_conviction - propability_of_non_conviction) > 0.5) {
				System.out.println("Convict Leocrates");
			}else {
				System.out.println("Do not convict Leocrates");	
				}
			}
	
	public void setAdoptersEvent(String event, int humanCount) {
		this.adoptersEvent =  event;
		this.adoptersEvents.add(event);
		this.cascadingCount ++;
		this.humanCount = humanCount;
	}

	public void setAttendersPublicSquare(int humanCount) {
		this.publicSquareAttenders.add("Citizen"+ (humanCount + 1));
	}
	
}
