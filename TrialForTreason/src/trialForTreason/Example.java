package trialForTreason;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.measure.quantity.Length;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Example {
	public static int findIndex(int[] my_array, int t) {
		if (my_array == null)
			return -1;
		int len = my_array.length;
		int i = 0;
		while (i < len) {
			if (my_array[i] == t)
				return i;
			else
				i = i + 1;
		}
		return -1;
	}

	public int[] getMaximumNode(HashMap<Integer, ArrayList<Integer>> edgeDetail, int val) {
		int[] maximumNodes = new int[val];
		for (Integer name : edgeDetail.keySet()) {
			String key = name.toString();
			String value = edgeDetail.get(name).toString();
			System.out.println(key + " " + value);
			maximumNodes[name] = value.length();
		}
		return maximumNodes;
	}

	public static Set<Integer> findDuplicates(int[] input) {
		Set<Integer> duplicates = new HashSet<Integer>();
		for (int i = 0; i < input.length; i++) {
			for (int j = 1; j < input.length; j++) {
				if (input[i] == input[j] && i != j) { // duplicate element found duplicates.add(input[i]);
					break;
				}
			}
		}
		return duplicates;
	}

	private static final Random generator = new Random();

	public static int getRandomInRange(int start, int end){ return start + generator.nextInt(end - start + 1); }
	
	public static void main(String args[]) throws IOException, InvalidFormatException {
		Example example = new Example();
//		double[] values = { 9967, 11281, 10752, 10576, 2366, 11882, 11798 };
//		double variance = StatUtils.populationVariance(values);
//		double sd = Math.sqrt(variance);
//		double mean = StatUtils.mean(values);
//		NormalDistribution nd = new NormalDistribution();
//		for (double value : values) {
//			double stdscore = (value - mean) / sd;
//			double sf = 1.0 - nd.cumulativeProbability(Math.abs(stdscore));
//			System.out.println("" + stdscore + " " + sf);
//		}
		////////////////////////////////////////////////////////////////////////////

		int[] arr = new int[] { 13, 16, 13, 6, 13, 6, 6, 15, 7, 9, 9, 6 };
		Arrays.sort(arr);
		arr[5] = 5;
		int[] top4 = Arrays.copyOfRange(arr, arr.length - 3, arr.length);
		System.out.println(Arrays.toString(top4));

		int noCitizens = 12;
		String filePath = "./data/12Agents.graph";
		propSocialNetwork prop = new propSocialNetwork(noCitizens, filePath);
		HashMap<String, String> citizensEthoses = prop.setEthoses(filePath, noCitizens, -1, 2);
		// getting all the nodes of citizens
		HashMap<Integer, ArrayList<Integer>> edgeDetail = prop.setEdges(filePath, noCitizens);
		int val = (noCitizens * 25) / 100;
//		prop.getEdges(edgeDetail);
//		// choosing jurors nodes
		int[] nodeSize = example.getMaximumNode(edgeDetail, noCitizens);
		System.out.println("nodeSize " + Arrays.toString(nodeSize));
		Arrays.sort(nodeSize);
		System.out.println("sorted array " + Arrays.toString(nodeSize));
		int[] top25perc = Arrays.copyOfRange(nodeSize, nodeSize.length - val, nodeSize.length);
		System.out.println(Arrays.toString(top25perc));
		int[] nodeSize1 = example.getMaximumNode(edgeDetail, noCitizens);
		System.out.println("nodeSize " + Arrays.toString(nodeSize1));
		int index = Arrays.asList(nodeSize1).indexOf(16);;
		System.out.println(" .." + index);

//		// System.out.println(findIndex(nodeSize, 16));

//		int[] nodeSize1 = { 13, 16, 13, 6, 13, 6, 6, 15, 7, 9, 9, 6 };
//		int[] top25perc = { 13, 15, 16 };
		HashMap<Integer, Integer> juriesAndPositions = new HashMap<Integer, Integer>();
		for (int j = 0; j < top25perc.length; j++) {
//			System.out.println("j " + top25perc[j]);
			for (int i = 0; i < nodeSize1.length; i++) {
//				System.out.println("nodeSize1[i] " + nodeSize1[i]);
				if (top25perc[j] == nodeSize1[i]) {
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
		int jurorsCount = 2;
		Integer[] randomJurynumbers = new Integer[top25perc.length];
	    for (int i = 0; i < randomJurynumbers.length; i++) {
	    	randomJurynumbers[i] = i;
	    }
	    Collections.shuffle(Arrays.asList(randomJurynumbers));
	    System.out.println(Arrays.toString(randomJurynumbers));
		
		//////////////////////////////////////////////////////////////////////
	    //Getting jurors exact positions
	    List randomJurorsExactPostioins = new ArrayList();
	    for (int i: randomJurynumbers) {
	    	System.out.println(top25perc[i]);
	    	if (randomJurorsExactPostioins.size() < jurorsCount - 1) {
	    	randomJurorsExactPostioins.add(juriesAndPositions.get(top25perc[i]));
	    	}else {
	    		break;
	    	}	
	    }

	    
	}
}
