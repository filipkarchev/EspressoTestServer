package it.gauss.invivo.server.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonLoader {
	public ArrayList<TestInfo> init(String jsonFileName) {
		if (checkIfListExists(jsonFileName) == false) {
			createNewTestFile(jsonFileName);
		}

		return readTestInfoFromFile(jsonFileName);
	}
	
	
	private ArrayList<TestInfo> readTestInfoFromFile(String jsonFileName) {

		String fileTextStr = readTextFromFile(jsonFileName);
		JSONArray json;
		if (fileTextStr.equals("")) {
			json = new JSONArray();
		} else {
			json = new JSONArray(fileTextStr);
		}

		// System.out.println("JSON is: " + json.toString());

		ArrayList<TestInfo> fullTestsList = new ArrayList<TestInfo>();
		for (int i = 0; i < json.length(); i++) {
			JSONObject newTest = json.getJSONObject(i);
			TestInfo info = new TestInfo();
			info.setHash(newTest.getString("hash"));
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

	private void createNewTestFile(String jsonFileName) {
		File file = new File(jsonFileName);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// Check if the json file with the test classes information exist
	private boolean checkIfListExists(String jsonFileName) {
		File file = new File(jsonFileName);
		return file.exists();
	}
}
