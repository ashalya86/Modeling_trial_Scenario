package trialForTreason;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
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
							citizensDetail.put(letter, "Ethos1");
						} else if (Double.parseDouble(popertiesList.get(1)) > threshold2) {
							citizensDetail.put(letter, "NoEthoses");
						} else {
							citizensDetail.put(letter, "Ethos2");
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
			System.out.println(key + " " + value);
		}
	}

	public HashMap<Integer, ArrayList<Integer>> setEdges(String filePath, int noCitizens) {
		HashMap<Integer, ArrayList<Integer>> edgeDetail = new HashMap<Integer, ArrayList<Integer>>();
		try {
			File myObj = new File(filePath);
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				if (data.length() == 3) {
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
}
