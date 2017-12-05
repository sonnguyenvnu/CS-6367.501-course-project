package edu.utd.localization;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.utd.configuration.Config;
import edu.utd.testing.utils.Utils;

/**
 * FaultLocaltor is implemented the first fault localization strategy
 * 
 * @author sonnguyen
 *
 */
public class FaultLocator_v2 {
	static String dataPath;
	static int calls = 0;
	static int numOfmethods = 0;
	static int numOfRmethods = 0;
	static int numOfCases = 0;

	public static void main(String[] args) throws FileNotFoundException {
		for (int i = 1; i <= 27; i++) {
			System.out.println("Start: " + i);
			localize(i);
		}
		System.out.println(numOfCases);
		System.out.println(calls);
		System.out.println(numOfmethods);
		System.out.println(numOfRmethods);
	}

	/**
	 * Specify all methods might be buggy. The result is written to result1.txt.
	 * That includes the number of methods, failing test, and methods
	 * 
	 * @param bug
	 *            bug id
	 * @throws FileNotFoundException
	 */
	public static void localize(int bug) throws FileNotFoundException {
		dataPath = Config.DATA + "/time_bug_" + bug;
		List<String> triggers = Utils.extractTriggerFromFailingTest(dataPath);
		String resultPath = dataPath + "/result.txt";
		if (Config.PHOSPHOR)
			resultPath = dataPath + "/result_phosphor.txt";
		String result = Utils.getResultContent(resultPath);
		int start = 0;
		int end = 0;
		StringBuffer postResult = new StringBuffer();
		String methods;

		while (true) {
			start = result.indexOf("Running ", end);
			end = result.indexOf("Tests run: ", start);
			if (start == -1 || end == -1)
				break;
			methods = result.substring(start, end);
			postResult.append(methods).append("---\n");
		}
		result = updateResult(postResult.toString(), triggers);
		result = refineResult2(result, triggers);
		System.out.println(result);
		// writeResult(result);
		writeResult2(result, bug);
	}

	private static void writeResult2(String result, int bug) {
		String finalResult = "/Users/sonnguyen/Desktop/Experimental results/result_" + bug + ".txt";
		try (PrintWriter out = new PrintWriter(finalResult)) {
			out.println(result);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Eliminate replicate methods in the result of each failing test
	 * 
	 * @param replicatedResult
	 * @return
	 */
	private static String refineResult2(String result, List<String> triggers) {
		String r = "Method calls\tMethod\tAfter elimination\n";
		String[] files = result.split("---");
		for (String file : files) {
			if (!file.trim().isEmpty()) {
				String[] failureStatements = file.split("===");
				for (String part : failureStatements) {
					String[] methods = part.trim().split("\n");
					if (methods.length > 1) {
						List<Method> ms = DependencyFinder.extractMethods(methods);
						List<Method> suspiciousSet = DependencyFinder.findSuspiciousSet(ms);
						List<Method> suspiciousSet1 = DependencyFinder.findSuspiciousSet1(ms);
						calls += ms.size();
						numOfmethods += suspiciousSet1.size();
						numOfRmethods += suspiciousSet.size();
						numOfCases++;
						r += (ms.size() + "\t\t" + suspiciousSet1.size() + "\t" + suspiciousSet.size() + "\n");
					}
				}
			}
		}
		return r;
	}

	private static String updateResult(String result, List<String> triggers) {
		// for (String trigger : triggers) {
		// trigger = trigger.replace("::", ".");
		// result = result.replace(trigger+"\n", "Running "+ trigger+"\n");
		// }
		return result;
	}

	private static Set<Method> extractSuspiciousSet(List<Method> targets, Set<Method> methods) {
		Set<Method> result = new HashSet<Method>();
		for (Method target : targets) {
			// List<Method> dp = Test2.findDependencies(target, methods);
			// result.addAll(dp);
			result.add(target);
			for (Method method : target.dependencies) {
				List<Method> ms = DependencyFinder.findMethodByName(method, methods);
				result.addAll(ms);
			}
		}
		return result;
	}

	private static List<Method> getTarget(List<Method> methods) {
		List<Method> result = new ArrayList<Method>();
		result.add(methods.get(methods.size() - 1));
		for (int i = methods.size() - 2; i >= 0; i--) {
			if (result.size() == Config.MAX_TARGET)
				break;
			Method m = methods.get(i);
			if (!m.type.equals("void")) {
				result.add(m);
			}
		}
		return result;
	}

	/**
	 * Write post-processing result to file
	 * 
	 * @param result
	 * @throws FileNotFoundException
	 */
	private static void writeResult(String result) throws FileNotFoundException {
		String finalResult = dataPath + "/result1.txt";
		if (Config.PHOSPHOR)
			finalResult = dataPath + "/result2.txt";
		try (PrintWriter out = new PrintWriter(finalResult)) {
			out.println(result);
		}
	}
}
