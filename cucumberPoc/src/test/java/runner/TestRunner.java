package runner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;



	//comment test
	
	@RunWith(Cucumber.class)
	@CucumberOptions(features="features",glue= {"stepDefination"},dryRun= true)
	
	public class TestRunner {
	
}
