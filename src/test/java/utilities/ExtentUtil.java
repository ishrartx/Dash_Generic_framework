package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import io.appium.java_client.AppiumDriver;
import org.apache.commons.compress.utils.IOUtils;

import com.Buffer.BufferUtilSuiteLevel;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import io.cucumber.java.Scenario;
import step_definitions.Hooks;

/**
 * The Class ExtentUtil.
 */
public class ExtentUtil {

	public static ExtentReports extent;
	public static ExtentSparkReporter spark;
	public static ExtentTest loggerTest;
	public static ExtentTest loggerTestStep;
	public static ThreadLocal<ExtentTest> logger = new ThreadLocal<ExtentTest>();

	/**
	 * Extent initialization.
	 *
	 * @param filePath
	 *        the file path
	 * 
	 * @throws IOException
	 */
	public static void extentInit(String filePath) throws IOException {
		extent = new ExtentReports();
		spark = new ExtentSparkReporter(filePath);
		spark.config().setTheme(Theme.DARK);
		spark.config().setTimelineEnabled(true);
		spark.config().setTimeStampFormat("dd/MM/yyyy HH:mm:ss");
		spark.config().thumbnailForBase64(true);
		extent.attachReporter(spark);
		extent.setSystemInfo("Executed by", System.getProperty("user.name"));
		extent.setSystemInfo("Operating System and Version", System.getProperty("os.name"));
		extent.setSystemInfo("JAVA Version", "JDK " + Runtime.version().toString());
	}

	/**
	 * Start test initialization.
	 *
	 * @param testCaseName
	 *        the test case name
	 */
	public static void startTestInit(String testCaseName) {
		System.out.println(testCaseName);
		loggerTest = extent.createTest(testCaseName);
		System.out.println(loggerTest);
		logger.set(loggerTest);
	}

	/**
	 * Attach screenshot to report on failure.
	 */
	public static void takeScreenshotAndAttachInReport() {
		try {
			if (System.getProperty("screen_shot") != null && System.getProperty("screen_shot").equalsIgnoreCase("true")) {

				String imagePath, pathForLogger;
				String scFileName = "ScreenShot_" + System.currentTimeMillis();
				String screenshotFilePath = ConfigReader.getValue("screenshotsPath") + "\\" + scFileName + ".png";

				imagePath = HTMLReportUtil.testFailTakeScreenshot(screenshotFilePath);

				InputStream is = new FileInputStream(imagePath);
				byte[] imageBytes = org.apache.commons.compress.utils.IOUtils.toByteArray(is);
				Thread.sleep(2000);
				String base64 = Base64.getEncoder().encodeToString(imageBytes);


			} else {
				if (ConfigReader.getValue("screenshotFlag").equalsIgnoreCase("true")) {
					String imagePath, pathForLogger;
					String scFileName = "ScreenShot_" + System.currentTimeMillis();
					String screenshotFilePath = ConfigReader.getValue("screenshotsPath") + "\\" + scFileName + ".png";

					imagePath = HTMLReportUtil.testFailTakeScreenshot(screenshotFilePath);

					InputStream is = new FileInputStream(imagePath);
					byte[] imageBytes = org.apache.commons.compress.utils.IOUtils.toByteArray(is);
					Thread.sleep(2000);
					String base64 = Base64.getEncoder().encodeToString(imageBytes);
					pathForLogger = loggerTest.addScreenCaptureFromPath("data:image/png;base64," + base64).toString();
					loggerTest.log(Status.PASS,
							HTMLReportUtil.passStringGreenColor(pathForLogger));

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


	}


	public static void attachScreenshotToReportOnFailure(Scenario scenario) {
		try {
			String scFileName = "ScreenShot_" + Hooks.executingTagName.replace("@", "") + "_" + KeywordUtil.currentDateTime() + ".png";
			BufferUtilSuiteLevel.screenshotFilePath = CommonConstants.generalFolderPath + CommonConstants.screenShotFolderName + File.separator + scFileName;
			String imagePath = DriverFactory.testFailTakeScreenshot(BufferUtilSuiteLevel.screenshotFilePath);
			InputStream is = new FileInputStream(imagePath);
			byte[] imageBytes = IOUtils.toByteArray(is);
			Thread.sleep(2000);
			String base64 = Base64.getEncoder().encodeToString(imageBytes);

			logger.get().fail(MarkupHelper.createLabel("Failed at Point: ", ExtentColor.RED));

			if (GlobalUtil.errorMsg != null) {
//				logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(GlobalUtil.errorMsg));
//				logger.get().log(Status.FAIL, HTMLReportUtil.showBase64Image("data:image/png;base64," + base64));
//				logger.get().addScreenCaptureFromBase64String(base64);
				logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(GlobalUtil.errorMsg), MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
			} else {
//				logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(GlobalUtil.e.toString()));
//				logger.get().log(Status.FAIL, HTMLReportUtil.showBase64Image("data:image/png;base64," + base64));
//				logger.get().addScreenCaptureFromBase64String(base64);
				logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(GlobalUtil.e.toString()), MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
			}
			scenario.attach(imageBytes, "image/png", "Failed Screenshot");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

		/**
	 * Open report on suite complete.
	 */
	@SuppressWarnings("deprecation")
	public static void openReportOnSuiteComplete(String generalPath, String backUpFolderName, String backUpReportName) {
		BufferUtilSuiteLevel.extentHtmlreportWithNameAndPath = generalPath + backUpFolderName + File.separator + backUpReportName;
		File extentReport = new File(BufferUtilSuiteLevel.extentHtmlreportWithNameAndPath);
		if (extentReport.exists()) {
			try {
				Runtime rt = Runtime.getRuntime();
				rt.exec("rundll32 url.dll,FileProtocolHandler " + BufferUtilSuiteLevel.extentHtmlreportWithNameAndPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void attachScreenshotOfPassedTestsInReport() {
		String screenshot = KeywordUtil.takeScreenShot();

		// ExtentReports log and screenshot operations for passed step.
		try {
			loggerTest.log(Status.PASS,
					HTMLReportUtil.passStringGreenColor(""),
					MediaEntityBuilder.createScreenCaptureFromPath("data:image/jpg;base64," + screenshot).build());
		} finally {

		}
//		catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		}

	public static void attachMobileScreenshotOfPassedTestsInReport() {
		String screenshot = Mobile_keywords.takeScreenShot();

		// ExtentReports log and screenshot operations for passed step.
		try {
			loggerTest.log(Status.PASS,
					HTMLReportUtil.passStringGreenColor(""),
					MediaEntityBuilder.createScreenCaptureFromPath("data:image/jpg;base64," + screenshot).build());
		} finally {

		}
//		catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
