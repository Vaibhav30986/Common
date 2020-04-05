package abs.framework.base;

import abs.framework.lib.DriverLib;
import abs.framework.lib.Util;
import abs.framework.logger.ScreenShots;

import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class BaseTest {
	public static Properties prop;
	public static Statement stmt;
	DriverLib driverLibObj;
	Util util;
	public ExtentHtmlReporter htmlReporter;
	public static ExtentReports extent;
	public static ExtentTest logger;
	public static WebDriver driver;
	ScreenShots screenShots;
	

	@BeforeSuite
	public void initializeExtentReport() {
	
		try {
		util = new Util();
		util.CreatePropertyfile();
		BaseTest.prop = Util.prop;
		
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH-mm-ss");
		Date date = new Date();
		
		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/extent-report/ABSExtentReport-" + dateFormat.format(date) + ".html");
		htmlReporter.config().enableTimeline(false);
		htmlReporter.config().setDocumentTitle("AUTOMATION");
		htmlReporter.config().setReportName("ABS FREEDOM SANITY AUTOMATION REPORT");
		htmlReporter.config().setTheme(Theme.DARK);
		
		
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("Operating System", BaseTest.prop.getProperty("env.os"));
        extent.setSystemInfo("Environment", BaseTest.prop.getProperty("env.environment"));
        extent.setSystemInfo("Browser", BaseTest.prop.getProperty("env.browser"));
        extent.setSystemInfo("Sprint", BaseTest.prop.getProperty("env.sprint"));
        extent.setSystemInfo("Feature", BaseTest.prop.getProperty("env.feature"));
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	
	@BeforeClass
	public void driverInitated() {
		driverLibObj = new DriverLib(BaseTest.prop);
		driver = driverLibObj.getDriver();
		
	}
	
	@BeforeClass
	public void initializeSnapshot() {
		screenShots = new ScreenShots(driver);
	}
	
	
	@BeforeMethod
	public void initializeDBConnection() {
		try {
			
		util.CreateConnection();
		BaseTest.stmt= Util.stmt;
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@AfterClass
	public void closeBrowserInstance() {
		driver.close();
		driver.quit();

	}
	
	@AfterSuite
	public void reportEnd() {
		extent.flush();

	}

	public ExtentTest getLogger() {
		return logger;

	}


	public WebDriver getDriver() {
		return driver;
	}

}
