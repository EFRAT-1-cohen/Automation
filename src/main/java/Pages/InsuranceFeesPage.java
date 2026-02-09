package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class InsuranceFeesPage extends BtlBasePage {

    public InsuranceFeesPage(WebDriver driver) {
        super(driver);
    }

    public boolean isPageTitleCorrect() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement title = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h1")));

            String titleText = title.getText();
            System.out.println("כותרת הדף: " + titleText);

            return titleText.contains("דמי ביטוח") || titleText.contains("דמי ביטוח לאומי");

        } catch (Exception e) {
            System.out.println("שגיאה בקריאת כותרת הדף: " + e.getMessage());
            return false;
        }
    }

    public InsuranceCalculatorPage clickOnCalculatorLink() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement calculatorLink = null;

        try {
            System.out.println("מנסה למצוא קישור לפי href...");
            calculatorLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("a[href*='BituahCalc']")
            ));
            System.out.println("נמצא קישור לפי href");
        } catch (Exception e1) {
            try {
                System.out.println("מנסה למצוא קישור לפי טקסט...");
                calculatorLink = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//a[contains(text(), 'מחשבון') and contains(text(), 'חישוב')]")
                ));
                System.out.println("נמצא קישור לפי טקסט");
            } catch (Exception e2) {
                System.out.println("מנסה למצוא קישור לפי טקסט מדויק...");
                calculatorLink = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//a[contains(text(), 'מחשבון לחישוב דמי ביטוח')]")
                ));
                System.out.println("נמצא קישור לפי טקסט מדויק");
            }
        }

        calculatorLink.click();
        System.out.println("לחצתי על קישור המחשבון");
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("Simulators"),
                    ExpectedConditions.urlContains("BituahCalc"),
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[id$='_BirthDate_Date']"))
            ));
            System.out.println("דף המחשבון נטען בהצלחה");
            System.out.println("URL נוכחי: " + driver.getCurrentUrl());
        } catch (Exception e) {
            System.out.println("שגיאה בטעינת דף המחשבון: " + e.getMessage());
        }

        return new InsuranceCalculatorPage(driver);
    }
}