package edu.utd.evaluation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
/**
 * 
 * @author sonnguyen
 *
 */
public class Eval {
	static String resultFile = "/Users/sonnguyen/Desktop/log.txt";
	static String trigger = "org/joda/time/TestPartial_Constructors#testConstructorEx7_TypeArray_intArray";
	static int numOfMethod = 0;

	public static void main(String[] args) throws FileNotFoundException {
		String result = getResultContent(resultFile);
		String triggerClass = getPostProcessTrigger(trigger);
		int start = result.indexOf(triggerClass) + triggerClass.length();
		int end = result.indexOf("Tests run: ");
		result = result.substring(start, end);
		writeResult(numOfMethod, result.trim());
	}

	private static void writeResult(int numOfMethod, String result) throws FileNotFoundException {
		try (PrintWriter out = new PrintWriter(resultFile)) {
			out.println(numOfMethod + "\n" + result);
		}
	}

	private static String getPostProcessTrigger(String t) {
		int sharp = t.indexOf("#");
		return t.substring(0, sharp).replace("/", ".");
	}

	private static String getResultContent(String f) {
		StringBuffer content = new StringBuffer();
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				if (!sCurrentLine.trim().isEmpty() && content.indexOf(sCurrentLine) == -1) {
					content.append(sCurrentLine).append("\n");
					numOfMethod++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content.toString();
	}

}
