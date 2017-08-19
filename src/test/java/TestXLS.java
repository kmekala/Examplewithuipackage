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

    @Test(groups = {"Regression","ssologin"}, dataProvider = "getData")
    public void testClientXLS(Hashtable<String, String> data) throws Exception {
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
