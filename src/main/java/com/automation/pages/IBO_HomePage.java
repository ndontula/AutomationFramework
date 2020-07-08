package com.automation.pages;

import org.openqa.selenium.By;
import com.automation.actions.ActionEngine;
import com.relevantcodes.extentreports.LogStatus;

public class IBO_HomePage extends ActionEngine {
    private By userName = By.id("username");
    private By passWord = By.id("password");
    private By loginButton = By.id("Login");
   /* private By username = By.xpath("//input[@id='username']");
    private By password = By.xpath("//input[@id='password']");
    private By ibProgramsLink = By.xpath("//a[contains(text(),'IB Programmes')]");
    private By serachTextFiled = By.xpath("//div[@class='searchBoxClearContainer']/input[@id='phSearchInput']");
    private By serachButton = By.xpath("//div[@id='searchButtonContainer']");
    private By serachText = By.xpath("//div[@id='searchButtonContainer']");
    private By userNameMS = By.id("PimsLogin:IBPortalSiteTemplate:loginForm:username");
	private By passWordMS = By.id("PimsLogin:IBPortalSiteTemplate:loginForm:password");
	private By loginButtonMS = By.id("PimsLogin:IBPortalSiteTemplate:loginForm:loginButton");
    private By environemnt = By.xpath("//div[@class='msgContent']/span");
    private By primaryLanguageOfInstruction = By.xpath("//span[contains(text(),'Primary Language of Instruction')]/../following-sibling::td/div[@id='00N20000009n290_ileinner']");
    private By saveButton = By.xpath("//td[@class='pbButton']/input[@value=' Save ']");
    private By ibenAssignmentsCount = By.xpath("//div[@class='listRelatedObject Custom100Block']//span[@class='searchFirstCell']");
    private By ibProgramsCount = By.xpath("//div[@class='listRelatedObject Custom8Block']//span");
    private By deleteButton = By.xpath("//td[@class='pbButton']/input[@value='Delete']");
    private By environment = By.xpath("//span[@class='subMsg normalImportance'][2]");
    private By ibProgramslink = By.xpath("//li[@class='wt-IB_Programme']");
    private By ibenAssignmentsFirstLink = By.xpath("//div[@id='Staffing_Event__c_body']/table//tr[2]/th/a");
    private By ibProgramsFirstLink = By.xpath("//div[@id='IB_Programme__c_body']/table//tr[2]/th/a");
    private By documentLink = By.xpath("//h3[contains(text(),'IB Application Attachments')]/ancestor::div[@class='pbHeader']/following-sibling::div//a[contains(text(),'Group_3_Subject_Outline')]");
	private By ibEvent = By.xpath("//li[@id='Campaign_Tab']");
	private By profile = By.id("globalHeaderNameMink");
	private By logout = By.xpath("//a[@title='Logout']");
	private By ibUserName = By.id("PimsLogin:IBPortalSiteTemplate:loginForm:username");
	private By ibPassword = By.id("PimsLogin:IBPortalSiteTemplate:loginForm:password");
	private By ibLogin = By.id("PimsLogin:IBPortalSiteTemplate:loginForm:loginButton");*/
	
    public void launch(String url) {
        try {
            launchUrl(url);
            waitForPageToLoad();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    
    public void login() throws Throwable {
        try {
            type(userName, "ibtest000001+selenium@gmail.com.uat", "User Name");
            type(passWord, "abcd1234!@", "Password");
            click(loginButton, "Login Button");
            Thread.sleep(3000);
            waitForPageToLoad();
        } catch (Exception e) {
            e.getStackTrace()[0].getLineNumber();
            reportLogger.log(LogStatus.FAIL, e.getStackTrace()[0].getLineNumber()+"");
        }
    }
    
    public void applicationLogin() throws Throwable {
    	try {
    		launch("https://ibo--uat.my.salesforce.com/");
    		login();
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
		
	}
}