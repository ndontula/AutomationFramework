package com.automation.utilities;

import static org.monte.media.FormatKeys.EncodingKey;
import static org.monte.media.FormatKeys.FrameRateKey;
import static org.monte.media.FormatKeys.KeyFrameIntervalKey;
import static org.monte.media.FormatKeys.MIME_AVI;
import static org.monte.media.FormatKeys.MediaTypeKey;
import static org.monte.media.FormatKeys.MimeTypeKey;
import static org.monte.media.VideoFormatKeys.CompressorNameKey;
import static org.monte.media.VideoFormatKeys.DepthKey;
import static org.monte.media.VideoFormatKeys.ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE;
import static org.monte.media.VideoFormatKeys.QualityKey;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.logging.log4j.Logger;
import org.monte.media.Format;
import org.monte.media.FormatKeys.MediaType;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;

/**
 * This class provides Utility Helper functions for the Logging Module
 *
 * @author Narasimha Dontula
 * @since 20/06/2020.
 */
public class ScreenRecorderUtil {
	public static ScreenRecorder screenRecorder;
	public static String videoName;
    private static final Logger LOGGER = LoggingUtils.getNamedLogger(ScreenRecorderUtil.class.getSimpleName());
    private static boolean isScreenRecordEnable = Boolean.parseBoolean(ConfigManager.getProperty("IS_SCREEN_RECORDER_ENABLE").trim());
	
	  // set the graphics configuration
	public static void startRecording(String methodName) throws Exception
    {
        if(isScreenRecordEnable) {
            try {
                LOGGER.info("Video recording is Started");
                videoName = methodName;
                String videoPath = System.getProperty("user.dir");
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
                File file = new File(videoPath + "//ScriptVideos//"+dateFormat.format(new Date()));
                LOGGER.info("Video recording path is : = " + videoPath + "//app");
                GraphicsConfiguration gc = GraphicsEnvironment
                        .getLocalGraphicsEnvironment()
                        .getDefaultScreenDevice()
                        .getDefaultConfiguration();

                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int width = screenSize.width;
                int height = screenSize.height;

                Rectangle captureSize = new Rectangle(0, 0, width, height);

                screenRecorder = new CustomisedScreenRecorders(gc, captureSize,
                        new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                        new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                                CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                                DepthKey, 24, FrameRateKey, Rational.valueOf(15),
                                QualityKey, 1.0f,
                                KeyFrameIntervalKey, 15 * 60),
                        new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black",
                                FrameRateKey, Rational.valueOf(30)),
                        null, file, methodName);

                screenRecorder.start();
            } catch (Exception e) {
                LOGGER.error("Error while starting video recording");
                e.printStackTrace();
            }
        }else {
            LOGGER.debug("Video Recording is set as false");
        }
    }
	
	public static void stopRecording() throws Exception
    {
		if(isScreenRecordEnable) {
            try {
                ScreenRecorderUtil.screenRecorder.stop();
                LOGGER.info("Video recording is stop");
            } catch (Exception e) {
                LOGGER.error("error while stopping Video recording.");
                e.printStackTrace();
            }
        }
        else {
            LOGGER.debug("Video Recording is set as false");
        }
    }
}
