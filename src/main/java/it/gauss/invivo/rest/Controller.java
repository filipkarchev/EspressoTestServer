package it.gauss.invivo.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.gauss.invivo.server.test.Constants;
import it.gauss.invivo.server.test.JsonLoader;
import it.gauss.invivo.server.test.TestInfo;

@RestController
public class Controller {
    
    @RequestMapping( value = "/classes_{id}.dex", method = RequestMethod.GET )
    public void getFile( @PathVariable( "id" )
    String hashId, HttpServletResponse response )
    {
        try
        {
        	 String workingDir = System.getProperty("user.dir");
             File temp = new File(workingDir + "/testClasses/" + hashId,"classes_"+hashId+".dex");
             
             System.out.println("file: " + temp.getAbsolutePath() + " exists: " + temp.exists());
             // get your file as InputStream
             InputStream is = new FileInputStream(temp);
             // copy it to response's OutputStream
             IOUtils.copy( is, response.getOutputStream() );
             response.flushBuffer();
        }
        catch( IOException ex )
        {
             throw new RuntimeException( "IOError writing file to output stream" );
        }

    }
    
    
    @RequestMapping("/config_{id}")
    public TestInfo configs( @PathVariable( "id" )
    String hashId) {
    	
    	JsonLoader loader = new JsonLoader();
    	ArrayList<TestInfo>allConfigTests = loader.init(Constants.jsonFileName);
    	int index = allConfigTests.indexOf(new TestInfo(hashId));
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
    
    
    @RequestMapping("/report_{id}")
    public String report(@PathVariable( "id" )
    String hashId, @RequestParam(value="report", defaultValue="") String report) {
    	System.out.println("New report comes hash: " + hashId + " value: " + report);
    	if(report.equals(""))
    	{
    		return "Wrong report";
    	}
    	
    	File fileReport = new File(Constants.reportsPath + hashId,"report_" + hashId + ".txt");
		
    	try {
			fileReport.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	
    	try (PrintWriter out = new PrintWriter(fileReport)) {
    	    out.println(report);
    	} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	
    	return "Report received";
    }
    
}
