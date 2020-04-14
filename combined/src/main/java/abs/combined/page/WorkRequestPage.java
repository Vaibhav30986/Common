package abs.combined.page;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import abs.framework.base.BasePage;

public class WorkRequestPage extends BasePage {

	public By selectWorkOrderDownArrow = By.xpath(
			"//div[@class='ui small fluid icon input showPlaceholder pos-rel multiColInput']//following::i[@class='chevron down icon inlineBlock-T']");

	public By searchVesselField = By.xpath("//input[@placeholder='Search...']");

	public By clickVesselSearch = By.xpath("//input[@placeholder='Search...']//following::i[1]");
	public By clkCreateNewRequest  = By.xpath("//button/i");
	public By poNumbe = By.id("abs_customer_wo_purchase_order");
	public By surveyLoactionDrpDwn = By.xpath("//div[@id='location']/input");
	public By selectLocation 	   = By.xpath("//div[@id='location']//div[@role='option'][2]");
	public By clkRequesterName	   = By.id("requester_company");
	public By searchRequester      = By.xpath("//input[@placeholder='Requester Search...']");
	public By srchIconRequester    = By.xpath("//input[@placeholder='Requester Search...']//following::i");
	public By selectRequester	   = By.xpath("//div[@id=\"pageContainer\"]//table//tr[2]/td[1]");
	public By clkAssignToMe	       = By.xpath("//button[contains(text(),'Assign to Me')]");
	public By clkSubmit            = By.xpath("//button[@id='SUBMIT']");
	public By loginUserName        = By.xpath("//a[@class='active link item']//b");
	public By assignedSurveyorName = By.xpath("//div[@class='assignedSurveyours']//div[@class='nine wide column pd5-R']");
	public By stateOfWorkOrder     = By.xpath("//div[@class='fs20 col1 mrg10-B']");
	public By textAssignedState    = By.xpath("//div[contains(text(),'Assigned')]");
	
	public void selectWorkRequestTypeFromDrpDwn(String WorkReqType) {
		driver.findElement(
				By.xpath("//div[@class='mrg5-B pd5 pointer hoverTyp1'][contains(text(),'" + WorkReqType + "')]"))
				.click();
	}

	public By selectVesselFromSrchRslt(String Vessel) {
		By srchRslt = By.xpath("//td[contains(text(),'"+Vessel+"')]");
		return srchRslt;
	}
	
	public By selectSurveyName (String Survey) {
		
		   By selectTask           = By.xpath("//div[contains(text(),'"+Survey+"')]//following::td//input");
		   System.out.println(selectTask.toString());
		return selectTask;
		
	}
	

}
