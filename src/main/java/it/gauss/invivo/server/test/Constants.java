package it.gauss.invivo.server.test;

public class Constants {
	// UPDATE THIS INFORMATION !!!!!!
	
	//The path to the file that contains information which tests should be run for every configuration
	public final static String jsonFileName = System.getProperty("user.dir") + "/src/main/resources/testJson.txt";
	
	//The path to the dx tool from the Android SDK, which will be used to create the dex file.
	//Leave dxPath empty if you have it installed
	public final static String dxPath = "/Users/teracomm/Library/Android/sdk/build-tools/28.0.3/";
	
	//The path to the folder which contains the compiled Instrumentaion tests
	public static final String testFilesPath = "/Users/teracomm/Documents/Trento/Research Project/Signal-Android/build/intermediates/classes/androidTest/play/debug/org/thoughtcrime/securesms/";
	//public static final String testFilesPath = "/Users/teracomm/Documents/Trento/Research Project/android-accessibility/GaussCalc/app/build/intermediates/classes/release/eu/fbk/calc/";
	
	//The package name of the Android application
	public final static String packageName = "org.thoughtcrime.securesms";
	//public final static String packageName = "eu.fbk.calc";
	
	//The path where the files and reports will be created
	public final static String reportsPath = "testClasses/";
	
	//An example hash of a configuration that is used.
	//TODO Add this value as a parameter that will be send to the DexGenerator
	public static final String hash = "b596e16c34cd9d5e553f93fe24a62afcff682b2eae9b73955d0bba9595847434";
}
