package trialForTreason;

import java.io.FileInputStream;  
import java.io.IOException;
import java.util.HashMap;

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

public class Example 
{  
public static void main(String args[]) throws IOException, InvalidFormatException  
{  
	    double[] values = {9967,11281,10752,10576,2366,11882,11798};
	    double variance = StatUtils.populationVariance(values);
	    double sd = Math.sqrt(variance);
	    double mean = StatUtils.mean(values);
	    NormalDistribution nd = new NormalDistribution();
	    for ( double value: values ) {
	        double stdscore = (value-mean)/sd;
	        double sf = 1.0 - nd.cumulativeProbability(Math.abs(stdscore));
	        System.out.println("" + stdscore + " " + sf);
	    
	}
} } 