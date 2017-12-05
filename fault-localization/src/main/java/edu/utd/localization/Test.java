package edu.utd.localization;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import edu.utd.configuration.Config;
import edu.utd.testing.utils.Utils;

/**
 * FaultLocaltor is implemented the first fault localization strategy 
 * @author sonnguyen
 *
 */
public class Test {
	static String dataPath;
	
	public static void main(String[] args) throws FileNotFoundException{
		for (int i = 0; i < 27; i++) {
			localize(i+1);
		}
	}
	public static void localize(int bug) throws FileNotFoundException {
		dataPath = Config.DATA + "/time_bug_"+bug;
		String result = Utils.getResultContent(dataPath + "/result1.txt");
		List<String> temp = new ArrayList<String>();
		String[] lines = result.split("\n");
		for (String line : lines) {
			if(line.length() > 10 && !line.contains("Running ") && !temp.contains(line))
				temp.add(line);
		}
		System.out.println(temp.size());
	}
}
