package step_definitions;

import com.Buffer.BufferUtilSuiteLevel;


import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import mantisutil.MantisReport;
import testlink.api.java.client.TestLinkAPIResults;
import utilities.ConfigReader;
import utilities.DriverUtil;
import utilities.ExtentUtil;
import utilities.GlobalUtil;
import utilities.KeywordUtil;
import utilities.LogUtil;


public class Hooks {

	String imagePath;
	String pathForLogger;
	public static String testCaseDescription;
	public static String executingTagName;

	public static String imagePath1;
	public static String concatt = ".";



	@Before("@MobileTest4")
	public void beforeMobileMethods(Scenario scenario) throws Exception {
		if (scenario.getName().contains("_"))
			testCaseDescription = scenario.getName().split("_")[1];
		else
			testCaseDescription = scenario.getName();
		testCaseDescription = scenario.getName().split("_")[1];
		executingTagName = scenario.getSourceTagNames().toArray()[0].toString();
		ExtentUtil.startTestInit(testCaseDescription);
		LogUtil.infoLog(getClass(), "Test Started with tag : " + executingTagName);

		LogUtil.infoLog(getClass(),

				"\n+----------------------------------------------------------------------------------------------------------------------------+");
		LogUtil.infoLog(getClass(), "Mobile Tests Started: " + scenario.getName());

		LogUtil.infoLog(Hooks.class,
				"Mobile Test is executed in OS: " + GlobalUtil.getCommonSettings().getAndroidName());

		//GlobalUtil.setMDriver(DriverUtil.getMobileApp());
		// GlobalUtil.setMDriver(DriverUtil.getMobileApp(GlobalUtil.getCommonSettings().getExecutionEnv()));
	}



	@After("@MobileTest4")
	public void afterMobileMethods(Scenario scenario) {
		String testName = scenario.getName().split("_")[0].trim();
		if (scenario.isFailed()) {
			try {
				String scFileName = "ScreenShot_" + System.currentTimeMillis();
				String screenshotFilePath = ConfigReader.getValue("screenshotPath") + "\\" + scFileName + ".png";

				// imagePath = HTMLReportUtil.testFailMobileTakeScreenshot(screenshotFilePath);
				// pathForLogger = RunCukesTest.logger.addScreenCapture(imagePath);
				// RunCukesTest.logger.log(LogStatus.FAIL,
				// HTMLReportUtil.failStringRedColor("Failed at point: " + pathForLogger) +
				// GlobalUtil.e);

				// scenario.write("Current Page URL is " +
				// GlobalUtil.getMDriver().getCurrentUrl());

				// byte[] screenshot = KeywordUtil.takeMobileScreenshot(screenshotFilePath);

				// scenario.embed(screenshot, "image/png");

				// report the bug
				String bugID = "Please check the Bug tool Configuration";
				if (GlobalUtil.getCommonSettings().getBugToolName().equalsIgnoreCase("Mantis")) {
					bugID = MantisReport.reporIssue(scenario.getName(), GlobalUtil.errorMsg, "General",
							"Automated on Android Device Version: " + GlobalUtil.getCommonSettings().getAndroidVersion()
									+ " and Build Name: " + GlobalUtil.getCommonSettings().getBuildNumber(),
							screenshotFilePath);
				}

				/*if (GlobalUtil.getCommonSettings().getBugToolName().equalsIgnoreCase("Jira")) {
					bugID = GlobalUtil.jiraapi.reporIssue(scenario.getName(),
							"Automated on Android Device Version: " + GlobalUtil.getCommonSettings().getAndroidVersion()
									+ ",\n Build Name: " + GlobalUtil.getCommonSettings().getBuildNumber()
									+ ". \n\n\n\n" + GlobalUtil.ErrorMsg,
							screenshotFilePath);
				}*/

				// updating the results in Testmangement tool
				if (GlobalUtil.getCommonSettings().getManageToolName().equalsIgnoreCase("TestLink")) {
					GlobalUtil.testlinkapi
							.updateTestLinkResult(
									testName, "Please find the BUGID in "
											+ GlobalUtil.getCommonSettings().getBugToolName() + " : " + bugID,
									TestLinkAPIResults.TEST_PASSED);
				}
				if (GlobalUtil.getCommonSettings().getManageToolName().equalsIgnoreCase("Jira")) {
					GlobalUtil.jiraapi.updateJiraTestResults(testName, "Please find the BUGID in "
							+ GlobalUtil.getCommonSettings().getBugToolName() + " : " + bugID, "Fail");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {

			LogUtil.infoLog(Hooks.class,
					"Test has ended closing Application: " + GlobalUtil.getCommonSettings().getAndroidName());
			// updating the results in Testmangement tool
			if (GlobalUtil.getCommonSettings().getManageToolName().equalsIgnoreCase("TestLink")) {
				GlobalUtil.testlinkapi.updateTestLinkResult(testName, "This test is passed",
						TestLinkAPIResults.TEST_PASSED);
			}
			if (GlobalUtil.getCommonSettings().getManageToolName().equalsIgnoreCase("Jira")) {
				GlobalUtil.jiraapi.updateJiraTestResults(testName, "This test is passed", "Pass");
			}
		}

		// close the browsers

		// We need to write the quit for local mobile device for time being we commented
		// for browser stack
		GlobalUtil.getMDriver().quit();
		


	}



}