package trialForTreason;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;

public class JurorsDecision {
	public float getResults() throws IOException, InterruptedException{
		
		List<String> list = new ArrayList<>();

		ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "cd \"C:\\ProbCog-master\\src\\main\\python\" && python trialInfer.py");
        	File log = new File("log");
            builder.redirectErrorStream(true);
            builder.redirectOutput(Redirect.appendTo(log));

            Process p = builder.start();
            p.waitFor();
            
            assert builder.redirectInput() == Redirect.PIPE;
            assert builder.redirectOutput().file() == log;
            assert p.getInputStream().read() == -1;

            FileInputStream fstream = new FileInputStream("log");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                /* parse strLine to obtain what you want */
                String search = "Results obtained";
                if (strLine.toLowerCase().indexOf(search.toLowerCase()) != -1) {
                    System.out.println("I found the keyword");
                    while ((strLine = br.readLine()) != null) {
                        list.add(strLine);
                        System.out.println("results " + strLine);
                    }
                }
            }
            fstream.close();
            System.out.println("List " + list);
            System.out.println("prob1 " + list.get(0));
            String prob1  = list.get(0).substring(0, 10);
            float propability  = Float.parseFloat(prob1.replace(" ", ""));
            return propability;
            
		
	}

}
