package abs.combined.page;

import org.openqa.selenium.By;

import abs.framework.base.BasePage;

public class QuoteOverviewPage extends BasePage {

	public By requestForQuoteBtn = By.xpath("//button[text()='Request for Quote']");
	public By quoteOverviewBreadCrub = By.xpath("//a[text()='Quotes Overview']");
		
}
