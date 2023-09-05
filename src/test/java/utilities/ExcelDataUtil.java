package utilities;

import java.io.*;
import java.util.*;

import com.aventstack.extentreports.Status;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebElement;

/**
 * ExcelDateUtil class is refer to read and write in excel
 */
@SuppressWarnings("unused")
public class ExcelDataUtil {

	private static FileInputStream fs = null;
	private static Workbook workbook = null;
	private static Sheet sheet = null;

	private static int columnToLookTestCaseID = Integer.parseInt(ConfigReader.getValue("columnToLookTestCaseID"));
	private static String testDatafilePath = ConfigReader.getValue("testDataExcelPath");
	private static String testDataSheetName = ConfigReader.getValue("testDataSheet");
	private static boolean isCopyTemplate = false;
	private static String filePath = "";
	protected static List<String> testsList = new ArrayList<>();
	private static String excelextensionxlsx = ".xlsx";
	public static final String TESTRESULTSHEET = "testResultSheet";
	public static final String Y = "Y";
	public static final String EXCEPTIONCAUGHT = "Exception caught";
	private static String excelextensionxls = ".xls";
	private static String automationcontrolexcelpath = "AutomationControlExcelPath";
	private static final String INVALID_SHEET_MESSAGE = "Error! No such sheet available in Excel file";

	private static CSVReader reader = null;

	/**
	 * <H1>Excel initialize</H1>
	 *
	 * @param filePath
	 * @param sheetName
	 */
//	public static void init(String filePath, String sheetName) {
//		String fileExtensionName = filePath.substring(filePath.indexOf('.'));
//		try {
//			fs = new FileInputStream(filePath);
//			if (fileExtensionName.equals(excelextensionxlsx)) {
//				// If it is xlsx file then create object of XSSFWorkbook class
//				workbook = new XSSFWorkbook(fs);
//			}
//			// Check condition if the file is xls file
//			else if (fileExtensionName.equals(excelextensionxls)) {
//				// If it is xls file then create object of XSSFWorkbook class
//				workbook = new HSSFWorkbook(fs);
//			}
//			sheet = workbook.getSheet(sheetName);
//		} catch (Exception e) {
//			LogUtil.errorLog(ExcelDataUtil.class, EXCEPTIONCAUGHT, e);
//		}
//
//	}
	public static void init(String filePath, String sheetName) {
		String fileExtensionName = filePath.substring(filePath.indexOf('.'));
		try {
			if (fileExtensionName.equals(excelextensionxlsx) || fileExtensionName.equals(excelextensionxls)) {
				fs = new FileInputStream(filePath);
				if (fileExtensionName.equals(excelextensionxlsx)) {
					// If it is xlsx file then create object of XSSFWorkbook class
					workbook = new XSSFWorkbook(fs);
				}
				// Check condition if the file is xls file
				else if (fileExtensionName.equals(excelextensionxls)) {
					// If it is xls file then create object of XSSFWorkbook class
					workbook = new HSSFWorkbook(fs);
				}

				sheet = workbook.getSheet(sheetName);
			} else if (fileExtensionName.equals(".csv")) {
				reader = new CSVReaderBuilder(new FileReader(filePath)).build();
			}

		} catch (Exception e) {
			LogUtil.errorLog(ExcelDataUtil.class, EXCEPTIONCAUGHT, e);
		}
	}

	public static void validateTestData(String filename,String value) throws IOException, CsvValidationException {
		boolean found = false;
		boolean isfirstRow = false;
		Row firstrow = null;
		// Initialize class
		// Get Path and Sheet Name from Property File
		final HashMap<String, String> currentRowData = new HashMap<String, String>();
		String scores_csv_file_path = System.getProperty("user.dir") + "/" + "/target/unzip/" + filename;
//		System.out.println(System.getProperty("user.dir"));
		init(scores_csv_file_path, "");
		String[] data;
		while ((data = reader.readNext()) != null) {
			for (String cell : data) {
				System.out.println("Results for " + cell + " is: " + cell.equalsIgnoreCase(value));
//			        	System.out.println(cell);
			}
			System.out.println();
		}
		reader.close();
	}


//
	public static void read_all_data(String filename,String globalvalues) throws IOException, CsvException {

		String scores_csv_file_path = System.getProperty("user.dir")+"/"+"/target/unzip/"+filename;
		init(scores_csv_file_path, "");
		String [] nextRecord;
		while((nextRecord=reader.readNext())!=null) {
			if(nextRecord[0].equalsIgnoreCase("Id")) {
				for(String cell:nextRecord){
					System.out.print(cell+"\t");
				}
				System.out.println();
			}
			if(Arrays.stream(nextRecord).anyMatch("Aman Roy"::equals)) {
				System.out.println(nextRecord);
				for(String cell:nextRecord){
					if(cell.equalsIgnoreCase(globalvalues)) {
						ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor("The "+cell+" when compared to "+globalvalues+" is: "+cell.equalsIgnoreCase(globalvalues)));
						System.out.println("The "+cell+" when compared to "+globalvalues+" is: "+cell.equalsIgnoreCase(globalvalues));
						break;
					}
				}
			}

		}
		reader.close();

	}

	public static void read_first_header(String path) throws CsvValidationException, IOException {
		String scores_csv_file_path = System.getProperty("user.dir") + "/" + "/target/unzip/" + path;
		init(scores_csv_file_path, "");
		String[] nextRecord;
		while ((nextRecord = reader.readNext()) != null) {
			if (nextRecord[0].equalsIgnoreCase("Id")) {
				for (String cell : nextRecord) {
					System.out.print(cell + "\t");
				}
				System.out.println();
			}
		}
	}



	public static String[] read_column(int column, String filePath, String delimeter){
		String[] data;
		String currentLine;
		ArrayList<String> coldata=new ArrayList<>();
		try{
			FileReader fr=new FileReader(filePath);
			BufferedReader br=new BufferedReader(fr);
			while((currentLine= br.readLine())!=null){

				if(currentLine.contains("Aman Roy")) {
					data = currentLine.split(delimeter);
					coldata.add(data[column]);
				}
			}

		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return coldata.toArray(new String[0])	;
	}


	public static void readDataLineByLine(String file) throws IOException {
		// Create an object of filereader

		// class with CSV file as a parameter.
		FileReader filereader = new FileReader(file);
		ArrayList<String> arr=new ArrayList<>();
		// create csvReader object passing
		// file reader as a parameter
		BufferedReader br = new BufferedReader( new FileReader(file));
//			CSVReader csvReader = new CSVReader(filereader);
		String[] data;


		String line = null;
		int count =0;
		while ((line = br.readLine()) != null) {


			if(line.contains("Aman Roy"))
			{
				arr.add(line);


			}
			count++;




		}




	}




	/**
	 * <H1>Get test data with test case id</H1>
	 *
	 * @param testCaseID
	 *
	 * @return
	 */
	public static HashMap<String, String> getTestDataWithTestCaseID(String sheetName, String testCaseID) {
		boolean found = false;
		boolean isfirstRow = false;
		Row firstrow = null;
		// Initialize class
		// Get Path and Sheet Name from Property File
		final HashMap<String, String> currentRowData = new HashMap<String, String>();
		init(testDatafilePath, sheetName);
		Iterator<Row> rowIterator = sheet.iterator();
		try {
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if (!isfirstRow) {
					firstrow = row;
					isfirstRow = true;
				}
				if (row.getCell(columnToLookTestCaseID).getStringCellValue().equalsIgnoreCase(testCaseID)) {
					found = true;
					for (int i = 0; i < row.getLastCellNum(); i++) {
						String cellValue = row.getCell(i).getStringCellValue();
						if (cellValue == null) {
							cellValue = "";
						}
						cellValue = getUniqueString(cellValue);
						currentRowData.put(firstrow.getCell(i).getStringCellValue(), cellValue);
					}
					break;
				}

			}

			fs.close();

		} catch (Exception e) {
			LogUtil.errorLog(ExcelDataUtil.class, "caught exception", e);
		}
		if (!found)
			LogUtil.infoLog(ExcelDataUtil.class, "No data found with given key-> " + testCaseID);

		return currentRowData;

	}

	public static void putTestData(String sheetName, String projname, String testCaseID, int columnNumber) {
		boolean found = false;
		boolean isfirstRow = false;
		Row firstrow = null;

		init(testDatafilePath, sheetName);
		Iterator<Row> rowIterator = sheet.iterator();

		try {

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if (!isfirstRow) {
					firstrow = row;
					isfirstRow = true;
				}
				if (row.getCell(columnToLookTestCaseID).getStringCellValue().equalsIgnoreCase(testCaseID)) {
					found = true;
					Cell column = row.getCell(columnNumber);
					String cellValue = column.getStringCellValue();
					if (cellValue == null) {
						cellValue = "";
					}
					cellValue = getUniqueString(cellValue);
					System.out.println(cellValue);
					column.setCellValue(projname);
					FileOutputStream out = new FileOutputStream(new File(testDatafilePath));
					workbook.write(out)
					;
					out.close();
					break;
				}

			}

			fs.close();

		} catch (Exception e) {
			LogUtil.errorLog(ExcelDataUtil.class, "caught exception", e);
		}
		if (!found)
			LogUtil.infoLog(ExcelDataUtil.class, "No data found with given key-> " + testCaseID);
	}

	public static String getUniqueString(String string) {
		return string.replaceAll("UNIQUE", "" + System.currentTimeMillis());
	}

	/**
	 * <H1>Get common settings</H1>
	 *
	 * @return
	 */
	public static CommonSettings getCommonSettings() {
		// 1. Read Excel File
		CommonSettings commonSettings = new CommonSettings();

		String sheetName = ConfigReader.getValue("AutomationControlSheet");
		try (FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + ConfigReader.getValue(automationcontrolexcelpath));
			 Workbook wb = WorkbookFactory.create(fis);) {

			if (wb.getSheetIndex(wb.getSheet(sheetName)) == -1) {
				LogUtil.infoLog(ExcelDataUtil.class, INVALID_SHEET_MESSAGE + sheetName);

			}

			Sheet sheet = wb.getSheet(sheetName);

			// Set Project name from Column B1
			commonSettings.setProjectName(sheet.getRow(0).getCell(1).getStringCellValue());

			// Set Fixed Common Settings

			// 1. Set Application type Column[B17] Row =16, Column =1
			String val = sheet.getRow(1).getCell(1).getStringCellValue();
			commonSettings.setAppType(val);

			// 2. Set Application environment type Column[B18] Row =18, Column
			val = sheet.getRow(2).getCell(1).getStringCellValue();
			commonSettings.setAppEnviornment(val);

			// 3. Set Email output(Y/N) Column[B20] Row =19, Column =1
			val = sheet.getRow(3).getCell(1).getStringCellValue();
			commonSettings.setEmailOutput(val);

			// 4. Set Email Id Comma List Column[B25] Row =24, Column =1
			val = sheet.getRow(4).getCell(1).getStringCellValue();
			commonSettings.setBuildNumber(val);

			// 4. Set Email Id Comma List Column[B25] Row =24, Column =1
			val = sheet.getRow(5).getCell(1).getStringCellValue();
			commonSettings.setUrl(val);

			// 4. Set Email Id Comma List Column[B25] Row =24, Column =1
			val = sheet.getRow(14).getCell(1).getStringCellValue();
			commonSettings.setEmailIds(val);

			// 5. Set Html report (Y/N) Column[B26] Row =25, Column =1
			val = sheet.getRow(15).getCell(1).getStringCellValue();
			commonSettings.setHtmlReport(val);

			// 6. Set XLS report (Y/N) Column[B27] Row =26, Column =1
			val = sheet.getRow(16).getCell(1).getStringCellValue();
			commonSettings.setXlsReport(val);

			// 7. Set Test Logs (Y/N) Column[B28] Row =27, Column =1
			val = sheet.getRow(17).getCell(1).getStringCellValue();
			commonSettings.setTestLogs(val);

			val = sheet.getRow(7).getCell(1).getStringCellValue();
			commonSettings.setExecutionEnv(val);

			val = sheet.getRow(8).getCell(1).getStringCellValue();
			commonSettings.setCloudProvider(val);

			val = sheet.getRow(9).getCell(1).getStringCellValue();
			commonSettings.setHostName(val);

			val = sheet.getRow(10).getCell(1).getStringCellValue();
			commonSettings.setKey(val);

			val = sheet.getRow(11).getCell(1).getStringCellValue();
			commonSettings.setRemoteOS(val);

			val = sheet.getRow(12).getCell(1).getStringCellValue();
			commonSettings.setBrowser(val);

			val = sheet.getRow(20).getCell(1).getStringCellValue();
			commonSettings.setManageToolName(val);

			val = sheet.getRow(21).getCell(1).getStringCellValue();
			commonSettings.setTestlinkTool(val);

			val = sheet.getRow(22).getCell(1).getStringCellValue();
			commonSettings.setTestLinkHostName(val);

			val = sheet.getRow(23).getCell(1).getStringCellValue();
			commonSettings.setTestlinkAPIKey(val);

			val = sheet.getRow(24).getCell(1).getStringCellValue();
			commonSettings.setTestlinkProjectName(val);

			val = sheet.getRow(25).getCell(1).getStringCellValue();
			commonSettings.setTestlinkPlanName(val);

			val = sheet.getRow(27).getCell(1).getStringCellValue();
			commonSettings.setBugToolName(val);

			val = sheet.getRow(28).getCell(1).getStringCellValue();
			commonSettings.setbugTool(val);

			val = sheet.getRow(29).getCell(1).getStringCellValue();
			commonSettings.setbugToolHostName(val);

			val = sheet.getRow(30).getCell(1).getStringCellValue();
			commonSettings.setbugToolUserName(val);

			val = sheet.getRow(31).getCell(1).getStringCellValue();
			commonSettings.setbugToolPassword(val);

			val = sheet.getRow(32).getCell(1).getStringCellValue();
			commonSettings.setbugToolProjectName(val);

			val = sheet.getRow(35).getCell(1).getStringCellValue();
			commonSettings.setJiraTestManagement(val);

			val = sheet.getRow(36).getCell(1).getStringCellValue();
			commonSettings.setJiraCycleID(val);

			val = sheet.getRow(37).getCell(1).getStringCellValue();
			commonSettings.setJiraProjectID(val);

//			val = sheet.getRow(39).getCell(1).getStringCellValue();
//			commonSettings.setRestURL(val);
//
//			val = sheet.getRow(40).getCell(1).getStringCellValue();
//			commonSettings.setRestAccessToken(val);
//
//			Double val1 = sheet.getRow(43).getCell(1).getNumericCellValue();
//			commonSettings.setAndroidVersion(val1);
//
//			val = sheet.getRow(44).getCell(1).getStringCellValue();
//			commonSettings.setAndroidName(val);
//
//			val = sheet.getRow(45).getCell(1).getStringCellValue();
//			commonSettings.setAndroidID(val);
//
//			val = sheet.getRow(46).getCell(1).getStringCellValue();
//			commonSettings.setAndroidBrowser(val);
//
//			val = sheet.getRow(47).getCell(1).getStringCellValue();
//			commonSettings.setAndroidCloudDeviceID(val);
//
		} // End try
		catch (Exception e) {
			LogUtil.errorLog(ExcelDataUtil.class, EXCEPTIONCAUGHT, e);
		}
		return commonSettings;

	}

	/**
	 * <H1>Get Flag from excel</H1>
	 *
	 * @param sheetName
	 * @param searchValue
	 *
	 * @return
	 */
	public static String getFlagExcel(String sheetName, String searchValue) {
		int sheetSize = 0;
		int rowNum = 1;
		String strVal = "";
		String strflag = "NA";
		try (FileInputStream fis = new FileInputStream(ConfigReader.getValue(automationcontrolexcelpath)); Workbook wb = WorkbookFactory.create(fis);) {

			if (wb.getSheetIndex(wb.getSheet(sheetName)) == -1) {
				LogUtil.infoLog(ExcelDataUtil.class, INVALID_SHEET_MESSAGE + sheetName);
				throw new InvalidSheetException("No such sheet available in Excel file: " + sheetName);
			}

			Sheet sheet = wb.getSheet(sheetName);
			sheetSize = sheet.getLastRowNum();

			for (int i = rowNum; i <= sheetSize; i++) {

				strVal = sheet.getRow(i).getCell(0).getStringCellValue();

				if (searchValue.equalsIgnoreCase(strVal)) {
					strflag = wb.getSheet(sheetName).getRow(i).getCell(1).getStringCellValue();
					break;
				}
			}

		} catch (Exception e) {
			strflag = "NA";
			LogUtil.errorLog(ExcelDataUtil.class, EXCEPTIONCAUGHT, e);
		}

		return strflag;
	}

	// Read Data from Excel File AutomationControlSheet.xls(SuiteControlSheet),
	// Reading Y/N for including a test case in suite to run.
	/**
	 * <H1>Read Data from Excel File AutomationControlSheet.xls(SuiteControlSheet)</H1>
	 *
	 * @param suiteName
	 *
	 * @return
	 */
	public static boolean isSuiteRunnable(String suiteName) {
		boolean isRunnable = false;
		String strVal;
		try {
			strVal = getFlagExcel("SuiteControlSheet", suiteName);
			if (strVal.equalsIgnoreCase(Y)) {
				isRunnable = true;
			} else {
				isRunnable = false;
			}
		} catch (Exception e) {
			isRunnable = false;
			LogUtil.errorLog(ExcelDataUtil.class, EXCEPTIONCAUGHT, e);
		}

		return isRunnable;
	}

	// Read Excel file for Script to run. Like Regression, Smoke, Functional
	/**
	 * <H1>Read Excel file for Script to run. Like Regression, Smoke, Functional</H1>
	 *
	 * @param suiteName
	 * @param scriptName
	 *
	 * @return
	 */
	public static boolean isScriptRunnable(String suiteName, String scriptName) {
		boolean isRunnable = false;
		String strVal = null;
		try {
			strVal = getFlagExcel(suiteName, scriptName);
			if (strVal.equalsIgnoreCase(Y)) {
				isRunnable = true;
			} else {
				isRunnable = false;
			}
		} catch (Exception e) {
			isRunnable = false;
			LogUtil.errorLog(ExcelDataUtil.class, EXCEPTIONCAUGHT, e);
		}
		return isRunnable;
	}

	/**
	 * <H1>Print test status</H1>
	 *
	 * @param suiteName
	 * @param searchValue
	 * @param testStatus
	 *
	 * @exception
	 */

	/**
	 * <H1>Print test status</H1>
	 *
	 * @param suiteName
	 * @param testCaseID
	 *
	 * @exception
	 */
	public static boolean getControls(String suiteName, String testCaseID) {

		if (suiteName.trim().isEmpty()) {
			LogUtil.infoLog(ExcelDataUtil.class, "Suite name not found!!!");
			return false;
		}

		if (testCaseID.trim().isEmpty()) {
			LogUtil.infoLog(ExcelDataUtil.class, "Test name is not specified!!!");
			return false;
		}

		return ExcelDataUtil.isScriptRunnable(suiteName.trim(), testCaseID.trim());
	}

	// Get browsers List
	/**
	 * <H1>Get browsers List</H1>
	 *
	 * @exception
	 *
	 * @return
	 */

}// End Excel class


class InvalidSheetException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	InvalidSheetException(String s) {
		super(s);
	}
}
