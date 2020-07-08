package com.automation.pages;

import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.automation.actions.ActionEngine;
import com.automation.utilities.ConfigManager;
import com.automation.utilities.ReadMail;
import com.relevantcodes.extentreports.LogStatus;

public class AutoScript011L1EmailPage extends ActionEngine {
	private By ibenOpportunityPublishedCheckBox = By.xpath("//img[@id='00N20000009cNGW_chkbox' and @title='Checked']");
	private By primaryLanguageOfInstruction = By.xpath("//span[contains(text(),'Primary Language of Instruction')]/../following-sibling::td/div[@id='00N20000009n290_ileinner']");
	private By selectLanguage = By.id("00N20000009n290");
	private By saveButton = By.xpath("//td[@class='pbButton']/input[@value=' Save ']");
	private By editButton = By.xpath("//td[@id='topButtonRow']/input[@value=' Edit ']");
	private By ibAppStatusBox = By.xpath("//div[@id='00N20000009hh9F_ileinner']");
	private By ibAppStatusListBox = By.id("00N20000009hh9F");
	private By ibAppSaveButton = By.xpath("//td[@id='topButtonRow']/input[@value=' Save ']");
	private By ibAppEditButton = By.xpath("//td[@id='topButtonRow']/input[@value=' Edit ']");
	private By serachTextFiled = By.xpath("//div[@class='searchBoxClearContainer']/input[@id='phSearchInput']");
	private By serachButton = By.xpath("//div[@id='searchButtonContainer']");
	private By mySchooLink = By.xpath("//div[contains(text(), 'My School')]");
	private By uatUserId = By.id("PimsLogin:IBPortalSiteTemplate:loginForm:username");
	private By uatUserPwd = By.id("PimsLogin:IBPortalSiteTemplate:loginForm:password");
	private By uatLoginButton = By.id("PimsLogin:IBPortalSiteTemplate:loginForm:loginButton");
	private By intendToApplyDPButton = By.xpath("//button[@id='intendDpButton']");
	private By entityTextFiledDP = By.xpath("//div[@id='showDpIntend']//div/input[contains(@id, 'PimsDashboard')]");
	private By headOfSchoolSelectBoxDP = By.xpath("//div[@id='showDpIntend']//label[contains(text(), 'Head of School')]/following-sibling::div/select");
	private By programmeCoordinatorDP = By.xpath("//div[@id='showDpIntend']//label[contains(text(), 'Programme Coordinator')]/following-sibling::div/select");
	private By billingContactDP = By.xpath("//div[@id='showDpIntend']//label[contains(text(), 'Billing contact')]/following-sibling::div/select");
	private By examContactDP = By.xpath("//div[@id='showDpIntend']//label[contains(text(), 'Exam Contact')]/following-sibling::div/select");
	private By examSessionDP = By.xpath("//div[@id='showDpIntend']//label[contains(text(), 'Month when the Diploma or Middle Year Programme')]/following-sibling::div/select");
	private By submit = By.xpath("//div[@id='showDpIntend']//div/input[@value='Submit']");
	private By sendLetterButton = By.xpath("//td[@class='pbButton']/input[@value='Send Letter']");
	private By ibProNextButton = By.xpath("//td[@class='pbButton FlowPageBlockBtns']/input[@value='Next']"); 
	private By candidacyNextButton = By.xpath("//td[@class='pbButton FlowPageBlockBtns']/input[@value='Next']");
	private By candadicyFinishButton = By.xpath("//input[@id='p:i:i:f:pb:pbb:finish'][@value='Finish']");
	private By buttonDelete = By.xpath("//td[@id='topButtonRow']/input[@title='Delete' and @type='button']");
	private By linkCourseOutlineReadings = By.xpath("//div[contains(@id, '_00N0O000009rNqQ_body')]//a[2]");
	private By ibEventStartDate =By.xpath("//*[@id='cpn5_ilecell']");
	private By ibEventStartDate1 =By.xpath("//*[@id='cpn5'][@type='text']");
	private By ibEventEndDate =By.xpath("//*[@id='cpn6_ilecell']");
	private By ibEventEndDate1 =By.xpath("//*[@id='cpn6'][@type='text']");
	private By ibEventStatus =By.id("cpn3_ilecell");
	private By ibEventSelectStatus =By.xpath("//*[@id='cpn3']");
	private By ibEventSaveButton =By.xpath("//td[@class='pbButton']/input[@value=' Save ']");
	
	public void forceSubmitRequestForCandidacy(String program, String school, String rfcEvent, String ccEvent) throws Throwable{
		boolean flag =false;
		try{
			String startDate =  getDateAfterDays("M/d/yyyy", 0);
			String endDate =  getDateAfterDays("M/d/yyyy", 7);
			String oneYear =  getDateAfterDays("M/d/yyyy", 365);
			By ccEventStartDate = By.xpath("//th/a[text()='"+ccEvent+"']/ancestor::tr/td[text()='"+startDate+"']");
			By ccEventEndDate = By.xpath("//th/a[text()='"+ccEvent+"']/ancestor::tr/td[text()='"+oneYear+"']");
			By rfcEventStartDate = By.xpath("//th/a[text()='"+rfcEvent+"']/ancestor::tr/td[text()='"+startDate+"']");
			By rfcEventEndDate = By.xpath("//th/a[text()='"+rfcEvent+"']/ancestor::tr/td[text()='"+endDate+"']");
			forceSubmitAFC(program, school, rfcEvent);
			navigateBack();
			Thread.sleep(2000);
			waitForPageToLoad();
			verifyEventIsPublished(ccEvent);
			navigateBack();
			waitForPageToLoad();
			// Verify dates
			scrollPageIntoView(ccEventStartDate);
			isElementPresent(ccEventStartDate);
			isElementPresent(ccEventEndDate);
			isElementPresent(rfcEventStartDate);
			isElementPresent(rfcEventEndDate);
			flag=true;
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			if (flag) {
				reportLogger.log(LogStatus.PASS, " RFC force submission is completed and verified dates ");
			} else {
				reportLogger.log(LogStatus.FAIL, " RFC force submission failed "+reportLogger.addScreenCapture(getScreenshot(webDriver, " RFC force submission failed ")));
			}
		}
	}
	public void forceSubmitAFC(String program, String school, String event) throws Throwable{
		boolean flag = false;
		try {
			searchForSchool(school);
			waitForPageToLoad();
			click(By.xpath("//a[text()='" + school + "']"),"Click on Account  : " + school);
			waitForPageToLoad();
			click(By.xpath("//a[text()='" + program + "']"), "select the program "+program);
			waitForPageToLoad();
			updatePrimaryLanguageOfInstruction("English");
			waitForPageToLoad();
			clickonIBApplication(school);
			waitForPageToLoad();
			updateIBAppStatus("Submitted");
			waitForPageToLoad();
			clickIBAppSaveButton();
			Thread.sleep(5000);
			waitForPageToLoad();
			// Navigate back to program page
			navigateBack();
			Thread.sleep(3000);
			waitForPageToLoad();
			verifyEventIsPublished(event);
			flag = true;
			} catch (Throwable e) {
				e.printStackTrace();
			}finally{
				if (flag) {
					reportLogger.log(LogStatus.PASS, " AFC force submission is completed ");
				} else {
					reportLogger.log(LogStatus.FAIL, " AFC force submission is failed "+reportLogger.addScreenCapture(getScreenshot(webDriver, " AFA force submission failed ")));
				}
			}
	}
	
	public void verifyEventIsPublished(String event) throws Throwable {
		click(By.xpath("//a[text()='"+event+"']"), event);
		Thread.sleep(2000);
		waitForPageToLoad();
		refreshPage();
		Thread.sleep(2000);
		waitForPageToLoad();
		verifyIBENOpportunityPublishedCheckBox();
	}
	public void verifyIBENOpportunityPublishedCheckBox() throws Throwable{		
		try {
			scrollPageIntoView(ibenOpportunityPublishedCheckBox);
			verifyCheckboxStatus(ibenOpportunityPublishedCheckBox, "IBEN Opportunity Published Checkbox");
		}catch(Throwable e) {
			e.printStackTrace();
		}
	}
	
	public void verifyCheckboxStatus(By by, String locatorName) throws Throwable {
		boolean flag = false;
		try {
			refreshPage();
			waitForElementPresent(by, locatorName);
			highlightElement(getWebElement(by));
			flag = getWebElement(by).isDisplayed();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (flag) {
				reportLogger.log(LogStatus.PASS, " Successfully selected element " + locatorName);
			} else {
				reportLogger.log(LogStatus.FAIL, " Falied to locate element " + locatorName
						+ reportLogger.addScreenCapture(getScreenshot(webDriver, locatorName)));
			}
		}
	}
	
	public void updatePrimaryLanguageOfInstruction(String language) throws Throwable {
		try {
			doubleClick(primaryLanguageOfInstruction, "Click on Primary Language of Instruction");
			selectByValue(selectLanguage, "English", "Primary Language of Instruction");
			click(saveButton, "Save button");
			waitForVisibilityOfElementLocated(editButton, "Edit Button", 30);
		} catch(Exception e) {
			e.printStackTrace();
			reportLogger.log(LogStatus.FAIL, "Unable to create fields"+ e.toString() +reportLogger.addScreenCapture(getScreenshot(webDriver, "Error_Type_Lead")) );
		}
	}
	
	public void clickonIBApplication(String School) throws Throwable {
		try {
			jsClick(ibApplication( School),"Click on "+School);
		} catch(Exception e) {
			e.printStackTrace();
			reportLogger.log(LogStatus.FAIL, "Unable to create fields"+ e.toString() +reportLogger.addScreenCapture(getScreenshot(webDriver, "Error_Type_Lead")) );
		}
	}
	public By ibApplication(String program) {
		return By.xpath("//a[contains(text(),"+ "'"+ program + " Application for Candidacy'"+ ")]");
	}
	public void updateIBAppStatus(String Status) throws Throwable {		
		try {
			doubleClick(ibAppStatusBox,"IB App Status Feild");
			selectByVisibleText(ibAppStatusListBox, Status, Status+" Selected in IB App Status list box");
		} catch(Exception e) {
			e.printStackTrace();
			reportLogger.log(LogStatus.FAIL, "Unable to create fields"+ e.toString() +reportLogger.addScreenCapture(getScreenshot(webDriver, "Error_Type_Lead")) );
		}
	}
	
	public void clickIBAppSaveButton(){
		try {
			click(ibAppSaveButton, "Save button");
			waitForVisibilityOfElementLocated(ibAppEditButton, "Edit Button", 30);
		}catch(Throwable e) {
			e.printStackTrace();
		}		
	}
	
    public void searchForSchool(String school) {
        try {
        	click(serachTextFiled,"Searching " + school);
            type(serachTextFiled, school, "Searching " + school);
            click(serachButton, "Click on Serach Button");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    
	public void intendToApplyDiplomaProgram(String username, String password) throws Throwable {
		try {
			uatPortalLogin(username, password);
			waitForPageToLoad();
			click(mySchooLink, "My School tile");
			waitForPageToLoad();
			applyForDP();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public void uatPortalLogin(String username, String password) throws Throwable {
		try {
			webDriver.manage().deleteAllCookies();
			launchUrl(ConfigManager.getProperty("uatportalurl").trim());
			waitForPageToLoad();
			click(uatUserId, " Click on Username text filed ");
			type(uatUserId, username, " User Name ");
			click(uatUserPwd, " Click on password text field ");
			type(uatUserPwd, password, " Password filed ");
			click(uatLoginButton, " Click on Login button ");
			Thread.sleep(3000);
			waitForPageToLoad();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void applyForDP() throws Throwable {
		scrollPageIntoView(intendToApplyDPButton);
		click(intendToApplyDPButton, "Intend To ApplyDPButton");
		waitForPageToLoad();
		type(entityTextFiledDP, "DP", "DP");
		selectByIndex(headOfSchoolSelectBoxDP, 1, "Head Of school");
		selectByIndex(programmeCoordinatorDP, 2, "Program Coordinator");
		selectByIndex(billingContactDP, 1, "Billing contact");
		selectByIndex(examContactDP, 2, "Exam Contact");
		scrollPageIntoView(examSessionDP);
		selectByIndex(examSessionDP, 1, "Exam Session");
		scrollPageIntoView(submit);
		click(submit, "Submit Button");
		waitForPageToLoad();
	}
	public void clickOnSendLetter() throws Throwable
	{
		try {
			click(sendLetterButton,"send Letter in Program");
			waitForElementPresent(ibProNextButton, "Next Button");
			click(ibProNextButton,"Next Button");
			waitForElementPresent(candidacyNextButton, "Next Button");
			click(candidacyNextButton,"Next Button");
			waitForElementPresent(candadicyFinishButton, "Finish Button");
			click(candadicyFinishButton,"Finish Button");
			waitForPageToLoad();
			Thread.sleep(5000);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public void dataCleanUp(String program, String school) throws Throwable{
		/*	This XPath is for element whose attribute value ends with a specific string
		 * 	Syntax: //div[substring(@tagname,string-length(@tagname) -string-length('Destination') +1) = 'Destination']
		 * 	Ex.: "Candidacy Consultation DP Automation School_E2E" is the event link and it ends with text DP Automation School_E2E
		 *	//a[substring(text(),string-length('DP Automation School_E2E')-string-length(text())+1)='DP Automation School_E2E']
		 * 	Above xpath returns all elements whose value ends with a specific string "DP Automation School_E2E"
		 */		
		By linkEvents =  By.xpath("//a[substring(text(),string-length(text()) -string-length('"+program+"')+1)='"+program+"']");
		boolean flag = true;
		int eventsCout = 0;
		try{
			searchForSchool(school);
			click(selectAccount(school)," Account/School  : " + school);
			By pgrm =  By.xpath("(//a[text()='"+program+"'])[1]");
			int programCount = isElementPresent(pgrm);
			Thread.sleep(5000);
			if(programCount == 1){
				click(pgrm, "program");
				Thread.sleep(5000);
				String currentURL = getCurrentURL();
				deleteCourseOutlineReadings();
				eventsCout = getWebElements(linkEvents).size();
				while(eventsCout > 0){
					List<WebElement> eventsList = getWebElements(linkEvents);
					eventsCout = eventsList.size()-1;
					click(eventsList.get(0), eventsList.get(0).getText());
					// To delete assignment associated to the event if exists
					deleteMyAssignment(program);
					//deleteMyAssignment();
					Thread.sleep(5000);
					updateIBEventDetails("Complete");
					Thread.sleep(5000);
					click(buttonDelete,"Button Delete");
					Thread.sleep(3000);
					acceptAlert();
					Thread.sleep(5000);
					navigateToURL(currentURL);
					Thread.sleep(5000);
				}
				if(eventsCout == 0){
					reportLogger.log(LogStatus.PASS, " Events Successfully Deleted ");
				} else {
					reportLogger.log(LogStatus.FAIL, " Falied to Delete Events");
				}
				refreshPage();
				waitForPageToLoad();
				click(buttonDelete,"Button Delete");
				Thread.sleep(3000);
				flag = acceptAlert();
			}
			Thread.sleep(5000);
		}catch(Throwable e){
			flag = false;
			e.printStackTrace();
		}
		finally {
			if (flag) {
				reportLogger.log(LogStatus.PASS, " DP Program Successfully Deleted ");
			} else {
				reportLogger.log(LogStatus.FAIL, " Falied to Delete DP Program");
			}
		}
	}
	
    public By selectAccount(String schoolName) {
        return By.xpath("//a[text()='" + schoolName + "']");
    }
    
	private boolean deleteCourseOutlineReadings() throws Throwable{
		boolean flag=true;
		int count = 0;
		try {
			count = getWebElements(linkCourseOutlineReadings).size();
			while(count>0){
				waitForPageToLoad();
				List<WebElement> courseOutlineReadings = getWebElements(linkCourseOutlineReadings);
				count = courseOutlineReadings.size()-1;
				click(courseOutlineReadings.get(0), " Delete Course Outline Reading ");
				Thread.sleep(2000);
				flag = acceptAlert();
				Thread.sleep(3000);
			}
		}catch(Throwable e) {
			e.printStackTrace();
			flag = false;
		}finally {
			if (flag) {
				reportLogger.log(LogStatus.PASS, " Deleted All Course Outline Readings ");
			} else {
				reportLogger.log(LogStatus.FAIL, " Falied to Delete Course Outline Readings ");
			}
		}
		return flag;
	}

	public boolean deleteMyAssignment(String program) throws Throwable{
		boolean flag=true;
		int count = 0;
		By assignment = By.xpath("//a[contains(text(), '"+program+"')]/ancestor::tr/td[1]/a[2]");
		try {
			count = getWebElements(assignment).size();
			while(count>0){
				waitForPageToLoad();
				List<WebElement> assignments = getWebElements(assignment);
				count = assignments.size()-1;
				click(assignments.get(0), " Delete Course Outline Reading ");
				Thread.sleep(2000);
				flag = acceptAlert();
				Thread.sleep(3000);
			}
		}catch(Throwable e) {
			e.printStackTrace();
			flag = false;
		}finally {
			if (flag) {
				reportLogger.log(LogStatus.PASS, "My Assignment Successfully Deleted");
			} else {
				reportLogger.log(LogStatus.FAIL, " Falied to Delete My Assignment");
			}
		}
		return flag;
	}
	public void updateIBEventDetails(String status) throws Throwable {
		boolean flag = false;
		try {
			String startDate = getDateAfterDays("M/d/yyyy", 20);
			String endDate = getDateAfterDays("M/d/yyyy", 40);
			doubleClick(ibEventStartDate, "start date");
			type(ibEventStartDate1, startDate, "start Date : " + startDate);
			click(ibEventStatus, "Status");
			doubleClick(ibEventEndDate, "end date");
			type(ibEventEndDate1, endDate, "end Date : " + endDate);
			SelectStatus(status);
			click(ibEventSaveButton, "Save Button");
			flag = true;
		} catch (Throwable e) {
			e.printStackTrace();
			flag = false;
		} finally {
			if (flag) {
				reportLogger.log(LogStatus.PASS, " Update IB Event Details Successful ");
			} else {
				reportLogger.log(LogStatus.FAIL, " Update IB Event Details Falied "
						+ reportLogger.addScreenCapture(getScreenshot(webDriver, "Update IB Event Details")));
			}
		}

	}
	
	public void SelectStatus(String Status) {		
		try {
			doubleClick(ibEventStatus, "status");
			selectByVisibleText(ibEventSelectStatus, Status, "Status : "+Status);
		}catch(Throwable e) {
			e.printStackTrace();
		}
	}
	
	public void compareRawtoEmail(String emailContentExcel) throws Throwable {
		String subject = "Sandbox: Update on Application for Candidacy is now available";
		String mailMessage = "";
		Scanner scanner = null;
		ReadMail readMail = new ReadMail();
		mailMessage = readMail.readInvitationMail(subject);
		if (!"".equals(mailMessage)) {
			scanner = new Scanner(mailMessage);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				// process the line
				if (line != null && !"".equals(line.trim())) {
					if (line.contains(emailContentExcel)) {
						System.out.println(line);
						reportLogger.log(LogStatus.PASS, "This line: <b style=\"color:blue;\">'" + line + "'</b>  is correct");
					} else {
						reportLogger.log(LogStatus.FAIL, "This line: <b style=\"color:red;\">'" + line + "'</b> is not correct");
					}
				}
			}
		}
	}
}
