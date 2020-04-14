package abs.combine.step;

import java.io.IOException;


import com.aventstack.extentreports.MediaEntityBuilder;

import abs.combined.page.VesselOverviewPage;
import abs.combined.page.VirtualVesselPage;
import abs.framework.base.BasePage;
import abs.framework.logger.ScreenShots;


public class VirtualVessel extends BasePage {

	VirtualVesselPage virtualVessel;
	VesselOverviewPage vesselOverviewPage;

	public VirtualVessel() {
		virtualVessel = new VirtualVesselPage();
		vesselOverviewPage = new VesselOverviewPage();
	}

	public void searchVessel(String ClassNum) {

		sendKeys(virtualVessel.searchVesselField, ClassNum);
		logger.info("Searched Vessel with Class Number " + ClassNum);
		click(virtualVessel.clkSearchIcon);
	}

	public void selectSrchRslt(String ClassNum) {

		click(virtualVessel.selectRslt(ClassNum));
		logger.info("Selected Vessel with Class Number " + ClassNum);
	}

	public void ValidateVesselOverviewTab() throws Exception {
	
		waitForElementPresent(vesselOverviewPage.VesselOverviewTab);

			if (isElementPresent(vesselOverviewPage.VesselOverviewTab) == true) {

				logger.pass("Successfully navigated to Vessel Overview Page");
			} else {

				logger.fail("Navigation to Vessel Overview Failed");
			}
		
	}

	public String getIMONumber() {

		waitForElementPresent(vesselOverviewPage.VesselDsgntnIMO);
		return getText(vesselOverviewPage.VesselDsgntnIMO);
	}

	public String getFlagState() {

		waitForElementPresent(vesselOverviewPage.VesselDsgntnFlag);
		return getText(vesselOverviewPage.VesselDsgntnFlag);
	}

	public void vesselIMONumberValidate(String actualIMONum, String expectedIMONum) throws IOException {

		if (actualIMONum.equals(expectedIMONum)) {

			logger.pass("IMO Num validation pass", MediaEntityBuilder
					.createScreenCaptureFromPath(ScreenShots.getScreenShot()).build());

		} else {

			logger.fail("IMO Num validation fail", MediaEntityBuilder
					.createScreenCaptureFromPath(ScreenShots.getScreenShot()).build());

		}

	}

	public void vesselFlagStateValidate(String actualFlagState, String expectedFlagStaet) throws IOException {

		if (actualFlagState.equals(expectedFlagStaet)) {

			logger.pass("Flag State validation pass", MediaEntityBuilder
					.createScreenCaptureFromPath(ScreenShots.getScreenShot()).build());

		} else {

			logger.fail("Flag State validation fail", MediaEntityBuilder
					.createScreenCaptureFromPath(ScreenShots.getScreenShot()).build());

		}

	}

}
