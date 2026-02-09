package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UnemploymentCalculatorPage extends BtlBasePage {

    public UnemploymentCalculatorPage(WebDriver driver) {
        super(driver);
    }


    public void fillInitialDetails() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            // המתנה לטעינת הדף
            Thread.sleep(2000);

            String stopDate = LocalDate.now().minusDays(15).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            System.out.println("תאריך הפסקת עבודה: " + stopDate);

            WebElement stopDateInput = null;

            try {
                System.out.println("מנסה אסטרטגיה 1: חיפוש לפי ID");
                stopDateInput = wait.until(
                        ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[id*='StopWorkDate']"))
                );
                System.out.println("✓ נמצא שדה תאריך לפי ID");
            } catch (Exception e1) {
                try {
                    System.out.println("מנסה אסטרטגיה 2: חיפוש לפי Name");
                    stopDateInput = wait.until(
                            ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[name*='StopWorkDate']"))
                    );
                    System.out.println("✓ נמצא שדה תאריך לפי Name");
                } catch (Exception e2) {
                    try {
                        System.out.println("מנסה אסטרטגיה 3: חיפוש לפי Type=text");
                        List<WebElement> textInputs = driver.findElements(By.cssSelector("input[type='text']"));
                        for (WebElement input : textInputs) {
                            if (input.isDisplayed() && input.isEnabled()) {
                                String placeholder = input.getAttribute("placeholder");
                                String id = input.getAttribute("id");
                                if ((placeholder != null && (placeholder.contains("תאריך") || placeholder.contains("date"))) ||
                                        (id != null && (id.contains("Date") || id.contains("תאריך")))) {
                                    stopDateInput = input;
                                    System.out.println("✓ נמצא שדה תאריך לפי Placeholder/ID: " + id);
                                    break;
                                }
                            }
                        }
                        if (stopDateInput == null) {
                            for (WebElement input : textInputs) {
                                if (input.isDisplayed() && input.isEnabled()) {
                                    stopDateInput = input;
                                    System.out.println("✓ משתמש בשדה טקסט ראשון גלוי");
                                    break;
                                }
                            }
                        }
                    } catch (Exception e3) {
                        System.out.println("✗ כל האסטרטגיות נכשלו לחיפוש שדה תאריך");
                        throw new RuntimeException("לא ניתן למצוא שדה תאריך הפסקת עבודה");
                    }
                }
            }

            if (stopDateInput != null) {
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                        stopDateInput
                );
                Thread.sleep(500);

                stopDateInput.clear();
                Thread.sleep(300);

                stopDateInput.sendKeys(stopDate);
                System.out.println("✓ הוזן תאריך הפסקת עבודה");
                Thread.sleep(500);
            }

            try {
                System.out.println("מחפש אפשרות גיל 'מעל 28'...");

                WebElement ageOption = null;
                try {
                    ageOption = wait.until(
                            ExpectedConditions.elementToBeClickable(By.xpath("//label[contains(text(),'מעל 28')]"))
                    );
                    System.out.println("✓ נמצאה אפשרות גיל לפי טקסט");
                } catch (Exception e) {
                    try {
                        List<WebElement> radioButtons = driver.findElements(By.cssSelector("input[type='radio']"));
                        for (WebElement radio : radioButtons) {
                            String value = radio.getAttribute("value");
                            String id = radio.getAttribute("id");
                            if ((value != null && value.contains("28")) ||
                                    (id != null && (id.contains("Above") || id.contains("28")))) {
                                ageOption = radio;
                                System.out.println("✓ נמצאה אפשרות גיל לפי Value/ID");
                                break;
                            }
                        }
                    } catch (Exception e2) {
                        System.out.println("⚠ לא נמצאה אפשרות גיל - ממשיך בלעדיה");
                    }
                }

                if (ageOption != null) {
                    ((JavascriptExecutor) driver).executeScript(
                            "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                            ageOption
                    );
                    Thread.sleep(300);

                    try {
                        ageOption.click();
                    } catch (Exception e) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", ageOption);
                    }
                    System.out.println("✓ נבחר: מעל 28");
                }
            } catch (Exception e) {
                System.out.println("⚠ בעיה בבחירת גיל: " + e.getMessage());
            }

            Thread.sleep(500);

        } catch (Exception e) {
            System.out.println("שגיאה במילוי פרטים ראשוניים: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("כשל במילוי פרטים ראשוניים", e);
        }
    }

    public void clickNext() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            System.out.println("מחפש כפתור 'המשך'...");

            WebElement nextBtn = null;

            try {
                nextBtn = wait.until(
                        ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'המשך')]"))
                );
                System.out.println("✓ נמצא כפתור המשך (button)");
            } catch (Exception e1) {
                try {
                    nextBtn = wait.until(
                            ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='button' and contains(@value,'המשך')]"))
                    );
                    System.out.println("✓ נמצא כפתור המשך (input button)");
                } catch (Exception e2) {
                    try {
                        nextBtn = wait.until(
                                ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='submit' and contains(@value,'המשך')]"))
                        );
                        System.out.println("✓ נמצא כפתור המשך (input submit)");
                    } catch (Exception e3) {
                        try {
                            nextBtn = wait.until(
                                    ExpectedConditions.elementToBeClickable(By.cssSelector("input[id*='Next'], button[id*='Next']"))
                            );
                            System.out.println("✓ נמצא כפתור המשך לפי ID");
                        } catch (Exception e4) {
                            System.out.println("✗ לא נמצא כפתור המשך");
                            throw new RuntimeException("לא ניתן למצוא כפתור המשך");
                        }
                    }
                }
            }

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                    nextBtn
            );
            Thread.sleep(500);

            try {
                nextBtn.click();
                System.out.println("✓ לחיצה רגילה על כפתור 'המשך'");
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextBtn);
                System.out.println("✓ לחיצת JavaScript על כפתור 'המשך'");
            }

            Thread.sleep(1500);

        } catch (Exception e) {
            System.out.println("שגיאה בלחיצה על המשך: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("כשל בלחיצה על כפתור המשך", e);
        }
    }

    public void fillEarnings(String amount) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            Thread.sleep(1500);

            System.out.println("מחפש שדות משכורת...");

            List<WebElement> salaryFields = null;

            try {
                salaryFields = wait.until(
                        ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("input[name*='Salary']"))
                );
                System.out.println("✓ נמצאו שדות משכורת לפי Name");
            } catch (Exception e1) {
                try {
                    salaryFields = wait.until(
                            ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("input[id*='Salary']"))
                    );
                    System.out.println("✓ נמצאו שדות משכורת לפי ID");
                } catch (Exception e2) {
                    try {
                        salaryFields = driver.findElements(By.cssSelector("input[type='text']"));
                        System.out.println("✓ משתמש בכל שדות הטקסט הגלויים");
                    } catch (Exception e3) {
                        throw new RuntimeException("לא ניתן למצוא שדות משכורת");
                    }
                }
            }

            System.out.println("נמצאו " + salaryFields.size() + " שדות משכורת פוטנציאליים");

            int filledCount = 0;
            for (int i = 0; i < salaryFields.size(); i++) {
                WebElement field = salaryFields.get(i);

                try {
                    if (field.isDisplayed() && field.isEnabled()) {
                        ((JavascriptExecutor) driver).executeScript(
                                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                                field
                        );
                        Thread.sleep(200);

                        field.clear();
                        Thread.sleep(200);
                        field.sendKeys(amount);
                        filledCount++;
                        System.out.println("✓ הוזן סכום " + amount + " בשדה " + (i + 1));
                    }
                } catch (Exception e) {
                    System.out.println("⚠ דילוג על שדה " + (i + 1) + ": " + e.getMessage());
                }
            }

            if (filledCount == 0) {
                throw new RuntimeException("לא הצלחנו למלא אף שדה משכורת");
            }

            System.out.println("✓ סה\"כ מולאו " + filledCount + " שדות");
            Thread.sleep(500);

        } catch (Exception e) {
            System.out.println("שגיאה במילוי סכומי השתכרות: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("כשל במילוי סכומי השתכרות", e);
        }
    }


    public boolean isResultsPageDisplayed() {
        try {
            Thread.sleep(2000);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            System.out.println("בודק אם הגענו לדף תוצאות...");
            System.out.println("URL נוכחי: " + driver.getCurrentUrl());

            try {
                WebElement resultsHeader = wait.until(
                        ExpectedConditions.presenceOfElementLocated(By.className("calculation-results-header"))
                );
                boolean displayed = resultsHeader.isDisplayed();
                System.out.println("✓ דף תוצאות מוצג (calculation-results-header): " + displayed);
                return displayed;
            } catch (Exception e1) {
                System.out.println("⚠ לא נמצא calculation-results-header");
            }

            try {
                WebElement resultsDiv = driver.findElement(By.id("resultsContainer"));
                if (resultsDiv.isDisplayed()) {
                    System.out.println("✓ דף תוצאות מוצג (resultsContainer)");
                    return true;
                }
            } catch (Exception e2) {
                System.out.println("⚠ לא נמצא resultsContainer");
            }

            try {
                WebElement resultsText = wait.until(
                        ExpectedConditions.presenceOfElementLocated(
                                By.xpath("//*[contains(text(),'שכר יומי ממוצע') or contains(text(),'דמי אבטלה ליום') or contains(text(),'דמי אבטלה לחודש')]")
                        )
                );
                boolean displayed = resultsText.isDisplayed();
                System.out.println("✓ דף תוצאות מוצג (לפי טקסט ספציפי): " + displayed);
                return displayed;
            } catch (Exception e3) {
                System.out.println("⚠ לא נמצא טקסט תוצאות ספציפי");
            }

            try {
                List<WebElement> textElements = driver.findElements(
                        By.xpath("//*[contains(text(),'דמי אבטלה') or contains(text(),'תוצאות חישוב')]")
                );
                if (textElements.size() > 0) {
                    System.out.println("✓ נמצאו " + textElements.size() + " אלמנטים עם טקסט רלוונטי");
                    return true;
                }
            } catch (Exception e4) {
                System.out.println("⚠ לא נמצאו אלמנטים עם טקסט רלוונטי");
            }

            String pageSource = driver.getPageSource();
            boolean hasResults = pageSource.contains("שכר יומי ממוצע") ||
                    pageSource.contains("דמי אבטלה ליום") ||
                    pageSource.contains("דמי אבטלה לחודש") ||
                    (pageSource.contains("דמי אבטלה") && pageSource.contains("₪"));

            if (hasResults) {
                System.out.println("✓ דף מכיל תוצאות (לפי תוכן page source)");
                return true;
            }

            boolean hasFormFields = !driver.findElements(By.cssSelector("input[type='text']")).isEmpty();
            boolean hasSubmitButton = !driver.findElements(By.cssSelector("input[type='submit']")).isEmpty();

            if (!hasFormFields && !hasSubmitButton) {
                System.out.println("✓ דף תוצאות מוצג (אין שדות טופס)");
                return true;
            }

            System.out.println("✗ לא זוהה דף תוצאות בשום אסטרטגיה");
            System.out.println("תוכן הדף (100 תווים ראשונים): " +
                    driver.findElement(By.tagName("body")).getText().substring(0, Math.min(100, driver.findElement(By.tagName("body")).getText().length())));

            return false;

        } catch (Exception e) {
            System.out.println("שגיאה בבדיקת דף תוצאות: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public String getResultsText() {
        try {
            Thread.sleep(2000);

            System.out.println("מנסה לקרוא את תוצאות החישוב...");

            WebElement resultsContainer = null;
            String resultsText = "";

            try {
                resultsContainer = driver.findElement(By.id("resultsContainer"));
                resultsText = resultsContainer.getText();
                System.out.println("✓ נמצא resultsContainer");
            } catch (Exception e1) {
                System.out.println("⚠ לא נמצא resultsContainer");
            }

            if (resultsText.isEmpty()) {
                try {
                    resultsContainer = driver.findElement(By.className("results"));
                    resultsText = resultsContainer.getText();
                    System.out.println("✓ נמצא results class");
                } catch (Exception e2) {
                    System.out.println("⚠ לא נמצא results class");
                }
            }

            if (resultsText.isEmpty()) {
                try {
                    List<WebElement> divs = driver.findElements(
                            By.xpath("//div[contains(text(),'שכר יומי') or contains(text(),'דמי אבטלה')]")
                    );
                    for (WebElement div : divs) {
                        resultsText += div.getText() + "\n";
                    }
                    if (!resultsText.isEmpty()) {
                        System.out.println("✓ נמצאו divs עם תוצאות");
                    }
                } catch (Exception e3) {
                    System.out.println("⚠ לא נמצאו divs עם תוצאות");
                }
            }

            if (resultsText.isEmpty()) {
                try {
                    resultsContainer = driver.findElement(By.tagName("body"));
                    resultsText = resultsContainer.getText();
                    System.out.println("✓ משתמש בכל תוכן הדף");
                } catch (Exception e4) {
                    System.out.println("⚠ לא הצלחנו לקרוא שום תוכן");
                }
            }

            if (!resultsText.isEmpty()) {
                System.out.println("\n=== תוצאות החישוב ===");
                System.out.println(resultsText);
                System.out.println("======================\n");
            } else {
                System.out.println("⚠ לא נמצא שום טקסט תוצאות");
            }

            return resultsText;

        } catch (Exception e) {
            System.out.println("שגיאה בקריאת תוצאות: " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

    public boolean resultsContain(String value) {
        String results = getResultsText();
        return results.contains(value);
    }
}