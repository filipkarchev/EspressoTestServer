package it.gauss.invivo.server.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

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

		JsonLoader loader = new JsonLoader();
		allConfigTests = loader.init(Constants.jsonFileName);

		int index = allConfigTests.indexOf(new TestInfo(hash));
		if (index == -1) {
			System.out.println("There is No information for this configuration");
			return;
		}

		packageName = Constants.packageName.replaceAll("\\.", "/");

		TestInfo infoFound = allConfigTests.get(index);

		// Clear the content inside this directory
		Runtime.getRuntime().exec("rm -rf " + hash);
		Runtime.getRuntime().exec("mkdir -p " + hash + "/" + packageName);
		Runtime.getRuntime().exec("mkdir -p " + "testClasses/" + hash);

		// Copy all files with that name inside the created directory
		File folder = new File(Constants.testFilesPath);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
			 //System.out.println("file: "+ file.getName());
			for (String testName : infoFound.getTestClasses()) {
				 //System.out.println("name: "+ testName);
				if (file.getName().startsWith(testName) || file.getName().startsWith("IMG")) {
					String command = "cp " + Constants.testFilesPath + "/" + file.getName() + " ./" + hash + "/"
							+ packageName;
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

		String command = "cp " + hash + "/" + hash + ".jar .";
		System.out.println(command);
		Runtime.getRuntime().exec(command);

		Process dexProcess = Runtime.getRuntime()
				.exec(Constants.dxPath + "dx --dex --verbose --output=classes_" + hash + ".dex " + hash + ".jar");
		dexProcess.waitFor();

		command = "mv classes_" + hash + ".dex testClasses/" + hash + "/classes_" + hash + ".dex";
		System.out.println(command);
		Runtime.getRuntime().exec(command);

		Runtime.getRuntime().exec("rm " + hash + ".jar");
		Runtime.getRuntime().exec("rm -rf " + hash);


	}

}
