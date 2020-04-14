/**
 * This is the library class for Quote liberary
 * 
 */

package abs.combine.step;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.openqa.selenium.By;

import com.aventstack.extentreports.MediaEntityBuilder;

import abs.combined.page.HomePage;
import abs.combined.page.LoginPage;
import abs.combined.page.QuoteOverviewPage;
import abs.combined.page.RequestForQuotePage;
import abs.framework.base.BasePage;
import abs.framework.lib.Util;
import abs.framework.logger.ScreenShots;


public class Quote extends BasePage {

	HomePage homePage;
	LoginPage loginPage;
	QuoteOverviewPage quoteOverviewPage;
	RequestForQuotePage requestForQuotePage;
	LinkedHashMap<String, String> requestorDetails;
	Common common;
	

	public Quote() {

		homePage = new HomePage();
		loginPage = new LoginPage();
		quoteOverviewPage = new QuoteOverviewPage();
		requestForQuotePage = new RequestForQuotePage();
		common = new Common();
		
	}

	public void loginPortal() {
		logger.info("opening URL");
		common.openURL(prop.getProperty("env.customerportal.url"));
		
		BasePage.click(loginPage.userName);
		logger.info("Entering UserName");
		BasePage.sendKeys(loginPage.userName, prop.getProperty("env.customerportal.username"));

		BasePage.click(loginPage.password);
		logger.info("Entering Password");
		BasePage.sendKeys(loginPage.password, Util.decryptString(prop.getProperty("env.customerportal.password")));

		logger.info("Clicking on Login Button");
		BasePage.click(loginPage.loginButton);

	}

	public void openURL() {

		logger.info("opening URL : " + prop.getProperty("env.url"));
		driver.navigate().to(prop.getProperty("env.url"));

	}

	public void navigateToRequestToQuote() throws Exception {

		logger.info("Navigate to Plan Review Menu");
		BasePage.click(homePage.planReviewMenu);

		try {

			logger.info("Navigate to Request for Quote");
			BasePage.click(homePage.requestForQuote);
			logger.pass("Successfully navigated to Quote Overview Page",
					MediaEntityBuilder.createScreenCaptureFromPath(ScreenShots.getScreenShot()).build());

			logger.info("Clicking on Request for Quote button");
			BasePage.click(quoteOverviewPage.requestForQuoteBtn);
			logger.pass("Successfully navigated to RFQ page",
					MediaEntityBuilder.createScreenCaptureFromPath(ScreenShots.getScreenShot()).build());

		} catch (Exception e) {
			throw e;
		}

	}

	public void selectValidateReviewAndEnggDptType() throws InterruptedException, IOException {

		if (BasePage.isElementDisplayedAndEnableOnPage(requestForQuotePage.reviewTypeDrpDwn)) {
			// logger.pass("Review Type Element is present");
			logger.pass("Review Type Element is present", MediaEntityBuilder
					.createScreenCaptureFromPath(ScreenShots.getScreenShot()).build());
			click(requestForQuotePage.reviewTypeDrpDwn);
			click(By.xpath("//span[text()='One']"));

		} else {
			logger.fail("Review Type Element is present", MediaEntityBuilder
					.createScreenCaptureFromPath(ScreenShots.getScreenShot()).build());
		}

		if (BasePage.isElementDisplayedAndEnableOnPage(requestForQuotePage.engineeringDptDrpDwn)) {
			// logger.pass("Engineer Department is present");
			logger.pass("Engineer Department is present", MediaEntityBuilder
					.createScreenCaptureFromPath(ScreenShots.getScreenShot()).build());
			click(requestForQuotePage.engineeringDptDrpDwn);

			click(By.xpath("//span[text()='Office one']"));

		} else {
			logger.fail("Engineer Department is present", MediaEntityBuilder
					.createScreenCaptureFromPath(ScreenShots.getScreenShot()).build());
		}

		Thread.sleep(10000);
	}

	public LinkedHashMap<String, String> getRequestorDetails() throws SQLException {

		String query;

		try {
			requestorDetails = new LinkedHashMap<String, String>();

			query = "Select c.customer_number, c.customer_name , vw.ADDRESS , u.FIRST_NAME, u.LAST_NAME , u.EMAIL, u.PHONE_NUMBER from LOCATIONS l, ABS_CUSTOMERS c , ABS_CUSTOMER_VW vw , ABS_ACM_USERS u where l.abs_customer_id=c.customer_id and c.CUSTOMER_ID=vw.CUSTOMER_ID and l.LOCATION in (Select CUSTOMER_LOCATION from ABS_ACM_USERS where username='"
					+ prop.getProperty("env.username") + "') and  u.username='" + prop.getProperty("env.username")
					+ "'";

			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {

				requestorDetails.put("WCN", rs.getString(1).trim());
				requestorDetails.put("Name", rs.getString(4).trim() + " " + rs.getString(5).trim());
				requestorDetails.put("Company Name", rs.getString(2).trim());
				requestorDetails.put("Address", rs.getString(3).trim());
				requestorDetails.put("Email", rs.getString(6).trim());
				requestorDetails.put("Phone", rs.getString(7).trim());

			}
			return requestorDetails;
		} catch (Exception e) {
			throw e;

		}

	}

	public String getRequestorWCNfromRFQ() {

		// return
		// driver.findElement(requestForQuotePage.getRequestorContactXpath("WCN")).getText();
		return getText(requestForQuotePage.getRequestorContactXpath("WCN"));
	}

	public String getRequestorNamefromRFQ() {

		// return
		// driver.findElement(By.xpath("//*[text()='Name:']/following-sibling::div[1]")).getText();
		return getText(requestForQuotePage.getRequestorContactXpath("Name"));
	}

	public String getRequestorCompanyNamefromRFQ() {
		return getText(requestForQuotePage.getRequestorContactXpath("Company Name"));
	}

	public String getRequestorAddressfromRFQ() {
		return getText(requestForQuotePage.getRequestorContactXpath("Address"));
	}

	public String getRequestorEmailfromRFQ() {

		// return
		// driver.findElement(By.xpath("//*[text()='Email:']/following-sibling::div[1]")).getText();
		return getText(requestForQuotePage.getRequestorContactXpath("Email"));
	}

	public String getRequestorPhonefromRFQ() {

		// return
		// driver.findElement(By.xpath("//*[text()='Phone:']/following-sibling::div[1]")).getText();
		return getText(requestForQuotePage.getRequestorContactXpath("Phone"));
	}

	public void requesterInfoRFQValidate() throws IOException, SQLException {

		requestorDetails = getRequestorDetails();

		if (getRequestorWCNfromRFQ().equals(requestorDetails.get("WCN"))) {
			logger.pass("WCN information matched successfully with Database", MediaEntityBuilder
					.createScreenCaptureFromPath(ScreenShots.getScreenShot()).build());
		} else {
			logger.fail(
					"WCN information on RFQ page is mismatched with Database. WCN from DB:'"
							+ requestorDetails.get("WCN") + "' and WCN from RFQ Page : " + getRequestorWCNfromRFQ(),
					MediaEntityBuilder.createScreenCaptureFromPath(ScreenShots.getScreenShot())
							.build());
		}

		if (getRequestorNamefromRFQ().equals(requestorDetails.get("Name"))) {
			logger.pass("Requestor Name is matched successfully with Database",
					MediaEntityBuilder.createScreenCaptureFromPath(ScreenShots.getScreenShot()).build());
		} else {
			logger.fail(
					"Requestor Name RFQ page is mismatched with Database. Requestor Name from DB:'"
							+ requestorDetails.get("Name") + "' and Requestor Name from RFQ Page : "
							+ getRequestorNamefromRFQ(),
					MediaEntityBuilder.createScreenCaptureFromPath(ScreenShots.getScreenShot()).build());
		}

		if (getRequestorCompanyNamefromRFQ().equals(requestorDetails.get("Company Name"))) {
			logger.pass("Company Name information matched successfully with Database", MediaEntityBuilder
					.createScreenCaptureFromPath(ScreenShots.getScreenShot()).build());
		} else {
			logger.fail(
					"Company Name information on RFQ page is mismatched with Database. Company Name from DB:'"
							+ requestorDetails.get("Company Name") + "' and Company Name from RFQ Page : "
							+ getRequestorCompanyNamefromRFQ(),
					MediaEntityBuilder.createScreenCaptureFromPath(ScreenShots.getScreenShot())
							.build());
		}

		if (getRequestorAddressfromRFQ().equals(requestorDetails.get("Address"))) {
			logger.pass("Address information matched successfully with Database", MediaEntityBuilder
					.createScreenCaptureFromPath(ScreenShots.getScreenShot()).build());
		} else {
			logger.fail("Address information on RFQ page is mismatched with Database. Address from DB:'"
					+ requestorDetails.get("Address") + "' and Address from RFQ Page : " + getRequestorAddressfromRFQ(),
					MediaEntityBuilder.createScreenCaptureFromPath(ScreenShots.getScreenShot())
							.build());
		}

		if (getRequestorEmailfromRFQ().equals(requestorDetails.get("Email"))) {
			logger.pass("Email matched successfully with Database",
					MediaEntityBuilder.createScreenCaptureFromPath(ScreenShots.getScreenShot()).build());
		} else {
			logger.fail(
					"Email on RFQ page is mismatched with Database. Email from DB:'" + requestorDetails.get("Email")
							+ "' and Email from RFQ Page : " + getRequestorEmailfromRFQ(),
					MediaEntityBuilder.createScreenCaptureFromPath(ScreenShots.getScreenShot()).build());
		}

		if (getRequestorPhonefromRFQ().equals(requestorDetails.get("Phone"))) {
			logger.pass("Phone details matched successfully with Database",
					MediaEntityBuilder.createScreenCaptureFromPath(ScreenShots.getScreenShot()).build());
		} else {
			logger.fail("Phone details on RFQ page is mismatched with Database. Phone from DB:'"
					+ requestorDetails.get("Phone") + "' and Phone from RFQ Page : " + getRequestorPhonefromRFQ(),
					MediaEntityBuilder.createScreenCaptureFromPath(ScreenShots.getScreenShot()).build());
		}

	}
	
	

	public void backButtonRFQValidation() throws IOException {
		logger.info("Clicking on Back Button");
		click(requestForQuotePage.backBtn);

		if (isElementDisplayedAndEnableOnPage(quoteOverviewPage.requestForQuoteBtn)) {
			logger.pass(
					"After Clicking on Back button on Request for Quote page , Successfully navigated to Quote overview Page",
					MediaEntityBuilder.createScreenCaptureFromPath(ScreenShots.getScreenShot())
							.build());
		} else {
			logger.fail(
					"After Clicking on Back button on Request for Quote page , unsuccessful to navigate to Quote overview Page",
					MediaEntityBuilder.createScreenCaptureFromPath(ScreenShots.getScreenShot())
							.build());
		}
		
		logger.info("Moving back to Request to quote Page");
		BasePage.click(quoteOverviewPage.requestForQuoteBtn);

	}
	
	
	

	public void quoteOverviewBreadCrubsValidation() throws IOException {
	

		logger.info("Clicking on quote Overview breadcrumbs");
		click(quoteOverviewPage.quoteOverviewBreadCrub);
		if (isElementDisplayedAndEnableOnPage(quoteOverviewPage.requestForQuoteBtn)) {
			logger.pass("After Clicking quotes overview breadcrubs , Successfully navigated to Quote overview Page",
					MediaEntityBuilder.createScreenCaptureFromPath(ScreenShots.getScreenShot()).build());
		} else {
			logger.fail("After Clicking quotes overview breadcrubs , not able to navigate to Quote overview Page",
					MediaEntityBuilder.createScreenCaptureFromPath(ScreenShots.getScreenShot())
							.build());
		}
		
		logger.info("Moving back to Request to quote Page");
		BasePage.click(quoteOverviewPage.requestForQuoteBtn);
	}

}
