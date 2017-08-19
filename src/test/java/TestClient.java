import com.pega.uiframwork.uitest.BaseTest;
import com.pega.uiframwork.uitest.pages.LoginPage;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by mekak2 on 4/20/17.
 */
public class TestClient extends BaseTest{

    @DataProvider(name = "client")
    // The number of times data is repeated, test will be executed the same no. of times
    // Here it will execute two times
    public static Object[][] client() {
        return new Object[][]{
                {"Testautomation-client@cisco.com","DietC0ke"}

        };
    }

    @Test(groups = { "Regression","ssologin" }, dataProvider = "client")
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

}
