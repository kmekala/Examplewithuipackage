package com.pega.uiframework.uitest;

import com.pega.uiframwork.uitest.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Created by mekak2 on 4/20/17.
 */
public class TestClient_CBT extends BaseTest{

    @DataProvider(name = "client")
    // The number of times data is repeated, test will be executed the same no. of times
    // Here it will execute two times
    public static Object[][] client() {
        return new Object[][]{
                {"Testautomation-client@cisco.com","DietC0ke"}

        };
    }




    @Test(groups = { "Regression"})
    public void nestedFrames() throws Exception {
        driver.get("http://web.kalyanmekala.net/nested_frames");
        driver.switchTo().frame("frame-top");
        driver.switchTo().frame("frame-middle");
        Assert.assertEquals(driver.findElement(By.id("content")).getText(), "MIDDLE");
    }

    @Test(groups = { "Regression"})
    public void iFrames() throws Exception {
        driver.get("http://web.kalyanmekala.net/tinymce");
        driver.switchTo().frame("mce_0_ifr");
        WebElement editor = driver.findElement(By.id("tinymce"));
        String beforeText = editor.getText();
        editor.clear();
        editor.sendKeys("Hello World!");
        String afterText = editor.getText();
        Assert.assertNotEquals(afterText,beforeText);
        driver.switchTo().defaultContent();
        Assert.assertEquals(driver.findElement(By.cssSelector("h3")).getText(),"An iFrame containing the TinyMCE WYSIWYG Editor");

    }

}
