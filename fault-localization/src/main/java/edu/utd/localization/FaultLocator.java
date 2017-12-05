package edu.utd.localization;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import edu.utd.configuration.Config;
import edu.utd.testing.utils.Utils;

/**
 * FaultLocaltor is implemented the first fault localization strategy 
 * @author sonnguyen
 *
 */
public class FaultLocator {
	static String dataPath;
	
	public static void main(String[] args) throws FileNotFoundException{
		for (int i = 0; i < 27; i++) {
			localize(i+1);
		}
	}
	/**
	 * Specify all methods might be buggy. The result is written to result1.txt.
	 * That includes the number of methods, failing test, and methods
	 * @param bug bug id
	 * @throws FileNotFoundException
	 */
	public static void localize(int bug) throws FileNotFoundException {
		dataPath = Config.DATA + "/time_bug_"+bug;
		List<String> triggers = Utils.extractTriggerFromFailingTest(dataPath);
		String resultPath = dataPath + "/result.txt";
		if(Config.PHOSPHOR)
			resultPath = dataPath + "/result_phosphor.txt";
		String result = Utils.getResultContent(resultPath);
		
		int start = 0;
		int end = 0;
		StringBuffer postResult = new StringBuffer();
		String methods;
		
		while(true){
			start = result.indexOf("Running ", end);
			end = result.indexOf("Tests run: ", start);
			if(start == -1 || end == -1)
				break;
			methods = result.substring(start, end);
			postResult.append(methods).append("---\n");
		}
		result = updateResult(postResult.toString(), triggers);
		result = refineResult(result);
		writeResult(result);
	}
	private static String updateResult(String result, List<String> triggers) {
		for (String trigger : triggers) {
			trigger = trigger.replace("::", ".");
			result = result.replace(trigger+"\n", "Running "+ trigger+"\n");
		}
		return result;
	}
	/**
	 * Eliminate replicate methods in the result of each failing test
	 * @param replicatedResult 
	 * @return
	 */
	private static String refineResult(String replicatedResult) {
		StringBuffer result = new StringBuffer();
		StringBuffer cl, mt;
		int counter = -1;
		String[] calls;
		String[] testMethods;
		//Get list of test classes
		String[] testClass = replicatedResult.trim().split("---");
		//For each class
		for (String tc : testClass) {
			if(!tc.isEmpty()){
				cl = new StringBuffer();
				//Get list of test methods in this class
				testMethods = tc.trim().split("Running ");
				//For each method
				for (String m : testMethods) {
					if(!m.trim().isEmpty()){
						mt = new StringBuffer();
						m = m.trim();
						//Get list of calls in this method
						calls = m.split("\n");
						//For each call
						for (String call : calls) {
							//TODO: Eliminate methods in test packages
							if(!call.trim().isEmpty() && mt.indexOf(call)==-1){
								mt.append(call).append("\n");
								counter++;
							}
						}
						cl.append(counter + "\nRunning " + mt).append("\n");
						counter = 0;
					}
				}
				result.append(cl).append("---\n");
			}
		}
		
		return result.toString();
	}
	/**
	 * Write post-processing result to file
	 * @param result
	 * @throws FileNotFoundException
	 */
	private static void writeResult(String result) throws FileNotFoundException {
		String finalResult = dataPath + "/result1.txt";
		if(Config.PHOSPHOR)
			finalResult = dataPath + "/result2.txt";
		try (PrintWriter out = new PrintWriter(finalResult)) {
			out.println(result);
		}
	}
}
