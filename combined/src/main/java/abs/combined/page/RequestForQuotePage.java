package abs.combined.page;

import org.openqa.selenium.By;

import abs.framework.base.BasePage;

public class RequestForQuotePage extends BasePage {

	public By reviewTypeDrpDwn = By.xpath("//label[text()='Review Type']/../div[@id='reviewtype-select']/input");
	public By engineeringDptDrpDwn = By.xpath("//label[text()='Engineering Office']/../div[@id='enggofc-select']/input");
	public By backBtn = By.xpath("//button[text()='Back']");
	
	
	public By getRequestorContactXpath(String section) {
		@SuppressWarnings("unused")
		By requestorContact = By.xpath("//*[text()='"+section+":']/following-sibling::div[1]");
		return requestorContact;
	}
	
}
