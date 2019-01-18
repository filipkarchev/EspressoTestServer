package it.gauss.invivo.server.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 
 * @author Karchev
 *
 */
public class JsonLoader {
	
	/**
	 * Read the information in the configuration file and parse it to list of TestInfo
	 * @param jsonFileName	Path to the configuration file that contains which tests should be run for every configuration
	 * @return	list of all available configurations and the tests for them
	 */
	public ArrayList<TestInfo> init(String jsonFileName) {
		if (checkIfListExists(jsonFileName) == false) {
			createNewTestFile(jsonFileName);
		}

		return readTestInfoFromFile(jsonFileName);
	}
	
	/**
	 * Read the information in the configuration file and parse it to list of TestInfo
	 * @param jsonFileName	Path to the configuration file that contains which tests should be run for every configuration
	 * @return	list of all available configurations and the tests for them
	 */
	private ArrayList<TestInfo> readTestInfoFromFile(String jsonFileName) {

		//Get the text in the file and parse it to json object
		String fileTextStr = readTextFromFile(jsonFileName);
		JSONArray json;
		if (fileTextStr.equals("")) {
			json = new JSONArray();
		} else {
			json = new JSONArray(fileTextStr);
		}

		//Iterate inside the json and create a list of all the configuration and the information about them
		ArrayList<TestInfo> fullTestsList = new ArrayList<TestInfo>();
		for (int i = 0; i < json.length(); i++) {
			JSONObject newTest = json.getJSONObject(i);
			TestInfo info = new TestInfo();
			info.setHash(newTest.getString("hash"));
			
			//Get all test that should be run for that configuration
			JSONArray tests = newTest.optJSONArray("testClasses");
			ArrayList<String> classes = new ArrayList<>();
			for (int j = 0; j < tests.length(); j++) {
				String newClass = tests.getString(j);
				classes.add(newClass);
			}
			info.setTestClasses(classes);

			fullTestsList.add(info);
		}

		return fullTestsList;
	}
	
	/**
	 * Read the content of a file
	 * @param jsonFileName	Path to the file
	 * @return	String with the file conent
	 */
	private String readTextFromFile(String jsonFileName) {

		try {
			BufferedReader br = new BufferedReader(new FileReader(jsonFileName));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			String everything = sb.toString();
			return everything;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";

	}

	/**
	 * Creates new file on that location if it does not exist
	 * @param jsonFileName Path to the file
	 */
	private void createNewTestFile(String jsonFileName) {
		File file = new File(jsonFileName);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 *  Check if the json file with the test classes information exist
	 * @param jsonFileName	Path to the file
	 * @return	true if the file exists
	 */
	private boolean checkIfListExists(String jsonFileName) {
		File file = new File(jsonFileName);
		return file.exists();
	}
}
