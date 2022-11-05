package trialForTreason;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jpl7.Atom;
import org.jpl7.Query;
import org.jpl7.Term;

public class CKofMonument {

	String prologPath;
	boolean resultCitizenAttendedAction;
	boolean result2;
	String event;
	int currentTick;
	int humanCount;
	Query queryConsult;
	List<Query> queriesOfPerceptsRecieved = new ArrayList<Query>();

	public CKofMonument(String prologPath, int queryConsultqueryConsult, HashMap<String, String> perceptsRecieved,
			Query queryConsult) {
		this.prologPath = prologPath;
		this.currentTick = currentTick;
		this.queryConsult = new Query("consult", new Term[] { new Atom(prologPath) });
		
		// feeding what agent percieved in the square
		this.queriesOfPerceptsRecieved.add(new Query("add_PFC((percept(me, citizen(me))))."));

		this.queriesOfPerceptsRecieved.add(new Query("add_PFC((percept(me, location(me, square))))."));

		this.queriesOfPerceptsRecieved
				.add(new Query("add_PFC((percept(me, location(" + perceptsRecieved.get("monument") + "square))))."));
		this.queriesOfPerceptsRecieved.add(new Query("add_PFC((percept(me, states(" + perceptsRecieved.get("monument")
				+ "," + perceptsRecieved.get("status") + "))))."));

		this.queriesOfPerceptsRecieved.add(new Query("add_PFC((percept(me, affordance("
				+ perceptsRecieved.get("monument") + "," + perceptsRecieved.get("affordance") + "))))."));
	}


	public Map<String, Term>[] gettingCK() {
		this.queryConsult.hasSolution();
		for (int i = 0; i < this.queriesOfPerceptsRecieved.size(); i++) {
			this.queriesOfPerceptsRecieved.get(i).hasSolution();
		}
		Query queryCK = new Query("baseKB:ck(A,X).");
		java.util.Map<String, Term>[] solutions = queryCK.allSolutions();
		return solutions;
	}

}
