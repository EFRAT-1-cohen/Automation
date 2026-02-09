package report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExtentReportManager {
    private static ExtentReports extent;

    public static ExtentReports getExtent() {
        if (extent == null) {
            initReport();
        }
        return extent;
    }

    private static void initReport() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String reportPath = "./target/ExtentReport/ExtentReport_" + timestamp + ".html";

        File reportDir = new File("./target/ExtentReport");
        if (!reportDir.exists()) {
            reportDir.mkdirs();
        }

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setReportName("BTL Automation Results"); // שם הדו"ח
        spark.config().setDocumentTitle("Test Results"); // כותרת הדף

        extent = new ExtentReports();
        extent.attachReporter(spark);

        extent.setSystemInfo("Tester", "QA Automation");
        extent.setSystemInfo("Environment", "Production");
    }

    public static void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }
}