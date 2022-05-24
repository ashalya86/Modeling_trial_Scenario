package trialForTreason;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.util.ContextUtils;
import repast.simphony.engine.schedule.ScheduleParameters;


public class ControllerAgent {
	public int actionNo;
	public String action;
	public float propability_of_non_conviction;
	
	public ControllerAgent() {
		// TODO Auto-generated constructor stub
	}

	Map<Integer, String> cascading_events = new HashMap<Integer, String>() {{
    	put(0, "buildingWalls");
    	put(1, "makingTrenches");
    	put(2, "makingPalisades");
    }};

@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		cascadeAction(cascading_events);	
	}
	
	
	public void cascadeAction(Map<Integer, String> cascading_events) {
		Random randy = new Random();
		actionNo = randy.nextInt(2);
		Context context = ContextUtils.getContext(this);
		Iterable<Citizen> i = context.getAgentLayer(Citizen.class);
	      Iterator<Citizen> it = i.iterator();
	      int cascadeIndex = 1; 
	      while (it.hasNext())
	      {
	  		System.out.println("controller agent, "+ actionNo);
	            Citizen c = it.next();  
	            action = cascading_events.get(actionNo);
	            c.setCascadeAction(cascadeIndex, action);
	            cascadeIndex++;
	      }
	      
	}
}
