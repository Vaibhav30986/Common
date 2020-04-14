package abs.combined.page;

import org.openqa.selenium.By;

import abs.framework.base.BasePage;

public class LoginPage extends BasePage {
	
	public By userName = By.xpath("//input[@type='email']");
	public By password = By.xpath("//input[@type='password']");
	
	public By loginButton = By.xpath("//button[@type='button']");
	

}
