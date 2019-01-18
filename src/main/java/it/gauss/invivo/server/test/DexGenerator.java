package it.gauss.invivo.server.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

/**
 * This Class Generates .dex file from available instrumentation classes in given directory.
 * To use it, First update the information inside the Constants.java
 * This file can be used only for Unix based systems due to the existence of system commands
 * @author Karchev
 *
 */
public class DexGenerator {

	static String packageName = "";
	static ArrayList<TestInfo> allConfigTests;

	public static void main(String[] args) {

		try {
			generateNewDexFile(Constants.hash);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void generateNewDexFile(String hash) throws IOException, InterruptedException {

		//Read the information available in the file and obtain list of the tests
		JsonLoader loader = new JsonLoader();
		allConfigTests = loader.init(Constants.jsonFileName);

		//Get the position of the configuration that has the same hash
		int index = allConfigTests.indexOf(new TestInfo(hash));
		if (index == -1) {
			System.out.println("There is No information for this configuration");
			return;
		}

		//Convert the package name of the application to path
		packageName = Constants.packageName.replaceAll("\\.", "/");

		//Get the element in the list with the given hash
		TestInfo infoFound = allConfigTests.get(index);

		// Clear the content inside this directory
		Runtime.getRuntime().exec("rm -rf " + hash);
		Runtime.getRuntime().exec("mkdir -p " + hash + "/" + packageName);
		Runtime.getRuntime().exec("mkdir -p " + Constants.reportsPath + hash);

		// Copy all files with that name inside the created directory
		File folder = new File(Constants.testFilesPath);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
			for (String testName : infoFound.getTestClasses()) {
				if (file.getName().startsWith(testName) || file.getName().startsWith("IMG")) {
					//Copy all the files that should be tested for this configuration on new temp location
					
					File newFile = new File(hash + "/"+ packageName+"/" + file.getName());
					System.out.println("New destination is: " + newFile.getAbsolutePath());
					FileUtils.copyFile(file,newFile);

				}
			}

		}

		// Create jar file from the selected classes
		Process jarProcess = Runtime.getRuntime().exec("jar cvf " + hash + ".jar " + packageName.split("/")[0], null,
				new File(hash + "/"));
		jarProcess.waitFor();

		//Copy the jar in the corresponding location
		String command = "cp " + hash + "/" + hash + ".jar .";
		System.out.println(command);
		Runtime.getRuntime().exec(command);

		//Use the dx tool to generate .dex file from the jar
		Process dexProcess = Runtime.getRuntime()
				.exec(Constants.dxPath + "dx --dex --verbose --output=classes_" + hash + ".dex " + hash + ".jar");
		dexProcess.waitFor();

		//Move the .dex file in the corresponding location for that configuration
		command = "mv classes_" + hash + ".dex "+ Constants.testFilesPath + hash + "/classes_" + hash + ".dex";
		System.out.println(command);
		Runtime.getRuntime().exec(command);

		//Delete the temp folders
		Runtime.getRuntime().exec("rm " + hash + ".jar");
		Runtime.getRuntime().exec("rm -rf " + hash);


	}

}
