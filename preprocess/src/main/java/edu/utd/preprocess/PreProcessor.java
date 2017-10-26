package edu.utd.preprocess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.utd.configuration.Config;

/**
 * Preprocessor The main purpose of Preprocessor is that updating pom.xlm in
 * data project and making it run with Java Agent. It includes the following
 * tasks:
 * <ul>
 * <li>Update Maven Surefire version to the version of 2.9. In this version, we
 * can narrow down the scope of test space, we only test a single test method
 * that is the trigger defined in defect4j.properties file. This function is not
 * supported in older version.</li>
 * <li>Update JUnit version to the version of 4.11</li>
 * <li>Extract trigger defined in defect4j.properties file</li>
 * <li>Update the configuration of maven surefire plugin: testing scope and java
 * agent command</li>
 * </ul>
 * 
 * @author sonnguyen
 */
public class PreProcessor {
	/**
	 * The path to buggy project
	 */
	private String dataPath;
	/**
	 * pom.xml object
	 */
	private Document pom;
	/**
	 * 
	 */
	private String triggers;

	public static void main(String[] args)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {
		for (int i = 6; i <= 27; i++) {
			System.out.println(Config.DATA + "/time_bug_" + i);
			PreProcessor preProcessor = new PreProcessor(Config.DATA + "/time_bug_" + i);
			preProcessor.process();
		}

	}

	/**
	 * Perform all tasks of Preprocessor
	 * 
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws TransformerException
	 */
	public void process() throws ParserConfigurationException, SAXException, IOException, TransformerException {
		updateSureFireVersion();
		updateJUnitVersion();
		triggers = extractTriggerFromFailingTest();
		// triggers = extractTriggerFromDefect4J();

		updateSureFireConfiguration(triggers);
		write(getFileByName(dataPath, "pom.xml"), true);
	}

	/**
	 * Get file path by name in specific folder
	 * 
	 * @param path
	 *            specific folder
	 * @param fileName
	 *            the name of file need to find
	 * @return absolute path of the file need to find
	 */
	private String getFileByName(String path, String fileName) {
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
	 * Update testing scope in surefire configuration by trigger
	 * 
	 * @param trigger
	 *            trigger extracted from defect4j.properties file
	 */
	private void updateSureFireConfiguration(String trigger) {
		NodeList dependencies = pom.getElementsByTagName("plugin");
		Element dependency = null;
		for (int i = 0; i < dependencies.getLength(); i++) {
			if (dependencies.item(i).getNodeType() == Node.ELEMENT_NODE) {
				dependency = (Element) dependencies.item(i);
				if (dependency.getElementsByTagName("artifactId").item(0).getTextContent()
						.equals("maven-surefire-plugin")) {
					Node config = dependency.getElementsByTagName("configuration").item(0);
					if (config != null && config.getNodeType() == Node.ELEMENT_NODE) {
						config.setTextContent("");
						Node test = pom.createElement("test");
						test.setTextContent(trigger);
						config.appendChild(test);
						Node argLine = pom.createElement("argLine");
						argLine.setTextContent(Config.AGENT_COMMAND);
						config.appendChild(argLine);
					}
					Node version = dependency.getElementsByTagName("version").item(0);
					if (version != null && version.getNodeType() == Node.ELEMENT_NODE) {
						version.setTextContent("");
						version.setTextContent(Config.SUREFIRE_VERSION);
					}
				}
			}
		}
	}

	/**
	 * Extract trigger from defect4j file
	 * 
	 * @return trigger (format: package/testclassname#testmethod
	 */
	private String extractTriggerFromDefect4J() {
		String defect4j = getFileByName(dataPath, Config.DEFECT4J_PROTERTIES);
		String trigger = null;
		try (BufferedReader br = new BufferedReader(new FileReader(defect4j))) {
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				if (sCurrentLine.contains("d4j.tests.trigger=")) {
					trigger = sCurrentLine.replace("d4j.tests.trigger=", "").replace(".", "/").replace("::", "#");
					break;
				}
			}
			System.out.println(trigger);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return trigger;
	}

	/**
	 * Update JUnit version up to 4.11
	 */
	private void updateJUnitVersion() {
		NodeList dependencies = pom.getElementsByTagName("dependencies").item(0).getChildNodes();
		Element dependency = null;
		for (int i = 0; i < dependencies.getLength(); i++) {
			if (dependencies.item(i).getNodeType() == Node.ELEMENT_NODE) {
				dependency = (Element) dependencies.item(i);
				if (dependency.getElementsByTagName("artifactId").item(0).getTextContent().equals("junit")) {
					dependency.getElementsByTagName("version").item(0).setTextContent(Config.JUNIT_VERSION);
				}
			}
		}
	}

	/**
	 * Update SureFire version up to 2.9
	 */
	private void updateSureFireVersion() {
		try {
			pom.getElementsByTagName("maven-surefire-plugin.version").item(0).setTextContent(Config.SUREFIRE_VERSION);
		} catch (Exception e) {

		}
	}

	/**
	 * Initiate the value of pom
	 * 
	 * @param dataPath
	 */
	public PreProcessor(String dataPath) {
		this.dataPath = dataPath;
		String pomFile = getFileByName(dataPath, "pom.xml");
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			pom = docBuilder.parse(pomFile);
			pom.getDocumentElement().normalize();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Write updated pom file back buggy project path
	 * 
	 * @param pomPath
	 *            pom path
	 * @param backup
	 *            need to backup the original pom file
	 * @throws TransformerException
	 * @throws IOException
	 */
	public void write(String pomPath, boolean backup) throws TransformerException, IOException {
		File p = new File(pomPath);
		File p_backup = new File(pomPath + ".backup");
		if (backup)
			Files.copy(p.toPath(), p_backup.toPath(), StandardCopyOption.REPLACE_EXISTING);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(pom);
		StreamResult result = new StreamResult(p);
		transformer.transform(source, result);
	}

	public String extractTriggerFromFailingTest() {
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
		System.out.println(trigger);
		return trigger;
	}

	public String getTrigger() {
		return triggers;
	}
}
