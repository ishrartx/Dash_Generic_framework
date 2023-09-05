package utilities;

import java.io.File;
import java.util.Calendar;

public class CommonConstants {

	public static final String baseFolderName = "ExecutionReports";
	public static final String generalFolderPath = System.getProperty("user.dir") + File.separator + baseFolderName + File.separator;
	public static final String screenShotFolderName = "FailedScreenshots";
	public static final String reportsFolderName = "HTMLReports";
	public static final String reportsBackupFolderName = "HTMLReportsBackup";
	public static final String LogFolderName = "Logs";
	public static final String automationControlSheetName = "AppControl";
	public static final String mainResourceFolder = "src/main/resources";
	public static final String testResourceFolder = "src/test/resources";
	public static final String automationControlExcelPath = System.getProperty("user.dir") + File.separator + testResourceFolder + File.separator
			+ "ExcelFiles/AutomationControlSheet.xlsx";
	public static final String testDataExcelPath = System.getProperty("user.dir") + File.separator + testResourceFolder + File.separator + "testData/TestData.xlsx";
	public static final String cloudServiceUsername = GlobalUtil.getCommonSettings().getHostName();
	public static final String cloudServiceKey = GlobalUtil.getCommonSettings().getKey();
	public static final String excelextensionxlsx = ".xlsx";
	public static final String excelextensionxls = ".xls";
	public static final String TESTRESULTSHEET = "testResultSheet";
	public static final String Y = "Y";
	public static final String EXCEPTIONCAUGHT = "Exception caught";
	public static final String INVALID_SHEET_MESSAGE = "Error! No such sheet available in Excel file";
	public static final int columnToLookTestCaseID = 0;
	public static int retryCount = 0;
	public static final int maxRetryCount = 2;
	public static String backUpReportName = "BackUpReport_" + Calendar.getInstance().getTime().toString().replace(" ", "_").replace(":", "-").trim() + ".html";
	public static final String configuration = "configuration";
	public static final String environment = "env";
	public static final long DEFAULT_WAIT_TIME_SMALL = 30;
	public static final String configPropertiesFilePath = mainResourceFolder + File.separator + "Config/config.properties";
	public static final String jsonFolderName = "JsonFiles";
	public static final String jsonFilePath = System.getProperty("user.dir") + File.separator + testResourceFolder + File.separator + jsonFolderName;
	public static final String testCasePriorityHighest = "Highest";
	public static final String testCasePriorityMedium = "Medium";
	public static final String testCasePriorityLow = "Low";
	public static final String testCasePriorityLowest = "Lowest";
}
