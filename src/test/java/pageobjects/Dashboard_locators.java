package pageobjects;

import io.appium.java_client.pagefactory.bys.builder.AppiumByBuilder;
import org.openqa.selenium.By;


public class Dashboard_locators {
    public static By loginBtn= By.className("android.widget.Button");
    public static By clickContinueBtn=By.xpath(
            "//android.widget.TextView[@text='Continue']"
    );
    public static By grantPermissionsBtn=By.id("com.android.packageinstaller:id/permission_allow_button");

    public static By LocPermissionBtn=By.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button");
    public static By ClickContinueBtn2=By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.widget.Button");
    public static By clickDigit(String digit) {
return By.xpath("//android.widget.TextView[@text='"+digit+"']");
        }
    
    public static By buttonbytext(String Class, String text) {
    	return By.xpath("//"+Class+"[@text='"+text+"']");
    }
}
