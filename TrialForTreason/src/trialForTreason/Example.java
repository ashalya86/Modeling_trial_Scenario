package trialForTreason;

import java.io.IOException;

public class Example {
	static String prologPath = "src/trialForTreason/cascadings.pl";
    static String[] salientEvents ={"buildingWalls", "makingPalisades"};
    String [] actions  = {"A", "R","R", "R", "A", "A", "A", "A", "A", "A"};

	public static void main(String[] args) throws IOException {
	    System.out.println("Hello from the Java Main Function!");
		FindingSalientEvent salientEvent = new FindingSalientEvent(prologPath, 2, 2, salientEvents);
	    System.out.println("Hello from the Java Main Function!");
		boolean resultSalient = salientEvent.resultOfSalient(prologPath, "randEvent", 2, 3, salientEvents);

		System.out.println(resultSalient);

	  }
	

}
