package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class Branches extends BtlBasePage {

    public Branches(WebDriver driver) {
        super(driver);
    }

    public void openBranch() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            System.out.println("מחפש סניף לפתיחה...");
            WebElement branchName = wait.until(
                    ExpectedConditions.elementToBeClickable(By.className("SnifName"))
            );

            String selectedBranch = branchName.getText();
            System.out.println("לוחץ על סניף: " + selectedBranch);
            branchName.click();

            Thread.sleep(2000);

            System.out.println("בודק שמופיעים כל השדות הנדרשים...");

            boolean hasAddress = false;
            boolean hasReception = false;
            boolean hasPhone = false;

            try {
                WebElement addressElement = wait.until(
                        ExpectedConditions.presenceOfElementLocated(
                                By.xpath("//label[contains(text(),'כתובת')]")
                        )
                );
                hasAddress = addressElement != null && addressElement.isDisplayed();
                System.out.println("✓ כתובת נמצאה");
            } catch (Exception e) {
                System.out.println("✗ כתובת לא נמצאה");
            }

            try {
                WebElement receptionElement = wait.until(
                        ExpectedConditions.presenceOfElementLocated(
                                By.xpath("//label[contains(text(),'קבלת קהל')]")
                        )
                );
                hasReception = receptionElement != null && receptionElement.isDisplayed();
                System.out.println("✓ קבלת קהל נמצאה");
            } catch (Exception e) {
                System.out.println("✗ קבלת קהל לא נמצאה");
            }

            try {
                WebElement phoneElement = wait.until(
                        ExpectedConditions.presenceOfElementLocated(
                                By.xpath("//label[contains(text(),'מענה טלפוני')]")
                        )
                );
                hasPhone = phoneElement != null && phoneElement.isDisplayed();
                System.out.println("✓ מענה טלפוני נמצא");
            } catch (Exception e) {
                System.out.println("✗ מענה טלפוני לא נמצא");
            }

            if (hasAddress && hasReception && hasPhone) {
                System.out.println("OK - כל השדות נמצאו!");
            } else {
                System.out.println("FAILED - חסרים שדות:");
                if (!hasAddress) System.out.println("  - כתובת");
                if (!hasReception) System.out.println("  - קבלת קהל");
                if (!hasPhone) System.out.println("  - מענה טלפוני");
            }

        } catch (Exception e) {
            System.out.println("שגיאה בפתיחת סניף: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }


    public boolean isBranchesPageLoaded() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("SnifName")));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}