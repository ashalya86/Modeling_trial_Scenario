package trialForTreason;

import trialForTreason.Citizen;
import trialForTreason.Jury;
import trialForTreason.ControllerAgent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
import repast.simphony.context.space.graph.DefaultNetworkFactory;
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
import repast.simphony.space.graph.RepastEdge;
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
		//String[] actions = {"A", "R", "R", "R", "A", "A", "A", "A", "A", "A", "A", "A"};
 		String[] actions = { "A", "R", "R", "R", "A", "A", "A", "A", "A", "A", "A", "R", "R", "R", "A", "A", "A", "A",
				"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
				"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
				"A", "R", "R", "R", "A", "A", "A", "A", "A", "A", "A", "R", "R", "R", "A", "A", "A", "A", "A", "A", "A",
				"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A"
				,"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
				"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
				"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
				"A", "R", "R", "R", "A", "A", "A", "A", "A", "A", "A", "R", "R", "R", "A", "A", "A", "A", "A", "A", "A",
				"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A"
				,"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
				"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
				"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
				"A", "R", "R", "R", "A", "A", "A", "A", "A", "A", "A", "R", "R", "R", "A", "A", "A", "A", "A", "A", "A",
				"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A"
				,"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
				"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
				"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
				"A", "R", "R", "R", "A", "A", "A", "A", "A", "A", "A", "R", "R", "R", "A", "A", "A", "A", "A", "A", "A",
				"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A"
				,"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
				"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
				"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
				"A", "R", "R", "R", "A", "A", "A", "A", "A", "A", "A", "R", "R", "R", "A", "A", "A", "A", "A", "A", "A",
				"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A"
				,"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
				"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
				"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
				"A", "R", "R", "R", "A", "A", "A", "A", "A", "A", "A", "R", "R", "R", "A", "A", "A", "A", "A", "A", "A",
				"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A"
				,"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
				"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
				"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
				"A", "R", "R", "R", "A", "A", "A", "A", "A", "A", "A", "R", "R", "R", "A", "A", "A", "A", "A", "A", "A",
				"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A"
				,"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
				"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
				"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
				"A", "R", "R", "R", "A", "A", "A", "A", "A", "A", "A", "R", "R", "R", "A", "A", "A", "A", "A", "A", "A",
				"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A"
				,"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
				"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
				"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
				"A", "R", "R", "R", "A", "A", "A", "A", "A", "A", "A", "R", "R", "R", "A", "A", "A", "A", "A", "A", "A",
				"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A"
				,"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
				"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
				"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
				"A", "R", "R", "R", "A", "A", "A", "A", "A", "A", "A", "R", "R", "R", "A", "A", "A", "A", "A", "A", "A",
				"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A"};
		Query queryConsult;	
		HashMap<String, String> perceptsRecieved = new HashMap<String, String>();
		perceptsRecieved.put("monument", "m27");
		perceptsRecieved.put("status", "traitor(hipparchus)");
		perceptsRecieved.put("affordance", "public_information");
		String graphPath = "./data/500Agents.graph";
		HashMap<String, String> citizensEthoses;
		int threshold1 = -1;
		int threshold2 = 2;
		List citizens = new ArrayList();
		List juries = new ArrayList();
		HashMap<Integer, ArrayList<Integer>> edgeDetail;
		HashMap<Integer, ArrayList<Integer>> citizensEdgeDetail = new HashMap<Integer, ArrayList<Integer>>();
		HashMap<Integer, ArrayList<Integer>> juriesEdgeDetail = new HashMap<Integer, ArrayList<Integer>>();

		Citizen citizen = null;
		Jury jury = null;
		ControllerAgent agent = null;
		List jurorsList = new ArrayList();

		context.setId("trialForTreason");

		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace("space", context,
				new RandomCartesianAdder<Object>(), new repast.simphony.space.continuous.WrapAroundBorders(), 50, 50);

		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid("grid", context, new GridBuilderParameters<Object>(
				new WrapAroundBorders(), new SimpleGridAdder<Object>(), true, 50, 50));

		Parameters params = RunEnvironment.getInstance().getParameters();
		int jurorsCount = (Integer) params.getValue("jurors_count");

		int humansCount = (Integer) params.getValue("human_count");
		double signalAccuracy = (double) params.getValue("signal_accuracy");
		System.out.println("signalAccuracy " + signalAccuracy);
		System.out.println("citizen_count " + humansCount);
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
		int noCitizen = humansCount - jurorsCount;

		propSocialNetwork prop = new propSocialNetwork(humansCount, graphPath);
		// Setting ethoses for citizens from graph attributes
		citizensEthoses = prop.setEthoses(graphPath, humansCount, threshold1, threshold2);
		// getting all the nodes of citizens
		edgeDetail = prop.setEdges(graphPath, humansCount);
		// prop.getEdges(edgeDetail);
		// choosing jurors nodes
		int[] nodeSize = prop.getMaximumNode(edgeDetail, humansCount);
		System.out.println("nodeSize " + Arrays.toString(nodeSize));
		List<Integer> randomJurorsExactPostioins = prop.jurorsExactPossition(humansCount, nodeSize, jurorsCount, prop,
				edgeDetail);
		System.out.println("randomJurorsExactPostioins " + randomJurorsExactPostioins.get(0));
		System.out.println("randomJurorsExactPostioins " + randomJurorsExactPostioins.get(1));

		queryConsult = new Query("consult", new Term[] { new Atom(cascadingPrologPath) });
		context.add(new ControllerAgent(humansCount, salientEvents, cascadingPrologPath));

		// Create citizens and Separating jurors from citizens
		for (int i = 0; i < humansCount; i++) {
			citizen = new Citizen(actions[i], cascadingPrologPath, salientEvents, i, humansCount, queryConsult,
					lewisPrologPath, perceptsRecieved, grid, space);
			citizens.add(citizen);
			juries.add(null);
			for (int j = 0; j < jurorsCount; j++) {
				if (randomJurorsExactPostioins.get(j) == i) {
					int lastIndexJurors = juries.size() - 1;
					juries.remove(lastIndexJurors);
					juries.add(citizen);
					// int lastIndexCitizens = citizens.size() - 1;
					// citizens.remove(lastIndexCitizens);
					// citizens.add(null);
				}
			}
			context.add(citizen);
		}

		System.out.println("juries " + juries.size());
		System.out.println("citizens " + citizens.size());

		// Create jurors
		System.out.println("jurors_count " + jurorsCount);
		for (int i = 0; i < jurorsCount; i++) {
			jury = new Jury(cascadingPrologPath, noCitizen, salientEvents, i, queryConsult, lewisPrologPath,
					perceptsRecieved, noCitizen, grid, space, randomJurorsExactPostioins.get(i), edgeDetail,
					citizensEthoses);
			for (int j = 0; j < juries.size(); j++) {
				System.out.println("juries.get(j) " + j + " " + juries.get(j));
				if (juries.get(j) != null) {
					juries.set(j, jury);
					context.add(jury);
				}
			}
		}

		for (int i = 0; i < randomJurorsExactPostioins.size(); i++) {
			randomJurorsExactPostioins.get(i);
		}

		// Create a social network
		WattsBetaSmallWorldGenerator wattsBetaSmallWorldGenerator = new WattsBetaSmallWorldGenerator(0.5, 4, true);
		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>("social network", context, true);
		netBuilder.setGenerator(wattsBetaSmallWorldGenerator);
		netBuilder.buildNetwork();

		Network<Object> colNet = (Network<Object>) context.getProjection("social network");
		// Remove old edges
		colNet.removeEdges();
		for (Map.Entry<Integer, ArrayList<Integer>> entry : edgeDetail.entrySet()) {
			Integer key = entry.getKey();
			ArrayList<Integer> values = entry.getValue();
			System.out.println(key + " : " + values.toString());
			for (int j = 0; j < jurorsCount; j++) {
				if (key == randomJurorsExactPostioins.get(j)) {
					for (int k = 0; k < values.size(); k++) {
						colNet.addEdge(juries.get(key), citizens.get(k));
						context.add(juries.get(key));
						System.out.println(key + " .. " + juries.get(key) + " .... " + citizens.get(k));
					}
				} else {
					for (int l = 0; l < values.size(); l++) {
						colNet.addEdge(citizens.get(key), citizens.get(l));
						context.add(citizens.get(key));
					}
				}
			}
		}
		for (int i = 0; i < noCitizen; i++) {
			System.out.println(i);
			System.out.println(colNet.getAdjacent(citizens.get(i)));
		}
//		for (int i = 0; i < randomJurorsExactPostioins.size(); i++){
//			System.out.println(colNet.getAdjacent(juries.get(i)));
//		}
		return context;
	}
}
