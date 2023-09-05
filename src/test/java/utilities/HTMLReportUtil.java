package utilities;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class HTMLReportUtil {

	static String html;

	public static String concatt = ".";

	public static String DummyString;
	public static String DummyString1;

	public static String ImagePathh;

	public static String testFailTakeScreenshot(String imagePath) throws IOException {

		File src = ((TakesScreenshot) GlobalUtil.getDriver()).getScreenshotAs(OutputType.FILE);
		File des = new File(imagePath);
		FileUtils.copyFile(src, des);
		System.out.println(des);
		DummyString = des.getAbsolutePath();
		String path = DummyString;
		String base = "TXAutomate/ExecutionReports/FailedScreenshots/";
		String relative = new File(base).toURI().relativize(new File(path).toURI()).getPath();

		return relative;
	}

	
	public static String failStringRedColor(String stepName) {
		html = "<span style='color:red'><b>" + stepName + "</b></span>";
		return html;
	}

	public static String passStringGreenColor(String stepName) {
		html = "<span style='color:#008000'><b>" + stepName + " - PASSED" + "</b></span>";
		return html;
	}

	/**
	 * Info string blue color.
	 *
	 * @param stepName
	 *        the step name
	 * 
	 * @return the string
	 */
	public static String infoStringBlueColor(String stepName) {
		html = "<span style='color:#41B2DB'><b> INFO - " + stepName + "</b></span>";
		return html;
	}

	/**
	 * Warning string orange color.
	 *
	 * @param stepName
	 *        the step name
	 * 
	 * @return the string
	 */
	public static String warningStringOrangeColor(String stepName) {
		html = "<span style='color:#FF8D42'><b> Warning! " + stepName + "</b></span>";
		return html;
	}

	/**
	 * Show base 64 image.
	 *
	 * @param base64string
	 *        the base 64 string
	 * 
	 * @return the string
	 */
	public static String showBase64Image(String base64string) {
		html = "<a href=\"" + base64string + "\" data-featherlight=\"image\"> <img class=\"report - img\" src = \"" + base64string
				+ "\" alt=\"Failure Screenshot\" style=\"width:750px;height:350px;\"</a>";
		return html;
	}

	public static String showBase64ImageMobile(String base64string) {
		html = "<a href=\"" + base64string + "\" data-featherlight=\"image\"> <img class=\"report - img\" src = \"" + base64string
				+ "\" alt=\"Failure Screenshot\" style=\"width:300px;height:550px;\"</a>";
		return html;
	}

}
