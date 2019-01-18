package it.gauss.invivo.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.gauss.invivo.server.test.Constants;
import it.gauss.invivo.server.test.JsonLoader;
import it.gauss.invivo.server.test.TestInfo;

@RestController
public class Controller {
  
    
	/**
	 * Endpoint to get the DEX file for given configuration
	 * @param request	The request where the inputStream could be used to get the files. Currently not used
	 * @param response	Object to get the outputStream where the DEx file will be send
	 * @param config	The Json representation of the obtained device Configuration
	 */
    @RequestMapping( value = "/get_test_classes")
    public void getTestFile( final HttpServletRequest request, HttpServletResponse response 
    		, @RequestParam(value="config", defaultValue="") String config) 
    {
    	System.out.println("New config comes: " + config);
        try
        {
        	//Compute the hash of the configuration => The order in the json is important!
        	String hexHash = computeHash(config);
        	
        	//String hexHash = getHashOfConfiguration(request);   
            System.out.println("Json hash is: " + hexHash);
            
            //Find the corresponding path and file for that configuration hash
            String workingDir = System.getProperty("user.dir");
            File temp = new File(workingDir + "/testClasses/" + hexHash,"classes_"+hexHash+".dex");
            
            System.out.println("file: " + temp.getAbsolutePath() + " exists: " + temp.exists());
            // get your file as InputStream
            InputStream fis = new FileInputStream(temp);
            // copy it to response's OutputStream
            IOUtils.copy( fis, response.getOutputStream() );
            response.flushBuffer();
          
        }
        catch(Exception ex )
        {
        	ex.printStackTrace();
            System.out.println("Error handeling this request");
        }

    }
    
    /**
     * Method that reads input and computes the hash of it
     * @param request	The request where we will read the input from
     * @return	The computes hash
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    private String getHashOfConfiguration(HttpServletRequest request) throws IOException, NoSuchAlgorithmException
    {
    	//Get the configuration json file from the input stream
    	InputStream is = request.getInputStream();
    
    	//Convert the input stream to String
    	StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        
        //Convert the String input to Json Object if you want to check the insight
        //JSONObject json = new JSONObject(sb.toString());
        
        System.out.println("received json: " +  sb.toString());
        
       return computeHash(sb.toString());
    }
    
    /**
     * Compute the hash of given String
     * @param text The string which we will use to compute the hash.
     * @return
     * @throws NoSuchAlgorithmException
     */
    private String computeHash(String text) throws NoSuchAlgorithmException
    {
    	//Compute the hash of the received json String
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(text.getBytes(StandardCharsets.UTF_8));
        byte[] digest = md.digest();
        String hashHex = String.format("%064x", new BigInteger(1, digest));
        
        return hashHex;
    }
    
    
    /**
     * Return all the tests from given configuration as a json
     * @param config	The configuration which tests will be shown
     * @return	TestInfo object that will be represented as json
     */
    @RequestMapping("/get_config_file")
    public TestInfo configs(@RequestParam(value="config", defaultValue="") String config) {
    	
    	//Compute the hash of that configuration
    	String hexHash="";
    	try {
    	 hexHash = computeHash(config);
    	}catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	
    	//Read the file for all the configurations and extract the desired one
    	JsonLoader loader = new JsonLoader();
    	ArrayList<TestInfo>allConfigTests = loader.init(Constants.jsonFileName);
    	int index = allConfigTests.indexOf(new TestInfo(hexHash));
		if (index == -1) {
			System.out.println("There is No information for this configuration");
			return null;
		}

		//Add the package name as path of the class
		TestInfo infoFound = allConfigTests.get(index);
		ArrayList<String> listClasses = new ArrayList<String>();
		for(String className :infoFound.getTestClasses())
		{
			listClasses.add(Constants.packageName + "." + className);
		}
		infoFound.setTestClasses(listClasses);
		
		return infoFound;
    }
    
    /**
     * The endpoint where the application will return the report of the tests execution
     * @param report	The json representation of the report containing all tests and the result from them.
     * @param config	The json representation of the configuration that was tested
     * @return	Data is the report was received properly
     */
    @RequestMapping("/send_report")
    public String report(@RequestParam(value="report", defaultValue="") String report,
    		@RequestParam(value="config", defaultValue="") String config) {
    	
    	//Compute the hash of that configuration
    	String hashId="";
    	try {
    		hashId = computeHash(config);
    	}catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	
    	System.out.println("New report comes hash: " + hashId + " value: " + report);
    	if(report.equals(""))
    	{
    		return "Wrong report";
    	}
    	
    	//Create new file to save that report
    	File fileReport = new File(Constants.reportsPath + hashId,"report_" + hashId + ".txt");
		
    	try {
			fileReport.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	
    	//Write the information in the file
    	try (PrintWriter out = new PrintWriter(fileReport)) {
    	    out.println(report);
    	} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	
    	return "Report received";
    }
    
}
