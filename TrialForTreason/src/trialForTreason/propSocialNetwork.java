package trialForTreason;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class propSocialNetwork {
	int noCitizens;
	String filePath;
	HashMap<Integer, ArrayList<Integer>> edgeDetail = new HashMap<Integer, ArrayList<Integer>>();

	propSocialNetwork(int noCitizens, String filePath) {
		this.noCitizens = noCitizens;
		this.filePath = filePath;
	}

	public HashMap<String, String> setEthoses(String filePath, int noCitizens, int threshold1, int threshold2) {
		HashMap<String, String> citizensDetail = new HashMap<String, String>();
		try {
			File myObj = new File(filePath);
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				for (int i = 0; i < noCitizens; i++) {
					String letter = Integer.toString(i);
					if (data.startsWith(letter)) {
						List<String> popertiesList = Arrays.asList(data.split(";"));
						if (Double.parseDouble(popertiesList.get(1)) < threshold1) {
							citizensDetail.put(letter, "No Ethoses");
						} else if (Double.parseDouble(popertiesList.get(1)) > threshold2) {
							citizensDetail.put(letter, "Ethos2");
						} else {
							citizensDetail.put(letter, "Ethos1");
						}
					}
				}
				if (data.endsWith("Edges")) {
					break;
				}
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		for (String name : citizensDetail.keySet()) {
			String key = name.toString();
			String value = citizensDetail.get(name).toString();
			System.out.println(key + " " + value);
		}
		return citizensDetail;
	}

	public void getEdges(HashMap<Integer, ArrayList<Integer>> edgeDetail) {
		for (Integer name : edgeDetail.keySet()) {
			String key = name.toString();
			String value = edgeDetail.get(name).toString();
			System.out.println("size " + value.length());
			System.out.println(key + " " + value);
		}
	}

	public int[] getMaximumNode(HashMap<Integer, ArrayList<Integer>> edgeDetail, int val) {
		int[] maximumNodes = new int [val];
		for (Integer name : edgeDetail.keySet()) {
			String key = name.toString();
			String value = edgeDetail.get(name).toString();
			//System.out.println(key + " " + value);
			maximumNodes[name] = value.length();
		}
		return maximumNodes;
	}

	public HashMap<Integer, ArrayList<Integer>> setEdges(String filePath, int noCitizens) {
		HashMap<Integer, ArrayList<Integer>> edgeDetail = new HashMap<Integer, ArrayList<Integer>>();
		try {
			File myObj = new File(filePath);
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				//condition for 2 digit, 3 digit and 4 digit number
				if (data.length() == 3 || data.length() == 4 ||data.length() == 5 ) {
					List<String> popertiesList = Arrays.asList(data.split(";"));
					if (edgeDetail.containsKey(Integer.parseInt(popertiesList.get(0)))) {
						edgeDetail.get(Integer.parseInt(popertiesList.get(0)))
								.add(Integer.parseInt(popertiesList.get(1)));
					} else {
						edgeDetail.put(Integer.parseInt(popertiesList.get(0)), new ArrayList<Integer>());
						edgeDetail.get(Integer.parseInt(popertiesList.get(0)))
								.add(Integer.parseInt(popertiesList.get(1)));
					}
				}
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return edgeDetail;
	}
	
	public List jurorsExactPossition (int noCitizens, int [] nodeSizes, int jurorsCount, propSocialNetwork prop, HashMap<Integer, ArrayList<Integer>> edgeDetail ) {
		int val = (noCitizens * 25) / 100;
		//System.out.println("val...." + val);
		Arrays.sort(nodeSizes);
//		System.out.println("sorted array " + Arrays.toString(nodeSizes));
		int[] top25perc = Arrays.copyOfRange(nodeSizes, nodeSizes.length - val, nodeSizes.length);
		//System.out.println(Arrays.toString(top25perc));
		int [] nodeSize = prop.getMaximumNode(edgeDetail, noCitizens);
//		System.out.println("sorted array " + Arrays.toString(nodeSize));
		HashMap<Integer, Integer> juriesAndPositions = new HashMap<Integer, Integer>();
		for (int j = 0; j < top25perc.length; j++) {
//			System.out.println("j " + top25perc[j]);
			for (int i = 0; i < nodeSize.length; i++) {
//				System.out.println("nodeSize1[i] " + nodeSize1[i]);
				if (top25perc[j] == nodeSize[i]) {
					juriesAndPositions.put(top25perc[j], i);
				}
			}
		}
		System.out.println("Most connected nodes (25% from the nodes) and positions");
		for (Integer name : juriesAndPositions.keySet()) {
			int key = name;
			int value = juriesAndPositions.get(name);
			System.out.println(key + " " + value);
		}

		// create random values
		Integer[] randomJurynumbers = new Integer[top25perc.length];
	    for (int i = 0; i < randomJurynumbers.length; i++) {
	    	randomJurynumbers[i] = i;
	    }
	    Collections.shuffle(Arrays.asList(randomJurynumbers));
	    //System.out.println(Arrays.toString(randomJurynumbers));
	    
	  //Getting jurors exact positions
	    List<Integer> randomJurorsExactPostioins = new ArrayList<Integer>();
	    for (int i: randomJurynumbers) {
	    	//System.out.println(top25perc[i]);
	    	if (randomJurorsExactPostioins.size() < jurorsCount) {
	    	randomJurorsExactPostioins.add(juriesAndPositions.get(top25perc[i]));
	    	}else {
	    		break;
	    	}	
	    }		
	    return randomJurorsExactPostioins;
	}
}
