/**
 * 
 */
package trialForTreason;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.jpl7.Query;
import org.jpl7.Term;

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
	String adoptersEvent;
	ArrayList<String> adoptersEvents = new ArrayList<String>();
	ArrayList<String> publicSquareAttenders = new ArrayList<String>();

	String salientPrologPath;
	String lewisPrologPath;
	int humanCount;
	String[] salientEvents;
	int cascadingCount;
	int noCitizens = 10;
	int jurors_count;
	Query queryConsult;
	HashMap<String, String> perceptsRecieved;

	ScheduleParameters params = ScheduleParameters.createRepeating(11, 1);

	public Jury(String salientPrologPath, int humanCount, String[] salientEvents, int jurors_count, Query queryConsult,
			String lewisPrologPath, HashMap<String, String> perceptsRecieved) {
		this.salientPrologPath = salientPrologPath;
		this.humanCount = humanCount;
		this.salientEvents = salientEvents;
		this.jurors_count = jurors_count;
		this.queryConsult = queryConsult;
		this.lewisPrologPath = lewisPrologPath;
		this.perceptsRecieved = perceptsRecieved;
	}

	Logger log = Logger.getLogger(Citizen.class.getName());

	@ScheduledMethod(start = 2, interval = 1)
	public void step() throws IOException {
		log.info("*************************************************************");
		Double tickcount = RepastEssentials.GetTickCount();
		int currentTick = tickcount.intValue();
		if (currentTick < (noCitizens + 1)) {
			observingSalientEvents(this.salientPrologPath, this.adoptersEvent, currentTick, this.humanCount,
					this.salientEvents, this.jurors_count);
		} else {
			passingPublicSquare(currentTick);
		}
		publicSquareAttenders.clear();
	}

	public void observingSalientEvents(String salientPrologPath, String adoptersEvent, int currentTick, int humanCount,
			String[] salientEvents, int jurors_count) throws IOException {
		System.out.println("I'm a jury member observing any salient events at tick " + (currentTick - 1));
		FindingSalientEvent salientEvent = new FindingSalientEvent(salientPrologPath, currentTick, salientEvents,
				queryConsult);
		boolean resultSalient = salientEvent.resultOfSalient(salientPrologPath, adoptersEvent, currentTick,
				humanCount + 1, salientEvents, jurors_count);
		if (resultSalient == true) {
			System.out.println(
					"There is a salient event at tick " + (currentTick - 1) + " which is " + this.adoptersEvent);
		} else {
			System.out.println("There is no salient event at tick " + (currentTick - 1));
		}
		if (currentTick == this.noCitizens) {
			System.out.println(
					"I'm a jury member observing cascade of " + this.cascadingCount + " agents at tick " + currentTick);
			System.out.println("I'm a jury member observing cascade of " + adoptersEvents);
		}
	}

//	@ScheduledMethod(start = 11, interval = 1)
	public void passingPublicSquare(int currentTick) {
		Random randy = new Random();
		if (randy.nextInt(2) == 1) {
			System.out.println("*************************************************************");
			System.out.println("I'm a jury member passing the public Square at tick " + (currentTick - 1));
			System.out.println("I'm observing" + publicSquareAttenders);
			if (publicSquareAttenders.size() > 0) {
				CKofMonument ck = new CKofMonument(this.lewisPrologPath, currentTick, this.perceptsRecieved,
						queryConsult);
				System.out.println("I'm attending public square");
				java.util.Map<String, Term>[] solutions = ck.gettingCK();
				for (int i = 0; i < solutions.length; i++) {
					System.out.println(
							"Jury realises " + solutions[i].get("X") + "is common knowledege among the citizens");
				}
			}
		}
	}

	public void setAdoptersEvent(String event, int humanCount) {
		this.adoptersEvent = event;
		this.adoptersEvents.add(event);
		this.cascadingCount++;
		this.humanCount = humanCount;
	}

	public void setAttendersPublicSquare(int humanCount) {
		this.publicSquareAttenders.add("Citizen" + (humanCount + 1));
	}
}
