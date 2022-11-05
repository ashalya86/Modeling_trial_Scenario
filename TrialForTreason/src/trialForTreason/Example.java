package trialForTreason;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jpl7.Atom;
import org.jpl7.Query;
import org.jpl7.Term;

public class Example {
	static String prologPath = "src/trialForTreason/lewis_model_ck.pl";
	static String[] salientEvents = { "buildingWalls", "makingPalisades" };
	String[] actions = { "A", "R", "R", "R", "A", "A", "A", "A", "A", "A" };
	static Query q1;
	Query q2;
	Query q3;
	List<Query> queries = new ArrayList<Query>();
	List<Query> queriesOfPerceptsRecieved = new ArrayList<Query>();

	static HashMap<String, String> perceptsRecieved = new HashMap<String, String>();

	public Example() {
		this.q1 = new Query("consult", new Term[] { new Atom(prologPath) });
		System.out.println("Consult true!" + q1.hasSolution());

		// feeding what salient events are
		perceptsRecieved.put("monument", "m27");
		perceptsRecieved.put("status", "traitor(hipparchus)");
		perceptsRecieved.put("affordance", "public_information");
//			this.queriesOfPerceptsRecieved
//					.add(new Query(" add_PFC(((percept(me, citizen(me)))))."));
//			this.queriesOfPerceptsRecieved
//					.add(new Query ("add_PFC((percept(me, location(me, square))))."));
//			this.queriesOfPerceptsRecieved
//			.add(new Query("add_PFC((percept(me, location(m27, square))))."));
//			this.queriesOfPerceptsRecieved
//			.add(new Query("add_PFC((percept(me, states(m27, traitor(hipparchus)))))."));
//			this.queriesOfPerceptsRecieved
//			.add(new Query("add_PFC((percept(me, affordance(m27, public_information))))."));
//			
		this.queriesOfPerceptsRecieved.add(new Query("add_PFC((percept(me, citizen(me))))."));

		this.queriesOfPerceptsRecieved.add(new Query("add_PFC((percept(me, location(me, square))))."));

		this.queriesOfPerceptsRecieved
				.add(new Query("add_PFC((percept(me, location(" + perceptsRecieved.get("monument") + "square))))."));
		this.queriesOfPerceptsRecieved.add(new Query("add_PFC((percept(me, states(" + perceptsRecieved.get("monument")
				+ "," + perceptsRecieved.get("status") + "))))."));

		this.queriesOfPerceptsRecieved.add(new Query("add_PFC((percept(me, affordance("
				+ perceptsRecieved.get("monument") + "," + perceptsRecieved.get("affordance") + "))))."));

	}

	public java.util.Map<String, Term>[] gettingCountAsEvents() {
		this.q1.hasSolution();
		System.out.println("X" + this.queriesOfPerceptsRecieved.size());
		for (int i = 0; i < this.queriesOfPerceptsRecieved.size(); i++) {
			System.out.println(this.queriesOfPerceptsRecieved.get(i).hasSolution());
		}

		Query queryCountAsEvents = new Query("baseKB:ck(A,X).");
		java.util.Map<String, Term>[] solutions = queryCountAsEvents.allSolutions();
		return solutions;
	}

	public static void main(String[] args) throws IOException {
		System.out.println("Hello from the Java Main Function!");
		// CKofMonument ck = new CKofMonument(prologPath, 1, perceptsRecieved, q1);
		Example ex = new Example();
		System.out.println("I'm attending public square");
		java.util.Map<String, Term>[] solutions = ex.gettingCountAsEvents();
		for (int i = 0; i < solutions.length; i++) {
			System.out.println("X = " + solutions[i].get("X"));
		}
	}
}
