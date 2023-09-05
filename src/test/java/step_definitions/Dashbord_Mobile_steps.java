package step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pageobjects.Dashboard_locators;
import utilities.*;


public class Dashbord_Mobile_steps extends KeywordUtil {
@Given("open the app on emulator {string}")
    public void open_chrome_on_emulator(String deviceDetails){
    try {

        if (GlobalUtil.getCommonSettings().getExecutionEnv().equalsIgnoreCase("Local"))
            DriverUtil.invokeLocalMobileApp(GlobalUtil.getCommonSettings().getExecutionEnv(), deviceDetails);

        else if (GlobalUtil.getCommonSettings().getExecutionEnv().equalsIgnoreCase("Remote"))
            DriverUtil.invokeLocalMobileApp(GlobalUtil.getCommonSettings().getExecutionEnv(),deviceDetails);

    } catch (Exception e) {
        GlobalUtil.errorMsg = e.getMessage();
        Assert.fail(e.getMessage());
    }

}

@When("user clicks on the login button")
    public void click_login() throws InterruptedException {
    click(Dashboard_locators.loginBtn,"click on login button");
    ExtentUtil.attachScreenshotOfPassedTestsInReport();

    Thread.sleep(5000);
}

@When("user clicks on the continue button")
    public void click_continue(){
    click(Dashboard_locators.clickContinueBtn,"click on continue button");
    ExtentUtil.attachScreenshotOfPassedTestsInReport();
}
@When("user grants the permission")
    public void grants_permissioon(){
    click(Dashboard_locators.LocPermissionBtn,"click on location permission");
    ExtentUtil.attachScreenshotOfPassedTestsInReport();

}
@When("user enter the mobile number")
    public void user_enters_mobile_number(){
    writeInInput("android.widget.EditText", "className", "712345678", "Enter the number in textBox");
    ExtentUtil.attachScreenshotOfPassedTestsInReport();
}
@When("user clicks on continue button")
    public void click_continue1(){
    click(Dashboard_locators.ClickContinueBtn2,"click on continue button");
}

@When("user enters the pin number")
    public void enters_pin_number() throws InterruptedException{
   click(Dashboard_locators.clickDigit("2"),"click on the 2");
    click(Dashboard_locators.clickDigit("5"),"click on the 5");
    click(Dashboard_locators.clickDigit("8"),"click on the 8");
    click(Dashboard_locators.clickDigit("0"),"click on the 0");
    Thread.sleep(4000);
    ExtentUtil.attachScreenshotOfPassedTestsInReport();

}

@And("user clicks on {string} button")
public void click_bybuttontext(String button) throws InterruptedException {
	click(Dashboard_locators.buttonbytext("android.widget.TextView", button), "click on the " +button);
	Thread.sleep(5000);
}


}
