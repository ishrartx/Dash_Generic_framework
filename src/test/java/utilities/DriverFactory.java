package utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.google.common.base.Function;


public class DriverFactory {

	static WebElement element = null;

	static List<WebElement> allElms = null;

	static String DummyString;

	
	public static void goToURL(String url) {
		try {
			if (GlobalUtil.getDriver() == null) {
				throw new Exception("Driver is null");
			}
			GlobalUtil.getDriver().manage().window().maximize();
			GlobalUtil.getDriver().manage().deleteAllCookies();
			GlobalUtil.getDriver().get(url);
			LogUtil.infoLog(DriverFactory.class, "Navigation to URL: " + url);
			LogUtil.htmlPassLog("Navigation to URL: " + url);
		} catch (Throwable ex) {
			LogUtil.htmlFailLog("Navigation to URL failed: " + url);
			LogUtil.errorLog(DriverFactory.class, "Navigation to URL failed: " + url, ex);
		}
	}

	
	public static String getCurrentURL() {
		String currentURL = null;
		try {
			if (GlobalUtil.getDriver() == null) {
				throw new Exception("Driver is null");
			} else {
				currentURL = GlobalUtil.getDriver().getCurrentUrl();
				LogUtil.htmlPassLog("Current URL is: " + currentURL);
				LogUtil.infoLog(DriverFactory.class, "Current URL is: " + currentURL);
			}
		} catch (Throwable ex) {
			LogUtil.errorLog(DriverFactory.class, "Failed to get current URL", ex);
			ex.printStackTrace();
		}
		return currentURL;
	}

	
	public static WebElement FindElementByXpath(String xpathExpression) {
		try {
			WebDriverWait wait = new WebDriverWait(GlobalUtil.getDriver(), CommonConstants.DEFAULT_WAIT_TIME_SMALL);
			element = wait.until(ExpectedConditions.visibilityOf(GlobalUtil.getDriver().findElement(By.xpath(xpathExpression))));
		} catch (Throwable e) {
			e.getMessage();
			Assert.fail(e.getMessage());
		}
		return element;
	}

	
	public static List<WebElement> FindElementsByXpath(String xpathExpression) {

		try {
			WebDriverWait wait = new WebDriverWait(GlobalUtil.getDriver(), CommonConstants.DEFAULT_WAIT_TIME_SMALL);
			allElms = wait.until(ExpectedConditions.visibilityOfAllElements(GlobalUtil.getDriver().findElements(By.xpath(xpathExpression))));
		} catch (Throwable e) {
			e.getMessage();
		}

		return allElms;
	}

	
	public static void quitDriver() {
		try {
			if (GlobalUtil.getDriver() == null) {
				throw new Exception("Driver is already closed.");
			} else {
				GlobalUtil.getDriver().quit();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	
	public static String testFailTakeScreenshot(String imagePath) throws IOException {

		File src = ((TakesScreenshot) GlobalUtil.getDriver()).getScreenshotAs(OutputType.FILE);
		File des = new File(imagePath);
		FileUtils.copyFile(src, des);

		DummyString = des.getAbsolutePath();
		String path = DummyString;
		String base = File.separator + CommonConstants.baseFolderName + File.separator + CommonConstants.screenShotFolderName + File.separator;
		String relative = new File(base).toURI().relativize(new File(path).toURI()).getPath();

		return relative;
	}

	
	public static byte[] takeScreenshot(String screenshotFilePath) {
		try {
			byte[] screenshot = ((TakesScreenshot) GlobalUtil.getDriver()).getScreenshotAs(OutputType.BYTES);
			FileOutputStream fileOuputStream = new FileOutputStream(screenshotFilePath);
			fileOuputStream.write(screenshot);
			fileOuputStream.close();
			return screenshot;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String testFailTakeScreenshotBase64(String imagePath) throws IOException {

		String src = ((TakesScreenshot) GlobalUtil.getDriver()).getScreenshotAs(OutputType.BASE64);
		File des = new File(imagePath);
		FileUtils.copyFile(new File(src), des);
//		System.out.println(des);
//		DummyString = des.getAbsolutePath();
//		String path = DummyString;
//		String base = "TXAutomate/ExecutionReports/FailedScreenshots/";
//		String relative = new File(base).toURI().relativize(new File(path).toURI()).getPath();

		return src;
	}

	
	public static void checkDefaultExecutionVariables(String executionVariableName, String defaultValue, String variableName) {
		if (executionVariableName == null || executionVariableName.isEmpty()) {
			executionVariableName = defaultValue;
		}
		LogUtil.infoLog(DriverFactory.class, "Setting " + variableName + " as:  " + executionVariableName);
	}

	
	public static WebElement waitForClickable(By locator) {
		try {
			WebDriverWait wait = new WebDriverWait(GlobalUtil.getDriver(), CommonConstants.DEFAULT_WAIT_TIME_SMALL);
			wait.ignoring(Exception.class);
			wait.ignoring(WebDriverException.class);
			return wait.until(ExpectedConditions.elementToBeClickable(locator));
		} catch (Throwable e) {
			GlobalUtil.e = e;
			return null;
		}
	}

	
	public static WebElement waitForVisible(By locator) {
		try {
			WebDriverWait wait = new WebDriverWait(GlobalUtil.getDriver(), CommonConstants.DEFAULT_WAIT_TIME_SMALL);
			wait.ignoring(Exception.class);
			wait.ignoring(WebDriverException.class);
			return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		} catch (Throwable e) {
			GlobalUtil.e = e;
			return null;
		}
	}

	
	public static void click(By locator, String logStep) {
		try {
			element = waitForClickable(locator);
			if (element == null) {
				String exceptionLog = "Element is not either not clickable or found.";
				LogUtil.errorLog(DriverFactory.class, exceptionLog);
				throw new NoSuchElementException(exceptionLog);
			} else {
				element.click();
				LogUtil.infoLog(CommonConstants.class, logStep + " locator: " + locator.toString());
				LogUtil.htmlPassLog(logStep);
			}
		} catch (Exception e) {
			GlobalUtil.e = e;
			e.printStackTrace();
		}
	}

	
	public static void inputText(By locator, String data, String logStep) {
		try {
			element = waitForVisible(locator);
			if (element == null) {
				String exceptionLog = "Element not found.";
				LogUtil.errorLog(DriverFactory.class, exceptionLog);
				throw new NoSuchElementException(exceptionLog);
			} else {
				element.clear();
				element.sendKeys(data);
				LogUtil.infoLog(DriverFactory.class, logStep + " value " + data + " in " + locator.toString());
				LogUtil.htmlPassLog(logStep);
			}
		} catch (Exception e) {
			GlobalUtil.e = e;
			e.printStackTrace();
		}
	}

	
	public static String getElementText(By locator, String logStep) {
		String elementText = null;
		try {
			element = waitForVisible(locator);
			if (element == null) {
				String exceptionLog = "Element not found.";
				LogUtil.errorLog(DriverFactory.class, exceptionLog);
				throw new NoSuchElementException(exceptionLog);
			} else {
				elementText = element.getText().trim();
				LogUtil.infoLog(DriverFactory.class, logStep + " value " + elementText + " in " + locator.toString());
				LogUtil.htmlPassLog(logStep);
			}
		} catch (Exception e) {
			GlobalUtil.e = e;
			e.printStackTrace();
		}
		return elementText;
	}

	
	public static boolean compareText(By locator, String expectedText, String logStep) {
		boolean flag = false;
		try {
			String actualText = getElementText(locator, logStep);
			if (actualText.contentEquals(expectedText)) {
				LogUtil.htmlPassLog("Expected " + expectedText + " And Actual " + actualText);
				flag = true;
			} else {
				LogUtil.htmlFailLog("Expected " + expectedText + " And Actual " + actualText);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	
	public static boolean waitForInvisible(By locator) {
		WebDriverWait wait = new WebDriverWait(GlobalUtil.getDriver(), CommonConstants.DEFAULT_WAIT_TIME_SMALL);
		return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}

	
	public static WebElement waitForPresent(By locator) {
		WebDriverWait wait = new WebDriverWait(GlobalUtil.getDriver(), CommonConstants.DEFAULT_WAIT_TIME_SMALL);
		wait.ignoring(ElementNotInteractableException.class);
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}

		public static WebElement waitForVisibleIgnoreStaleElement(By locator) {
		WebDriverWait wait = new WebDriverWait(GlobalUtil.getDriver(), CommonConstants.DEFAULT_WAIT_TIME_SMALL);
		wait.ignoring(StaleElementReferenceException.class);
		wait.ignoring(ElementNotInteractableException.class);
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	
	public static WebElement findWithFluentWait(final By locator, int secondsTimeout, int pollingMil) throws Exception {
		GlobalUtil.getDriver().manage().timeouts().implicitlyWait(CommonConstants.DEFAULT_WAIT_TIME_SMALL, TimeUnit.SECONDS);// Because if implicit wait is
				try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(GlobalUtil.getDriver()).withTimeout(Duration.ofSeconds(secondsTimeout)).pollingEvery(Duration.ofMillis(pollingMil))
					.ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class).ignoring(ElementNotInteractableException.class)
					.ignoring(WebDriverException.class);
			element = wait.until(new Function<WebDriver, WebElement>() {

				@Override
				public WebElement apply(WebDriver driver) {
					return driver.findElement(locator);
				}
			});
		} catch (Exception t) {
			throw new Exception("Timeout reached when searching for element! Time: " + secondsTimeout + " seconds " + "\n" + t.getMessage());
		} finally {
			GlobalUtil.getDriver().manage().timeouts().implicitlyWait(CommonConstants.DEFAULT_WAIT_TIME_SMALL, TimeUnit.SECONDS);
		}

		return element;
	}

		@SuppressWarnings("deprecation")
	public static WebElement findWithFluentWait(final By locator) throws Exception {
		GlobalUtil.getDriver().manage().timeouts().implicitlyWait(CommonConstants.DEFAULT_WAIT_TIME_SMALL, TimeUnit.SECONDS); // Because if implicit wait
		

		try {
			Wait<WebDriver> wait = new FluentWait<>(GlobalUtil.getDriver()).withTimeout(CommonConstants.DEFAULT_WAIT_TIME_SMALL, TimeUnit.SECONDS)
					.pollingEvery(Duration.ofMillis(200)).ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class)
					.ignoring(ElementNotInteractableException.class);

			element = wait.until(new Function<WebDriver, WebElement>() {

				@Override
				public WebElement apply(WebDriver driver) {
					return driver.findElement(locator);
				}
			});
		} catch (Exception t) {
			throw new Exception("Timeout reached when searching for element! Time: " + CommonConstants.DEFAULT_WAIT_TIME_SMALL + " seconds " + "\n" + t.getMessage());
		} finally {
			GlobalUtil.getDriver().manage().timeouts().implicitlyWait(CommonConstants.DEFAULT_WAIT_TIME_SMALL, TimeUnit.SECONDS);
		}

		return element;
	}

	
	public static WebElement getWebElement(By locator) throws Exception {
		return findWithFluentWait(locator);
	}

	
	public static boolean clickOnWebElement(WebElement webElement, String logStep) {

		WebDriverWait wait = new WebDriverWait(GlobalUtil.getDriver(), CommonConstants.DEFAULT_WAIT_TIME_SMALL);
		wait.until(ExpectedConditions.elementToBeClickable(webElement)).isDisplayed();
		if (webElement == null) {
			return false;
		} else {
			webElement.click();
			LogUtil.htmlPassLog(logStep);
			return true;
		}
	}

	
	public static boolean clickOnElementPosition(By locator, String logStep) {
		allElms = GlobalUtil.getDriver().findElements(locator);
		if (allElms.isEmpty()) {
			return false;
		}

		Actions ac = new Actions(GlobalUtil.getDriver());
		ac.moveToElement(GlobalUtil.getDriver().findElement(locator)).click();

		LogUtil.htmlPassLog(logStep);
		return true;
	}

	
	public static boolean clickJS(WebElement webElement, String logStep) {
		if (webElement == null) {
			return false;
		} else {
			((JavascriptExecutor) GlobalUtil.getDriver()).executeScript("return arguments[0].click();", webElement);
			LogUtil.htmlPassLog(logStep);

			return true;
		}
	}

	
	public static boolean acceptAlert() {
		Alert alert = GlobalUtil.getDriver().switchTo().alert();
		alert.accept();
		return true;
	}

	
	public static boolean switchToWindow() {
		ArrayList<String> tabs2 = new ArrayList<String>(GlobalUtil.getDriver().getWindowHandles());
		GlobalUtil.getDriver().switchTo().window(tabs2.get(1));
		return true;
	}

	
	public static String getImageTitle(By locator) {
		element = waitForVisible(locator);
		return element.getAttribute("title");

	}

	
	public static List<WebElement> getListElements(By locator) {
		try {
			findWithFluentWait(locator, 60, 300);
		} catch (Exception e) {
			return null;
		}

		return GlobalUtil.getDriver().findElements(locator);
	}

	
	public static List<WebElement> getListElements(By locator, String logStep) {
		try {
			findWithFluentWait(locator, 60, 300);
		} catch (Exception e) {
			return null;
		}
		LogUtil.htmlPassLog(logStep);

		return GlobalUtil.getDriver().findElements(locator);
	}

	
	public static List<WebElement> getListElements(By locator, String logStep, int waitTimeInSec, int poolingtimeinMilisec) {
		try {
			findWithFluentWait(locator, waitTimeInSec, poolingtimeinMilisec);
		} catch (Exception e) {
			e.printStackTrace();
		}
		LogUtil.htmlPassLog(logStep);

		return GlobalUtil.getDriver().findElements(locator);

	}

	
	public static boolean isWebElementNotPresent(By locator) {
		Boolean flag = false;
		try {
			GlobalUtil.getDriver().findElement(locator);
		} catch (Exception e) {
			flag = true;
		}

		return flag;
	}

	
	public static void pressEnter(By locator) {
		element = waitForVisible(locator);
		element.sendKeys(Keys.ENTER);
	}

	
	public static boolean inputTextJS(By locator, String data, String logStep) {
		boolean flag = false;
		try {
			element = waitForVisible(locator);
			((JavascriptExecutor) GlobalUtil.getDriver()).executeScript("arguments[0].value = arguments[1]", element, data);
			if (element.getText().equalsIgnoreCase(data)) {
				LogUtil.htmlPassLog(logStep + " - Input Text: \"" + data + "\"");
				flag = true;
			}
		} catch (Exception e) {
			GlobalUtil.e = e;
		}
		return flag;
	}

	
	public static void clearInput(By locator) {
		element = waitForVisible(locator);
		element.clear();
	}

	
	public static boolean selectByIndex(By locator, int index, String logStep) {
		boolean flag = false;
		try {
			Select sel = new Select(GlobalUtil.getDriver().findElement(locator));
			sel.selectByIndex(index);

			sel = new Select(GlobalUtil.getDriver().findElement(locator));
			if (sel.getFirstSelectedOption().isDisplayed()) {
				LogUtil.htmlPassLog(logStep);
				flag = true;
			}
		} catch (Exception e) {
			GlobalUtil.e = e;
		}
		return flag;
	}

	
	public static boolean selectByValue(By locator, String value, String logStep) {
		boolean flag = false;
		try {
			Select sel = new Select(GlobalUtil.getDriver().findElement(locator));
			sel.selectByValue(value);

			sel = new Select(GlobalUtil.getDriver().findElement(locator));
			if (sel.getFirstSelectedOption().isDisplayed()) {
				LogUtil.htmlPassLog(logStep);
				flag = true;
			}
		} catch (Exception e) {
			GlobalUtil.e = e;
		}
		return flag;
	}

	
	public static boolean selectByVisibleText(By locator, String value, String logStep) {
		try {
			Select sel = new Select(GlobalUtil.getDriver().findElement(locator));
			sel.selectByVisibleText(value);
			LogUtil.htmlPassLog(logStep);

			return true;
		} catch (Exception e) {
			GlobalUtil.e = e;
			return false;
		}
	}

	
	public static boolean verifyDropdownSelectedValue(By locator, String data, String logStep) {
		String defSelectedVal = null;
		try {
			Select sel = new Select(waitForVisible(locator));
			defSelectedVal = sel.getFirstSelectedOption().getText();
			LogUtil.htmlPassLog(logStep);
		} catch (Exception e) {
			GlobalUtil.e = e;
		}
		return defSelectedVal.trim().equals(data.trim());
	}

	
	public static boolean doubleClick(By locator, String logStep) {
		boolean result = false;
		try {
			element = GlobalUtil.getDriver().findElement(locator);
			Actions action = new Actions(GlobalUtil.getDriver()).doubleClick(element);
			action.build().perform();
			LogUtil.htmlPassLog(logStep);

			result = true;

		} catch (StaleElementReferenceException e) {
			GlobalUtil.e = e;
			LogUtil.errorLog(DriverFactory.class, locator.toString() + " - Element is not attached to the page document ", e);
			result = false;
		} catch (NoSuchElementException e) {
			GlobalUtil.e = e;
			LogUtil.errorLog(DriverFactory.class, locator.toString() + " - Element is not attached to the page document ", e);
			result = false;
		} catch (Exception e) {
			GlobalUtil.e = e;
			LogUtil.errorLog(DriverFactory.class, locator.toString() + " - Element is not attached to the page document ", e);
			result = false;
		}
		return result;
	}

	
	public static boolean switchToFrame(String frameName) {
		try {
			GlobalUtil.getDriver().switchTo().frame(frameName);
			return true;
		} catch (Exception e) {
			GlobalUtil.e = e;
			LogUtil.errorLog(DriverFactory.class, frameName + " TO FRAME FAILED", e);
			return false;
		}
	}

	
	public static boolean switchToFrameByWebElement(By locator) {
		try {
			element = GlobalUtil.getDriver().findElement(locator);
			GlobalUtil.getDriver().switchTo().frame(element);
			return true;
		} catch (Exception e) {
			GlobalUtil.e = e;
			LogUtil.errorLog(DriverFactory.class, "TO FRAME FAILED", e);
			return false;
		}
	}

	
	public static String getElementProperty(By locator, String attributeName) {
		String attribute = null;
		try {
			element = GlobalUtil.getDriver().findElement(locator);
			attribute = element.getAttribute(attributeName);
		} catch (Exception e) {
			GlobalUtil.e = e;
		}
		return attribute;
	}

	
	public static void refreshPage() {
		try {
			GlobalUtil.getDriver().navigate().refresh();
		} catch (Exception e) {
			GlobalUtil.e = e;
		}
	}

	
	public static void waitForAjax() {
		new WebDriverWait(GlobalUtil.getDriver(), CommonConstants.DEFAULT_WAIT_TIME_SMALL).until(new ExpectedCondition<Boolean>() {

			public Boolean apply(WebDriver driver) {
				driver = GlobalUtil.getDriver();
				JavascriptExecutor js = (JavascriptExecutor) driver;
				return (Boolean) js.executeScript("return jQuery.active == 0");
			}
		});
	}

	
	public static void waitForDOMLoadToComplete() {
		new WebDriverWait(GlobalUtil.getDriver(), CommonConstants.DEFAULT_WAIT_TIME_SMALL).until(new ExpectedCondition<Boolean>() {

			public Boolean apply(WebDriver driver) {
				driver = GlobalUtil.getDriver();
				JavascriptExecutor js = (JavascriptExecutor) driver;
				return (Boolean) js.executeScript("return document.readyState == complete");
			}
		});
	}
}