package abs.combined.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import abs.framework.base.BasePage;

public class VirtualVesselPage extends BasePage {

	public By searchVesselField = By.xpath("//div[@class='ui icon input']/input");
	public By clkSearchIcon = By.xpath("//i[@title='Search']");
	
	public By selectRslt(String ClassNum) {
		 By selectSrchRslt = By.xpath("//td[contains(text(),'" + ClassNum + "')]");
		 return selectSrchRslt;
	}

}