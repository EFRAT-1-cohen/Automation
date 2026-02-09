import Pages.Branches;
import Pages.BtlBasePage;
import Pages.Menu;
import Pages.InsuranceCalculatorPage;
import Pages.InsuranceFeesPage;
import Pages.UnemploymentPage;
import Pages.UnemploymentCalculatorPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import com.aventstack.extentreports.ExtentTest;
import extensions.ExtentReportExtension;
import report.ExtentTestManager;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ExtentReportExtension.class)
public class tests extends btlBasePageTest {

    @Test
    @DisplayName("בדיקת חיפוש באתר ביטוח לאומי")
    public void searchTest() {
        ExtentTest test = ExtentTestManager.getTest();

        test.info("מתחיל טסט חיפוש");
        System.out.println("מתחיל טסט חיפוש...");

        BtlBasePage page = new BtlBasePage(driver);
        test.info("מבצע חיפוש עבור: 'חישוב סכום דמי לידה ליום'");
        page.Search("חישוב סכום דמי לידה ליום");

        test.info("בודק שהגענו לדף תוצאות חיפוש");
        assertTrue(driver.getCurrentUrl().contains("search") ||
                        driver.getPageSource().contains("תוצאות חיפוש"),
                "לא הגענו לדף תוצאות חיפוש");

        test.pass("טסט חיפוש הושלם בהצלחה ✓");
        System.out.println("טסט חיפוש הושלם בהצלחה");
    }

    @Test
    @DisplayName("פתיחת דף סניפים")
    public void openBranches() throws InterruptedException {
        ExtentTest test = ExtentTestManager.getTest();

        test.info("מתחיל טסט פתיחת סניפים");
        System.out.println("מתחיל טסט פתיחת סניפים...");

        BtlBasePage page = new BtlBasePage(driver);
        test.info("לוחץ על קישור סניפים");
        Branches b = page.openBranches();

        test.info("פותח סניף ספציפי");
        b.openBranch();

        test.pass("טסט סניפים הושלם בהצלחה ✓");
        System.out.println("טסט סניפים הושלם בהצלחה");
    }

    @Test
    @DisplayName("חישוב דמי ביטוח לתלמיד ישיבה")
    public void yeshivaStudentInsuranceTest() {
        ExtentTest test = ExtentTestManager.getTest();

        test.info("=== מתחיל טסט חישוב דמי ביטוח לתלמיד ישיבה ===");
        System.out.println("=== מתחיל טסט חישוב דמי ביטוח לתלמיד ישיבה ===");

        BtlBasePage page = new BtlBasePage(driver);

        test.info("שלב 1: ניווט לדמי ביטוח לאומי");
        System.out.println("שלב 1: ניווט לדמי ביטוח לאומי");
        page.clickOnMenu(Menu.paymentBituch, "דמי ביטוח לאומי");

        InsuranceFeesPage feesPage = new InsuranceFeesPage(driver);

        test.info("שלב 2: בדיקת כותרת הדף");
        System.out.println("שלב 2: בדיקת כותרת הדף");
        assertTrue(feesPage.isPageTitleCorrect(),
                "הכותרת 'דמי ביטוח לאומי' לא נמצאה");
        test.pass("כותרת הדף תקינה ✓");

        test.info("שלב 3: כניסה למחשבון");
        System.out.println("שלב 3: כניסה למחשבון");
        InsuranceCalculatorPage calculator = feesPage.clickOnCalculatorLink();

        test.info("שלב 4: בדיקת URL המחשבון");
        System.out.println("שלב 4: בדיקת URL המחשבון");
        assertTrue(driver.getCurrentUrl().contains("Simulators") ||
                        driver.getCurrentUrl().contains("BituahCalc"),
                "לא עברנו לדף הסימולטורים");
        test.pass("URL המחשבון תקין ✓");

        test.info("שלב 5: מילוי צעד 1 - תלמיד ישיבה");
        System.out.println("שלב 5: מילוי צעד 1 - תלמיד ישיבה");
        calculator.stepOneYeshivaStudent();
        calculator.clickNext();

        test.info("שלב 6: בדיקה שהגענו לצעד 2");
        System.out.println("שלב 6: בדיקה שהגענו לצעד 2");
        assertTrue(calculator.isStepTwoDisplayed(),
                "לא הגענו לצעד 2 (שאלת נכות)");
        test.pass("הגענו לצעד 2 בהצלחה ✓");

        test.info("שלב 7: בחירה - לא מקבל קצבת נכות");
        System.out.println("שלב 7: בחירה - לא מקבל קצבת נכות");
        calculator.stepTwoSelectDisability(false);
        calculator.clickNext();

        test.info("שלב 8: קבלת ובדיקת תוצאות");
        System.out.println("שלב 8: קבלת ובדיקת תוצאות");
        String results = calculator.getCalculationResults();
        test.info("תוצאות החישוב שנתקבלו: " + results);
        System.out.println("תוצאות החישוב שנתקבלו:\n" + results);

        test.info("שלב 9: וידוי סכומים");
        System.out.println("שלב 9: וידוי סכומים");

        assertTrue(results.contains("43"),
                "דמי ביטוח לאומי שגויים (צפוי: 43 ש\"ח)");
        test.pass("דמי ביטוח לאומי: 43 ש\"ח ✓");

        assertTrue(results.contains("120"),
                "דמי ביטוח בריאות שגויים (צפוי: 120 ש\"ח)");
        test.pass("דמי ביטוח בריאות: 120 ש\"ח ✓");

        assertTrue(results.contains("163"),
                "סך הכל שגוי (צפוי: 163 ש\"ח)");
        test.pass("סך הכל: 163 ש\"ח ✓");

        test.pass("=== טסט הושלם בהצלחה! ===");
        System.out.println("=== טסט הושלם בהצלחה! ===");
    }

    @Test
    @DisplayName("חישוב דמי אבטלה")
    public void unemploymentCalculationTest() {
        ExtentTest test = ExtentTestManager.getTest();

        test.info("=== מתחיל טסט חישוב דמי אבטלה ===");
        System.out.println("=== מתחיל טסט חישוב דמי אבטלה ===");

        BtlBasePage page = new BtlBasePage(driver);

        test.info("שלב 1: ניווט לעמוד אבטלה");
        System.out.println("שלב 1: ניווט לעמוד אבטלה");
        page.clickOnMenu(Menu.kitsAndHat, "אבטלה");

        UnemploymentPage unemploymentPage = new UnemploymentPage(driver);
        test.info("פותח את מחשבון האבטלה");
        UnemploymentCalculatorPage calculator = unemploymentPage.goToCalculator();

        test.info("שלב 2: מילוי פרטים ראשוניים");
        System.out.println("שלב 2: מילוי פרטים ראשוניים");
        calculator.fillInitialDetails();
        calculator.clickNext();

        test.info("שלב 3: מילוי סכומי השתכרות - 10,000 ש\"ח");
        System.out.println("שלב 3: מילוי סכומי השתכרות");
        calculator.fillEarnings("10000");
        calculator.clickNext();

        test.info("שלב 4: בדיקת דף תוצאות");
        System.out.println("שלב 4: בדיקת דף תוצאות");
        assertTrue(calculator.isResultsPageDisplayed(),
                "דף תוצאות לא הוצג");
        test.pass("דף תוצאות הוצג בהצלחה ✓");

        test.info("שלב 5: בדיקת תוכן התוצאות");
        System.out.println("שלב 5: בדיקת תוכן התוצאות");
        String results = calculator.getResultsText();

        assertTrue(results.contains("שכר יומי ממוצע לצורך חישוב דמי אבטלה"),
                "חסר: שכר יומי ממוצע");
        test.pass("נמצא: שכר יומי ממוצע ✓");

        assertTrue(results.contains("דמי אבטלה ליום"),
                "חסר: דמי אבטלה ליום");
        test.pass("נמצא: דמי אבטלה ליום ✓");

        assertTrue(results.contains("דמי אבטלה לחודש"),
                "חסר: דמי אבטלה לחודש");
        test.pass("נמצא: דמי אבטלה לחודש ✓");

        test.pass("=== טסט הושלם בהצלחה! ===");
        System.out.println("=== טסט הושלם בהצלחה! ===");
    }

    @ParameterizedTest(name = "בדיקת breadcrumbs עבור: {0}")
    @DisplayName("בדיקת breadcrumbs בעמודי קצבאות והטבות")
    @ValueSource(strings = {"אבטלה", "קצבת ילדים", "אזרח ותיק", "נכות כללית", "סיעוד"})
    public void checkBreadcrumbsParameterized(String subMenu) {
        ExtentTest test = ExtentTestManager.getTest();

        test.info("=== בדיקת breadcrumbs עבור: " + subMenu + " ===");
        System.out.println("=== בדיקת breadcrumbs עבור: " + subMenu + " ===");

        BtlBasePage page = new BtlBasePage(driver);
        try {
            test.info("לוחץ על תפריט: קצבאות והטבות -> " + subMenu);
            page.clickOnMenu(Menu.kitsAndHat, subMenu);
            Thread.sleep(1500);

            test.info("מחפש את ה-breadcrumb בדף");
            String breadcrumbText = findBreadcrumbText();
            test.info("Breadcrumb שנמצא: " + breadcrumbText);
            System.out.println("Breadcrumb שנמצא: " + breadcrumbText);

            assertTrue(breadcrumbText.contains(subMenu) || breadcrumbText.contains("קצבאות והטבות"),
                    "ה-breadcrumb לא מכיל את '" + subMenu + "' או 'קצבאות והטבות'");

            test.pass("=== Breadcrumb תקין עבור: " + subMenu + " ✓ ===");
            System.out.println("=== Breadcrumb תקין! ===");

        } catch (Exception e) {
            test.fail("שגיאה בבדיקת breadcrumb עבור " + subMenu + ": " + e.getMessage());
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