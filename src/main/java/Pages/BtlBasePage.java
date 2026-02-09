package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class BtlBasePage extends BasePage {

    public BtlBasePage(WebDriver driver) {
        super(driver);
    }


    public void clickOnMenu(Menu m, String subM) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            System.out.println("מחפש תפריט ראשי: " + m.getMainMenuItem());

            WebElement mainMenu = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//li[@class='insideItem']//a[contains(text(),'" + m.getMainMenuItem() + "') and not(contains(@class,'mob'))]")
            ));

            System.out.println("נמצא תפריט ראשי, לוחץ...");
            mainMenu.click();

            Thread.sleep(700);

            System.out.println("מחפש תפריט משני: " + subM);

            WebElement subMenu = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//li//a[normalize-space(text())='" + subM + "']")
            ));

            System.out.println("נמצא תפריט משני, לוחץ...");
            subMenu.click();

            Thread.sleep(1000);

            System.out.println("ניווט הושלם בהצלחה ל: " + subM);

        } catch (Exception e) {
            System.out.println("שגיאה בניווט לתפריט: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("כשל בניווט לתפריט: " + m.getMainMenuItem() + " -> " + subM);
        }
    }

    public void Search(String str) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            System.out.println("מחפש: " + str);

            WebElement searchBox = wait.until(
                    ExpectedConditions.elementToBeClickable(By.cssSelector("#TopQuestions"))
            );

            searchBox.clear();
            searchBox.sendKeys(str);
            System.out.println("הוזן טקסט לשדה החיפוש");

            WebElement searchBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(By.cssSelector("#ctl00_SiteHeader_reserve_btnSearch"))
            );

            searchBtn.click();
            System.out.println("לחצתי על כפתור החיפוש");

            Thread.sleep(1500);

        } catch (Exception e) {
            System.out.println("שגיאה בחיפוש: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Branches openBranches() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            System.out.println("מחפש כפתור סניפים: " + Menu.branches.getMainMenuItem());

            WebElement branchesBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@class='top-menu-left']//a[contains(text(),'" + Menu.branches.getMainMenuItem() + "')]")
            ));

            branchesBtn.click();
            System.out.println("לחצתי על כפתור סניפים");

            Thread.sleep(1500);

            return new Branches(driver);

        } catch (Exception e) {
            System.out.println("שגיאה בפתיחת עמוד סניפים: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("כשל בפתיחת עמוד סניפים");
        }
    }


    public boolean verifyBreadcrumbs(String expectedText) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement breadcrumb = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.className("breadcrumb"))
            );
            String breadcrumbText = breadcrumb.getText();
            System.out.println("Breadcrumb: " + breadcrumbText);
            return breadcrumbText.contains(expectedText);
        } catch (Exception e) {
            System.out.println("לא נמצא breadcrumb: " + e.getMessage());
            return false;
        }
    }
}