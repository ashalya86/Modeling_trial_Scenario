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
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import java.util.logging.Logger;
import java.util.Iterator;

/**
 * @author sriashalya
 *
 */
public class Citizen {
	
	private Grid<Object> grid;
	private ContinuousSpace<Object> space;
	String event;
	String[] events;
	String prologPath;
	String lewisPrologPath;
	int currentTick;
	int cascadeIndex;
	String action;
	int humanCount;
	ArrayList<String> cascading_events;
	Query[] primitiveActionsQueries;
	String[] salientEvents;
	int salientEventAdopters;
	String cascadingEvent;
	int noCitizens;
	Query queryConsult;
	HashMap<String, String> perceptsRecieved;
	String ethos;
	String day;
	Random randy;
	String name;
	Citizen citizen;
	private static List<Citizen> citizens= new ArrayList<Citizen>();
	HashMap<Integer, ArrayList<Integer>> edgeDetail;
	String graphPath = "./data/12Agents.graph";
	HashMap<String, String> citizensEthoses;
	int threshold = -1;
	int threshold2 = 2;
	
	public Citizen(String action, String prologPath, String[] salientEvents, int humanCount, int noCitizens,
			Query queryConsult, String lewisPrologPath, HashMap<String, String> perceptsRecieved,
		 Grid<Object> grid, ContinuousSpace<Object> space) {
		this.action = action;
		this.prologPath = prologPath;
		this.salientEvents = salientEvents;
		this.humanCount = humanCount;
		this.salientEventAdopters = 0;
		this.noCitizens = noCitizens;
		this.queryConsult = queryConsult;
		this.lewisPrologPath = lewisPrologPath;
		this.perceptsRecieved = perceptsRecieved;
		this.grid = grid;
		this.space = space;
		this.randy = new Random();	
	}
	
	public Citizen(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;
		this.grid = grid;
	}
	
	Logger log = Logger.getLogger(Citizen.class.getName());

	@ScheduledMethod(start = 1, interval = 1, priority = ScheduleParameters.LAST_PRIORITY)
	public void step() throws IOException {
		log.info("........................................................");
		//Random randy = new Random();
		Double tickcount = RepastEssentials.GetTickCount();
		currentTick = tickcount.intValue();
		System.out.println("currentTick " + currentTick);
		handShake(humanCount);
		if (currentTick <= noCitizens) {
			scene1(this.humanCount, this.prologPath, this.currentTick, this.salientEvents, this.queryConsult);
		} else if ((currentTick <= 2 * noCitizens) && (currentTick > noCitizens)) {
			if (currentTick % 3 == 0) {
				scene2FestivalDay(this.humanCount, this.noCitizens);
			} else {
				scene2NormalDay(this.noCitizens, this.humanCount, currentTick);
			}
		} else {
			propSocialNetwork prop = new propSocialNetwork(this.noCitizens, this.graphPath);
			this.edgeDetail = prop.setEdges(this.graphPath, this.noCitizens);
			//createNetwork(this.citizens, this.edgeDetail);
		}
		log.info("........................................................");
	}
	
	private void handShake(int humanCount) {
		System.out.println("hello, I am a citizen " + (humanCount + 1));
		this.name = "citizen " + (humanCount + 1);
	}

	public void addCitizens(Citizen citizen) {
		this.citizens.add(citizen);
	}
		
	public void createNetwork(List<Citizen> citizens, HashMap<Integer, ArrayList<Integer>> edgeDetail) {
		Context<Object> context = ContextUtils.getContext(this);
		context.remove(this);
		
		Network<Object> colNet = (Network<Object>)context.getProjection("social network");	
		for(Map.Entry<Integer,ArrayList<Integer>> entry: edgeDetail.entrySet())
      {
          Integer key=entry.getKey();
          ArrayList<Integer> values=entry.getValue();
  			context.add(citizens.get(key));
          for (int i = 0; i < values.size(); i++) {
      		colNet.addEdge(citizens.get(key), citizens.get(i));
          }
      }
		}
	
	// cascading
	public void scene1(int humanCount, String prologPath, int currentTick, String[] salientEvents, Query queryConsult) {
		FindingSalientEvent salientEvent = new FindingSalientEvent(prologPath, currentTick, salientEvents,
				queryConsult);
		if ((currentTick == (humanCount + 1)) && (action == "A")) {
			System.out.println("I'm adopting an action");
			java.util.Map<String, Term>[] solutions = salientEvent.gettingCountAsEvents();
			for (int i = 0; i < solutions.length; i++) {
				System.out.println("X = " + solutions[i].get("A"));
			}
			Random randy = new Random();
			int actionNo = randy.nextInt(solutions.length);
			System.out.println("I'm choosing " + solutions[actionNo].get("A"));
			passAdoptersEvent(solutions[actionNo].get("A").toString(), humanCount);
		} else if ((currentTick == (humanCount + 1)) && (action == "R")) {
			System.out.println("I'm rejecting an action");
			event = getRandomAction();
			passAdoptersEvent(event, humanCount);
			System.out.println("So, I'm choosing an action " + event);
		}
	}

//	attendingPublicSquare 
	public void scene2FestivalDay(int humanCount, int noOfCitizens) {
		int randNo = this.randy.nextInt(noOfCitizens);
		if (randNo < (noOfCitizens * 0.95)) {
			System.out.println("It is festival!!!!, I'm attending public forum");
			passPublicSquareAttenders(humanCount, "festival");
		}
	}

	public void scene2NormalDay(int noCitizens, int humanCount, int currentTick) {
		Random randy = new Random();
		int randNo = randy.nextInt(noCitizens);
		if ((humanCount > randNo)) {
			System.out.println("I'm attending public forum");
			passPublicSquareAttenders(humanCount, "normal");
		} else {
			System.out.println("I'm not attending public forum");
		}
	}

	public void passPublicSquareAttenders(int humanCount, String day) {
		Context context = ContextUtils.getContext(this);
		Iterable<Jury> i = context.getAgentLayer(Jury.class);
		Iterator<Jury> it = i.iterator();
		int index = 0;
		while (it.hasNext()) {
			Jury j = it.next();
			j.setAttendersPublicSquare(humanCount, day);
			index++;
		}
	}

	public void passAdoptersEvent(String event, int humanCount) {
		Context context = ContextUtils.getContext(this);
		Iterable<Jury> i = context.getAgentLayer(Jury.class);
		Iterator<Jury> it = i.iterator();
		int index = 0;
		while (it.hasNext()) {
			Jury j = it.next();
			j.setAdoptersEvent(event, humanCount);
			index++;
		}
	}

	public void passCitizens(String citizen) {
		Context context = ContextUtils.getContext(this);
		Iterable<Jury> i = context.getAgentLayer(Jury.class);
		Iterator<Jury> it = i.iterator();
		int index = 0;
		while (it.hasNext()) {
			Jury j = it.next();
			j.setCitizens(citizen);;
			index++;
		}
	}

	public void setRandomAction(String cascadingEvent, String[] salientEvents) {
		this.cascadingEvent = cascadingEvent;
	}

	public String getName() {
		return name;
	}

	public String getRandomAction() {
		return cascadingEvent;
	}

	public String[] getsalientAction() {
		return salientEvents;
	}

	public void setPremitiveAction(Query[] primitiveActionsQueries) {
		this.primitiveActionsQueries = primitiveActionsQueries;
	}

	public Query[] getPremitiveAction() {
		return primitiveActionsQueries;
	}
}
