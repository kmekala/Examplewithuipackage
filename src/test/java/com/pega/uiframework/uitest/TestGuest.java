package com.pega.uiframework.uitest;

import com.pega.uiframework.utils.TestDataProvider;
import com.pega.uiframwork.uitest.BaseTest;
import com.pega.uiframwork.uitest.pages.LoginPage;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by mekak2 on 4/17/17.
 */
public class TestGuest extends BaseTest {
    public WebDriverWait wait;


    @Test(groups = {"Regression","ssologin"}, dataProvider = "testdataprovider", dataProviderClass = TestDataProvider.class)
    public void testGuest(String email, String password) throws Exception {
        LoginPage loginpage = new LoginPage(driver);
        driver.manage().timeouts().implicitlyWait(4000, TimeUnit.SECONDS);
        loginpage.ClickonSignInLandingPage();
        driver.manage().timeouts().implicitlyWait(4000, TimeUnit.SECONDS);
        loginpage.enterEmail(email);
        loginpage.enterPassword(password);
        driver.manage().timeouts().implicitlyWait(4000, TimeUnit.SECONDS);
        loginpage.ClickonSubmit();
        loginpage.Logout();

    }

}
