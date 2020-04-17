package stepDefination;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import junit.framework.Assert;

public class LoginStepDef {
	WebDriver driver;
	
	
	@Given("Open Chrome and start my application")
	public void open_Chrome_and_start_my_application() {
	    // Write code here that turns the phrase above into concrete actions
		System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir") + "\\driver\\chromedriver.exe");
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("start-maximized");
		driver = new ChromeDriver(chromeOptions);
		
		driver.get("https://fredint.eagle.org/freedom/#/");
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	   
	}

	@When("I enter valid {string} and valid password {string}")
	public void i_enter_valid_and_valid_password(String string, String string2) {
	    // Write code here that turns the phrase above into concrete actions
		driver.findElement(By.xpath("//div[@class='ui fluid left icon input showPlaceholder']//following::input[@placeholder='User name']")).sendKeys(string);
		driver.findElement(By.xpath("//body/div[3]//*[@id='b']")).sendKeys(string2);
		driver.findElement(By.xpath("//body/div[3]//button[@type='submit']")).click();
		

	}

	@Then("I should be able to login")
	public void i_should_be_able_to_login() {
		
	String title = driver.getTitle();
	System.out.println(title);
	
	Assert.assertEquals("ABS Freedom", title);

		
	   
	}

}
