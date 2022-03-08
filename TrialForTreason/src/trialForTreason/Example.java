package trialForTreason;

import java.util.HashMap;
import java.util.Map;

public class Example {
	  public static void main(String[] args) {
	HashMap<Double, Double> capitalCities = new HashMap<Double, Double>();
    capitalCities.put(0.5, 0.6);
    capitalCities.put(0.6, 0.4);
    capitalCities.put(0.7, 0.9);
    capitalCities.put(0.8, 0.7);
    System.out.println(capitalCities); 

    Map<Double, Double> myMap = new HashMap<Double, Double>() {{
    	put(0.5, 0.6);
    	put(0.6, 0.4);
    	put(0.7, 0.9);
    	put(0.8, 0.7);
    	put(0.9, 0.6);
    }};
    System.out.println(myMap); 

    System.out.println(myMap.get(0.8)); 
	double siganlAccuracy =   (double)8/10;
    System.out.println(myMap.get(siganlAccuracy)); 



    
}
}
