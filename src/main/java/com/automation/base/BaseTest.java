package com.automation.base;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import com.automation.actions.ActionEngine;
import com.automation.utilities.ConfigManager;
import com.automation.utilities.ScreenRecorderUtil;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

/**
 * This class provides Utility Helper functions for the Logging Module
 *
 * @author Narasimha Dontula
 * @since 20/06/2020.
 */
public class BaseTest {
	public static Logger logger = Logger.getLogger(BaseTest.class.getName());
	public static int EXPLICIT_WAIT_TIME;
	public static String browserName;
	public static ITestContext itc;
	public static File file = null;
	public static String startDate = null;
	public static String suiteName = "";
	public static String chartName = null;
	public static ExtentReports extent;
	public static ExtentTest reportLogger;
	public static WebDriver webDriver;
	public Boolean highlightElement = Boolean.valueOf(ConfigManager.getProperty("HIGHLIGHT_ELEMENT").trim());
	public String filePath = System.getProperty("user.dir") + "\\DownloadedFiles";;

	@BeforeSuite(alwaysRun = true)
	public void setup(ITestContext context) throws Throwable {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("MMM_dd_yyyy_z_HH_mm_ss");
			dateFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
			startDate = dateFormat.format(new Date());
			Map<String, String> suiteParameters = context.getCurrentXmlTest().getSuite().getParameters();
			browserName = suiteParameters.get("browser");
			EXPLICIT_WAIT_TIME = Integer.parseInt(suiteParameters.get("ExplicitWaitTime"));
			suiteName = context.getCurrentXmlTest().getSuite().getName().replace(" ", "_").trim() + "_";
			String log4jConfigFile = System.getProperty("user.dir") + File.separator + "Properties" + File.separator + "log4j.properties";
			PropertyConfigurator.configure(log4jConfigFile);
			extent = new ExtentReports(System.getProperty("user.dir") + File.separator + "Reports" + File.separator + suiteName + startDate + ".html");
			if (Boolean.valueOf(ConfigManager.getProperty("deletescreenshorts").trim())) {
				ActionEngine.deleteAllFilesFromFolder();
			}
		} catch (Exception ex) {
		}
	}

	@Parameters({ "browser" })
	@BeforeMethod(alwaysRun = true)
	public void reportHeader(Method method, ITestContext ctx, String browser) throws Throwable {
		itc = ctx;
		ScreenRecorderUtil.startRecording(method.getName());
		if (browser.equals("chrome")) {
			System.setProperty("webdriver.chrome.driver", "C:\\Selenium\\chromedriver_win32 (1)\\chromedriver.exe");
			System.setProperty("webdriver.chrome.silentOutput", "true");
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--disable-application-cache");
			options.addArguments("--disable-notifications");
			options.addArguments("--disable-extensions");
			options.addArguments("--disable-infobar");
			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("download.default_directory", filePath);
			chromePrefs.put("plugins.always_open_pdf_externally", true);
			options.setExperimentalOption("prefs", chromePrefs);
			webDriver = new ChromeDriver(options);
		} else if (browser.equalsIgnoreCase("ie")) {

		} else if (browser.equalsIgnoreCase("firefox")) {

		}
		try {
			webDriver.manage().window().maximize();
			webDriver.manage().deleteAllCookies();
			webDriver.manage().timeouts().implicitlyWait(EXPLICIT_WAIT_TIME, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterMethod
	public void flush() {
		try {
			ScreenRecorderUtil.stopRecording();
		} catch (Exception e) {
			e.printStackTrace();
		}
		extent.flush();
		webDriver.close();
	}

	@AfterTest
	public void teardown() throws Exception {
		webDriver.quit();
	}

	@AfterSuite
	public void SuiteClose() {

	}
}
