package trialForTreason;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.jpl7.Atom;
import org.jpl7.Query;
import org.jpl7.Term;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.util.ContextUtils;
import repast.simphony.engine.schedule.ScheduleParameters;

public class ControllerAgent {
	public int actionNo;
	public String action;
	public float propability_of_non_conviction;
	int humanCount;
	ArrayList<String> cascading_events;
	String prologPath;
	String[] salientEvents;
	Query[] primitiveActionsQueries;

	public ControllerAgent(int human_count, String[] salientEvents, String prologPath) {
		this.cascading_events = creatingAgentsEvents(human_count, salientEvents);
		this.prologPath = prologPath;
		this.salientEvents = salientEvents;
	}

	public static ArrayList<String> creatingAgentsEvents(int humanCount, String[] salientEvents) {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < humanCount; i++) {
			list.add("randEvent" + i);
		}
		return list;
	}

	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		cascadeAction(this.cascading_events);
	}

	public void cascadeAction(ArrayList<String> cascading_events) {
		Context context = ContextUtils.getContext(this);
		Iterable<Citizen> i = context.getAgentLayer(Citizen.class);
		Iterator<Citizen> it = i.iterator();
		int cascadeIndex = 0;
		while (it.hasNext()) {
			Citizen c = it.next();
			c.setRandomAction(cascading_events.get(cascadeIndex), this.salientEvents);
			cascadeIndex++;
		}
	}

}
