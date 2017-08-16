package com.pega.uiframwork.uitest;

/**
 * Created by mekak2 on 4/17/17.
 */


import com.pega.uiframework.builder.WebCapabilitiesBuilder;
import com.pega.uiframework.factory.WebDriverFactory;
import com.pega.uiframework.utils.ObjectRepository;
import com.pega.uiframework.utils.ProjectConfigurator;
import com.pega.uiframework.utils.Xls_Reader;
import com.pega.uiframwork.uitest.util.Constants;
import com.pega.uiframwork.uitest.util.ExtentManager;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.*;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class BaseTest {
    protected static Properties config;
    protected WebDriver driver;
    public Xls_Reader xls = new Xls_Reader(Constants.DATA_XLS_PATH);
    public ExtentReports extent = ExtentManager.getInstance();
    public ExtentTest test;
    public Integer testiteration=0;

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(ITestContext context) throws Exception {
        config = ProjectConfigurator.initializeProjectConfigurationsFromFile("Project.properties");
        ObjectRepository.setRepositoryDirectory(config.getProperty("object.repository.dir"));
    }

    @AfterSuite(alwaysRun = true)
    public void removeFile() throws InterruptedException {
        String filepath = System.getProperty("user.dir") + "\\Data.properties";
        File file = new File(filepath);
        if (file.exists()) {
            System.out.println("File exists and deletion in progress..");
            Boolean status = file.delete();
            Thread.sleep(3000);
            System.out.println(" Deletion status: " + status);
        }
    }

    @BeforeTest(alwaysRun = true)
    public void beforeClass(ITestContext context) throws Exception {
        final String browser = context.getCurrentXmlTest().getParameter("browser");
        final String version = context.getCurrentXmlTest().getParameter("version");
        final String platform = context.getCurrentXmlTest().getParameter("platform");
        DesiredCapabilities caps = new WebCapabilitiesBuilder().addBrowser(browser)
                .addBrowserDriverExecutablePath(config.getProperty("chrome.driver.path")).addVersion(version)
                .addPlatform(platform).build();

        driver = new WebDriverFactory().createDriver(caps);
        driver.manage().deleteAllCookies();
        driver.get(config.getProperty("url"));
        driver.manage().window().maximize();

    }

    @AfterTest(alwaysRun = true)
    public void afterClass(ITestContext context) throws Exception {
        if (null != driver) {
            driver.close();
            driver.quit();
        }

    }


    public void reportFailure(String failureMessage) {
        takeScreenShot();
        Assert.fail(failureMessage);
    }

    public void takeScreenShot() {
        Date d = new Date();
        String screenshotFile = d.toString().replace(":", "_").replace(" ", "_") + ".png";
        String filePath = Constants.REPORTS_PATH + "//screenshots//" + screenshotFile;
        // store screenshot in that file
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        try {
            FileUtils.copyFile(scrFile, new File(filePath));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}