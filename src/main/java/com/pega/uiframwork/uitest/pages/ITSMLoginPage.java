package com.pega.uiframwork.uitest.pages;

import com.pega.uiframework.core.BasePage;
import com.pega.uiframework.exception.InvalidLocatorStrategyException;
import com.pega.uiframework.exception.PropertyNotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;

/**
 * Created by mekak2 on 4/24/17.
 */




public class ITSMLoginPage extends BasePage{
    public ITSMLoginPage(WebDriver driver) throws IOException {
        super(driver);
    }

    protected WebDriverWait wait;

    public void enterusername(String username) throws InvalidLocatorStrategyException, PropertyNotFoundException {
        type("username",username);
    }

    public void enterpwd(String password) throws InvalidLocatorStrategyException, PropertyNotFoundException {
        type("password",password);
    }

    public void clicksubmit() throws InvalidLocatorStrategyException, PropertyNotFoundException {
        click("submitbtn");
    }

    public void clickMenu() throws InvalidLocatorStrategyException, PropertyNotFoundException {
        // wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("menulabel")));
        click("menulabel");
    }

    public void clicklogoff() throws InvalidLocatorStrategyException, PropertyNotFoundException {
        click("logoff");
    }
}
