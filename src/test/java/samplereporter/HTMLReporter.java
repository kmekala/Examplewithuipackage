package samplereporter;

import com.pega.uiframework.core.ReporterAPI;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ITestNGMethod;
import org.testng.TestListenerAdapter;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by mekak2 on 4/21/17.
 */
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
            pw = new PrintWriter(new File(System.getProperty("user.dir")+"/reports/result.html"));
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
               // pw.println(api.getTestFailureStackTrace(test, m.getMethodName()));
            }
        }

        pw.write("</body></html>");
        pw.flush();
        pw.close();
    }
}
