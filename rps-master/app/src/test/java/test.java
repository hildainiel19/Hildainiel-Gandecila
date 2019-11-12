import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;

import static io.appium.java_client.touch.offset.PointOption.point;

public class test {


    @Test
    public void testFacebook() throws MalformedURLException, InterruptedException {

        DesiredCapabilities dc = new DesiredCapabilities();
        dc.setCapability(MobileCapabilityType.DEVICE_NAME,"emulator-5554");
        dc.setCapability("platformName","android");
        dc.setCapability("appPackage","funplay.nativeapp.rps");
        dc.setCapability("appActivity",".activities.MainActivity");

        AndroidDriver<AndroidElement> ad = new AndroidDriver<AndroidElement>(new URL("http://127.0.0.1:4723/wd/hub"),dc);

        Thread.sleep(7000);
        MobileElement el1 = ad.findElementById("funplay.nativeapp.rps:id/item_facebook");
        el1.click();
        Thread.sleep(7000);


                Assert.assertEquals(ad.findElementByXPath("//android.view.View[@content-desc=\"Cao Thủ Kéo Búa Bao\"]")
                        .getAttribute("content-desc"),"Cao Thủ Kéo Búa Bao");
    }

    @Test
    public void testHowToPlay() throws MalformedURLException, InterruptedException {
        DesiredCapabilities dc = new DesiredCapabilities();
        dc.setCapability(MobileCapabilityType.DEVICE_NAME,"emulator-5554");
        dc.setCapability("platformName","android");
        dc.setCapability("appPackage","funplay.nativeapp.rps");
        dc.setCapability("appActivity",".activities.MainActivity");

        AndroidDriver<AndroidElement> ad = new AndroidDriver<AndroidElement>(new URL("http://127.0.0.1:4723/wd/hub"),dc);

        Thread.sleep(7000);
        MobileElement e21 = (MobileElement) ad.findElementById("funplay.nativeapp.rps:id/item_howtoplay");
        e21.click();
        Thread.sleep(7000);

            Assert.assertEquals(ad.findElementByXPath("//android.webkit.WebView[@content-desc=\"Rock Paper Scissors\"]")
                .getAttribute("content-desc"), "Rock Paper Scissors");
    }

    @Test
    public void testRegisterInputs() throws MalformedURLException, InterruptedException {
        DesiredCapabilities dc = new DesiredCapabilities();
        dc.setCapability(MobileCapabilityType.DEVICE_NAME,"emulator-5554");
        dc.setCapability("platformName","android");
        dc.setCapability("appPackage","funplay.nativeapp.rps");
        dc.setCapability("appActivity",".activities.MainActivity");

        AndroidDriver<AndroidElement> ad = new AndroidDriver<AndroidElement>(new URL("http://127.0.0.1:4723/wd/hub"),dc);

        Thread.sleep(5000);
        MobileElement e31 = (MobileElement) ad.findElementById("funplay.nativeapp.rps:id/glow");
        e31.click();
        Thread.sleep(5000);
        MobileElement el2 = (MobileElement) ad.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.webkit.WebView");
        el2.click();
        MobileElement e32 = (MobileElement) ad.findElementByXPath("//android.webkit.WebView[@content-desc=\"funplay/register?affiliateID=17566\"]/android.view.View/android.view.View/android.view.View[3]/android.view.View[2]/android.view.View/android.widget.EditText[1]");
        e32.sendKeys("deyn123");
        MobileElement e33 = (MobileElement) ad.findElementByXPath("//android.webkit.WebView[@content-desc=\"funplay/register?affiliateID=17566\"]/android.view.View/android.view.View/android.view.View[3]/android.view.View[2]/android.view.View/android.widget.EditText[2]");
        e33.sendKeys("Deyn123456");
        ad.navigate().back();
        MobileElement e34 = (MobileElement) ad.findElementByXPath("//android.webkit.WebView[@content-desc=\"funplay/register?affiliateID=17566\"]/android.view.View/android.view.View/android.view.View[3]/android.view.View[2]/android.view.View/android.widget.EditText[3]");
        e34.sendKeys("Deyn123456");
        MobileElement e35 = (MobileElement) ad.findElementByXPath("//android.webkit.WebView[@content-desc=\"funplay/register?affiliateID=17566\"]/android.view.View/android.view.View/android.view.View[3]/android.view.View[2]/android.view.View/android.widget.EditText[4]");
        e35.sendKeys("hildainiel.gandecila@mnltechnology.com");

        TouchAction action = new TouchAction(ad);
        action.press(point(600, 1600));
        action.moveTo(point(600, 250));
        action.release();
        action.perform();

        MobileElement e36 = (MobileElement) ad.findElementByXPath("//android.webkit.WebView[@content-desc=\"funplay/register?affiliateID=17566\"]/android.view.View/android.view.View/android.view.View[3]/android.view.View[2]/android.view.View/android.view.View[6]/android.widget.EditText");
        e36.sendKeys("9063847581");
        MobileElement e37 = (MobileElement) ad.findElementByXPath("//android.webkit.WebView[@content-desc=\"funplay/register?affiliateID=17566\"]/android.view.View/android.view.View/android.view.View[3]/android.view.View[2]/android.view.View/android.widget.EditText[3]");
        e37.sendKeys("Hildainiel Gandecila");

        action.press(point(600, 250));
        action.moveTo(point(600, 1600));
        action.release();
        action.perform();

        String password = "Deyn123456";
        int x = password.length();
        String str = "•";
        String pw = StringUtils.repeat(str, x);

            Assert.assertEquals(ad.findElementByXPath("//android.webkit.WebView[@content-desc=\"funplay/register?affiliateID=17566\"]/android.view.View/android.view.View/android.view.View[3]/android.view.View[2]/android.view.View/android.widget.EditText[1]")
                .getText(), "deyn123");
            Assert.assertEquals(ad.findElementByXPath("//android.webkit.WebView[@content-desc=\"funplay/register?affiliateID=17566\"]/android.view.View/android.view.View/android.view.View[3]/android.view.View[2]/android.view.View/android.widget.EditText[2]")
                .getText(), pw);
            Assert.assertEquals(ad.findElementByXPath("//android.webkit.WebView[@content-desc=\"funplay/register?affiliateID=17566\"]/android.view.View/android.view.View/android.view.View[3]/android.view.View[2]/android.view.View/android.widget.EditText[3]")
                .getText(), pw);
            Assert.assertEquals(ad.findElementByXPath("//android.webkit.WebView[@content-desc=\"funplay/register?affiliateID=17566\"]/android.view.View/android.view.View/android.view.View[3]/android.view.View[2]/android.view.View/android.widget.EditText[4]")
                .getText(), "hildainiel.gandecila@mnltechnology.com");

            action.press(point(600, 1600));
            action.moveTo(point(600, 250));
            action.release();
            action.perform();

            Assert.assertEquals(ad.findElementByXPath("//android.webkit.WebView[@content-desc=\"funplay/register?affiliateID=17566\"]/android.view.View/android.view.View/android.view.View[3]/android.view.View[2]/android.view.View/android.view.View[6]/android.widget.EditText")
                .getText(), "9063847581");
            Assert.assertEquals(ad.findElementByXPath("//android.webkit.WebView[@content-desc=\"funplay/register?affiliateID=17566\"]/android.view.View/android.view.View/android.view.View[3]/android.view.View[2]/android.view.View/android.widget.EditText[3]")
                .getText(), "Hildainiel Gandecila");
    }


}
