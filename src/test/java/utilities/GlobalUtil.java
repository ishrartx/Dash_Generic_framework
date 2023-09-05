package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import jirautil.JiraUtil;

/**
 * This class will get date and time and it will rename the file with date and time
 */
public class GlobalUtil {

	private static CommonSettings commonSettings = new CommonSettings();
	private static WebDriver driver = null;
	private static int totalSuites = 0;
	private static boolean suitesRunStarted = false;
	private static int lastRunId = 0;
	private static Exception testException;
	private static String currentBrowser;
	private static String currentSuiteName;
	private static String currentUserEmail;
	private static String currentUserType;
	static String currentUserFirstName;
	static String currentUserLastName;
	public static String currentUserFullName;
	private static AndroidDriver<MobileElement> androidDriver;
	public static AndroidDriver<MobileElement> mdriver;
	public static Map<String, String> propertyCurrentRecord = new HashMap<>();
	public static HashMap<String, String> propertyDeletedRecord = new HashMap<>();
	public static HashMap<String, String> updatesScheduleRecord = new HashMap<>();
	public static HashMap<String, String> propertyRestoredRecord = new HashMap<>();
	public static ArrayList<String> listOfClients = new ArrayList<>();
	private String androidName;
	public static ArrayList<String> listOfProperties = new ArrayList<>();
	public static HashMap<String, String> client = new HashMap<>();
	static String clientFullName = "FullName";
	static String clientEmail = "Email";
	public static String result_FolderName = System.getProperty("user.dir") + "/target/cucumber-html-report";
	public static JiraUtil jiraapi;
	public static String errorMsg;
	public static Throwable e;

	// For HashMap
	public static final String PROPERTYADDRESSKEY = "Address";
	public static final String PROPERTYDATETIMEKEY = "DateTime";
	public static final String PROPERTYCLIENTKEY = "Client";
	public static final String PROPERTYAGENTKEY = "Agent";
	public static final String PROPERTYNOTEKEY = "Note";
	public static TestLinkUtil testlinkapi;

	protected static final HashMap<String, String> popupCurrentData = new HashMap<String, String>();

	/**
	 * Get data and time stamp
	 */
	public static String getDateTime() {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String dateOfExecution = dateFormat.format(date);
		return dateOfExecution;
	}

	public static AndroidDriver<?> getAndroidDriver() {
		return androidDriver;
	}

	/**
	 * Update filename with time stamp
	 */
	public static void renameFile() {

		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HHmmss");
		String timeStamp = dateFormat.format(date);

		try {

			File oldFile = new File(System.getProperty("user.dir") + ConfigReader.getValue("testResultExcelPath"));

			String newFilePath = oldFile.getAbsolutePath().replace(oldFile.getName(), "") + "\\ReportHistory\\" + timeStamp + "-TestResult.xlsx";

			File newFile = new File(newFilePath);

			FileUtils.copyFile(oldFile, newFile);
			LogUtil.infoLog(GlobalUtil.class, "History File created successfully");

		} catch (IOException e) {
			LogUtil.errorLog(GlobalUtil.class, "Exception caught", e);
		}
	}

	
	public static CommonSettings getCommonSettings() {
		return commonSettings;
	}
	public static AndroidDriver<?> getMDriver() {
		return mdriver;
	}
	
	public static void setCommonSettings(CommonSettings commonSettings) {
		GlobalUtil.commonSettings = commonSettings;
	}

	
	public static int getTotalSuites() {
		return totalSuites;
	}

	
	public static void setTotalSuites(int totalSuites) {
		GlobalUtil.totalSuites = totalSuites;
	}

	
	public static boolean isSuitesRunStarted() {
		return suitesRunStarted;
	}

	public String getAndroidName() {
		return androidName;
	}

	
	public static void setSuitesRunStarted(boolean suitesRunStarted) {
		GlobalUtil.suitesRunStarted = suitesRunStarted;
	}

	
	public static int getLastRunId() {
		return lastRunId;
	}

	
	public static void setLastRunId(int lastRunId) {
		GlobalUtil.lastRunId = lastRunId;
	}

	
	public static Exception getTestException() {
		return testException;
	}

	
	public static void setTestException(Exception testException) {
		GlobalUtil.testException = testException;
	}

	
	public static String getCurrentBrowser() {
		return currentBrowser;
	}

	
	public static void setCurrentBrowser(String currentBrowser) {
		GlobalUtil.currentBrowser = currentBrowser;
	}

	
	public static String getCurrentSuiteName() {
		return currentSuiteName;
	}

	
	public static void setCurrentSuiteName(String currentSuiteName) {
		GlobalUtil.currentSuiteName = currentSuiteName;
	}

	
	public static String getCurrentUserEmail() {
		return currentUserEmail;
	}

	
	public static void setCurrentUserEmail(String currentUserEmail) {
		GlobalUtil.currentUserEmail = currentUserEmail;
	}

	
	public static String getCurrentUserType() {
		return currentUserType;
	}

	public static void setAndroidDriver(AndroidDriver<?> androidDriver) {
		GlobalUtil.androidDriver = (AndroidDriver<MobileElement>) androidDriver;
	}



	public static void setCurrentUserType(String currentUserType) {
		GlobalUtil.currentUserType = currentUserType;
	}

//	public static WebDriver getDriver() {
//		return driver;
//	}
	public static WebDriver getDriver() {
		return  mdriver;
	}


	public static WebDriver getMdriver(){
		return mdriver;
	}




	public static void setDriver(WebDriver driver) {
		GlobalUtil.driver = driver;
	}

	public static String createZipFile() throws IOException {
		result_FolderName = result_FolderName.replace("\\", "/");
		String outputFile = result_FolderName + ".zip";
		FileOutputStream fos = new FileOutputStream(outputFile);
		ZipOutputStream zos = new ZipOutputStream(fos);
		packCurrentDirectoryContents(result_FolderName, zos);
		zos.closeEntry();
		zos.close();
		fos.close();
		return outputFile;
	}

	public static void packCurrentDirectoryContents(String directoryPath, ZipOutputStream zos) throws IOException {

		for (String dirElement : new File(directoryPath).list()) {

			String dirElementPath = directoryPath + "/" + dirElement;

			if (new File(dirElementPath).isDirectory()) {
				packCurrentDirectoryContents(dirElementPath, zos);

			} else {
				ZipEntry ze = new ZipEntry(dirElementPath.replaceAll(result_FolderName + "/", ""));

				zos.putNextEntry(ze);

				FileInputStream fis = new FileInputStream(dirElementPath);
				byte[] bytesRead = new byte[512];

				int bytesNum;
				while ((bytesNum = fis.read(bytesRead)) > 0) {
					zos.write(bytesRead, 0, bytesNum);
				}

				fis.close();
			}
		}

	}

}
