package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import com.aventstack.extentreports.ExtentTest;
import com.google.common.collect.ImmutableMap;
import io.appium.java_client.remote.MobilePlatform;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.github.bonigarcia.wdm.config.OperatingSystem;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.aventstack.extentreports.Status;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.github.bonigarcia.wdm.WebDriverManager;
import step_definitions.Hooks;
import step_definitions.RunCukesTest;


public class DriverUtil {
     public static DesiredCapabilities capabilities = new DesiredCapabilities();
    public static String deviceName = null;
	public static String osVersion = null;
	 public static AppiumDriverLocalService service;
	
	private DriverUtil() {

	}
	public static AndroidDriver<MobileElement> invokeLocalMobileApp(String exeEnv, String deviceDetails) {
		
		String deviceName = deviceDetails.split("_")[0];
		String osVersion = deviceDetails.split("_")[1];

		System.out.println(deviceName);
		System.out.println(osVersion);
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,deviceName);
		capabilities.setCapability(MobileCapabilityType.APP,"C:\\Dashboard_Appium_framework\\src\\test\\resources\\APK\\app-debug (1).apk");

		try {
			GlobalUtil.mdriver = new AndroidDriver<MobileElement>(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);
		} catch (MalformedURLException e) {
			System.err.println("");
			e.printStackTrace();
		}
		GlobalUtil.mdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		return GlobalUtil.mdriver;
	}
	
	

	

	

}

