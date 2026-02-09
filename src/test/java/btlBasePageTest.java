import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class btlBasePageTest {
    public WebDriver driver;
    protected static ExtentReports extent;
    protected static ExtentTest test;
    protected static ExtentSparkReporter reporter;

    @BeforeAll
    public static void startTests(){
        System.out.println("התחלת טסטים");
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        reporter = new ExtentSparkReporter("./target/ExtentReport/ExtentReport_" + timestamp + ".html");
        extent = new ExtentReports();
        extent.attachReporter(reporter);
    }

    @AfterAll
    public static void endTests(){
        System.out.println("סיום טסטים");
        if (extent != null) {
            extent.flush();
        }
    }

    @BeforeEach
    public void loadDriver(){
        String webType = System.getProperty("webType","chrome");
        if(webType.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else {
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
        }
        driver.manage().window().maximize();
        driver.get("https://www.btl.gov.il/Pages/default.aspx");
    }

    @AfterEach
    public void closeDriver(){
        if (driver != null) {
            driver.quit();
        }
    }

    public String getScreenshot(String testName) {
        String dateName = new java.text.SimpleDateFormat("yyyyMMddhhmmss").format(new java.util.Date());
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);

        String destination = System.getProperty("user.dir") + "/target/ExtentReport/screenshots/" + testName + "_" + dateName + ".png";
        File finalDestination = new File(destination);

        try {
            FileUtils.copyFile(source, finalDestination);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return destination;
    }
}