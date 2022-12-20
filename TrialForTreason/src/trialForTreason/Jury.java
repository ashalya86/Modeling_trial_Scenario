/**
 * 
 */
package trialForTreason;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import org.jpl7.Query;
import org.jpl7.Term;

import bsh.This;
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
	ArrayList<String> citizens = new ArrayList<String>();
	ArrayList<Double> proportionsEthos1 = new ArrayList<Double>();
	ArrayList<Double> proportionsEthos2 = new ArrayList<Double>();

	String salientPrologPath;
	String lewisPrologPath;
	int humanCount;
	String[] salientEvents;
	int cascadingCount;
	int jurorsCount;
	Query queryConsult;
	HashMap<String, String> perceptsRecieved;
	String day;
	int noOfCitizens;
	private Grid<Object> grid;
	private ContinuousSpace<Object> space;
	int randomJurorsExactPostioin;
	HashMap<Integer, ArrayList<Integer>> edgeDetail;
	HashMap<String, String> citizensEthoses;
	double proportionEthos1;
	double proportionEthos2;
	int noOfJurors;

	ScheduleParameters params = ScheduleParameters.createRepeating(11, 1);

	public Jury(String salientPrologPath, int humanCount, String[] salientEvents, int jurorsCount, Query queryConsult,
			String lewisPrologPath, HashMap<String, String> perceptsRecieved, int noOfCitizens, Grid<Object> grid,
			ContinuousSpace<Object> space, int randomJurorsExactPostioin,
			HashMap<Integer, ArrayList<Integer>> edgeDetail, HashMap<String, String> citizensEthoses, int noOfJurors) {
		this.salientPrologPath = salientPrologPath;
		this.humanCount = humanCount;
		this.salientEvents = salientEvents;
		this.jurorsCount = jurorsCount;
		this.queryConsult = queryConsult;
		this.lewisPrologPath = lewisPrologPath;
		this.perceptsRecieved = perceptsRecieved;
		this.noOfCitizens = noOfCitizens;
		this.grid = grid;
		this.space = space;
		this.randomJurorsExactPostioin = randomJurorsExactPostioin;
		this.edgeDetail = edgeDetail;
		this.citizensEthoses = citizensEthoses;
		this.noOfJurors = noOfJurors;
	}

	public Jury(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;
		this.grid = grid;
	}

	Logger log = Logger.getLogger(Citizen.class.getName());

	@ScheduledMethod(start = 2, interval = 1)
	public void step() throws IOException {
		log.info("*************************************************************");
		Double tickcount = RepastEssentials.GetTickCount();
		int currentTick = tickcount.intValue();
		handShake(jurorsCount);
		// scene1
		if (currentTick < (noOfCitizens + 1)) {
			observingSalientEvents(this.salientPrologPath, this.adoptersEvent, currentTick, this.humanCount,
					this.salientEvents, this.jurorsCount);
			// scene2
		} else if ((currentTick <= 2 * noOfCitizens) && (currentTick > noOfCitizens)) {
			passingPublicSquare(currentTick, this.day, this.noOfCitizens, jurorsCount);
			// scene3
		} else {
			gettingProportionOfEthoses(this.randomJurorsExactPostioin, this.edgeDetail, this.citizensEthoses);
				//System.out.println(proportionsEthos1);
				//System.out.println(proportionsEthos2);
			    Double averageEthos1 = proportionsEthos1.stream().mapToDouble(val -> val).average().orElse(0.0);
			    Double averageEthos2 = proportionsEthos2.stream().mapToDouble(val -> val).average().orElse(0.0);
				System.out.println("common knowledge of ethos1 among jurors group (averageEthos1) " + averageEthos1);
				System.out.println("common knowledge of ethos1 among jurors group (averageEthos2) " + averageEthos2);


		}
		publicSquareAttenders.clear();
		proportionsEthos1.clear();
		proportionsEthos2.clear();
	}

	public void gettingProportionOfEthoses(int randomJurorsExactPostioin,
			HashMap<Integer, ArrayList<Integer>> edgeDetail, HashMap<String, String> citizensEthoses) {
		List connectedCitizenEthos = new ArrayList(); 
		List CitizenEthoses  = new ArrayList();
		float countEthos1 = 0;
		float countEthos2 = 0;
		float sizeOfConnectedGroup = 0;
		//getting all citizens ethoses in to list
		for (Map.Entry<String, String> ethos : citizensEthoses.entrySet()) {
			int key1 = Integer.parseInt(ethos.getKey());
			String values1 = ethos.getValue();
				CitizenEthoses.add(values1);
		}	
		//Getting ethoses for connected nodes
		for (Map.Entry<Integer, ArrayList<Integer>> entry : edgeDetail.entrySet()) {
			Integer key = entry.getKey();
			ArrayList<Integer> values = entry.getValue();
				if (key == randomJurorsExactPostioin) {
					for (int k = 0; k < values.size(); k++) {
						connectedCitizenEthos.add(CitizenEthoses.get(values.get(k)));
						if(CitizenEthoses.get(values.get(k)) == "Ethos1") {
							countEthos1 += 1;
						}else if(CitizenEthoses.get(values.get(k)) == "Ethos2") {
							countEthos2 += 1;
						}
						sizeOfConnectedGroup = values.size();
					}
				}
		}
		System.out.println(connectedCitizenEthos.toString()+ countEthos1+ ".."+ countEthos2+ ".."+ sizeOfConnectedGroup);
		proportionEthos1 = countEthos1/sizeOfConnectedGroup;
		proportionEthos2 = countEthos2/sizeOfConnectedGroup;
		passProportions(proportionEthos1, proportionEthos2);
		System.out.println("Proportion of Ethos1 in my group" + proportionEthos1);
		System.out.println("Proportion of Ethos2 in my group" + proportionEthos2);
		connectedCitizenEthos.clear();
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
			System.out.println("I'm a jury member " + (jurors_count + 1) + " observing cascade of "
					+ this.cascadingCount + " agents at tick " + currentTick);
			System.out.println("I'm a jury member observing cascade of " + adoptersEvents);
		}
	}

	public void passingPublicSquare(int currentTick, String day, int noOfCitizens, int jurorsCount) {
		Random randy = new Random();
		if (day == "festival") {
			System.out.println("*************************************************************");
			System.out.println(
					"I'm a jury member" + (jurorsCount + 1) + "passing the public Square at tick " + (currentTick - 1));
			System.out.println("I'm observing a festival");
			if (publicSquareAttenders.size() > (noOfCitizens * 0.85)) {
//				System.out.println(".......... " + publicSquareAttenders.size() + noOfCitizens);
				System.out
						.println("I perceive certain threshhold " + ((publicSquareAttenders.size() - this.noOfJurors) * 100) / noOfCitizens
								+ "% of citizens in the festival" + publicSquareAttenders);
				CKofMonument ck = new CKofMonument(this.lewisPrologPath, currentTick, this.perceptsRecieved,
						queryConsult);
				java.util.Map<String, Term>[] solutions = ck.gettingCK();
				for (int i = 0; i < solutions.length; i++) {
					System.out.println(
							"Jury realises " + solutions[i].get("X") + "is common knowledege among the citizens");
				}
			} else {
				System.out.println("I don't perceive certain threshhold of citizens in the festival. It is only "
						+ ((publicSquareAttenders.size() - this.noOfJurors) * 100) / noOfCitizens + "% " + publicSquareAttenders);
			}
		}
		if (day == "normal") {
			System.out.println("*************************************************************");
			if (randy.nextInt(3) < (jurorsCount + 1)) {
				System.out.println("I'm a jury member " + (jurorsCount + 1) + " passing the public Square at tick "
						+ (currentTick - 1));
				System.out.println("I'm observing" + publicSquareAttenders);
				if (publicSquareAttenders.size() > 0) {
					CKofMonument ck = new CKofMonument(this.lewisPrologPath, currentTick, this.perceptsRecieved,
							queryConsult);
					java.util.Map<String, Term>[] solutions = ck.gettingCK();
					for (int i = 0; i < solutions.length; i++) {
						System.out.println("Jury " + (jurorsCount + 1) + " realises " + solutions[i].get("X")
								+ "is common knowledege among these citizens");
					}
				}
			} else {
				System.out.println("I'm not attending the sqaure today ");
			}
		}
	}

	public void passProportions(double proportionEthos1, double proportionEthos2) {
		Context context = ContextUtils.getContext(this);
		Iterable<Jury> i = context.getAgentLayer(Jury.class);
		Iterator<Jury> it = i.iterator();
		int index = 0;
		while (it.hasNext()) {
			Jury j = it.next();
			j.setProportions(proportionEthos1, proportionEthos2);
			index++;
		}
	}
	
	public void setProportions(double proportionEthos1, double proportionEthos2) {
		this.proportionsEthos1.add(proportionEthos1);
		this.proportionsEthos2.add(proportionEthos2);
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

	public void setCitizens(String citizen) {
		this.citizens.add(citizen);
	}

	public void handShake(int juryCount) {
		System.out.println("hello, I am a jury member " + (juryCount + 1));
	}
}
