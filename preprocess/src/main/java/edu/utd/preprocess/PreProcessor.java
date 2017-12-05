package edu.utd.preprocess;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

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
import edu.utd.testing.utils.Utils;

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

	public static void main(String[] args)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {
		for (int i = 1; i <= 27; i++) {
			System.out.println(Config.DATA + "/time_bug_" + i);
			PreProcessor preProcessor = new PreProcessor(Config.DATA + "/time_bug_" + i);
			preProcessor.process(Config.PHOSPHOR);
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
	public void process(boolean phosphor) throws ParserConfigurationException, SAXException, IOException, TransformerException {
		updateSureFireVersion();
		updateJUnitVersion();
//		String triggers = Utils.extractTriggerFromFailingTest(dataPath);
		List<String> triggers = Utils.extractTriggerFromFailingTest(dataPath);
		// triggers = extractTriggerFromDefect4J();
		
		updateSureFireConfiguration(getConfigTriggers(triggers), phosphor);
		write(Utils.getFileByName(dataPath, "pom.xml"), true);
	}


	private String getConfigTriggers(List<String> triggers) {
		String ts = "";
		for (String t : triggers) {
			t = t.replace(".", "/").replace("::", "#");
			if (ts.isEmpty())
				ts = t;
			else
				ts = ts+ ", " + t;
		}
		return ts;
	}

	/**
	 * Update testing scope in surefire configuration by trigger
	 * 
	 * @param trigger trigger extracted from defect4j.properties file
	 */
	private void updateSureFireConfiguration(String trigger, boolean phosphor) {
		NodeList dependencies = pom.getElementsByTagName("plugin");
		String agent = Config.AGENT_COMMAND;
		if(phosphor)
			agent = Config.AGENT_COMMAND_PHOSPHOR;
		Element dependency = null;
		for (int i = 0; i < dependencies.getLength(); i++) {
			if (dependencies.item(i).getNodeType() == Node.ELEMENT_NODE) {
				dependency = (Element) dependencies.item(i);
				if (dependency.getElementsByTagName("artifactId").item(0).getTextContent()
						.equals("maven-surefire-plugin")) {
					Node config = dependency.getElementsByTagName("configuration").item(0);
					if (config != null && config.getNodeType() == Node.ELEMENT_NODE) {
						config.setTextContent("");
						if(phosphor){
							Node jvm = pom.createElement("jvm");
							jvm.setTextContent(Config.JRE_PHOSPHOR);
							config.appendChild(jvm);
//							Node jvm = pom.createElement("jvm");
//							jvm.setTextContent(Config.JRE_PHOSPHOR);
//							config.appendChild(jvm);
						}
						Node test = pom.createElement("test");
						System.out.println(trigger);
						test.setTextContent(trigger);
						config.appendChild(test);
						Node argLine = pom.createElement("argLine");
						argLine.setTextContent(agent);
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
		String pomFile = Utils.getFileByName(dataPath, "pom.xml");
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
	 * @param pomPath pom path
	 * @param backup need to backup the original pom file
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
}
