package edu.utd.localization;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import edu.utd.configuration.Config;

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
		String result = getResultContent(dataPath + "/result.txt");
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
			methods = eliminateReplicateMethod(methods);
			postResult.append(methods).append("\n");
		}
		writeResult(postResult.toString());
	}
	/**
	 * Eliminate replicate methods in the result of each failing test
	 * @param methods 
	 * @return
	 */
	private static String eliminateReplicateMethod(String methods) {
		StringBuffer result = new StringBuffer();
		String[] methodsText = methods.trim().split("\n");
		int counter = -1;
		for (String m : methodsText) {
			if(!m.isEmpty() && result.indexOf(m)==-1){
				result.append(m).append("\n");
				counter++;
			}
		}
		return counter+"\n"+result.toString();
	}
	/**
	 * Write post-processing result to file
	 * @param result
	 * @throws FileNotFoundException
	 */
	private static void writeResult(String result) throws FileNotFoundException {
		try (PrintWriter out = new PrintWriter(dataPath + "/result1.txt")) {
			out.println(result);
		}
	}
	/**
	 * Read the raw result
	 * @param f the path of file containing raw result
	 * @return raw result
	 */
	private static String getResultContent(String f) {
		StringBuffer content = new StringBuffer();
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				if (!sCurrentLine.trim().isEmpty()) 
					content.append(sCurrentLine).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content.toString();
	}
}
