package abs.framework.base;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;
import org.testng.internal.Utils;

import com.aventstack.extentreports.ExtentTest;
import abs.framework.lib.DriverLib;
import abs.framework.logger.ExtentReportClass;
import abs.framework.lib.Util;

public class BasePage {

	protected static WebDriver driver;
	protected WebDriverWait wait;
	BaseTest baseTest;
	protected static Properties prop;
	protected static Statement stmt;
	public static ExtentTest logger;
	DriverLib browserLib;
	public ExtentReportClass extentReport;
	public static int DEFAULT_TIMEOUT;
	//This is contructor of Base Page class
	public BasePage() {
		baseTest = new BaseTest();
		BasePage.driver = baseTest.getDriver();
		BasePage.prop = new Util().GetPropertyObject();
		BasePage.stmt = new Util().getDBObject();
		// driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		DEFAULT_TIMEOUT = Integer.parseInt(prop.getProperty("env.DEFAULT_TIMEOUT"));
		BasePage.logger = baseTest.getLogger();

	}

	/*
	 * ===============================================WebElement
	 * Display/Enable/Present=======================================================
	 * ============================
	 */

	/**
	 * Check if Element displayed or enable on Page
	 *
	 * @name isElementDisplayedAndEnableOnPage
	 * @description
	 * @author Vaibhav Narkhede
	 * @return boolean ||description: returns True of if webElement is displayed or
	 *         Enable
	 * @throws @jiraId
	 */
	public static boolean isElementDisplayedAndEnableOnPage(By element) {
		
		int WaitTimeCounter = 0;
		do {

			try {

				WebElement webelement = driver.findElement(element);
				if (webelement.isDisplayed() || webelement.isEnabled())
					return true;
				else
					return false;

			} catch (Exception e) {

				WaitTimeCounter = WaitTimeCounter + 1;
				// System.out.println(WaitTimeCounter);
				try {
					Thread.sleep(500);
				} catch (Exception exp) {
					exp.printStackTrace();
				}
			}
		} while (WaitTimeCounter <= DEFAULT_TIMEOUT);

		
		if (WaitTimeCounter > DEFAULT_TIMEOUT) {
			throw new NoSuchElementException("The element by locator '" + element + "' is not enable on submit.");
		}
		return false;
		
		
	}

	/**
	 * Checks if is element present.
	 *
	 * @name isElementPresent
	 * @description Checks if an element isPresent
	 * @author Vaibhav Narkhede
	 * @param elementLocator ||description: ElementLocator locator of the element to
	 *                       check ||allowedRange:
	 * @return boolean ||description: True|False, True - If element is found
	 * @jiraId
	 */

	public static boolean isElementPresent(By elementLocator) throws Exception {
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			List<WebElement> list = driver.findElements(elementLocator);
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			if (list.isEmpty()) {
				return false;
			} else {
				return list.get(0).isDisplayed();
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			throw ex;
		}

	}

	/*
	 * ===============================================WAIT
	 * CLASSES======================================================================
	 * =============
	 */

	/**
	 * wait for Load
	 *
	 * @name WaitForLoading
	 * @description This will check for loading page icon (Specific to Freedom)
	 * @author Vaibhav Narkhede
	 * @return void ||description:
	 * @throws @jiraId
	 */
	public static void WaitForLoading() {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("(//div[contains(text(),'Loading')])[1]")));

	}

	/**
	 * Wait for page to load.
	 *
	 * @name waitForAjaxToLoad
	 * @description Waits for the page to load(Ajax Loading)
	 * @author Vaibhav Narkhede
	 * @param DEFAULT_WAIT_TIME ||description: Wait timeout in seconds
	 *                          ||allowedRange:
	 * @return void ||description:
	 * @jiraId
	 */
	public static void waitForAjaxToLoad(int DEFAULT_WAIT_TIME) {
		long entryTime = System.currentTimeMillis();
		try {
			@SuppressWarnings("deprecation")
			Wait<WebDriver> wait1 = new FluentWait<WebDriver>(driver).withTimeout(DEFAULT_WAIT_TIME, TimeUnit.SECONDS)
					.pollingEvery(DEFAULT_WAIT_TIME, TimeUnit.SECONDS);

			wait1.until(new ExpectedCondition<Boolean>() {

				public Boolean apply(WebDriver driver) {
					try {
						JavascriptExecutor js = (JavascriptExecutor) driver;

						if (js.executeScript("return document.readyState").equals("complete")) {
							if ((Boolean) js.executeScript("return window.jQuery==undefined")) {
								return true;
							} else if ((Boolean) js.executeScript("return jQuery.active==0")) {
								return true;
							} else {
								waitForMSeconds(1000);

								return false;
							}
						}
					} catch (Exception ex) {
						waitForMSeconds(3000);

						try {
							waitForMSeconds(3000);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					return false;
				}
			});

		} catch (Exception e) {

			if (DEFAULT_WAIT_TIME > 3000) {
				waitForAjaxToLoad(DEFAULT_WAIT_TIME - (int) (System.currentTimeMillis() - entryTime));
			}
		}
	}

	/**
	 * Wait for M seconds.
	 *
	 * @name waitForMSeconds
	 * @description Waits the thread for set milliseconds
	 * @author Vaibhav Narkhede
	 * @param timeoutInMilliSeconds ||description: Wait timeout in milliseconds
	 *                              ||allowedRange:
	 * @return void ||description:
	 * @throws InterruptedException
	 * @jiraId
	 */
	public static void waitForMSeconds(int timeoutInMilliSeconds) {
		try {
			Thread.sleep(timeoutInMilliSeconds);
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * Wait for element visible.
	 *
	 * @name waitForElementVisible
	 * @description Waits for an element to be visible
	 * @author Vaibhav Narkhede
	 * @param elementLocator    ||description: Locator of an element to check
	 *                          ||allowedRange:
	 * @param timeoutInMSeconds ||description: Wait timeout in Milliseconds
	 *                          ||allowedRange:
	 * @return void ||description:
	 * @throws @jiraId
	 */
	public static void waitForElementVisible(By elementLocator, int timeoutInMSeconds) {
		try {
			waitForMSeconds(1000);
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
					.pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);

			wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(elementLocator)));

		} catch (TimeoutException e) {
			throw new NoSuchElementException(
					"The element by locator '" + elementLocator + "' is not visible on the page.");
		} catch (Exception ex) {
			throw ex;
		}

	}

	/**
	 * Wait for element clickable.
	 *
	 * @name waitForElementClickable
	 * @description Waits for element to be clickable
	 * @author Vaibhav Narkhede
	 * @param elementLocator    ||description: Locator of an element to check
	 *                          ||allowedRange:
	 * @param timeoutInMSeconds ||description: Wait Timeout in Seconds
	 *                          ||allowedRange:
	 * @return void ||description:
	 * @throws @jiraId
	 */
	public static void waitForElementClickable(By elementLocator, int timeoutInSeconds) {
		try {
			waitForMSeconds(1000);
			@SuppressWarnings("deprecation")
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(timeoutInSeconds, TimeUnit.SECONDS)
					.pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);

			wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(elementLocator)));
		} catch (TimeoutException e) {
			throw new NoSuchElementException(
					"The element by locator '" + elementLocator + "' is not available on the page.");
		} catch (Exception ex) {
			throw ex;
		}

	}

	/**
	 * Wait for element present.
	 *
	 * @name waitForElementPresent
	 * @description Waits for an element to be present
	 * @author Vaibhav Narkhede
	 * @param elementLocator    ||description: ||allowedRange:
	 *                          ||allowedRange:
	 * @return void ||description:
	 * @throws @jiraId
	 */
	public static void waitForElementPresent(By elementLocator) {
		

		
		boolean isTimeToStop = false;
		boolean isPresent = false;
		long endCounter= 5;
		int counter = 0;
		while (!isPresent && !isTimeToStop) {
			isPresent = isElementDisplayedAndEnableOnPage(elementLocator);
			counter = counter + 1;
			if (counter >= endCounter) {
				isTimeToStop = true;
			}
			BasePage.waitForMSeconds(500);
		}
		
		
	}

	/**
	 * Wait until tab is opened.
	 *
	 * @name waitUntilTabIsOpened
	 * @description Waits for a tab to be opened
	 * @author vaibhav narkhede
	 * @param timeoutinMSeconds ||description: Wait timeout in Milliseconds
	 *                          ||allowedRange:
	 * @param tabCountToOpen    ||description: Number of opened tabs after the tab
	 *                          is opened ||allowedRange:
	 * @return void ||description:
	 * @throws @jiraId
	 */
	public static void waitUntilTabIsOpened(int timeoutinMSeconds, int tabCountToOpen) {
		try {
			waitForMSeconds(1000);

			(new WebDriverWait(driver, timeoutinMSeconds))
					.until(ExpectedConditions.numberOfWindowsToBe(tabCountToOpen));
		} catch (Exception ex) {
			throw ex;
		}
	}

	/*
	 * ===============================================Scrolling=====================
	 * ==============================================================
	 */

	/**
	 * Scroll to bottom.
	 *
	 * @name scrollToBottom
	 * @description Scroll to the bottom of page
	 * @author Vaibhav Narkhede
	 * @return void ||description:
	 * @jiraId
	 */
	public static void scrollToBottom() throws Exception {
		try {
			if (driver instanceof JavascriptExecutor) {
				waitForMSeconds(1000);
				((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
			}
		} catch (Exception e) {
			if (e.getMessage().toLowerCase().contains("javascript error")) {
				new SkipException("JavaScriptExecutor's exception thrown:");
				// loggerManager.logger.info(e.getMessage());
			} else
				throw e;
		}
	}

	/**
	 * Scroll to top.
	 *
	 * @name scrollToTop
	 * @description Scroll to the top of page
	 * @author Vaibhav Narkhede
	 * @return void ||description:
	 * @jiraId
	 */
	public static void scrollToTop() {
		try {
			if (driver instanceof JavascriptExecutor) {
				waitForMSeconds(1000);
				((JavascriptExecutor) driver).executeScript("window.scrollTo(0,0);");
			}
		} catch (Exception e) {
			if (e.getMessage().toLowerCase().contains("javascript error")) {
				new SkipException("JavaScriptExecutor's exception thrown:");
				logger.error(e.getMessage());
			} else
				throw e;
		}
	}

	/**
	 * Scroll to element.
	 *
	 * @name scrollToElement
	 * @description Scroll up/down to the element on the page
	 * @author Vaibhav Narkhede
	 * @param by ||description: Element's Locator ||allowedRange:
	 * @return void ||description:
	 */

	public static void scrollToElement(By by) {
		try {
			WebElement webElement = driver.findElement(by);

			Actions actions = new Actions(driver);
			actions.moveToElement(webElement);
			actions.perform();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * ===============================================WebElement Regular
	 * Operation====================================================================
	 * ===============
	 */

	/**
	 * click on webelement.
	 *
	 * @name click
	 * @description click on web element
	 * @author Vaibhav Narkhede
	 * @param by ||description: Element's Locator ||allowedRange:
	 * @return void ||description:
	 * @jiraId
	 */

	public static void click(By by) {

		int WaitTimeCounter = 0;

		do {

			try {
					
				highLight(driver.findElement(by));
				driver.findElement(by).click();
				break;

			} catch (Exception e) {

				WaitTimeCounter = WaitTimeCounter + 1;
				try {

					Thread.sleep(500);
				} catch (Exception exp) {
					exp.printStackTrace();
				}

			}
		} while (WaitTimeCounter <= DEFAULT_TIMEOUT);

		if (WaitTimeCounter > DEFAULT_TIMEOUT) {

			throw new NoSuchElementException("The element by locator '" + by + "' is not enable on the page to click.");

		}

	}

	/**
	 * clear text field.
	 *
	 * @name clear
	 * @description clear text field
	 * @author Vaibhav Narkhede
	 * @param by ||description: Element's Locator ||allowedRange:
	 * @return void ||description:
	 * @jiraId
	 */

	public static void clear(By by) {

		int WaitTimeCounter = 0;

		do {

			try {

				driver.findElement(by).clear();
				break;

			} catch (Exception e) {

				WaitTimeCounter = WaitTimeCounter + 1;
				// System.out.println(WaitTimeCounter);
				try {
					Thread.sleep(500);
				} catch (Exception exp) {
					exp.printStackTrace();
				}
			}
		} while (WaitTimeCounter <= DEFAULT_TIMEOUT);

		if (WaitTimeCounter > DEFAULT_TIMEOUT) {
		
			throw new NoSuchElementException("The element by locator '" + by + "' is not enable on the page to click.");

		}

	}

	/**
	 * get the text value
	 *
	 * @name getText
	 * @description Get the text from web element
	 * @author Vaibhav Narkhede
	 * @param by ||description: Element's Locator ||allowedRange:
	 * @return void ||description:
	 * @jiraId
	 */

	public static String getText(By by) {
		String text = null;
		int WaitTimeCounter = 0;

		do {

			try {

				text = driver.findElement(by).getText();
				break;

			} catch (Exception e) {

				WaitTimeCounter = WaitTimeCounter + 1;
				// System.out.println(WaitTimeCounter);
				try {
					Thread.sleep(500);
				} catch (Exception exp) {
					exp.printStackTrace();
				}
			}
		} while (WaitTimeCounter <= DEFAULT_TIMEOUT);

		if (WaitTimeCounter > DEFAULT_TIMEOUT) {
			throw new NoSuchElementException("The element by locator '" + by + "' is not enable on the page to return.");
		}

		return text.trim();

	}

	/**
	 * send keys
	 *
	 * @name SendKey
	 * @description send keys used to send the keyboard input
	 * @author Vaibhav Narkhede
	 * @param by ||description: Element's Locator ||allowedRange:
	 * @return void ||description:
	 * @jiraId
	 */

	public static void sendKeys(By by, String Text) {

		int WaitTimeCounter = 0;

		do {

			try {

				driver.findElement(by).sendKeys(Text);
				break;

			} catch (Exception e) {

				WaitTimeCounter = WaitTimeCounter + 1;
				// System.out.println(WaitTimeCounter);
				try {
					Thread.sleep(500);
				} catch (Exception exp) {
					exp.printStackTrace();
				}
			}
		} while (WaitTimeCounter <= DEFAULT_TIMEOUT);

		if (WaitTimeCounter > DEFAULT_TIMEOUT) {
	
			throw new NoSuchElementException(
					"The element by locator '" + by + "' is not enable on the page to Send keys.");

		}

	}

	/**
	 * submit
	 *
	 * @name submit
	 * @description submit will submit the form
	 * @author Vaibhav Narkhede
	 * @param by ||description: Element's Locator ||allowedRange:
	 * @return void ||description:
	 * @jiraId
	 */

	public static void submit(By by) {

		int WaitTimeCounter = 0;

		do {

			try {

				driver.findElement(by).submit();
				break;

			} catch (Exception e) {

				WaitTimeCounter = WaitTimeCounter + 1;
				// System.out.println(WaitTimeCounter);
				try {
					Thread.sleep(500);
				} catch (Exception exp) {
					exp.printStackTrace();
				}
			}
		} while (WaitTimeCounter <= DEFAULT_TIMEOUT);

		if (WaitTimeCounter > DEFAULT_TIMEOUT) {
			driver.close();
			driver.quit();
			throw new NoSuchElementException("The element by locator '" + by + "' is not enable on submit.");

		}

	}

	/*----------------------------------------------------------Switching windows----------------------------------------------------------------------*/

	/**
	 * Switch to the tab by index.
	 *
	 * @name switchToTheTabByIndex
	 * @description switch to a tab by given index
	 * @author Vaibhav Narkhede
	 * @param tabIndex ||description: Tab index to switch ||allowedRange:
	 * @return void ||description:
	 * @throws @jiraId
	 */

	public static void switchToTheTabByIndex(int tabIndex) {
		try {
			waitUntilTabIsOpened(DEFAULT_TIMEOUT, tabIndex);

			ArrayList<String> availableWindows = new ArrayList<String>(driver.getWindowHandles());
			if (!availableWindows.isEmpty()) {
				driver.switchTo().window(availableWindows.get(tabIndex - 1));
			}

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Select child window element.
	 *
	 * @name selectChildWindowElement
	 * @description Switches to the child window and perform operations
	 * @author
	 * @param element ||description: WebElement to be clicked ||allowedRange:
	 * @return void ||description:8/
	 * @jiraId
	 */
	public static void selectChildWindowElement(WebElement element) {
		try {
			for (String windowHandle : driver.getWindowHandles()) {
				driver.switchTo().window(windowHandle);
			}
			element.click();
		} catch (Exception e) {
			throw e;
		}
	}

	/*----------------------------------------------------------Dropdown----------------------------------------------------------------------*/
	/**
	 * Select drop down by index.
	 *
	 * @name selectDropDownByIndex
	 * @description selects drop down by index
	 * @author Vaibhav Narkhede
	 * @param element ||description: Drop down WebElement ||allowedRange:
	 * @param index   ||description: Index of element to be selected ||allowedRange:
	 * @return void ||description:
	 * @jiraId
	 */
	public static void selectDropDownByIndex(WebElement element, int index) {
		try {
			Select dropdown = new Select(element);
			dropdown.selectByIndex(index);
		} catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * Select drop down by value.
	 *
	 * @name selectDropDownByValue
	 * @description Selects drop down element by value
	 * @author Vaibhav Narkhede
	 * @param element ||description: Drop down WebElement ||allowedRange:
	 * @param value   ||description: Value of element to be selected ||allowedRange:
	 * @return void ||description:
	 * @jiraId
	 */
	public static void selectDropDownByValue(WebElement element, String value) {
		try {
			Select dropdown = new Select(element);
			dropdown.selectByValue(value);
		} catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * Select Drop Down By Visible Text.
	 *
	 * @name selectDropDownByVisibleText
	 * @description This method is used to select dropdown by visible text
	 * @author BalamuruganG
	 * @param element ||description: WebElement ||allowedRange:
	 * @param value   ||description: Select option ||allowedRange:
	 * @return void ||description:
	 * @jiraId
	 */
	public static void selectDropDownByVisibleText(WebElement element, String value) {
		try {
			if (element == null)
				throw (new IllegalArgumentException("WebElement is null"));
			else if (value.isEmpty())
				throw (new IllegalArgumentException("String value to select is empty"));
			else {
				Select dropdown = new Select(element);
				dropdown.selectByVisibleText(value);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/*----------------------------------------------------------Others----------------------------------------------------------------------*/

	/**
	 * Clear input.
	 *
	 * @name clearInput
	 * @description clear input using BACKSPACE
	 * @author Vaibhav Narkhede
	 * @param element ||description: WebElement to be cleared ||allowedRange:
	 * @return void ||description:
	 * @jiraId
	 */

	public static void clearInput(WebElement element) {
		try {
			String text = element.getAttribute("value");
			for (int i = 0; i < text.length(); i++) {
				element.sendKeys(Keys.BACK_SPACE);
			}
		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * Refresh page.
	 *
	 * @name refreshPage
	 * @description Refresh the WebPage
	 * @author Vaibhav Narkhede
	 * @return void ||description:
	 * @jiraId
	 */
	public static void refreshPage() {
		try {
			WebDriver.Navigation navigation = driver.navigate();
			navigation.refresh();
		} catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * Accept alert.
	 *
	 * @name acceptAlert
	 * @description Accepts an alert Message
	 * @author Vaibhav Narkhede
	 * @param sBtnText ||description: Button string value to be selected
	 *                 ||allowedRange:
	 * @return void ||description:
	 * @jiraId
	 */
	public static void acceptAlert(String sBtnText) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 2);
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			switch (sBtnText) {

			case "Accept":
				alert.accept();
				break;

			case "Dismiss":
				alert.dismiss();
				break;

			default:
				alert.accept();
			}
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	
	
	/**
	 * highLight the WebElement
	 *
	 * @name highLight
	 * @description Highlight the WebElement
	 * @author Vaibhav Narkhede
	 * @param webElement ||description: WebElement to be highlighted
	 *                 ||allowedRange:
	 * @return void ||description:
	 * @jiraId
	 */
	
	
	public static void highLight(WebElement webElement){
        WebElement element_node = webElement;
        		//driver.findElement(By.xpath(webElement));
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].style.border='3px solid red'", element_node);
	}

}
