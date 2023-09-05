package step_definitions;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import jirautil.JiraUtil;
import mantisutil.ConnectMantis;
import utilities.*;

@CucumberOptions(features = "classpath:features", plugin = { "pretty", "html:target/cucumber-html-report.html", "json:target/cucumber.json" },
		tags ="@Dashboard_01", monochrome = true

)
public class RunCukesTest extends AbstractTestNGCucumberTests {
	static ExtentReports extent;
	public static ExtentTest logger;

	@BeforeSuite
	public void directoryCleanUp() {
		try {

			String filePath = System.getProperty("user.dir") + File.separator + ConfigReader.getValue("screenshotPath");
			if (!new File(filePath).exists())
				FileUtils.forceMkdir(new File(filePath));

			filePath = System.getProperty("user.dir") + File.separator + "Jmeter" + File.separator + "Results";
			if (new File(filePath).exists())
				FileUtils.cleanDirectory(new File(filePath));

			filePath = System.getProperty("user.dir") + File.separator + "ExecutionReports" + File.separator + "HTMLReports";
			if (!new File(filePath).exists())
				FileUtils.forceMkdir(new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@BeforeClass
	public void onStart() {
		try {

			ExtentUtil.extentInit(System.getProperty("user.dir") + ConfigReader.getValue("extentReportPath"));

			GlobalUtil.setCommonSettings(ExcelDataUtil.getCommonSettings());

			String browser = "";
			browser = GlobalUtil.getCommonSettings().getBrowser();
			System.out.println(browser);

			String executionEnv = "";
			executionEnv = GlobalUtil.getCommonSettings().getExecutionEnv();

			String url = "";
			url = GlobalUtil.getCommonSettings().getUrl();

			


			if (browser == null)
				browser = ConfigReader.getValue("defaultBrowser");

			if (executionEnv == null)
				executionEnv = ConfigReader.getValue("defaultExecutionEnvironment");

		

			if (GlobalUtil.getCommonSettings().getManageToolName().equalsIgnoreCase("Jira")) {
				// Jira Test management config
				JiraUtil.JIRA_CYCLE_ID = GlobalUtil.getCommonSettings().getJiraCycleID();
				JiraUtil.JIRA_PROJECT_ID = GlobalUtil.getCommonSettings().getJiraProjectID();
				JiraUtil.ZEPHYR_URL = ConfigReader.getValue("zephyr_url");
				JiraUtil.ZAPI_ACCESS_KEY = ConfigReader.getValue("zapi_access_key");
				JiraUtil.ZAPI_SECRET_KEY = ConfigReader.getValue("zapi_secret_key");

			} else
				GlobalUtil.getCommonSettings().setTestlinkTool("NO");

			// setting up of Bug tracking "MANTIS" tool configuration
			if (GlobalUtil.getCommonSettings().getBugToolName().equalsIgnoreCase("Mantis")) {
				ConnectMantis.MANTIS_URL = "http://" + GlobalUtil.getCommonSettings().getbugToolHostName() + "/bugTool/api/soap/bugToolconnect.php";
				ConnectMantis.MANTIS_USER = GlobalUtil.getCommonSettings().getbugToolUserName();
				ConnectMantis.MANTIS_PWD = GlobalUtil.getCommonSettings().getbugToolPassword();
				ConnectMantis.MANTIS_PROJET = GlobalUtil.getCommonSettings().getbugToolProjectName();
			}

			// setting up of Bug tracking "Jira" tool configuration
			if (GlobalUtil.getCommonSettings().getBugToolName().equalsIgnoreCase("Jira")) {
				JiraUtil.JIRA_URL = GlobalUtil.getCommonSettings().getbugToolHostName();
				JiraUtil.USERNAME = GlobalUtil.getCommonSettings().getbugToolUserName();
				JiraUtil.PASSWORD = GlobalUtil.getCommonSettings().getbugToolPassword();
				JiraUtil.JIRA_PROJECT = GlobalUtil.getCommonSettings().getbugToolProjectName();
				GlobalUtil.jiraapi = new JiraUtil();
			} else
				GlobalUtil.getCommonSettings().setbugTool("NO");

			if (url == null) {
				url = ConfigReader.getValue("BASE_URL");
				GlobalUtil.getCommonSettings().setUrl(url);
			}
			LogUtil.infoLog(getClass(), "\n\n+===========================================================================================================+");
			LogUtil.infoLog(getClass(), " Suite started" + " at " + new Date());
			LogUtil.infoLog(getClass(), "Suite Execution For Web application on environment : " + executionEnv);

			LogUtil.infoLog(getClass(), "\n\n+===========================================================================================================+");

		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.errorLog(getClass(), "Common Settings not properly set may not run the scripts properly");
		}
	}

	@AfterClass
	public void onFinish() {
		LogUtil.infoLog(getClass(), " suite finished" + " at " + new Date());
		LogUtil.infoLog(getClass(), "\n\n+===========================================================================================================+");
		ExtentUtil.extent.flush();
	   KeywordUtil.onExecutionFinish();

	}

}