package abs.quote.feature;

import org.testng.annotations.Test;

import abs.combine.step.Quote;
import abs.framework.base.BaseTest;


import org.testng.annotations.BeforeMethod;

import java.util.LinkedHashMap;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

public class FEQ50_51_46 extends BaseTest {

	String TestTitle = "Scenario : Validate Navigation";
	Quote quote;
	
	@BeforeClass
	public void createLogger() {
		logger = extent.createTest(TestTitle);
	}

	@Test
	public void validateNaviagtion() {
		LinkedHashMap<String, String> requestorDetails;
		
		try {
			
	
		quote.loginPortal();
		quote.navigateToRequestToQuote();
		quote.requesterInfoRFQValidate();
		quote.backButtonRFQValidation();
		quote.backButtonRFQValidation();
		quote.quoteOverviewBreadCrubsValidation();
				
		}catch(Exception e) {
			
			logger.error(e.getMessage());
			
		}
			
	}

	@BeforeMethod
	public void beforeMethod() {
		quote = new Quote();
		
		
	}

	@AfterMethod
	public void afterMethod() {
	}

}
