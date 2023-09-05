package utilities;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataHelper {

	@SuppressWarnings("null")
	public static List<HashMap<String, String>> data() {

		List<HashMap<String, String>> mydata = null;
		FileInputStream fs = null;
		XSSFWorkbook workbook = null;
		try {
			fs = new FileInputStream("classpath:TestData-seleniumframework.xlsx");
			workbook = new XSSFWorkbook(fs);
			XSSFSheet sheet = workbook.getSheet("SignInSignOut");
			Row HeaderRow = sheet.getRow(0);
			for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
				Row currentRow = sheet.getRow(i);
				HashMap<String, String> currentHash = new HashMap<String, String>();
				for (int j = 0; j < currentRow.getPhysicalNumberOfCells(); j++) {
					Cell currentCell = currentRow.getCell(j);
					switch (currentCell.getCellType()) {
						case Cell.CELL_TYPE_STRING:
							System.out.print(currentCell.getStringCellValue() + "\t");
							currentHash.put(HeaderRow.getCell(j).getStringCellValue(), currentCell.getStringCellValue());
							break;
					}
				}
				mydata.add(currentHash);
			}
			workbook.close();
			fs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mydata;
	}
}
