package report;

import com.aventstack.extentreports.ExtentTest;

public class ExtentTestManager {
    private static ExtentTest test;

    public static void setTest(ExtentTest extentTest) {
        test = extentTest;
    }

    public static ExtentTest getTest() {
        return test;
    }
}