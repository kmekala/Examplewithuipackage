package com.pega.uiframwork.uitest.pages;

import com.pega.uiframework.core.BasePage;
import com.pega.uiframework.exception.InvalidLocatorStrategyException;
import com.pega.uiframework.exception.PropertyNotFoundException;
import org.openqa.selenium.WebDriver;

import java.io.IOException;

/**
 * Created by mekak2 on 4/17/17.
 */
public class LoginPage extends BasePage {

    public LoginPage(WebDriver driver) throws IOException {
        super(driver);
    }


   public void ClickonSignInLandingPage() throws Exception {

        click("ClickonSignInLandingPage");
    }


    public void enterEmail(String enterEmail) throws PropertyNotFoundException, InvalidLocatorStrategyException {
        type("Email", enterEmail);
    }

    public void enterPassword(String enterPassword) throws PropertyNotFoundException, InvalidLocatorStrategyException {
        type("Password", enterPassword);
    }

    public void ClickonSubmit() throws PropertyNotFoundException, InvalidLocatorStrategyException {
        click("ClickonSubmit");
    }

    public void Logout() throws PropertyNotFoundException, InvalidLocatorStrategyException {
        click("Logout");
    }

}