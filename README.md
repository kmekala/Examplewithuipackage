# uiautomation
uiautomation is an Automation framework for developing tests using Selenium/WebDriver and TestNG

This framework abstracts away the boiler plate code that are needed for setting up any Selenium/WebDriver/TestNG based automation framework. It provides the below abilities:

- Logging
- Generation of screenshots in case of test failure
- Run tests on different browsers/platforms
- Run tests locally, on in-house selenium grid, on sauce labs or on browserstack
- Generate customized reports
- Abstract away driver creation, capability creation based on browser
- Rerun failed test cases
- Inject test data from data source like xml
- Store/retrieve locators from object repositories
- Scalable and Maintainable
- Configurable project parameters like, application url, time waits, selenium grid information etc.

#Preparation:

- Install Eclipse (or any other IDE)
- Install TestNG plugin (for Eclipse)
- Install m2e (or similar for any other IDE)
- Install maven
  - for mac
```
   brew install chromedriver
   brew install maven

   sudo nano /etc/paths
 
   Enter your password
   Add this path:
 
   /usr/local/Cellar/chromedriver
   /usr/local/Cellar/maven
     
   control X
   Save
   Enter
 
   echo $PATH

```

#Example pom.xml for the  project and how to use this
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.pega</groupId>
    <artifactId>Demo</artifactId>
    <version>1.0-SNAPSHOT</version>
    <properties>
        <org.selenium-version>3.0.1</org.selenium-version>
        <org.fitnesse-version>20160515</org.fitnesse-version>
        <jdk.version>1.8</jdk.version>
        <org.springframework-version>3.2.3.RELEASE</org.springframework-version>
        <resources.encoding>UTF-8</resources.encoding>
        <sources.encoding>UTF-8</sources.encoding>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <restlet.version>2.0.14</restlet.version>
    </properties>
<dependencies>
    <dependency>
        <groupId>com.github.kmekala</groupId>
        <artifactId>uiautomation</artifactId>
        <version>0.0.1</version>
    </dependency>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.11</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>com.relevantcodes</groupId>
        <artifactId>extentreports</artifactId>
        <version>2.41.2</version>
    </dependency>
</dependencies>
 <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.3</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>2.19.1</version>
                <configuration>
                    <testFailureIgnore>true</testFailureIgnore>
                    <forkCount>0</forkCount>
                    <suiteXmlFiles>
                        <suiteXmlFile>src/main/resources/testng.xml</suiteXmlFile>
                    </suiteXmlFiles>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```


### Steps to create a new test script

Create a new file `BaseTest.java` in `src/main/java` in package `com.pega.uiframeworks.uitest`. We'll be using this as a base test class, that all test script classes will extend. This class will contain few configuration methods, that will initialize the project and create appropriate driver. At the end of the test, it will close the driver.

```
package com.pega.uiframwork.uitest;

/**
 * Created by mekak2 on 4/17/17.
 */


import WebCapabilitiesBuilder;
import com.pega.uiframework.factory.WebDriverFactory;
import com.pega.uiframework.utils.ObjectRepository;
import com.pega.uiframework.utils.ProjectConfigurator;
import com.pega.uiframwork.uitest.util.Constants;
import com.pega.uiframwork.uitest.util.ExtentManager;
import com.pega.uiframwork.uitest.util.Xls_Reader;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class BaseTest {
    protected static Properties config;
    protected WebDriver driver;
    public Xls_Reader xls = new Xls_Reader(Constants.DATA_XLS_PATH);
    public ExtentReports extent = ExtentManager.getInstance();
    public ExtentTest test;

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(ITestContext context) throws Exception {
        config = ProjectConfigurator.initializeProjectConfigurationsFromFile("project.properties");
        ObjectRepository.setRepositoryDirectory(config.getProperty("object.repository.dir"));
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

    public void init(String bType) {
        test.log(LogStatus.INFO, "Opening browser - " + bType);
        if (!Constants.GRID_RUN) {
            // local machine
            if (bType.equals("Mozilla"))
                driver = new FirefoxDriver();
            else if (bType.equals("Chrome")) {
                //System.setProperty("webdriver.chrome.driver", Constants.CHROME_DRIVER_EXE);
                driver = new ChromeDriver();
            }
        } else {
            // grid
            DesiredCapabilities cap = null;
            if (bType.equals("Mozilla")) {
                cap = DesiredCapabilities.firefox();
                cap.setBrowserName("firefox");
                cap.setJavascriptEnabled(true);
                cap.setPlatform(org.openqa.selenium.Platform.WINDOWS);

            } else if (bType.equals("Chrome")) {
                cap = DesiredCapabilities.chrome();
                cap.setBrowserName("chrome");
                cap.setPlatform(org.openqa.selenium.Platform.WINDOWS);
            }

            try {
                driver = new RemoteWebDriver(new URL("https://emailaddress%40pega.com:authkey@hub.crossbrowsertesting.com/wd/hub"), cap);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        test.log(LogStatus.INFO, "Opened browser successfully - " + bType);

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



```
### Create a project configuration file

Create a new file `project.properties` in the root of the project and add following  lines:

```
# Base application url
url=https://pegauser:newpega2015@pdn-stg.pega.com

# Directory where element locators will be stored. All element locator files should end with .properties.
# Locators can be specified in this format: locator_name=locator_strategy,locator
# For ex: mail_link=XPATH,//span[text()='Mail']. Supported locator strategies are 'XPATH', 'ID', 'NAME',
# 'CLASS_NAME', 'TAG_NAME', 'CSS_SELECTOR', 'LINK_TEXT', 'PARTIAL_LINK_TEXT'
object.repository.dir=src/main/resources

# Application modules
application.modules=ssologin

# Test Groups
test.groups=smoke,regression

# Number of times to rerun the test in case of test failure. '0' means, failed tests will not be
# executed again
max.retry.count=0

# Path to chromedriver executable file
chrome.driver.path=/Users/mekak2/Documents/workspace/uitest/ChromeDriver/chromedriver

```

### Create Test Script

Create a test script to open browser and login. Under `src/test/java` and package `com.pega.uiframework.uitest`. This tests reads data from testdata.xml

```
package com.pega.uiframework.uitests;

import org.testng.annotations.Test;

import com.pega.uiframework.utils.TestDataProvider;

public class TestClass extends BaseTest
{
    @Test(groups = { "smoke" }, dataProvider = "testdataprovider", dataProviderClass = TestDataProvider.class)
    public void testMethod(String email, String password) throws Exception
    {
        HomePage homePage = new HomePage(driver);
        
        MailLoginPage loginPage = homePage.clickOnMailLink();
        loginPage.typeEmailAddress(email);
        loginPage.typePassword(password);
        
        MailHomePage mailHomePage = loginPage.clickOnSigninButton();

        // Do something with mail home page
    }
}
```
Note that, email and password parameters are injected into the test method from outside data source. To create the data source, create a new file in `src/main/resources` directory. For example our testdata.xml file looks like this:

```
<?xml version="1.0" encoding="UTF-8"?>

<testdatasuite>
    <testclass name="com.pega.uiframework.uitest.TestGuest">
        <dataset>
            <email>Testautomation-guest@guest.com</email>
            <password>DietC0ke</password>
        </dataset>
    </testclass>
</testdatasuite>
```
You can have any number of `dataset` sections inside `testclass` section. If there are multiple `dataset` sections, test method will be executed multiple times with different `dataset` test data.

## Create few page classes. Under `src/main/java` -->package `com.pega.uiframework.uitest` -->package `pages`

```
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
```


## Store element locators

To store element locators create .properties files inside src/main/resources folder. Sample locator file will look like this:

```
mail_link=XPATH,//span[text()='Mail']
email_text_field=ID,email
password_field=NAME,password
```


## Tests with data provider

```
package com.pega.uiframework.uitest;

import com.pega.uiframwork.uitest.BaseTest;
import com.pega.uiframwork.uitest.pages.LoginPage;
import com.pega.uiframwork.uitest.util.DataUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by mekak2 on 4/17/17.
 */
public class TestClient extends BaseTest{

    String testCaseName="SSOLogin";
    @DataProvider(name = "client")
    // The number of times data is repeated, test will be executed the same no. of times
    // Here it will execute two times
    public static Object[][] client() {
        return new Object[][]{
                {"Testautomation-client@cisco.com","DietC0ke"}
        };
    }

    @Test(groups = { "smoke" }, dataProvider = "client")
    public void testClient(String email, String password) throws Exception
    {
        LoginPage loginpage=new LoginPage(driver);
        driver.manage().timeouts().implicitlyWait(4000, TimeUnit.SECONDS);
        loginpage.ClickonSignInLandingPage();
        driver.manage().timeouts().implicitlyWait(4000, TimeUnit.SECONDS);
        loginpage.enterEmail(email);
        loginpage.enterPassword(password);
        driver.manage().timeouts().implicitlyWait(4000, TimeUnit.SECONDS);
        loginpage.ClickonSubmit();

    }
    @DataProvider
    public Object[][] getData(){
        return DataUtil.getData(xls, testCaseName);
    }

}

```

## Test to read data from xlsx files
```
package com.pega.uiframework.uitest;

import com.pega.uiframwork.uitest.BaseTest;
import com.pega.uiframwork.uitest.pages.LoginPage;
import com.pega.uiframwork.uitest.util.Constants;
import com.pega.uiframwork.uitest.util.DataUtil;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

/**
 * Created by mekak2 on 4/17/17.
 */
public class TestXLS extends BaseTest {

    String testCaseName = "SSOLogin";

    @Test(groups = {"regression"}, dataProvider = "getData")
    public void testClient(Hashtable<String, String> data) throws Exception {
        test = extent.startTest(testCaseName);
        if (!DataUtil.isTestExecutable(xls, testCaseName) || data.get(Constants.RUNMODE_COL).equals("N")) {
            test.log(LogStatus.SKIP, "Skipping the test as Rnumode is N");
            throw new SkipException("Skipping the test as Runmode is N");
        }
        test.log(LogStatus.INFO, "Starting test");
        LoginPage loginpage = new LoginPage(driver);
        driver.manage().timeouts().implicitlyWait(4000, TimeUnit.SECONDS);
        loginpage.ClickonSignInLandingPage();
        driver.manage().timeouts().implicitlyWait(4000, TimeUnit.SECONDS);
        loginpage.enterEmail(data.get("Username"));
        loginpage.enterPassword(data.get("Password"));
        driver.manage().timeouts().implicitlyWait(4000, TimeUnit.SECONDS);
        loginpage.ClickonSubmit();
        loginpage.Logout();
        test.log(LogStatus.PASS, "Test Passed");
    }


    @AfterMethod
    public void quit() {
        if (extent != null) {
            extent.endTest(test);
            extent.flush();
        }
    }

    @DataProvider
    public Object[][] getData() {

        return DataUtil.getData(xls, testCaseName);
    }

}

```
##Create  `util` package under `src\main\java` under package `com.pega.uiframework` and add these files

#1
```
package com.pega.uiframwork.uitest.util;

import java.util.Hashtable;

/**
 * Created by mekak2 on 4/17/17.
 */
public class Constants {

    public static final boolean GRID_RUN=false;

    //paths
    public static final String CHROME_DRIVER_EXE=System.getProperty("user.dir")+"/ChromeDriver/chromedriver";
    //paths
    public static final String REPORTS_PATH = System.getProperty("user.dir")+ "/reports";
    public static final String DATA_XLS_PATH = System.getProperty("user.dir") + "/config/Data.xlsx";
    public static final String TESTDATA_SHEET = "TestData";
    public static final Object RUNMODE_COL = "Runmode";
    public static final String TESTCASES_SHEET = "TestCases";


    public static Hashtable<String,String> table;


    public static Hashtable<String,String> getEnvDetails(){
        if(table==null){
            table = new Hashtable<String,String>();
        }
        return table;
    }
}
```

#2
```
package com.pega.uiframwork.uitest.util;

import com.pega.uiframework.utils.Xls_Reader;

import java.util.Hashtable;

public class DataUtil {

    public static Object[][] getData(Xls_Reader xls, String testCaseName) {
        String sheetName = Constants.TESTDATA_SHEET;
        // reads data for only testCaseName

        int testStartRowNum = 1;
        System.out.println(xls.getCellData(sheetName, 0, testStartRowNum));
        while (!xls.getCellData(sheetName, 0, testStartRowNum).equals(testCaseName)) {
            testStartRowNum++;
        }
        System.out.println("Test starts from row - " + testStartRowNum);
        int colStartRowNum = testStartRowNum + 1;
        int dataStartRowNum = testStartRowNum + 2;

        // calculate rows of data
        int rows = 0;
        while (!xls.getCellData(sheetName, 0, dataStartRowNum + rows).equals("")) {
            rows++;
        }
        System.out.println("Total rows are  - " + rows);

        //calculate total cols
        int cols = 0;
        while (!xls.getCellData(sheetName, cols, colStartRowNum).equals("")) {
            cols++;
        }
        System.out.println("Total cols are  - " + cols);
        Object[][] data = new Object[rows][1];
        //read the data
        int dataRow = 0;
        Hashtable<String, String> table = null;
        for (int rNum = dataStartRowNum; rNum < dataStartRowNum + rows; rNum++) {
            table = new Hashtable<String, String>();
            for (int cNum = 0; cNum < cols; cNum++) {
                String key = xls.getCellData(sheetName, cNum, colStartRowNum);
                String value = xls.getCellData(sheetName, cNum, rNum);
                table.put(key, value);
                // 0,0 0,1 0,2
                //1,0 1,1
            }
            data[dataRow][0] = table;
            dataRow++;
        }
        return data;
    }

    public static boolean isTestExecutable(Xls_Reader xls, String testCaseName) {
        int rows = xls.getRowCount(Constants.TESTCASES_SHEET);
        for (int rNum = 2; rNum <= rows; rNum++) {
            String tcid = xls.getCellData(Constants.TESTCASES_SHEET, "TCID", rNum);
            if (tcid.equals(testCaseName)) {
                String runmode = xls.getCellData(Constants.TESTCASES_SHEET, "Runmode", rNum);
                if (runmode.equals("Y"))
                    return true;
                else
                    return false;

            }
        }
        return false;
    }
}

```
#3

```

package com.pega.uiframwork.uitest.util;

//http://relevantcodes.com/Tools/ExtentReports2/javadoc/index.html?com/relevantcodes/extentreports/ExtentReports.html


import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;

import java.io.File;
import java.util.Date;

public class ExtentManager {
	private static ExtentReports extent;

	public static ExtentReports getInstance() {
		if (extent == null) {
			Date d = new Date();
			String fileName = d.toString().replace(":", "_").replace(" ", "_") + ".html";
			String reportPath = Constants.REPORTS_PATH + fileName;

			extent = new ExtentReports(reportPath, true, DisplayOrder.NEWEST_FIRST);


			extent.loadConfig(new File(System.getProperty("user.dir") + "/ReportsConfig.xml"));
			// optional
			extent.addSystemInfo("Selenium Version", "3.0.1").addSystemInfo(
					"Environment", "QA");
		}
		return extent;
	}
}
```

## Add data file under config /config/Data.xlsx

```
name Sheet1 as TestCases.
with two columns TCID and run mode

TCID	   Runmode
SSOLogin	Y
```
```
name sheet2 as TestData, 3 rows:

SSOLogin				
Runmode	Browser	  Username	                        Password	ExpectedResult
Y	     Chrome	  qa.contractor1@pegasystems.com	Pegasys321+	
```


## Run Test Script

To run the test script, we need a testng xml file. Create an xml file in the root of the directory and add the following content:

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd" >
<suite name="Sample Test Suite" verbose="1" parallel="classes" thread-count="1">
    <listeners>
        <listener class-name="com.pega.uiframework.listener.ScreenshotListener" />
        <listener class-name="com.pega.uiframework.listener.RetryListener" />
    </listeners>

    <test name="Sample Smoke Tests">
        <parameter name="browser" value="chrome" />
        <parameter name="version" value="57.0" />
        <parameter name="platform" value="MAC" />
        <parameter name="testdataxml" value="src/main/resources/testdata.xml" />
        <groups>
            <run>
                <include name="smoke" />
            </run>
        </groups>
        <classes>
            <class name="com.pega.uiframework.uitest.TestGuest" />
        </classes>
    </test>
    <test name="Client Tests">
        <parameter name="browser" value="chrome" />
        <parameter name="version" value="57.0" />
        <parameter name="platform" value="MAC" />
        <groups>
            <run>
                <include name="smoke" />
            </run>
        </groups>
        <classes>
            <class name="com.pega.uiframework.uitest.TestClient" />
        </classes>
    </test>
    <test name="XLS Tests">
        <parameter name="browser" value="chrome" />
        <parameter name="version" value="57.0" />
        <parameter name="platform" value="MAC" />
        <groups>
            <run>
                <include name="regression" />
            </run>
        </groups>
        <classes>
            <class name="com.pega.uiframework.uitest.TestXLS" />
        </classes>
    </test>
</suite>
```
At this point, chrome browser will be launched and test will be executed. Reports will be generated inside test-output folder. Currently supported browsers are `firefox`, `chrome` and `ie`.

# Additional Information

## Creating browser profile

We can set different profile parameters for different browsers using browser profiles. Currently supported browser profiles are ChromeBrowserProfile, FirefoxBrowserProfile and IEBrowserProfile. To use a profile, use it like this:

```
FirefoxBrowserProfile ffProfile = new FirefoxBrowserProfile();
	
ffProfile.setAcceptUntrustedCertificates(true).showDownloadManagerWhenStarting(false)
                .setDownloadDirectory("/Users/kmekala/Downloads");
        
DesiredCapabilities caps = new WebCapabilitiesBuilder().addBrowser(browser)
                .addBrowserDriverExecutablePath(config.getProperty("gecko.driver.path")).addVersion(version)
                .addPlatform(platform).addBrowserProfile(ffProfile).build();

driver = new WebDriverFactory().createDriver(caps);

```
## Using proxy

To access the websites using proxy, use it like:

```
DesiredCapabilities caps = new WebCapabilitiesBuilder().addBrowser(browser).addProxy("192.168.3.60", 5678).
                .addBrowserDriverExecutablePath(config.getProperty("gecko.driver.path")).addVersion(version)
                .addPlatform(platform).addBrowserProfile(ffProfile).build();
```

## Running tests using selenium GRID

Assuming that selenium Grid is configued on localhost and is running on port 4444, you can run your tests on grid using `GridUrlBuilder`

```
URL remoteHubUrl = new SeleniumGridUrlBuilder().addProtocol(Protocol.HTTP).addSeleniumHubHost("127.0.0.1")
                .addSeleniumHubPort(4444).build();

driver = new WebDriverFactory().createDriver(remoteHubUrl, caps);
```

## Supported element actions

To check the supported element actions/supported selenium commands see [BasePage.java](src/main/java/com/pega/uiframework/core/BasePage.java) 

## Failure screenshot

In case of test failure, the framework will generate the failure screenshot and will copy the file to target folder

## Logging

To capture logs, initialize the logger in `@BeforeClass` like this:

```
LoggerUtils.startTestLogging(this.getClass().getSimpleName());
```

This will create a new file in logs folder with the name of test class being executed, and will capture all logs in that file. Ideally it's a good idea to capture the test script logs in separate log files, instead of putting all logs inside a huge log file. It helps in debugging issues faster.

To stop logging, in `@AfterClass` add a line like this:

```
LoggerUtils.stopTestLogging();
```

## Reporting (Alpha)

You can create custom reports by using the [ReporterAPI](src/main/java/com/pega/uiframework/core/ReporterAPI.java). Use the api like this:

```
public class HTMLReporter extends TestListenerAdapter implements IReporter
{
    ReporterAPI api;
    PrintWriter pw;

    @Override
    public void generateReport(List<XmlSuite> arg0, List<ISuite> arg1, String arg2)
    {
        try
        {
            api = new ReporterAPI(arg0, arg1, arg2);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            pw = new PrintWriter(new File("result.html"));
        } catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        pw.write("<html><head>Report</head><body>");
        pw.write(api.getParallelRunParameter() + "<br/>" + api.getSuccessPercentageForSuite());
        pw.write(api.getSuiteName() + "<br/>" + api.getTotalFailedMethodsForSuite());
        
        List<String> testNames = api.getAllTestNames();
        for(String test:testNames){
            pw.write("Test Name: " + test + "<br/>");
            ITestNGMethod[] methods = api.getAllMethodsForTest(test);
            for(ITestNGMethod m: methods){
                pw.println(m.getMethodName());
                pw.println(api.getFailedTestScreenshotPath(test, m.getMethodName()));
                pw.println(api.getTestFailureStackTrace(test, m.getMethodName()));
            }
        }
        
        pw.write("</body></html>");
        pw.flush();
        pw.close();
    }
}
```

To generate the report, add the listener to testng listener section

```
<listeners>
	<listener class-name="com.pega.uiframework.HTMLReporter" />
</listeners>
```

#Tips and Tricks
```
 https://saucelabs.com/resources/articles/selenium-tips-css-selectors
```

