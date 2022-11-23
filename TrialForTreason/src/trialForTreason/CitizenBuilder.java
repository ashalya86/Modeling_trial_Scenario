package trialForTreason;

import trialForTreason.Citizen;
import trialForTreason.Jury;
import trialForTreason.ControllerAgent;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jpl7.Atom;
import org.jpl7.JPL;
import org.jpl7.Query;
import org.jpl7.Term;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.context.space.graph.NetworkFileFormat;
import repast.simphony.context.space.graph.NetworkGenerator;
import repast.simphony.context.space.graph.NodeCreator;
import repast.simphony.context.space.graph.WattsBetaSmallWorldGenerator;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.graph.EdgeCreator;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;
import repast.simphony.util.ContextUtils;

public class CitizenBuilder implements ContextBuilder<Object> {
	@Override
	public Context build(Context<Object> context) {
		String cascadingPrologPath = "src/trialForTreason/cascadings.pl";
		String lewisPrologPath = "src/trialForTreason/lewis_model_ck.pl";
		String[] salientEvents = { "buildingWalls", "makingPalisades" };
		String[] actions = { "A", "R", "R", "R", "A", "A", "A", "A", "A", "A" };
		Query queryConsult;
		String day;
		HashMap<String, String> perceptsRecieved = new HashMap<String, String>();
		perceptsRecieved.put("monument", "m27");
		perceptsRecieved.put("status", "traitor(hipparchus)");
		perceptsRecieved.put("affordance", "public_information");
				
		context.setId("trialForTreason");

		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace("space", context,
				new RandomCartesianAdder<Object>(), new repast.simphony.space.continuous.WrapAroundBorders(), 50, 50);

		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid("grid", context, new GridBuilderParameters<Object>(
				new WrapAroundBorders(), new SimpleGridAdder<Object>(), true, 50, 50));

		Parameters params = RunEnvironment.getInstance().getParameters();
		int jurorsCount = (Integer) params.getValue("jurors_count");

		int humanCount = (Integer) params.getValue("human_count");
		double signalAccuracy = (double) params.getValue("signal_accuracy");
		System.out.println("signalAccuracy " + signalAccuracy);
		System.out.println("citizen_count " + humanCount);
		System.out.print("salientEvents { ");
		for (int i = 0; i < salientEvents.length; i++) {
			System.out.print(salientEvents[i] + " , ");
		}
		System.out.println("}");

		System.out.print("actions { ");
		for (int i = 0; i < actions.length; i++) {
			System.out.print(actions[i] + " , ");
		}
		System.out.println("}");

		context.add(new ControllerAgent(humanCount, salientEvents, cascadingPrologPath));

		queryConsult = new Query("consult", new Term[] { new Atom(cascadingPrologPath) });

		//Create humans
		for (int i = 0; i < humanCount; i++) {
			context.add(new Citizen(actions[i], cascadingPrologPath, salientEvents, i, humanCount, queryConsult,
					lewisPrologPath, perceptsRecieved, grid, space));
		}

		//Create jurors
		System.out.println("jurors_count " + jurorsCount);
		for (int i = 0; i < jurorsCount; i++) {
			context.add(new Jury(cascadingPrologPath, humanCount, salientEvents, i, queryConsult, lewisPrologPath,
					perceptsRecieved, humanCount, grid, space));
		}
		
//		NodeCreator nodeCreator = null;
//		NetworkBuilder builder = new NetworkBuilder("Network", context, true);
//		try {
//			OPCPackage pkg = OPCPackage.open(new File("./data/sample.xlsx"));
//			XSSFWorkbook wb = new XSSFWorkbook(pkg);
//			builder.load("./data/sample.xlsx",
//			NetworkFileFormat.EXCEL, nodeCreator);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidFormatException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		Network net = builder.buildNetwork();
		

		WattsBetaSmallWorldGenerator wattsBetaSmallWorldGenerator = new WattsBetaSmallWorldGenerator(0.5, 4, true);
		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>("social network", context, true);
		netBuilder.setGenerator(wattsBetaSmallWorldGenerator);
		netBuilder.buildNetwork();
		Network<Object> colNet = (Network<Object>) context.getProjection("social network");	
		
		for (Object obj : context) {
			NdPoint pt = space.getLocation(obj);
			grid.moveTo(obj, (int) pt.getX(), (int) pt.getY());
		}
		return context;
	}
}
