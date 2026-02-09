import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

import java.io.File;
import java.io.IOException;

public class btlBasePageTest {

    public WebDriver driver;

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