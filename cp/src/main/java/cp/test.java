package cp;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class test {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub

		
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\src\\test\\resources\\drivers\\chromedriver.exe");
		 
		// Initialize browser
		WebDriver driver=new ChromeDriver();
		 
		// Open facebook
		driver.get("https://portaluat.eagle.org/portal//#/");
		
			 
		// Maximize browser
		 
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		
		driver.findElement(By.xpath("//input[@type='email']")).sendKeys("mtm");
		driver.findElement(By.xpath("//input[@type='password']")).sendKeys("abc12345");
		driver.findElement(By.xpath("//button[@type='button']")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("//ul[@class='custom-scrollbar list-unstyled']//*[text()='Access Manager']")).click();
		
		
		List<WebElement> users = driver.findElements(By.xpath("//div[@class='dd']/div/div"));
		
		System.out.println(users.size());
		
		
		driver.close();
		driver.quit();
		
		
		
	}

}
