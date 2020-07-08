package com.automation.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * This class provides Utility Helper functions for the screenshots.
 *
 * @author Narasimha Dontula
 * @since 20/06/2020.
 */
public class GetScreenShot {

  /**
   * This method is used to capture the screenshot of failed scripts.
   *
   * @param webDriver
   *          web driver object
   * @param screenshotName
   *          name of the screenshot
   * @return returns file destination
   */
  public static String captureScreenshot(WebDriver webDriver, String screenshotName)
      throws IOException {
    String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
    TakesScreenshot ts = (TakesScreenshot) webDriver;
    File source = ts.getScreenshotAs(OutputType.FILE);
    // after execution, you could see a folder "FailedTestsScreenshots"
    // under src folder
    String destination = System.getProperty("user.dir") + "/Screenshots/" + screenshotName
        + dateName + ".png";
    File finalDestination = new File(destination);
    FileUtils.copyFile(source, finalDestination);
    return destination;
  }
}
