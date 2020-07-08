package com.automation.actions;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import com.automation.base.BaseTest;
import com.google.common.base.Function;
import com.relevantcodes.extentreports.LogStatus;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This class provides Utility Helper functions for the Logging Module.
 *
 * @author Narasimha Dontula
 * @since 20/06/2020.
 */
public class ActionEngine extends BaseTest {

public void refreshPage() throws Throwable {
  webDriver.navigate().refresh();
  waitForPageToLoad();
  }

  public void navigateBack() throws Throwable {
    webDriver.navigate().back();
    waitForPageToLoad();
  }

  public WebElement getWebElement(By locator) {
    return webDriver.findElement(locator);
  }

  public List<WebElement> getWebElements(By locator) throws Throwable {
    List<WebElement> webElements = webDriver.findElements(locator);
    return webElements;
  }

  public void scrollPage(int pixelValue) throws Throwable {
    // + value for scroll down and -ve value for scroll up
    JavascriptExecutor js = (JavascriptExecutor) webDriver;
    js.executeScript("window.scrollBy(0," + pixelValue + ")", "");
  }

  public void scrollPageIntoView(By locator) throws Throwable {
    JavascriptExecutor js = (JavascriptExecutor) webDriver;
    WebElement element = getWebElement(locator);
    // This will scroll the page Horizontally till the element is found
    js.executeScript("arguments[0].scrollIntoView();", element);
  }

  public int getRandomNumberInRange(int min, int max) throws Throwable {
    if (min >= max) {
      throw new IllegalArgumentException("max must be greater than min");
    }

    Random r = new Random();
    return r.nextInt((max - min) + 1) + min;
  }

  public String getRandomAlphaNumericString(int count) throws Throwable {
    String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    StringBuilder builder = new StringBuilder();
    while (count-- != 0) {
      int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
      builder.append(ALPHA_NUMERIC_STRING.charAt(character));
    }
    return builder.toString();
  }

  public void highlightElement(WebElement webElement) {
    if (highlightElement) {
      // Create object of a JavascriptExecutor interface
      JavascriptExecutor js = (JavascriptExecutor) webDriver;
      // use executeScript() method and pass the arguments
      // Here i pass values based on css style. Yellow background color
      // with solid red color border.
      js.executeScript(
          "arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');",
          webElement);
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      js.executeScript("arguments[0].setAttribute('style','border: solid 2px white');", webElement);
    }
  }

  public static String getDateAfterDays(String format, int futureDays) {
    String retDate = null;
    try {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(new Date());
      calendar.add(Calendar.DATE, futureDays);
      SimpleDateFormat objSDF = new SimpleDateFormat(format);
      retDate = objSDF.format(calendar.getTime());
    } catch (Exception e) {
    }
    return retDate;
  }

  public static String getDateAfterMonths(String format, int futureMonths) {
    String retDate = null;
    try {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(new Date());
      calendar.add(Calendar.MONTH, futureMonths);
      SimpleDateFormat objSDF = new SimpleDateFormat(format);
      retDate = objSDF.format(calendar.getTime());
    } catch (Exception e) {
    }
    return retDate;
  }

  public static String getDateAfterYears(String format, int futureYear) {
    String retDate = null;
    try {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(new Date());
      calendar.add(Calendar.YEAR, futureYear);
      String strDateFormat = format;
      SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);
      retDate = objSDF.format(calendar.getTime());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return retDate;
  }

  public static String getScreenshot(WebDriver driver, String screenshotName) throws Throwable {
    String destination = System.getProperty("user.dir");
    try {
      // below line is just to append the date format with the screenshot
      // name to
      // avoid duplicate names
      String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
      TakesScreenshot ts = (TakesScreenshot) webDriver;
      File source = ts.getScreenshotAs(OutputType.FILE);
      destination = destination + "/FailedTestsScreenshots/" + screenshotName + dateName + ".png";
      File finalDestination = new File(destination);
      FileUtils.copyFile(source, finalDestination);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return destination;
  }

  public boolean dragAndDrop(By draggable, By droppable) throws Throwable {
    boolean flag = false;
    WebElement drag = null;
    try {
      Actions action = new Actions(webDriver);
      drag = getWebElement(draggable);
      highlightElement(drag);
      WebElement drop = getWebElement(droppable);
      action.dragAndDrop(drag, drop).build().perform();
      highlightElement(drop);
      flag = true;
    } catch (Exception e) {
      e.printStackTrace();
      flag = false;
    } finally {
      if (flag) {
        reportLogger.log(LogStatus.PASS, drag.getText() + " is dropped to target as expected");
      } else
        reportLogger.log(LogStatus.FAIL,
            drag.getText() + " couldn't be dropped to target as expected");
    }
    return flag;
  }

  public void click(By locator, String locatorName) throws Throwable {
    boolean flag = false;
    WebElement webElement = null;
    try {
      waitForVisibilityOfElementLocated(locator, locatorName, 1);
      webElement = getWebElement(locator);
      highlightElement(webElement);
      webElement.click();
      flag = true;
    } catch (Exception e) {
      flag = false;
      e.printStackTrace();
    } finally {
      if (flag) {
        reportLogger.log(LogStatus.PASS,
            "Sucessfully clicked on '<b style=\"color:blue;\">" + locatorName + "</b>' element");
      } else
        reportLogger.log(LogStatus.FAIL, "Unable to click on element " + locatorName
            + reportLogger.addScreenCapture(getScreenshot(webDriver, locatorName)));
    }
  }

  public void click(WebElement webElement, String locatorName) throws Throwable {
    boolean flag = false;
    try {
      highlightElement(webElement);
      webElement.click();
      flag = true;
    } catch (Exception e) {
      flag = false;
      e.printStackTrace();
    } finally {
      if (flag) {
        reportLogger.log(LogStatus.PASS,
            "Sucessfully clicked on '<b style=\"color:blue;\">" + locatorName + "</b>' element");
      } else
        reportLogger.log(LogStatus.FAIL, "Unable to click on element " + locatorName
            + reportLogger.addScreenCapture(getScreenshot(webDriver, locatorName)));
    }
  }

  public void jsClick(By locator, String locatorName) throws Throwable {
    boolean flag = false;
    try {
      WebElement webElement = getWebElement(locator);
      JavascriptExecutor executor = (JavascriptExecutor) webDriver;
      highlightElement(webElement);
      executor.executeScript("arguments[0].click();", webElement);
      flag = true;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (flag) {
        reportLogger.log(LogStatus.PASS, "JavaScript click is sucessful on element " + locatorName);
      } else
        reportLogger.log(LogStatus.FAIL, "JavaScript click is unsucessful on element " + locatorName
            + reportLogger.addScreenCapture(getScreenshot(webDriver, locatorName)));
    }
  }

  public void staleClick(By locator, String locatorName) {
    try {
      WebElement element = getWebElement(locator);
      JavascriptExecutor executor = (JavascriptExecutor) webDriver;
      executor.executeScript("arguments[0].click();", element);
    } catch (org.openqa.selenium.StaleElementReferenceException ex) {
      WebElement element = getWebElement(locator);
      JavascriptExecutor executor = (JavascriptExecutor) webDriver;
      executor.executeScript("arguments[0].click();", element);
    }
  }

  public boolean doubleClick(By locator, String locatorName) throws Throwable {
    boolean flag = false;
    try {
      waitForVisibilityOfElementLocated(locator, locatorName, 30);
      Actions action = new Actions(webDriver);
      WebElement webElement = getWebElement(locator);
      highlightElement(webElement);
      action.doubleClick(webElement).perform();
      flag = true;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (flag) {
        reportLogger.log(LogStatus.PASS, "Clicked on Text filed '" + locatorName + "'");
      } else {
        reportLogger.log(LogStatus.FAIL, "Unable to double click on '" + locatorName + "'"
            + reportLogger.addScreenCapture(getScreenshot(webDriver, locatorName)));
      }
    }
    return flag;
  }

  public void type(By locator, String testdata, String locatorName) throws Throwable {
    boolean isTextEntered = false;
    try {
      fluentWaitForElementPresent(locator, locatorName, 30, 1);
      WebElement webElement = getWebElement(locator);
      highlightElement(webElement);
      webElement.clear();
      webElement.sendKeys(testdata);
      isTextEntered = true;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (isTextEntered) {
        reportLogger.log(LogStatus.PASS, "'<b style=\"color:blue;\"> " + testdata
            + "</b>' is entered into " + locatorName + " field");
      } else {
        reportLogger.log(LogStatus.FAIL, "Unable to type '" + testdata + "' into " + locatorName
            + " field. " + reportLogger.addScreenCapture(getScreenshot(webDriver, "Error_Type")));
      }
    }
  }

  public boolean doubleClickAndType(By locator, String value, String locatorName) throws Throwable {
    boolean flag = false;
    try {
      WebElement webElement = getWebElement(locator);
      Actions action = new Actions(webDriver);
      highlightElement(webElement);
      action.doubleClick(webElement).sendKeys(value).perform();
      flag = true;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (flag) {
        reportLogger.log(LogStatus.PASS, "Clicked on Text filed '" + locatorName + "'");
      } else {
        reportLogger.log(LogStatus.FAIL, "Unable to double click on '" + locatorName + "'"
            + reportLogger.addScreenCapture(getScreenshot(webDriver, locatorName)));
      }
    }
    return flag;
  }

  // revisit this method
  public static void sendKeyBoardData(String strText) throws InterruptedException {
    try {
      StringSelection stringSelection = new StringSelection(strText);
      Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
      Robot robot = new Robot();
      robot.setAutoDelay(1000);
      robot.keyPress(KeyEvent.VK_TAB);
      robot.keyRelease(KeyEvent.VK_TAB);
      Thread.sleep(1000);
      robot.keyPress(KeyEvent.VK_CONTROL);
      robot.keyPress(KeyEvent.VK_CONTROL);
      robot.keyPress(KeyEvent.VK_V);
      robot.keyRelease(KeyEvent.VK_CONTROL);
      robot.keyRelease(KeyEvent.VK_V);
      robot.keyPress(KeyEvent.VK_ENTER);
    } catch (AWTException e) {
      e.printStackTrace();
    }
  }

  public void switchToWindowByIndex(int windowIndex) throws Throwable {
    boolean flag = false;
    try {
      ArrayList<String> tabs = new ArrayList<String>(webDriver.getWindowHandles());
      if (windowIndex < tabs.size()) {
        webDriver.switchTo().window(tabs.get(windowIndex));
        flag = true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (flag) {
        reportLogger.log(LogStatus.PASS, " Swithed to window " + windowIndex + " successfully ");
      } else {
        reportLogger.log(LogStatus.FAIL, " Swithed to window " + windowIndex + " failed "
            + reportLogger.addScreenCapture(getScreenshot(webDriver, "Error_Type")));
      }
    }
  }

  public void closeWindowByIndex(int windowIndex) throws Throwable {
    boolean flag = false;
    try {
      ArrayList<String> tabs = new ArrayList<String>(webDriver.getWindowHandles());
      if (windowIndex < tabs.size()) {
        webDriver.switchTo().window(tabs.get(windowIndex));
        webDriver.close();
        flag = true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (flag) {
        reportLogger.log(LogStatus.PASS, " Window " + windowIndex + " closed successfully ");
      } else {
        reportLogger.log(LogStatus.FAIL, " Window " + windowIndex + " failed close "
            + reportLogger.addScreenCapture(getScreenshot(webDriver, "Error_Type")));
      }
    }
  }

  // switch to frames based on index
  public void switchToFrameByIndex(int frameIndex) throws Throwable {
    boolean flag = false;
    try {
      webDriver.switchTo().frame(frameIndex);
      flag = true;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (flag) {
        reportLogger.log(LogStatus.PASS, " Swithed to Frame " + frameIndex + " successfully ");
      } else {
        reportLogger.log(LogStatus.FAIL, " Swithed to Frame " + frameIndex + " failed "
            + reportLogger.addScreenCapture(getScreenshot(webDriver, "Error_Type")));
      }
    }
  }

  public boolean isElementPresent(By locator, String locatorName) throws Throwable {
    boolean flag = false;
    try {
      JavascriptExecutor js = (JavascriptExecutor) webDriver;
      WebElement webElement = getWebElement(locator);
      highlightElement(webElement);
      js.executeScript("arguments[0].setAttribute('style','border: solid 5px yellow;');",
          webElement);
      flag = true;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (flag) {
        reportLogger.log(LogStatus.PASS, locatorName + " is present on the page");
      } else {
        reportLogger.log(LogStatus.FAIL, locatorName + " is not present on the page" + locatorName
            + reportLogger.addScreenCapture(getScreenshot(webDriver, locatorName)));
      }
    }
    return flag;
  }

  public boolean isElementDisplayed(By locator, String locatorName) throws Throwable {
    boolean flag = false;
    try {
      WebElement webElement = getWebElement(locator);
      flag = webElement.isDisplayed();
      highlightElement(webElement);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (flag) {
        reportLogger.log(LogStatus.PASS, locatorName + " is displayed on the page");
      } else {
        reportLogger.log(LogStatus.FAIL, locatorName + "is not displayed on the page" + locatorName
            + reportLogger.addScreenCapture(getScreenshot(webDriver, locatorName)));
      }
    }
    return flag;
  }

  public void waitForPageToLoad() throws Throwable {
    String s = "";
    Thread.sleep(1000L);
    while (!s.equals("complete")) {
      s = (String) ((JavascriptExecutor) webDriver).executeScript("return document.readyState",
          new Object[0]);
      try {
        Thread.sleep(1000L);
      } catch (InterruptedException var3) {
        throw var3;
      }
    }
  }

  public void waitForElementToBeClickable(By locator, int withTime) throws Throwable {
    try {
      WebDriverWait wait = new WebDriverWait(webDriver, withTime);
      wait.until(ExpectedConditions.elementToBeClickable(locator));
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  public void fluentWaitForElementPresent(By locator, String locatorName, int withTimeout,
      int pollingEvery) throws Throwable {
    boolean flag = false;
    try {
      Wait<WebDriver> wait = new FluentWait<>(webDriver)
          .withTimeout(Duration.ofSeconds(withTimeout))
          .pollingEvery(Duration.ofSeconds(pollingEvery)).ignoring(WebDriverException.class);

      wait.until(new Function<WebDriver, WebElement>() {
        public WebElement apply(WebDriver driver) {
          return getWebElement(locator);
        }
      });
      flag = true;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (flag) {
        reportLogger.log(LogStatus.PASS, "Successfully located element '" + locatorName + "'");
      } else {
        reportLogger.log(LogStatus.FAIL, "Falied to locate element '" + locatorName + "'");
      }
    }
  }

  public boolean waitForVisibilityOfElementLocated(By locator, String locatorName, int timeToWait)
      throws Throwable {
    boolean flag = false;
    try {
      WebDriverWait wait = new WebDriverWait(webDriver, timeToWait);
      wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
      flag = true;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (flag) {
        reportLogger.log(LogStatus.PASS, "Element is visible with the name '" + locatorName + "'");
      } else {
        reportLogger.log(LogStatus.FAIL, "Element is not visible with the name  '" + locatorName
            + "'" + reportLogger.addScreenCapture(getScreenshot(webDriver, locatorName)));
      }
    }
    return flag;
  }

  public boolean waitForElementPresent(By locator, String locatorName) throws Throwable {
    boolean flag = false;
    try {
      WebDriverWait wait = new WebDriverWait(webDriver, 30);
      wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
      flag = true;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (flag) {
        reportLogger.log(LogStatus.PASS, "Locator is found for '" + locatorName + "'");
      } else {
        reportLogger.log(LogStatus.FAIL, "Locator is not found for '" + locatorName + "'"
            + reportLogger.addScreenCapture(getScreenshot(webDriver, locatorName)));
      }
    }
    return flag;
  }

  public boolean hitKeyBoardEnterKey(By locator, String KeyName, String locatorName)
      throws Throwable {
    boolean isKeBoardEnterKeyPressed = false;
    try {
      waitForElementToBeClickable(locator, 30);
      getWebElement(locator).sendKeys(Keys.ENTER);
      isKeBoardEnterKeyPressed = true;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (isKeBoardEnterKeyPressed) {
        reportLogger.log(LogStatus.PASS, "Pressed '" + KeyName + "'");
      } else {
        reportLogger.log(LogStatus.FAIL, "Pressed'" + KeyName + "'"
            + reportLogger.addScreenCapture(getScreenshot(webDriver, locatorName)));
      }
    }
    return isKeBoardEnterKeyPressed;
  }

  // revisit this method
  public static void downloadLocation(String location) throws InterruptedException {
    try {
      StringSelection stringSelection = new StringSelection(location);
      Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
      Robot robot = new Robot();
      robot.setAutoDelay(2000);
      robot.keyPress(KeyEvent.VK_CONTROL);
      robot.keyPress(KeyEvent.VK_V);
      robot.keyRelease(KeyEvent.VK_CONTROL);
      robot.keyRelease(KeyEvent.VK_V);
      robot.keyPress(KeyEvent.VK_ENTER);
    } catch (AWTException e) {
      e.printStackTrace();
    }
  }

  public boolean clickAndWaitForElementPresent(By locator, By waitElement, String locatorName,
      int timeToWait) throws Throwable {
    boolean flag = false;
    try {

      click(locator, locatorName);
      waitForVisibilityOfElementLocated(waitElement, locatorName, timeToWait);
      waitForPageToLoad();
      highlightElement(getWebElement(waitElement));
      flag = true;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (flag) {
        reportLogger.log(LogStatus.PASS,
            "Successfully performed clickAndWaitForElementPresent action");
      } else {
        reportLogger.log(LogStatus.FAIL, "Failed to perform clickAndWaitForElementPresent action"
            + reportLogger.addScreenCapture(getScreenshot(webDriver, locatorName)));

      }
    }
    return flag;
  }

  public boolean selectByIndex(By locator, int index, String locatorName) throws Throwable {
    boolean isByIndexSelected = false;
    try {
      WebElement webElement = getWebElement(locator);
      highlightElement(webElement);
      Select dropDown = new Select(webElement);
      dropDown.selectByIndex(index);
      isByIndexSelected = true;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (isByIndexSelected) {
        reportLogger.log(LogStatus.PASS, " Option at index '<b style=\"color:blue;\"> " + index
            + "</b>' is selected from the drop down " + locatorName);
      } else {
        reportLogger.log(LogStatus.FAIL,
            "Option at index " + index + " is Not selected from the drop dpwn " + locatorName
                + reportLogger.addScreenCapture(getScreenshot(webDriver, locatorName)));
      }
    }
    return isByIndexSelected;
  }

  public boolean selectByValue(By locator, String value, String locatorName) throws Throwable {
    boolean isByValueSelected = false;
    try {
      WebElement webElement = getWebElement(locator);
      highlightElement(webElement);
      Select dropDown = new Select(webElement);
      dropDown.selectByValue(value);
      isByValueSelected = true;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (isByValueSelected) {
        reportLogger.log(LogStatus.PASS, "'<b style=\"color:blue;\">" + value
            + "</b>' is selected from the drop down " + locatorName);
      } else {
        reportLogger.log(LogStatus.FAIL, value + " is not selected from the drop down" + locatorName
            + reportLogger.addScreenCapture(getScreenshot(webDriver, locatorName)));
      }
    }
    return isByValueSelected;
  }

  public boolean selectByVisibleText(By locator, String visibletext, String locatorName)
      throws Throwable {
    boolean isVisibleTestSelected = false;
    try {
      waitForVisibilityOfElementLocated(locator, locatorName, 30);
      WebElement webElement = getWebElement(locator);
      highlightElement(webElement);
      Select dropDown = new Select(webElement);
      dropDown.selectByVisibleText(visibletext);
      isVisibleTestSelected = true;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (isVisibleTestSelected) {
        reportLogger.log(LogStatus.PASS, "'<b style=\"color:blue;\">" + visibletext
            + "'  is selected from the drop down " + locatorName + "</b>'");
      } else {
        reportLogger.log(LogStatus.FAIL, "'" + visibletext + "' is not select from the drop down "
            + locatorName + reportLogger.addScreenCapture(getScreenshot(webDriver, locatorName)));
      }
    }
    return isVisibleTestSelected;
  }

  public void selectAllOptions(By locator, String locatorName) throws Throwable {
    boolean isAllOptionsSelected = false;
    try {

      WebElement webElement = getWebElement(locator);
      highlightElement(webElement);
      Select dropDown = new Select(webElement);
      List<WebElement> allOptions = dropDown.getOptions();
      int totlOptions = allOptions.size();
      for (int i = 0; i < totlOptions; i++) {
        dropDown.selectByIndex(i);
      }
      isAllOptionsSelected = true;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (isAllOptionsSelected) {
        reportLogger.log(LogStatus.PASS,
            "'<b style=\"color:blue;\"> All options are selected from the drop down" + locatorName
                + "</b>'");
      } else {
        reportLogger.log(LogStatus.FAIL, "All options are not selected from the drop down"
            + locatorName + reportLogger.addScreenCapture(getScreenshot(webDriver, locatorName)));
      }
    }
  }

  public boolean acceptAlert() throws Throwable {
    boolean isAlertAccepted = false;
    try {
      WebDriverWait wait = new WebDriverWait(webDriver, 15);
      Alert alert = wait.until(ExpectedConditions.alertIsPresent());
      alert.accept();
      isAlertAccepted = true;
    } catch (NoAlertPresentException ex) {
      ex.printStackTrace();
    } finally {
      if (isAlertAccepted) {
        reportLogger.log(LogStatus.PASS, "<b style=\"color:blue;\">Alet accepted</b>");
      } else {
        reportLogger.log(LogStatus.FAIL, "Failed to accept alert"
            + reportLogger.addScreenCapture(getScreenshot(webDriver, "Failed to accept alert!")));
      }
    }
    return isAlertAccepted;
  }

  public boolean dismissAlert() throws Throwable {
    boolean isAlertDismissed = false;
    try {
      WebDriverWait wait = new WebDriverWait(webDriver, 15);
      Alert alert = wait.until(ExpectedConditions.alertIsPresent());
      alert.dismiss();
      isAlertDismissed = true;
    } catch (NoAlertPresentException ex) {
      ex.printStackTrace();
    } finally {
      if (isAlertDismissed) {
        reportLogger.log(LogStatus.PASS, "Alet dismissed!");
      } else {
        reportLogger.log(LogStatus.FAIL, "Failed to dismiss alert!"
            + reportLogger.addScreenCapture(getScreenshot(webDriver, "Failed to dismiss alert!")));
      }
    }
    return isAlertDismissed;
  }

  public String getTextFromAlert() throws Throwable {
    String alertText = "";
    boolean isAlertPresent = false;
    try {
      WebDriverWait wait = new WebDriverWait(webDriver, 15);
      Alert alert = wait.until(ExpectedConditions.alertIsPresent());
      alertText = alert.getText();
    } catch (NoAlertPresentException ex) {
      ex.printStackTrace();
    } finally {
      if (isAlertPresent) {
        reportLogger.log(LogStatus.PASS, alertText + " is took from alert box");
      } else {
        reportLogger.log(LogStatus.FAIL, "Failed to get message from alert!" + reportLogger
            .addScreenCapture(getScreenshot(webDriver, "Failed to message from alert!")));
      }
    }
    return alertText;
  }

  public void enterTextIntoAlertBox(String text) throws Throwable {
    boolean isTextEnetered = false;
    try {
      WebDriverWait wait = new WebDriverWait(webDriver, 15);
      Alert alert = wait.until(ExpectedConditions.alertIsPresent());
      alert.sendKeys(text);
      isTextEnetered = true;
    } catch (NoAlertPresentException ex) {
      ex.printStackTrace();
    } finally {
      if (isTextEnetered) {
        reportLogger.log(LogStatus.PASS, text + " is entered into alert box");
      } else {
        reportLogger.log(LogStatus.FAIL, "Failed to enter text into alert box" + reportLogger
            .addScreenCapture(getScreenshot(webDriver, "Failed to enter text into alert box")));
      }
    }
  }

  public void launchUrl(String url) throws Throwable {
    boolean flag = false;
    try {
      webDriver.manage().deleteAllCookies();
      webDriver.get(url);
      waitForPageToLoad();
      if (webDriver.getCurrentUrl().contains(url)) {
        flag = true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (flag) {
        reportLogger.log(LogStatus.PASS,
            "URL '<b style=\"color:blue;\">" + url + "'</b> launched sucessfully.");
      } else {
        reportLogger.log(LogStatus.FAIL, "Failed to launch URL " + url
            + reportLogger.addScreenCapture(getScreenshot(webDriver, "URL")));
      }
    }
  }

  public void navigateToURL(String url) throws Throwable {
    boolean flag = false;
    try {
      webDriver.get(url);
      if (webDriver.getCurrentUrl().contains(url)) {
        flag = true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (flag) {
        reportLogger.log(LogStatus.PASS,
            "Navigated to URL '<b style=\"color:blue;\">" + url + "</b>' sucessfully.");
      } else {
        reportLogger.log(LogStatus.FAIL, url + " Failed to launch"
            + reportLogger.addScreenCapture(getScreenshot(webDriver, "URL")));
      }
    }
  }

  /**
   * @throws Throwable
   * @return: It returns current URL of the page.
   */
  public String getCurrentURL() throws Throwable {
    String text = "";
    boolean flag = false;
    try {
      text = webDriver.getCurrentUrl();
      flag = true;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (flag) {
        reportLogger.log(LogStatus.PASS, "Current page URL is '" + text + "'");
      } else {
        reportLogger.log(LogStatus.FAIL, "Failed to get current page URL '" + text + "'");
      }
    }
    return text;
  }

  public int isElementPresent(By locator) throws Throwable {
    int elementsCount = 0;
    boolean flag = false;
    try {
      elementsCount = getWebElements(locator).size();
      if (elementsCount == 1) {
        flag = true;
        highlightElement(getWebElement(locator));
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (flag) {
        reportLogger.log(LogStatus.PASS, "Element located sucessfully.");
      } else {
        reportLogger.log(LogStatus.FAIL, " Locator not present "
            + reportLogger.addScreenCapture(getScreenshot(webDriver, "Element presence")));
      }
    }
    return elementsCount;
  }

  public void mouseHoverOnWebElement(By locator) throws Throwable {
    boolean flag = false;
    String locatorName = "";
    WebElement menuOption = null;
    try {
      // Instantiate Action Class
      Actions actions = new Actions(webDriver);
      // Retrieve WebElement 'locator' to perform mouse hover
      menuOption = getWebElement(locator);
      locatorName = menuOption.getText();
      // Mouse hover menuOption 'locator'
      actions.moveToElement(menuOption).perform();
      flag = true;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (flag) {
        reportLogger.log(LogStatus.PASS, "Done Mouse hover on " + locatorName);
      } else {
        reportLogger.log(LogStatus.FAIL, "Failed to mouse hover on " + locatorName);
      }
    }
  }

  /**
   * @throws Throwable
   * @return: It returns title of the current page.
   */

  public String getTitle() throws Throwable {
    String text = "";
    boolean flag = false;
    try {
      text = webDriver.getTitle();
      flag = true;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (flag) {
        reportLogger.log(LogStatus.PASS, "Title of the page is '" + text + "'");
      } else {
        reportLogger.log(LogStatus.FAIL, "Title of the page is '" + text + "'");
      }
    }
    return text;
  }

  /**
   * Verifies text is present or not
   * 
   * @param text
   *          is to verify on the current page.
   */
  public boolean isTextPresent(String text) throws Throwable {
    boolean flag = false;
    try {
      if ((webDriver.getPageSource()).contains(text)) {
        flag = true;
      } else {
        flag = false;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (flag) {
        reportLogger.log(LogStatus.PASS, "Text is present in the page ");
      } else {
        reportLogger.log(LogStatus.FAIL, "Text is not present in the page ");
      }
    }
    return flag;
  }

  public String getText(By locator, String locatorName) throws Throwable {
    boolean flag = false;
    String text = "";
    try {
      waitForVisibilityOfElementLocated(locator, locatorName, 30);
      if (isElementPresent(locator, locatorName)) {
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        WebElement webElement = getWebElement(locator);
        highlightElement(webElement);
        js.executeScript("arguments[0].setAttribute('style','border: solid 5px yellow;');",
            webElement);
        text = webElement.getText();
        flag = true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (flag) {
        reportLogger.log(LogStatus.PASS, " Able to get text from " + locatorName);
      } else {
        reportLogger.log(LogStatus.FAIL, " Unable to get text from " + locatorName);
      }
    }
    return text;
  }

  public String getDropdownSelectedValue(By locator) throws Throwable {
    String selectedValue = "";
    boolean flag = false;
    try {
      waitForElementToBeClickable(locator, 30);
      WebElement webElement = getWebElement(locator);
      highlightElement(webElement);
      Select selectOption = new Select(webElement);
      selectedValue = selectOption.getFirstSelectedOption().getText();
      flag = true;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (flag) {
        reportLogger.log(LogStatus.PASS, " Drop down selected value is '" + selectedValue + "'");
      } else {
        reportLogger.log(LogStatus.FAIL, " Failed to get drop down selected value ");
      }
    }
    return selectedValue;
  }

  public static void deleteAllFilesFromFolder() throws Throwable {
    String path = System.getProperty("user.dir") + "\\DownloadedFiles";
    File file = new File(path);
    File[] files = file.listFiles();
    for (File f : files) {
      if (f.isFile() && f.exists()) {
        reportLogger.log(LogStatus.PASS,
            " File found and deleted <b style=\"color:blue;\">" + f.getName() + "</b>");
        f.delete();
      }
    }
  }

  public String choseFileToUpload() throws Throwable {
    String fileName = "";
    String path = System.getProperty("user.dir") + "\\DownloadedFiles";
    File file = new File(path);
    File[] files = file.listFiles();
    for (File f : files) {
      if (f.isFile() && f.exists()) {
        reportLogger.log(LogStatus.PASS, " File found to choose ");
        fileName = f.getName();
      } else {
        reportLogger.log(LogStatus.FAIL, " File Not found to choose ");
      }
    }
    return fileName;
  }

  public boolean verifyText(By locator, String expected) throws Throwable {
    WebElement webElement = getWebElement(locator);
    highlightElement(webElement);
    String actual = webElement.getText();
    if (actual.equals(expected)) {
      reportLogger.log(LogStatus.PASS,
          "Text verified successfully, actual text is " + actual + " expected text is" + expected);
      return true;
    } else {
      reportLogger.log(LogStatus.FAIL,
          "Text verification failed, actual text is " + actual + " expected text is" + expected);
      return false;
    }
  }
}