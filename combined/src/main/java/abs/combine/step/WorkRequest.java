package abs.combine.step;

import java.io.IOException;


import org.openqa.selenium.JavascriptExecutor;


import com.aventstack.extentreports.MediaEntityBuilder;

import abs.combined.page.CommonPage;
import abs.combined.page.WorkRequestPage;
import abs.framework.base.BasePage;
import abs.framework.logger.ScreenShots;


public class WorkRequest extends BasePage {
	WorkRequestPage workRequestPage;
	CommonPage commonPage;

	public WorkRequest() {
		
		workRequestPage = new WorkRequestPage();
		commonPage = new CommonPage();
	}

	/**
	 * selectWorkRequestType
	 *
	 * @name selectWorkRequestType
	 * @description Select work Request type 
	 * @author Vaibhav Narkhede
	 * @param  WorkReqType ||description: Valid WorkReq type
	 *                 ||allowedRange:
	 * @return void ||description:
	 * @jiraId
	 */
	
	public void selectWorkRequestType(String WorkReqType) {

		try {
			logger.info("Drilling down the work Request Dropdown");
			click(workRequestPage.selectWorkOrderDownArrow);
			logger.info("Selecting Work Request : " + WorkReqType);
			workRequestPage.selectWorkRequestTypeFromDrpDwn(WorkReqType);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	
	/**
	 * searchVessel
	 *
	 * @name searchVessel
	 * @description Search Vessel from Result 
	 * @author Vaibhav Narkhede
	 * @param  Vessel ||description: Vessel to be searched
	 *                 ||allowedRange:
	 * @return void ||description:
	 * @jiraId
	 */
	public void searchVessel(String Vessel) {

		try {

			logger.info("Selecting vessel : " + Vessel);

			sendKeys(workRequestPage.searchVesselField, Vessel);
			// driver.findElement(createWorkRequest.searchVesselField).sendKeys(sVesselNum);
			logger.info("Searching Vessel");

			click(workRequestPage.clickVesselSearch);
			// driver.findElement(createWorkRequest.clickVesselSearch).click();
		} catch (Exception e) {

			logger.error(e.getMessage());

		}

	}

	
	/**
	 * selectVesselFromResult
	 *
	 * @name selectVesselFromResult
	 * @description Select Vessel from result set
	 * @author Vaibhav Narkhede
	 * @param  Vessel ||description: Vessel to be selected
	 *                 ||allowedRange:
	 * @return void ||description:
	 * @jiraId
	 */
	public void selectVesselFromResult(String Vessel) throws InterruptedException {
		
	
		click(workRequestPage.selectVesselFromSrchRslt(Vessel));
		click(workRequestPage.clkCreateNewRequest);
		logger.info(Vessel + " vessel selected & Create New Request button has been clicked");
	}

	
	/**
	 * fillRequestOverviewCard
	 *
	 * @name fillRequestOverviewCard
	 * @description Fill Request Overview card
	 * @author Vaibhav Narkhede
	 * @param   ||description: 
	 *                 ||allowedRange:
	 * @return void ||description:
	 * @jiraId
	 */
	public void fillRequestOverviewCard() {

		sendKeys(workRequestPage.poNumbe, "525213");
		logger.info("PO Number enetered");
	}

	
	/**
	 * fillLocationCard
	 *
	 * @name fillLocationCard
	 * @description Fill location card 
	 * @author Vaibhav Narkhede
	 * @param   ||description: 
	 *                 ||allowedRange:
	 * @return void ||description:
	 * @jiraId
	 */
	public void fillLocationCard() {

		// Location Field
		click(workRequestPage.surveyLoactionDrpDwn);
		click(workRequestPage.selectLocation);
		logger.info("Location enetered");

		// Request Name
		click(workRequestPage.clkRequesterName);
		sendKeys(workRequestPage.searchRequester, "a");
		click(workRequestPage.srchIconRequester);
		click(workRequestPage.selectRequester);
		logger.info("Requester Name enetered");
	}

		
	/**
	 * selectSurvey
	 *
	 * @name selectSurvey
	 * @description Select Survey on work Request page
	 * @author Vaibhav Narkhede
	 * @param  Survey ||description: 
	 *                 ||allowedRange:
	 * @return void ||description:
	 * @jiraId
	 */
	public void selectSurvey(String Survey) {

		try {
			Thread.sleep(2000);
			// Actions actions = new Actions(driver);
			// actions.moveToElement(driver.findElement(createWorkRequest.selectSurveyName(Survey))).click().build().perform();

			// click(createWorkRequest.selectSurveyName(Survey));
			((JavascriptExecutor) driver).executeScript("arguments[0].click();",
					driver.findElement(workRequestPage.selectSurveyName(Survey)));
			logger.info("Survey " + Survey + " has been selected in the Work Order");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	/**
	 * clkAssignToMe
	 *
	 * @name clkAssignToMe
	 * @description Click assign to me button under assign Survey section on work Request page
	 * @author Vaibhav Narkhede
	 * @param   ||description: 
	 *                 ||allowedRange:
	 * @return void ||description:
	 * @jiraId
	 */
	public void clkAssignToMe() throws InterruptedException {

		click(workRequestPage.clkAssignToMe);
		click(commonPage.LogoutIcon);
		String loginUserName = getText(workRequestPage.loginUserName).trim();
		String assignedSurvyeorName = getText(workRequestPage.assignedSurveyorName).trim();

		if ((assignedSurvyeorName.contains(loginUserName))) {

			try {
				logger.pass("Surveyor assigned successfully", MediaEntityBuilder
						.createScreenCaptureFromPath(ScreenShots.getScreenShot()).build());
			} catch (IOException e) {

				logger.error(e.getMessage());
			}
		} else {
			try {
				logger.fail("Surveyor assigned failed", MediaEntityBuilder
						.createScreenCaptureFromPath(ScreenShots.getScreenShot()).build());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage());
			}
		}
		Thread.sleep(2000);
		((JavascriptExecutor) driver).executeScript("arguments[0].click();",
				driver.findElement(workRequestPage.clkSubmit));
		
	}
	
	
	
	/**
	 * validateWOState
	 *
	 * @name validateWOState
	 * @description Validate WO state after submission.
	 * @author Vaibhav Narkhede
	 * @param   ||description: 
	 *                 ||allowedRange:
	 * @return void ||description:
	 * @jiraId
	 */
	public void validateWOState() {
	
		BasePage.waitForElementVisible(workRequestPage.textAssignedState, DEFAULT_TIMEOUT);
	
		
		String statestr = getText(workRequestPage.stateOfWorkOrder).trim();
		String[] str = statestr.split(",");
		String state = str[str.length-1].trim();
	
		if (state.endsWith("Assigned")) {
			try {
				logger.pass("WorkOrder moved to Assigned state successfully", MediaEntityBuilder.createScreenCaptureFromPath(ScreenShots.getScreenShot()).build());
			}  catch (IOException e) {
				
				logger.error(e.getMessage());
			}}
		else {
			try {
				logger.fail("WorkOrder did not move to Assigned state", MediaEntityBuilder
						.createScreenCaptureFromPath(ScreenShots.getScreenShot()).build());
			} catch (IOException e) {
			
				logger.error(e.getMessage());
			}}
	}
}
