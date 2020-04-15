/**
 * This is the library class for CLC application.
 * 
 */

package org.customerportal.step;

import org.customerportal.page.LoginPage;

import abs.framework.base.BasePage;
import abs.framework.lib.Util;

public class Common extends BasePage {
	
	LoginPage loginPage;
	
	public Common() {
		
		loginPage = new LoginPage();
		
	}
	
	
	/**
	 * Checks if is element present.
	 *
	 * @name cpLogin
	 * @description Login 
	 * @author Vaibhav Narkhede
	 * @param  ||description: || allowedRange:
	 * @return boolean ||description: True|False, True - If element is found
	 * @jiraId
	 */

	
	public void cpLogin() throws Exception {
		
		try {
			
			logger.info("Opening URL");
			driver.navigate().to(BasePage.prop.getProperty("env.url"));
			
			logger.info("Entering Username");
			BasePage.sendKeys(loginPage.userName, BasePage.prop.getProperty("env.username"));
			
			logger.info("Entering Password");
			BasePage.sendKeys(loginPage.password, Util.decryptString(BasePage.prop.getProperty("env.password")));
			
			logger.info("Clicking on Submit button");
			BasePage.click(loginPage.loginButton);
			
		}catch(Exception e) {
			throw e;
		}
		
		
		
		
		
		
		
		
	}
	
	
	
	
	
	

}
