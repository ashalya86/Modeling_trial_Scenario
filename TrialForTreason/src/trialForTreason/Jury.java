/**
 * 
 */
package trialForTreason;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
	ArrayList<String> group1 = new ArrayList<String>();
	ArrayList<String> group2 = new ArrayList<String>();

	String salientPrologPath;
	String lewisPrologPath;
	int humanCount;
	String[] salientEvents;
	int cascadingCount;
	int jurors_count;
	Query queryConsult;
	HashMap<String, String> perceptsRecieved;
	String day;
	int noOfCitizens;

	ScheduleParameters params = ScheduleParameters.createRepeating(11, 1);

	public Jury(String salientPrologPath, int humanCount, String[] salientEvents, int jurors_count, Query queryConsult,
			String lewisPrologPath, HashMap<String, String> perceptsRecieved, int noOfCitizens) {
		this.salientPrologPath = salientPrologPath;
		this.humanCount = humanCount;
		this.salientEvents = salientEvents;
		this.jurors_count = jurors_count;
		this.queryConsult = queryConsult;
		this.lewisPrologPath = lewisPrologPath;
		this.perceptsRecieved = perceptsRecieved;
		this.noOfCitizens = noOfCitizens;
	}

	Logger log = Logger.getLogger(Citizen.class.getName());

	@ScheduledMethod(start = 2, interval = 1)
	public void step() throws IOException {
		log.info("*************************************************************");
		Double tickcount = RepastEssentials.GetTickCount();
		int currentTick = tickcount.intValue();
		//System.out.println("%%%%%%%%%%%%%%%%%%%%%%%" + RepastEssentials.GetAdjacent("social network", humanCount));
		//scene1
		if (currentTick < (noOfCitizens + 1)) {
			observingSalientEvents(this.salientPrologPath, this.adoptersEvent, currentTick, this.humanCount,
					this.salientEvents, this.jurors_count);
		//scene2
		} else if ((currentTick <= 2*noOfCitizens) && (currentTick > noOfCitizens)) {
			passingPublicSquare(currentTick, this.day);
		//scene3
		}else {
			gettingAGroup(this.group1, this.group2);
		}
		publicSquareAttenders.clear();
		group1.clear();
		group2.clear();
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
		if (currentTick == this.noOfCitizens) {
			System.out.println(
					"I'm a jury member observing cascade of " + this.cascadingCount + " agents at tick " + currentTick);
			System.out.println("I'm a jury member observing cascade of " + adoptersEvents);
		}
	}

//	@ScheduledMethod(start = 11, interval = 1) in scene 2
	public void passingPublicSquare(int currentTick, String day) {
		Random randy = new Random();
		if (day == "festival") {
			System.out.println("*************************************************************");
			System.out.println("I'm a jury member passing the public Square at tick " + (currentTick - 1));
			System.out.println("I'm observing a festival and everyone attends" + publicSquareAttenders);
			CKofMonument ck = new CKofMonument(this.lewisPrologPath, currentTick, this.perceptsRecieved,
					queryConsult);
			java.util.Map<String, Term>[] solutions = ck.gettingCK();
			for (int i = 0; i < solutions.length; i++) {
				System.out.println(
						"Jury realises " + solutions[i].get("X") + "is common knowledege among the citizens");
			}	
		}
		if (randy.nextInt(2) == 1 && day == "normal") {
			System.out.println("*************************************************************");
			System.out.println("I'm a jury member passing the public Square at tick " + (currentTick - 1));
			System.out.println("I'm observing" + publicSquareAttenders);
			if (publicSquareAttenders.size() > 0) {
				CKofMonument ck = new CKofMonument(this.lewisPrologPath, currentTick, this.perceptsRecieved,
						queryConsult);
				java.util.Map<String, Term>[] solutions = ck.gettingCK();
				for (int i = 0; i < solutions.length; i++) {
					System.out.println(
							"Jury realises " + solutions[i].get("X") + "is common knowledege among the citizens");
				}
			}
		}}
	
	public void gettingAGroup(ArrayList<String> group1, ArrayList<String> group2) {
		int countEthos1;
		int countEthos2;
		ArrayList<String> connectedGr;
		System.out.println("group1 has " + group1);
		System.out.println("group2 has " + group2);
		Random randy = new Random();
		int randNo = randy.nextInt(2);
		if (randNo == 1) {
			System.out.println("Jury connected with group1 " + group1);
			connectedGr = group1;
			countEthos1 = Collections.frequency(group1, "ethos1");
			countEthos2 = Collections.frequency(group1, "ethos2");
 		}else {
 			connectedGr = group2;
			System.out.println("Jury connected with group2 " + group2);
 			countEthos1 = Collections.frequency(group2, "ethos1");
 			countEthos2 = Collections.frequency(group2, "ethos2"); 			
 		}
		if (countEthos1 > countEthos2 && countEthos1 > connectedGr.size()/2) {
			System.out.println("Jury member realises his group connected with ethos1");
		}else if (countEthos2 > countEthos1 && countEthos2 > connectedGr.size()/2) {
			System.out.println("Jury member realises his group connected with ethos2 ");	
		}
	}

	public void setAdoptersEvent(String event, int humanCount) {
		this.adoptersEvent = event;
		this.adoptersEvents.add(event);
		this.cascadingCount++;
		this.humanCount = humanCount;
	}

	public void setAttendersPublicSquare(int humanCount, String day) {
		this.publicSquareAttenders.add("Citizen" + (humanCount + 1));
		this.day = day;
	}
	
	public void setEthosHolders(String group, String ethos) {
		if (group == "group1") {
			this.group1.add(ethos);
		}else {
			this.group2.add(ethos);
		}		
	}
}
