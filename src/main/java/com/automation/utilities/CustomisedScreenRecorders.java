package com.automation.utilities;

import org.monte.screenrecorder.ScreenRecorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.monte.media.Format;
import org.monte.media.Registry;

/**
 * This class provides Utility Helper functions for the customised screen recorders Module
 *
 * @author Narasimha Dontula
 * @since 20/06/2020.
 */
public class CustomisedScreenRecorders extends ScreenRecorder {
	 
    private String name;
 
    public CustomisedScreenRecorders(GraphicsConfiguration cfg,
           Rectangle captureArea, Format fileFormat, Format screenFormat,
           Format mouseFormat, Format audioFormat, File movieFolder,
           String name) throws IOException, AWTException {
         super(cfg, captureArea, fileFormat, screenFormat, mouseFormat,
                  audioFormat, movieFolder);
         this.name = name;
    }
 
    @Override
    protected File createMovieFile(Format fileFormat) throws IOException {
          if (!movieFolder.exists()) {
                movieFolder.mkdirs();
          } else if (!movieFolder.isDirectory()) {
                throw new IOException("\"" + movieFolder + "\" is not a directory.");
          }
                           
          SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy_HHmmss");
          //String name = new Object(){}.getClass().getEnclosingMethod().getName();
                                  
          return new File(movieFolder, name +"-" + dateFormat.format(new Date()) + "."+ Registry.getInstance().getExtension(fileFormat));
    }
 }
