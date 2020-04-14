package abs.framework.lib;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

//import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Reporter;

public class DriverLib {
	private WebDriver driver;
	static String driverPath = System.getProperty("user.dir");
	Properties prop;
	String browser = null;

	public DriverLib(Properties prop) {
		this.prop = prop;
		browser = this.prop.getProperty("env.browser");
	}

	// final static Logger logger = Logger.getLogger(browserLib.class);

	// String browser= "Chrome";//System.getProperty("browser");

	public WebDriver getDriver() {
		setDriver();
		return driver;
	}

	public enum browserTypes {
		FIREFOX, CHROME, EDGE, IE;

	}

	
	
	
	public browserTypes getState(String browserType) {
		if ("Firefox".equalsIgnoreCase(browserType)) {
			return browserTypes.FIREFOX;
		} else if ("Chrome".equalsIgnoreCase(browserType)) {
			return browserTypes.CHROME;
		} else if ("Edge".equalsIgnoreCase(browserType)) {
			return browserTypes.EDGE;
		} else {
			return browserTypes.IE;
		}
	}

	
	
	/**
	 * Checks if is element present.
	 *
	 * @name setDriver
	 * @description This will initiate the browser driver
	 * @author Vaibhav Narkhede
	 * @param NA ||description: NA ||allowedRange:
	 * @return NA
	 * @jiraId
	 */
	public void setDriver() {
		try {
			switch (getState(browser)) {
			case CHROME:
				System.setProperty("webdriver.chrome.driver",
						driverPath + "\\src\\test\\resources\\drivers\\chromedriver.exe");
				ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.addArguments("start-maximized");
				driver = new ChromeDriver(chromeOptions);
				break;
			case FIREFOX:
				driver = new FirefoxDriver();
				driver.manage().window().maximize();
				break;
			case EDGE:
				System.setProperty("webdriver.edge.driver",
						driverPath + "\\src\\test\\resources\\drivers\\MicrosoftWebDriver.exe");
				driver = new EdgeDriver();
				driver.manage().window().maximize();
				driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
				driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
				break;
			default:
				System.setProperty("webdriver.ie.driver",
						driverPath + "\\src\\main\\resources\\drivers\\IEDriverServer.exe");
			}
		} catch (Exception e) {
			// logger.error("Browser not launched successfully" +e);
		}
	//	driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		// Reporter.log("Application opened in Browser: " + browser );
	}

}
