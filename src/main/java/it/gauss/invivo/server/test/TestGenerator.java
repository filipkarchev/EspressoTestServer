package it.gauss.invivo.server.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class TestGenerator {

	static String packageName="";
	static ArrayList<TestInfo> allConfigTests;

	public static void main(String[] args) {
		
		System.out.println("Welcome to the new DEX file generator.\n Press 1 to coninue:\n");

		//System.out.println("1 - Generate new DEX file for a configuration\n");
		// System.out.println("2 - Check Configuration's file name\n");
		Scanner scanner = new Scanner(System.in);
		String command = scanner.nextLine();
		switch (command) {
		case "1":
			try {
				generateNewDexFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case "2":
			checkFileName();
			break;
		case "3":
			try {
				// Runtime.getRuntime().exec("cp ./hash3/TestClass1* ./hash1");
				// Runtime.getRuntime().exec("cp /Users/teracomm/Documents/Trento/Research
				// Project/testing/TestClass1.txt ./hash1");
				// Runtime.getRuntime().exec("mkdir -p hash1/kur2/kur3");
				// Runtime.getRuntime().exec("jar cvf " + "hash1"+".jar " +
				// "eu/fbk/calc".split("/")[0],null,new File("hash1"+"/"));
				// Process proc = Runtime.getRuntime().exec("cp calc/GaussCalcTest2*
				// ./hash1/eu/fbk/calc");

				Runtime rt = Runtime.getRuntime();
				String[] commands = { "cp", "calc/GaussCalcTest2*", "./hash1/eu/fbk/calc" };
				Process proc = rt.exec(commands);

				BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

				BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

				// read the output from the command
				System.out.println("Here is the standard output of the command:\n");
				String s = null;
				while ((s = stdInput.readLine()) != null) {
					System.out.println(s);
				}

				// read any errors from the attempted command
				System.out.println("Here is the standard error of the command (if any):\n");
				while ((s = stdError.readLine()) != null) {
					System.out.println(s);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			System.out.println("Invalid command: " + command);
		}

		scanner.close();
	}

	private static void generateNewDexFile() throws IOException, InterruptedException {
		System.out.println("Enter the configuration hash: \n");
		Scanner scaner = new Scanner(System.in);
		String hash = scaner.nextLine();

		JsonLoader loader = new JsonLoader();
		allConfigTests = loader.init(Constants.jsonFileName);

//		if (allConfigTests != null && allConfigTests.contains(new TestInfo(hash))) {
//			System.out.println("Test file for this configuration has already been created. Press Y to continue");
//			String confirm = scaner.nextLine();
//			if (!confirm.equalsIgnoreCase("Y")) {
//				return;
//			}
//		}

		int index = allConfigTests.indexOf(new TestInfo(hash));
		if (index == -1) {
			System.out.println("There is No information for this configuration");
			scaner.close();
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
			//	System.out.println("name: "+ testName);
				if (file.getName().startsWith(testName)) {
					String command = "cp " + Constants.testFilesPath + "/" + file.getName() + " ./" + hash + "/" + packageName;
					System.out.println(command);
					Process proc = Runtime.getRuntime().exec(command);
					BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
					String s = null;
					while ((s = stdError.readLine()) != null) {
						System.out.println(s);
					}
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
		
		command = "mv classes_" + hash + ".dex testClasses/" + hash+ "/classes_" + hash + ".dex";
		System.out.println(command);
		Runtime.getRuntime().exec(command);

		Runtime.getRuntime().exec("rm " + hash + ".jar");
		Runtime.getRuntime().exec("rm -rf " + hash);

		scaner.close();

	}

	private static void checkFileName() {
		System.out.println("Enter the configuration hash: \n");
		Scanner scanner = new Scanner(System.in);
		String hash = scanner.nextLine();

		JsonLoader loader = new JsonLoader();
		allConfigTests = loader.init(Constants.jsonFileName);

		if (allConfigTests != null && allConfigTests.contains(new TestInfo(hash))) {
			TestInfo infoFound = allConfigTests.get(allConfigTests.indexOf(new TestInfo(hash)));

			String fileName = "";
			if (!fileName.equals("")) {
				System.out.println("File exists: " + fileName);
			} else {
				System.out.println("File does not exists 1\n");
			}
		} else {
			System.out.println("File does not exists 2\n");
		}

		scanner.close();
	}

	

	

	

}
