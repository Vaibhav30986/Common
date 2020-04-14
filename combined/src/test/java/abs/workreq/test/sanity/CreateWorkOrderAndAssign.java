package abs.workreq.test.sanity;

import org.testng.annotations.Test;

import abs.combine.step.Common;
import abs.combine.step.WorkRequest;
import abs.framework.base.BaseTest;
import abs.framework.lib.Excel;
import abs.framework.logger.ExtentReportClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import java.util.LinkedHashMap;
import java.util.List;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

public class CreateWorkOrderAndAssign extends BaseTest {

	String TestTitle = "Scenario : Freedom Login to INT";

	WorkRequest workRequest;
	Common common;
	
	
	ExtentReportClass extentReportClass;
	
	
	// WebDriver driver;

	@BeforeClass
	public void createLogger() {
		logger = extent.createTest(TestTitle);
	}

	@BeforeMethod
	public void beforeMethod() {
		
		//Step Class Object Initialization
		workRequest = new WorkRequest();
		common = new Common();

		//Steps
		common.loginFreedom();
	}

	@Test(dataProvider = "SearchProvider")
	public void CreateworkOrderflow(LinkedHashMap<String, String> TestData ){

		try {
			
			common.navigateToWorkOrder();
			workRequest.selectWorkRequestType(TestData.get("RequestType"));
			workRequest.searchVessel(TestData.get("Vessel"));
			workRequest.selectVesselFromResult(TestData.get("Vessel"));
			workRequest.fillRequestOverviewCard();
			workRequest.fillLocationCard();
			workRequest.selectSurvey(TestData.get("Survey"));
			workRequest.clkAssignToMe();
			workRequest.validateWOState();
		
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	@DataProvider(name = "SearchProvider")
	public Object[][] getDataFromTestData() {

		List<LinkedHashMap<String, String>> data = Excel
				.excelReadHashMap(System.getProperty("user.dir") + "//src//test//resources//test data//TestData.xlsx", "WF1", "Execution_Status", "Y");

		int rows = data.size();
		Object[][] datasetRnR = new Object[rows][1];

		for (int i = 0; i < rows; i++) {
			LinkedHashMap<String, String> map = data.get(i);
			// String td = map.get("TC_ID");
			// map.remove("TC_ID");
			datasetRnR[i][0] = map;
			// datasetRnR[i][1] = map;
		}

		return datasetRnR;

	}


	@AfterMethod
	public void afterMethod() {

		common.logout();
}

}
