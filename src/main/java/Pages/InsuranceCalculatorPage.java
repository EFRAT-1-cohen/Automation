package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class InsuranceCalculatorPage extends BtlBasePage {

    public InsuranceCalculatorPage(WebDriver driver) {
        super(driver);
    }

    public void stepOneYeshivaStudent() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));


        WebElement yeshivaBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//label[normalize-space(text())='תלמיד ישיבה']")
        ));
        yeshivaBtn.click();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            WebDriverWait genderWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            WebElement genderMale = genderWait.until(
                    ExpectedConditions.elementToBeClickable(By.cssSelector("input[id$='_rdb_Gender_0']"))
            );
            genderMale.click();
            System.out.println("מגדר נבחר בהצלחה");
        } catch (Exception e) {
            System.out.println("שדה מגדר לא נדרש או לא זמין");
        }

        int randomAge = ThreadLocalRandom.current().nextInt(18, 71);
        String randomBirthDate = LocalDate.now().minusYears(randomAge).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        System.out.println("Generated Date: " + randomBirthDate);

        WebElement dateInput = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("input[id$='_BirthDate_Date']"))
        );

        dateInput.click();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        dateInput.clear();

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        dateInput.sendKeys(randomBirthDate);

        // סגירת date picker
        dateInput.sendKeys(Keys.ENTER);

        System.out.println("תאריך לידה הוזן בהצלחה: " + randomBirthDate);
    }

    public void clickNext() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement nextBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("input[id$='_StepNextButton']")
        ));

        nextBtn.click();
        System.out.println("כפתור 'המשך' נלחץ");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isStepTwoDisplayed() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("input[id$='_rdb_GetNechut_0'], input[id$='_rdb_GetNechut_1']")
            ));

            boolean displayed = element.isDisplayed();
            System.out.println("צעד 2 מוצג: " + displayed);
            return displayed;

        } catch (Exception e) {
            System.out.println("צעד 2 לא נמצא: " + e.getMessage());
            return false;
        }
    }

    public void stepTwoSelectDisability(boolean hasDisability) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        String index = hasDisability ? "0" : "1";

        WebElement radioBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("input[id$='_rdb_GetNechut_" + index + "']")
        ));

        radioBtn.click();
        System.out.println("נבחר: " + (hasDisability ? "מקבל קצבת נכות" : "לא מקבל קצבת נכות"));
    }

    public String getCalculationResults() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement resultsContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div[id$='_div_Result']")
        ));

        String results = resultsContainer.getText();
        System.out.println("תוצאות החישוב:\n" + results);

        return results;
    }


    public boolean isResultsPageDisplayed() {
        try {
            return driver.getCurrentUrl().contains("Result") ||
                    driver.findElement(By.cssSelector("div[id$='_div_Result']")).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}