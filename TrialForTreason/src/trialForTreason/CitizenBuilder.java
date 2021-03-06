package trialForTreason;
import trialForTreason.Citizen;
import trialForTreason.Jury;
import trialForTreason.ControllerAgent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.jpl7.Atom;
import org.jpl7.JPL;
import org.jpl7.Query;
import org.jpl7.Term;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.context.space.graph.WattsBetaSmallWorldGenerator;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;
import repast.simphony.util.ContextUtils;


public class CitizenBuilder implements ContextBuilder<Object>{
	@Override
	public Context build(Context<Object> context) {
		float propability_of_conviction  = (float) 1.0;
		float propability_of_non_conviction = (float) 0.1;
		String prologPath = "src/trialForTreason/cascadings.pl";
	    String[] salientEvents ={"buildingWalls", "makingPalisades"};
	    String [] actions  = {"A", "R","R", "R", "A", "A", "A", "A", "A", "A"};

		context.setId("trialForTreason");		

		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder
				.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace(
				"space", context, new RandomCartesianAdder<Object>(),
				new repast.simphony.space.continuous.WrapAroundBorders(), 50,
				50);

		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid("grid", context,
				new GridBuilderParameters<Object>(new WrapAroundBorders(),
						new SimpleGridAdder<Object>(), true, 50, 50));
		
		
		JurorsDecision jurors = new JurorsDecision ();
//		try {
//			propability = jurors.getResults();
//	
			Parameters params = RunEnvironment.getInstance().getParameters();
			int jurorsCount = (Integer) params.getValue("jurors_count"); 
			
			int humanCount = (Integer) params.getValue("human_count");
			double signalAccuracy = (double)params.getValue("signal_accuracy");
			System.out.println("signalAccuracy " + signalAccuracy);
			System.out.println("citizen_count " + humanCount );
			System.out.print("salientEvents { ");
			for (int i = 0; i< salientEvents.length; i++) {
				System.out.print(salientEvents[i] + " , ");
			}
			System.out.println("}");
			
			System.out.print("actions { ");
			for (int i = 0; i< actions.length; i++) {
				System.out.print(actions[i] + " , ");
			}
			System.out.println("}");
			
			context.add(new ControllerAgent(humanCount, salientEvents, prologPath));											
			
			for (int i = 0; i < humanCount; i++) {	
				context.add(new Citizen(actions[i],  prologPath, salientEvents, i, humanCount));
			}	
			
			System.out.println("jurors_count " + jurorsCount );
			for (int i = 0; i < jurorsCount; i++) {
				context.add(new Jury(prologPath, humanCount, salientEvents));
			}
//			
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block 
//			e.printStackTrace();
//		}

			WattsBetaSmallWorldGenerator wattsBetaSmallWorldGenerator = new WattsBetaSmallWorldGenerator(0.5, 4, true);		
			NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>(
					"social network", context, true); 
			netBuilder.setGenerator(wattsBetaSmallWorldGenerator);
			netBuilder.buildNetwork();
			
		for (Object obj : context) {
			NdPoint pt = space.getLocation(obj);
			grid.moveTo(obj, (int) pt.getX(), (int) pt.getY());
		}		
		return context;
	}
}
