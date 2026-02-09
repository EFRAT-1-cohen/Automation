package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class UnemploymentPage extends BtlBasePage {

    public UnemploymentPage(WebDriver driver) {
        super(driver);
    }

    public UnemploymentCalculatorPage goToCalculator() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            System.out.println("מחפש קישור 'למחשבוני דמי אבטלה'...");

            WebElement calculatorLink = null;

            try {
                calculatorLink = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("a[href*='AvtCalcIndex']")
                ));
                System.out.println("✓ נמצא קישור לפי href: AvtCalcIndex");
            } catch (Exception e1) {
                try {
                    calculatorLink = wait.until(ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//a[.//strong[contains(text(), 'למחשבוני דמי אבטלה')]]")
                    ));
                    System.out.println("✓ נמצא קישור לפי טקסט 'למחשבוני דמי אבטלה'");
                } catch (Exception e2) {
                    try {
                        WebElement strongElement = wait.until(ExpectedConditions.presenceOfElementLocated(
                                By.xpath("//strong[contains(text(), 'למחשבוני דמי אבטלה')]")
                        ));
                        calculatorLink = strongElement.findElement(By.xpath("./ancestor::a"));
                        System.out.println("✓ נמצא קישור דרך אלמנט strong");
                    } catch (Exception e3) {
                        System.out.println("✗ לא נמצא קישור למחשבונים!");
                        throw new RuntimeException("לא ניתן למצוא קישור למחשבוני דמי אבטלה");
                    }
                }
            }

            System.out.println("גולל לאלמנט...");
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                    calculatorLink
            );

            Thread.sleep(1000);

            try {
                wait.until(ExpectedConditions.elementToBeClickable(calculatorLink));
                calculatorLink.click();
                System.out.println("✓ לחיצה רגילה הצליחה");
            } catch (Exception e) {
                System.out.println("לחיצה רגילה נכשלה, משתמש ב-JavaScript...");
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", calculatorLink);
                System.out.println("✓ לחיצת JavaScript הצליחה");
            }

            Thread.sleep(2000);

            System.out.println("URL אחרי לחיצה: " + driver.getCurrentUrl());

            System.out.println("מחפש קישור ספציפי לחישוב דמי אבטלה...");

            WebElement specificCalcLink = null;

            try {
                specificCalcLink = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//a[contains(@href, 'Avtalah') or contains(text(), 'חישוב') or contains(text(), 'סכום')]")
                ));
                System.out.println("✓ נמצא קישור ספציפי");

                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                        specificCalcLink
                );
                Thread.sleep(500);

                try {
                    specificCalcLink.click();
                } catch (Exception e) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", specificCalcLink);
                }

                System.out.println("✓ לחצתי על הקישור הספציפי");
                Thread.sleep(2000);

            } catch (Exception e) {
                System.out.println("⚠ לא נמצא קישור ספציפי נוסף - כנראה כבר במחשבון");
            }

            System.out.println("URL סופי: " + driver.getCurrentUrl());

            return new UnemploymentCalculatorPage(driver);

        } catch (Exception e) {
            System.out.println("שגיאה ב-goToCalculator: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("כשל בניווט למחשבון דמי אבטלה", e);
        }
    }
}