package abs.combine.step;

import org.apache.http.HttpException;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

import abs.combined.page.CommonPage;
import abs.combined.page.WorkRequestPage;
import abs.framework.base.BasePage;
import abs.framework.lib.Util;
import abs.framework.logger.ScreenShots;


public class Common extends BasePage {

	CommonPage commonPage;
	WorkRequestPage workRequestPage;
    Common common;
	public Common() {
		commonPage = new CommonPage();
		workRequestPage = new WorkRequestPage();
		common = new Common();
	}

	
	/**
	 * loginFreedom <test , This is vaiabhav">
	 *
	 * @name loginFreedom
	 * @description Login to freedom applcation
	 * @author Vaibhav Narkhede
	 * @param  ||description: 
	 *                 ||allowedRange:
	 * @return void ||description:
	 * @jiraId
	 */
	
	public void loginFreedom() {

		try {
			
			
	
			BasePage.refreshPage();

			openURL(prop.getProperty("env.wr.url"));

			BasePage.refreshPage();

			BasePage.sendKeys(commonPage.loginUserName, prop.getProperty("env.wr.username"));
			// driver.findElement(commonPage.loginUserName).sendKeys(prop.getProperty("env.username"));
			logger.info("Entered UserName");

			BasePage.sendKeys(commonPage.loginPwd, Util.decryptString(prop.getProperty("env.wr.password")));
			// driver.findElement(commonPage.loginPwd).sendKeys(Util.decryptString(prop.getProperty("env.password")));

			logger.info("Entering password");

			BasePage.click(commonPage.loginSubmit);

			// driver.findElement(commonPage.loginSubmit).click();
			logger.info("Clicking on Submit button");

			// logger.log(Status.PASS, "Login to Freedom is succussful.");

			logger.pass("Login Successful", MediaEntityBuilder
					.createScreenCaptureFromPath(ScreenShots.getScreenShot()).build());
			

		} catch (Exception e) {
			logger.error(e.getMessage());

		}

	}

	
	/**
	 * logout
	 *
	 * @name logout
	 * @description logout to freedom application
	 * @author Vaibhav Narkhede
	 * @param  ||description: 
	 *                 ||allowedRange:
	 * @return void ||description:
	 * @jiraId
	 */
	
	public void logout() {

		try {
			// wait.until(ExpectedConditions.visibilityOfElementLocated(commonPage.LogoutIcon)).click();
			// wait.until(ExpectedConditions.visibilityOfElementLocated(commonPage.Logout)).click();

			BasePage.click(commonPage.LogoutIcon);
			BasePage.click(commonPage.Logout);
			logger.info("Logout");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * openURL
	 *
	 * @name openURL
	 * @description open the URL 
	 * @author Vaibhav Narkhede
	 * @param  URL ||description: URL which want to open
	 *                 ||allowedRange:
	 * @return void ||description:
	 * @jiraId
	 */
	
	public void openURL(String url) {

		try {
		driver.get(url);


		if (driver.getPageSource().contains("This site can’t be reached")) {

			logger.info("URL is not working , Please check with BnD team on ongoing deployment");
			try {
				throw new HttpException();
			} catch (HttpException t) {
				logger.error(t.getMessage());

			}

		}
		}catch(Exception e) {
			
			throw e;
			
		}

	}
	
	
	/**
	 * navigateToDashboard
	 *
	 * @name navigateToDashboard
	 * @description Navigate to Dashboard page of Freedom 
	 * @author Vaibhav Narkhede
	 * @param   ||description: 
	 *                 ||allowedRange:
	 * @return void ||description:
	 * @jiraId
	 */
	
	public void navigateToDashboard() throws InterruptedException {
		click(commonPage.navigateToDashboard);
	}

	
	
	
	/**
	 * navigateToWorkOrder
	 *
	 * @name navigateToWorkOrder
	 * @description Navigate to work order page of Freedom 
	 * @author Vaibhav Narkhede
	 * @param   ||description: 
	 *                 ||allowedRange:
	 * @return void ||description:
	 * @jiraId
	 */
	public void navigateToWorkOrder() throws InterruptedException {
		click(commonPage.navigateToWorkOrder);
	}

	
	
	
	/**
	 * navigateToVirtualVessel
	 *
	 * @name navigateToVirtualVessel
	 * @description Navigate to virtual vessel page of Freedom 
	 * @author Vaibhav Narkhede
	 * @param   ||description: 
	 *                 ||allowedRange:
	 * @return void ||description:
	 * @jiraId
	 */
	
	public void navigateToVirtualVessel() throws InterruptedException {

		click(commonPage.navigateToVirtualVessel);
		logger.info("Successfully Navigated to Virtual Vessel");
	}

}
