/**
 * This is the library class for CLC application.
 * 
 */

package org.customerportal.page;

import org.openqa.selenium.By;
import abs.framework.base.BasePage;

public class LoginPage extends BasePage {
	public By userName = By.xpath("//input[@type='email']");
	public By password = By.xpath("//input[@type='password']");
	
	public By loginButton = By.xpath("//button[@type='button']");
	
	private void syso() {
		// TODO Auto-generated method stub
       // i am adding a method
	}

	private void syos1() {
		// TODO Auto-generated method stub
// I am adding a method - by vaibhav
	}
	
}
