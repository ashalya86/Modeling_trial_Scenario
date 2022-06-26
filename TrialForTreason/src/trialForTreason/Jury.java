/**
 * 
 */
package trialForTreason;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
	ArrayList<String> publicSquareAttenders = new ArrayList<String>(); 

	String prologPath;
	int humanCount;
	String[] salientEvents;
	int cascadingCount;
	int noCitizens  = 10;
	
	public Jury(String prologPath, int humanCount, String[] salientEvents) {
		this.prologPath = prologPath;
		this.humanCount = humanCount;
		this.salientEvents = salientEvents;	
	}
	
	Logger log = Logger.getLogger(
    		Citizen.class.getName());


	@ScheduledMethod(start = 2, interval = 1)
	public void step() throws IOException {
			log.info("*************************************************************");
			Double tickcount = RepastEssentials.GetTickCount();
			int currentTick = tickcount.intValue();
			if (currentTick <= humanCount+1) {
				observingSalientEvents(this.prologPath, this.adoptersEvent, currentTick, humanCount, salientEvents);
			} else if  (currentTick > noCitizens) {
				passingPublicSquare(currentTick);
			}
			publicSquareAttenders.clear();
//			adoptersEvents.clear();
//			makedecision(this.propability_of_conviction, this.propability_of_non_conviction);
//			log.info("*************************************************************");
	}
	
	public void observingSalientEvents(String prologPath, String adoptersEvent, int currentTick, int humanCount, String [] salientEvents) throws IOException {
		System.out.println("I'm a jury member observing any salient events at tick " + (currentTick - 1));
		FindingSalientEvent salientEvent = new FindingSalientEvent(prologPath, currentTick, humanCount, salientEvents);
		boolean resultSalient = salientEvent.resultOfSalient(prologPath, adoptersEvent, currentTick, humanCount+1, salientEvents);
		if (resultSalient == true) {
			System.out.println("There is a salient event at tick " + (currentTick - 1) + " which is " + this.adoptersEvent);
		}else {
			System.out.println("There is no salient event at tick " + (currentTick - 1));
		}
		if (currentTick == humanCount+1) {
			System.out.println("I'm a jury member observing cascade of " +  this.cascadingCount + " agents at tick " + (currentTick - 1));
			System.out.println("I'm a jury member observing cascade of " + adoptersEvents );
		}

	}
	
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
	
	public void setAdoptersEvent(String event) {
		this.adoptersEvent =  event;
		this.adoptersEvents.add(event);
		this.cascadingCount ++;
	}

	public void setAttendersPublicSquare(int humanCount) {
		this.publicSquareAttenders.add("Citizen"+ (humanCount + 1));
	}
	
}
