package edu.utd.testing.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.utd.configuration.Config;

public class Utils {
	public static String extractTriggerFromFailingTest_backup(String dataPath) {
		String defect4j = getFileByName(dataPath, Config.FAILING_TEST_FILE);
		String trigger = null;
		try (BufferedReader br = new BufferedReader(new FileReader(defect4j))) {
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				if (sCurrentLine.contains("--- ")) {
					if (trigger == null)
						trigger = sCurrentLine.replace("--- ", "").replace(".", "/").replace("::", "#");
					else
						trigger += ", " + sCurrentLine.replace("--- ", "").replace(".", "/").replace("::", "#");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return trigger;
	}
	public static List<String> extractTriggerFromFailingTest(String dataPath) {
		String defect4j = getFileByName(dataPath, Config.FAILING_TEST_FILE);
		List<String> triggers = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(defect4j))) {
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				if (sCurrentLine.contains("--- ")) {
					triggers.add(sCurrentLine.replace("--- ", ""));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return triggers;
	}
	/**
	 * Get file path by name in specific folder
	 * 
	 * @param path specific folder
	 * @param fileName the name of file need to find
	 * @return absolute path of the file need to find
	 */
	public static String getFileByName(String path, String fileName) {
		String pom = null;
		File project = new File(path);
		if (project.isDirectory()) {
			File[] children = project.listFiles();
			for (File child : children) {
				if (child.getName().equals(fileName)) {
					pom = child.getAbsolutePath();
					break;
				}
			}
		}
		return pom;
	}
	/**
	 * Extract trigger from defect4j file
	 * 
	 * @return trigger (format: package/testclassname#testmethod
	 */
	public String extractTriggerFromDefect4J(String dataPath) {
		String defect4j = Utils.getFileByName(dataPath, Config.DEFECT4J_PROTERTIES);
		String trigger = null;
		try (BufferedReader br = new BufferedReader(new FileReader(defect4j))) {
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				if (sCurrentLine.contains("d4j.tests.trigger=")) {
					trigger = sCurrentLine.replace("d4j.tests.trigger=", "").replace(".", "/").replace("::", "#");
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return trigger;
	}
	public static void main(String[] args){
		List<String> ts = extractTriggerFromFailingTest(Config.DATA + "/time_bug_" + 1);
		System.out.println(ts.size());
	}
	/**
	 * Read the raw result
	 * @param f the path of file containing raw result
	 * @return raw result
	 */
	public static String getResultContent(String f) {
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
