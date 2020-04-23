package runner;

import org.junit.runner.RunWith;
import org.omg.Messaging.SyncScopeHelper;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;



	//Garima is doing good job
	
	@RunWith(Cucumber.class)
	@CucumberOptions(features="features",glue= {"stepDefination"},dryRun= true)
	
	public class TestRunner {
    
}
