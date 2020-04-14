package abs.combined.page;

import org.openqa.selenium.By;

import abs.framework.base.BasePage;

public class VesselOverviewPage extends BasePage {
	
	
	public By VesselOverviewTab = By.xpath("//div[@class='large screen only three wide computer three wide large screen three wide widescreen column sidebarParent']//a[@class='active item']/i");
	
	
	/*	----------------Vessel Designation------------------  */
	public By VesselDsgntnIMO = By.xpath("//div[@id='CARD_DESIGNATION']//parent::div//div[2]//table/tbody/tr[1]/td[2]"); 
	public By VesselDsgntnFlag = By.xpath("//div[@id='CARD_DESIGNATION']//parent::div//div[2]//table/tbody/tr[2]/td[2]");
}
