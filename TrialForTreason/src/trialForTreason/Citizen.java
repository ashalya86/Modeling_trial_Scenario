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
 * @author sriashalya
 *
 */
public class Citizen {

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
	String group;
	String day;

	public Citizen(String action, String prologPath, String[] salientEvents, int humanCount, int noCitizens,
			Query queryConsult, String lewisPrologPath, HashMap<String, String> perceptsRecieved) {
		this.action = action;
		this.prologPath = prologPath;
		this.salientEvents = salientEvents;
		this.humanCount = humanCount;
		this.salientEventAdopters = 0;
		this.noCitizens = noCitizens;
		this.queryConsult = queryConsult;
		this.lewisPrologPath = lewisPrologPath;
		this.perceptsRecieved = perceptsRecieved;
		
		Random randy = new Random();
		int groupNo = randy.nextInt(2);
		if (groupNo == 1) {
			group = "group1";
		}else {
			group = "group2";
		}	
	}

	Logger log = Logger.getLogger(Citizen.class.getName());

	@ScheduledMethod(start = 1, interval = 1, priority = ScheduleParameters.LAST_PRIORITY)
	public void step() throws IOException {
		log.info("........................................................");
		Double tickcount = RepastEssentials.GetTickCount();
		Random randy = new Random();
		int dayNo = randy.nextInt(2);
		if (dayNo == 1) {
			day = (String) RepastEssentials.GetParameter("day1");
		}else {
			day = (String) RepastEssentials.GetParameter("day2");
		}
		currentTick = tickcount.intValue();
		System.out.println("currentTick " + currentTick);
		System.out.println("################################### " + day);
		handShake(humanCount);
		if (currentTick <= noCitizens) {
			scene1(this.humanCount, this.prologPath, this.currentTick, this.salientEvents, this.queryConsult);
		} else if ((currentTick <= 2*noCitizens) && (currentTick > noCitizens)) {
			if (this.day == "festival") {
				scene2FestivalDay(this.humanCount);
			}else {
			scene2NormalDay(this.noCitizens, this.humanCount, currentTick);
			}
		}else {
			scene3(humanCount, currentTick, this.group);
		}
		log.info("........................................................");
	}

//	cascading
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
	public void scene2FestivalDay(int humanCount) {
			System.out.println("It is festival!!!!, I'm attending public forum");
			passPublicSquareAttenders(humanCount, "festival");
	}
	
	public void scene2NormalDay(int noCitizens, int humanCount, int currentTick) {
		Random randy = new Random();
		int randNo = randy.nextInt(noCitizens);
		if ((humanCount > randNo) && (day == "normal")){
			System.out.println("I'm attending public forum");
			passPublicSquareAttenders(humanCount, "normal");
		}
		else {
			System.out.println("I'm not attending public square");
		}
	}
	
	//citizens having ethoses
	public void scene3(int humanCount, int currentTick, String group) {
		Random randy = new Random();
		int randNo = randy.nextInt(noCitizens);
		if (humanCount > randNo) {
			ethos = "ethos1";
		}else if ((humanCount == randNo) ) {
			ethos = "no ethoses";
		}else {
			ethos = "ethos2";
		}
		passEthoses(this.group, ethos);
		System.out.println("I'm citizen " + humanCount + " belongs to "+ group + " having ethos "+ ethos);	
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
	
	public void passEthoses(String group, String ethos) {
		Context context = ContextUtils.getContext(this);
		Iterable<Jury> i = context.getAgentLayer(Jury.class);
		Iterator<Jury> it = i.iterator();
		int index = 0;
		while (it.hasNext()) {
			Jury j = it.next();
			j.setEthosHolders(group, ethos);
			index++;
		}
	}

	public void handShake(int humanCount) {
		System.out.println("hello, I am a citizen " + (humanCount + 1));
	}

	public void setRandomAction(String cascadingEvent, String[] salientEvents) {
		this.cascadingEvent = cascadingEvent;
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
