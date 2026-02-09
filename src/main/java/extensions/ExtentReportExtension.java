package extensions;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import report.ExtentReportManager;
import report.ExtentTestManager;
import org.junit.jupiter.api.extension.*;
import java.lang.reflect.Method;

public class ExtentReportExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback, AfterAllCallback {
    private static ExtentTest extentTest;
    private String testName;

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        testName = context.getDisplayName().replaceAll("[^a-zA-Z0-9\u0590-\u05FF]", "_");
        extentTest = ExtentReportManager.getExtent().createTest(testName);
        ExtentTestManager.setTest(extentTest);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        if (context.getExecutionException().isPresent()) {
            Throwable exception = context.getExecutionException().get();

            try {
                Object testInstance = context.getRequiredTestInstance();

                Method getScreenshotMethod = testInstance.getClass().getMethod("getScreenshot", String.class);

                String screenshotPath = (String) getScreenshotMethod.invoke(testInstance, context.getDisplayName());

                extentTest.fail(exception.getMessage(),
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());

            } catch (Exception e) {
                extentTest.fail(exception.getMessage());
                extentTest.warning("Could not take screenshot: " + e.getMessage());
            }
        } else {
            extentTest.pass("Test passed successfully");
        }
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        ExtentReportManager.flushReport();
    }
}