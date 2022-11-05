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
 * @author nick
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
	}

	Logger log = Logger.getLogger(Citizen.class.getName());

	@ScheduledMethod(start = 1, interval = 1, priority = ScheduleParameters.LAST_PRIORITY)
	public void step() throws IOException {
		log.info("........................................................");
		Double tickcount = RepastEssentials.GetTickCount();
		currentTick = tickcount.intValue();
		System.out.println("currentTick " + currentTick);
		handShake(humanCount);
		if (currentTick <= noCitizens) {
			scene1(this.humanCount, this.prologPath, this.currentTick, this.salientEvents, this.queryConsult);
		} else {
			scene2(this.noCitizens, this.humanCount);
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

//	attendingPublicSquare and getting CK of public monuments
	public void scene2(int noCitizens, int humanCount) {
		Random randy = new Random();
		int randNo = randy.nextInt(noCitizens);
		CKofMonument ck = new CKofMonument(lewisPrologPath,  currentTick, perceptsRecieved, queryConsult);
		if (humanCount > randNo) {
			System.out.println("I'm attending public square");
			passPublicSquareAttenders(humanCount);
			java.util.Map<String, Term>[] solutions = ck.gettingCK();
			for (int i = 0; i < solutions.length; i++) {
				System.out.println("ck = " + solutions[i].get("X"));
			}

		} else {
			System.out.println("I'm not attending public square");
		}
	}

	public void passPublicSquareAttenders(int humanCount) {
		Context context = ContextUtils.getContext(this);
		Iterable<Jury> i = context.getAgentLayer(Jury.class);
		Iterator<Jury> it = i.iterator();
		int index = 0;
		while (it.hasNext()) {
			Jury j = it.next();
			j.setAttendersPublicSquare(humanCount);
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
