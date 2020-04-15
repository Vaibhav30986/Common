package org.customerportal.test.sanity;

import org.testng.annotations.Test;

import abs.framework.base.BaseTest;

import org.testng.annotations.BeforeMethod;
import org.customerportal.step.Common;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

public class LoginFunctionalityValidation extends BaseTest {
	String TestTitle = "Scenario : Login functionality validation";
	Common common;

	@BeforeClass
	public void createLogger() {
		logger = extent.createTest(TestTitle);
	}

	@BeforeMethod
	public void beforeMethod() {
		common = new Common();

	}

	@Test
	public void loginTest() {

		try {

			common.cpLogin();

		} catch (Exception e) {

			logger.error(e);

		}

	}

	@AfterMethod
	public void afterMethod() {
		//Log out 
	}

}
