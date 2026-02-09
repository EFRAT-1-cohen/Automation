import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class btlBasePageTest {
    WebDriver driver;


//    protected static ExtentReports extent;
//    protected static ExtentTest test;
//    protected static ExtentSparkReporter reporter;
        @BeforeAll
        public static void startTests(){
          System.out.println("התחלת טסטים");
//        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
//        reporter = new ExtentSparkReporter("./target/test-output/ExtentReport_"+timestamp+".html");
//        extent = new ExtentReports();
//        extent.attachReporter(reporter);
        }

        @AfterAll
        public static void endTests(){
            System.out.println("סיום טסטים");
//        extent.flush();
        }

        @BeforeEach
        public void loadDriver(){
//        test = extent.createTest("Example", "דוגמה לשימוש ב Extent Report");
//        test.info("start load driver");
            String webType = System.getProperty("webType","chrome");
            if(webType.equalsIgnoreCase("chrome")) {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
            }else {
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
            }
            driver.manage().window().maximize();
            driver.get("https://www.btl.gov.il/Pages/default.aspx");
        }


    @AfterEach
        public void closeDriver(){
            driver.quit();
        }
    }




