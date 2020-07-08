package com.automation.scripts;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.testng.annotations.Test;

import com.automation.actions.ActionEngine;
import com.automation.pages.AutoScript011L1EmailPage;
import com.automation.pages.IBO_HomePage;
import com.automation.utilities.ConfigManager;
import com.automation.utilities.ExcelLib;
import com.automation.utilities.ReadMail;

public class AutoScript011L1Email extends ActionEngine {
	String path = System.getProperty("user.dir") + File.separator + "TestData" + File.separator+ "L1_Email_Content.xlsx";
	AutoScript011L1EmailPage autoScript011L1EmailPage = new AutoScript011L1EmailPage();
	ExcelLib excelLib = new ExcelLib(path);
	ReadMail readMail = new ReadMail();
	IBO_HomePage homepage = new IBO_HomePage();
	
	private String appURL = ConfigManager.getProperty("url").trim();
	private String hosUser = ConfigManager.getProperty("e2ehosuser").trim();
	private String hosUserPwd = ConfigManager.getProperty("e2ehosuserpwd").trim();
	private String program = ConfigManager.getProperty("e2eprogram").trim();
	private String school = ConfigManager.getProperty("e2eschool").trim();
	private String rfcEvent = ConfigManager.getProperty("e2erfcevent").trim();
	private String ccEvent = ConfigManager.getProperty("e2eccevent").trim();

	@Test
	public void L1Email(Method method) throws Throwable {
		try {
			reportLogger = extent.startTest("AutoScript011L1Email","AutoScript011L1Email");
			HashMap<String,String> testData = excelLib.getTestCaseDataMultipleSheets();
			String emailContentExcel = testData.get("L1_Email");
			System.out.println(emailContentExcel);
			dataCleanUp();
			autoScript011L1EmailPage.intendToApplyDiplomaProgram(hosUser, hosUserPwd);
			applicationLogin();
			autoScript011L1EmailPage.forceSubmitRequestForCandidacy(program, school, rfcEvent, ccEvent);
			//sendL1();
			autoScript011L1EmailPage.clickOnSendLetter();
			autoScript011L1EmailPage.compareRawtoEmail(emailContentExcel);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dataCleanUp() throws Throwable {
		applicationLogin();
		autoScript011L1EmailPage.dataCleanUp(program, school);
	}

	public void applicationLogin() throws Throwable {
		homepage.launch(appURL);
		homepage.login();
	}
}





