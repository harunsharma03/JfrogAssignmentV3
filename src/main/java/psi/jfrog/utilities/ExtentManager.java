package psi.jfrog.utilities;

import java.io.IOException;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    private static ExtentReports extent;
    private static ExtentTest test;

    public static ExtentReports getExtent() throws IOException {
        if (extent == null) {
            ExtentSparkReporter reporter = new ExtentSparkReporter("target/extent-report.html");
            reporter.loadXMLConfig("src/test/resources/extent-config.xml");

            extent = new ExtentReports();
            extent.attachReporter(reporter);
        }
        return extent;
    }

    public static ExtentTest getTest() {
        return test;
    }

    public static void setTest(ExtentTest testObj) {
        test = testObj;
    }

    public static void flush() {
        if (extent != null) {
            extent.flush();
        }
    }
}