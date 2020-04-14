package abs.combined.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import abs.framework.base.BasePage;

public class CommonPage extends BasePage {

	public By loginUserName = By.xpath(
			"//div[@class='ui fluid left icon input showPlaceholder']//following::input[@placeholder='User name']");
	public By loginPwd = By.xpath("//body/div[3]//*[@id='b']");
	public By loginSubmit = By.xpath("//body/div[3]//button[@type='submit']");
	
	public By navigateToDashboard = By.xpath("//a[contains(text(),'Dashboard')]");
	public By navigateToWorkOrder = By.xpath("//a[contains(text(),'Work Orders')]");
	public By navigateToVirtualVessel = By.xpath("//div[@class='ui secondary stackable right floated menu']/a[contains(text(),'Virtual Vessel')]");
	
			
	
	public By LogoutIcon = By.xpath("//*[@id=\"TopBar\"]//i[@title='User Information']/i[@class='user circular inverted icon']");
	public By Logout = By.xpath("//a[contains(text(),'Sign Out')]/i");

}
