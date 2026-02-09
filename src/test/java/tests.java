import Pages.Branches;
import Pages.BtlBasePage;
import Pages.Menu;
import Pages.InsuranceCalculatorPage;
import Pages.InsuranceFeesPage;
import Pages.UnemploymentPage;
import Pages.UnemploymentCalculatorPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class tests extends btlBasePageTest {

    @Test
    public void searchTest() {
        System.out.println("מתחיל טסט חיפוש...");
        BtlBasePage page = new BtlBasePage(driver);
        page.Search("חישוב סכום דמי לידה ליום");

        assertTrue(driver.getCurrentUrl().contains("search") ||
                        driver.getPageSource().contains("תוצאות חיפוש"),
                "לא הגענו לדף תוצאות חיפוש");

        System.out.println("טסט חיפוש הושלם בהצלחה");
    }

    @Test
    public void openBranches() throws InterruptedException {
        System.out.println("מתחיל טסט פתיחת סניפים...");

        BtlBasePage page = new BtlBasePage(driver);
        Branches b = page.openBranches();
        b.openBranch();

        System.out.println("טסט סניפים הושלם בהצלחה");
    }

    @Test
    public void yeshivaStudentInsuranceTest() {
        System.out.println("=== מתחיל טסט חישוב דמי ביטוח לתלמיד ישיבה ===");

        BtlBasePage page = new BtlBasePage(driver);

        System.out.println("שלב 1: ניווט לדמי ביטוח לאומי");
        page.clickOnMenu(Menu.paymentBituch, "דמי ביטוח לאומי");

        InsuranceFeesPage feesPage = new InsuranceFeesPage(driver);

        System.out.println("שלב 2: בדיקת כותרת הדף");
        assertTrue(feesPage.isPageTitleCorrect(),
                "הכותרת 'דמי ביטוח לאומי' לא נמצאה");

        System.out.println("שלב 3: כניסה למחשבון");
        InsuranceCalculatorPage calculator = feesPage.clickOnCalculatorLink();

        System.out.println("שלב 4: בדיקת URL המחשבון");
        assertTrue(driver.getCurrentUrl().contains("Simulators") ||
                        driver.getCurrentUrl().contains("BituahCalc"),
                "לא עברנו לדף הסימולטורים");

        System.out.println("שלב 5: מילוי צעד 1 - תלמיד ישיבה");
        calculator.stepOneYeshivaStudent();
        calculator.clickNext();

        System.out.println("שלב 6: בדיקה שהגענו לצעד 2");
        assertTrue(calculator.isStepTwoDisplayed(),
                "לא הגענו לצעד 2 (שאלת נכות)");

        System.out.println("שלב 7: בחירה - לא מקבל קצבת נכות");
        calculator.stepTwoSelectDisability(false);
        calculator.clickNext();

        System.out.println("שלב 8: קבלת ובדיקת תוצאות");
        String results = calculator.getCalculationResults();
        System.out.println("תוצאות החישוב שנתקבלו:\n" + results);

        System.out.println("שלב 9: וידוי סכומים");
        assertTrue(results.contains("48"),
                "דמי ביטוח לאומי שגויים (צפוי: 48 ש\"ח)");
        assertTrue(results.contains("123"),
                "דמי ביטוח בריאות שגויים (צפוי: 123 ש\"ח)");
        assertTrue(results.contains("171"),
                "סך הכל שגוי (צפוי: 171 ש\"ח)");

        System.out.println("=== טסט הושלם בהצלחה! ===");
    }

    @Test
    public void unemploymentCalculationTest() {
        System.out.println("=== מתחיל טסט חישוב דמי אבטלה ===");

        BtlBasePage page = new BtlBasePage(driver);

        System.out.println("שלב 1: ניווט לעמוד אבטלה");
        page.clickOnMenu(Menu.kitsAndHat, "אבטלה");

        UnemploymentPage unemploymentPage = new UnemploymentPage(driver);
        UnemploymentCalculatorPage calculator = unemploymentPage.goToCalculator();

        System.out.println("שלב 2: מילוי פרטים ראשוניים");
        calculator.fillInitialDetails();
        calculator.clickNext();

        System.out.println("שלב 3: מילוי סכומי השתכרות");
        calculator.fillEarnings("10000");
        calculator.clickNext();

        System.out.println("שלב 4: בדיקת דף תוצאות");
        assertTrue(calculator.isResultsPageDisplayed(),
                "דף תוצאות לא הוצג");

        System.out.println("שלב 5: בדיקת תוכן התוצאות");
        String results = calculator.getResultsText();

        assertTrue(results.contains("שכר יומי ממוצע לצורך חישוב דמי אבטלה"),
                "חסר: שכר יומי ממוצע");
        assertTrue(results.contains("דמי אבטלה ליום"),
                "חסר: דמי אבטלה ליום");
        assertTrue(results.contains("דמי אבטלה לחודש"),
                "חסר: דמי אבטלה לחודש");

        System.out.println("=== טסט הושלם בהצלחה! ===");
    }


    @ParameterizedTest
    @ValueSource(strings = {"אבטלה", "קצבת ילדים", "אזרח ותיק", "נכות כללית", "סיעוד"})
    public void checkBreadcrumbsParameterized(String subMenu) {
        System.out.println("=== בדיקת breadcrumbs עבור: " + subMenu + " ===");
        BtlBasePage page = new BtlBasePage(driver);
        try {
            page.clickOnMenu(Menu.kitsAndHat, subMenu);
            Thread.sleep(1500);
            String breadcrumbText = findBreadcrumbText();
            System.out.println("Breadcrumb שנמצא: " + breadcrumbText);
            assertTrue(breadcrumbText.contains(subMenu) || breadcrumbText.contains("קצבאות והטבות"),
                    "ה-breadcrumb לא מכיל את '" + subMenu + "' או 'קצבאות והטבות'");
            System.out.println("=== Breadcrumb תקין! ===");
        } catch (Exception e) {
            System.err.println("✗ שגיאה בבדיקת breadcrumb עבור " + subMenu + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("הבדיקה נכשלה עבור " + subMenu, e);
        }
    }

    private String findBreadcrumbText() {
        String[] selectors = {
                ".breadcrumb",
                "nav.breadcrumb",
                "[class*='breadcrumb']",
                "ol.breadcrumb",
                ".breadcrumbs",
                "#breadcrumb"
        };
        for (String selector : selectors) {
            try {
                org.openqa.selenium.WebElement breadcrumb = driver.findElement(By.cssSelector(selector));
                if (breadcrumb.isDisplayed()) {
                    System.out.println("✓ נמצא breadcrumb עם selector: " + selector);
                    return breadcrumb.getText();
                }
            } catch (Exception e) {
            }
        }
        String[] xpaths = {
                "//nav[@aria-label='breadcrumb']",
                "//*[contains(@class, 'breadcrumb')]",
                "//ol[@class='breadcrumb']",
                "//*[contains(text(), 'קצבאות והטבות')]"
        };
        for (String xpath : xpaths) {
            try {
                org.openqa.selenium.WebElement breadcrumb = driver.findElement(By.xpath(xpath));
                if (breadcrumb.isDisplayed()) {
                    System.out.println("✓ נמצא breadcrumb עם XPath: " + xpath);
                    return breadcrumb.getText();
                }
            } catch (Exception e) {
            }
        }
        System.out.println("⚠ לא נמצא breadcrumb אמיתי, משתמש ב-URL כחלופה");
        return driver.getCurrentUrl();
    }
}