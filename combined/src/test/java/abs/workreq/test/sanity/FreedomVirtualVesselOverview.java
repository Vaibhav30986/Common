package abs.workreq.test.sanity;

import org.testng.annotations.Test;

import abs.combine.step.Common;
import abs.combine.step.VirtualVessel;
import abs.framework.base.BaseTest;
import abs.framework.lib.Excel;
import abs.framework.logger.ExtentReportClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import java.util.LinkedHashMap;
import java.util.List;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

public class FreedomVirtualVesselOverview extends BaseTest {

	String TestTitle = "Scenario : Virtual Vessel Designation Validation";
//	FreedomLogin login;
//	NavigateToMenus navigate;
	// CreateWorkOrder createWorkOrder;
	ExtentReportClass extentReportClass;
	VirtualVessel virtualVessel;
	Common common;

	@BeforeClass
	public void createLogger() {
		logger = extent.createTest(TestTitle);
	}

	@BeforeMethod
	public void beforeMethod() throws InterruptedException {

	//	login = new FreedomLogin();
	//	navigate = new NavigateToMenus();
		virtualVessel = new VirtualVessel();
		common = new Common();
		
		
		common.loginFreedom();
		common.navigateToVirtualVessel();

	}

	@Test(dataProvider = "SearchVirtualVessel")
	public void SrchSlctVessel(LinkedHashMap<String, String> TestData) throws InterruptedException {

		try {


			virtualVessel.searchVessel(TestData.get("ClassNum"));
			virtualVessel.selectSrchRslt(TestData.get("ClassNum"));
			virtualVessel.ValidateVesselOverviewTab();
			
			//String actualIMONum = virtualVessel.getIMONumber();
			//String actualFlagState = virtualVessel.getFlagState();

			virtualVessel.vesselIMONumberValidate(virtualVessel.getIMONumber(), TestData.get("IMO Number"));
			virtualVessel.vesselFlagStateValidate(virtualVessel.getFlagState(), TestData.get("Flag State"));
		} catch (Exception e) {

			logger.error(e.getMessage());
		}

	}

	@DataProvider(name = "SearchVirtualVessel")
	public Object[][] getDataFromTestData() {

		List<LinkedHashMap<String, String>> data = Excel.excelReadHashMap(
				System.getProperty("user.dir") + "//src//test//resources//test data//TestData.xlsx", "WF2", "Execution_Status", "Y");

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
