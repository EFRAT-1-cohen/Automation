package Pages;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class DebugUnemploymentPage {
    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            driver.manage().window().maximize();
            driver.get("https://www.btl.gov.il/Pages/default.aspx");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            System.out.println("=== Navigating to Unemployment Page ===");
            WebElement mainMenu = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//li[@class='insideItem']//a[contains(text(),'קצבאות והטבות')]")
            ));
            mainMenu.click();
            Thread.sleep(700);

            WebElement subMenu = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//li//a[normalize-space(text())='אבטלה']")
            ));
            subMenu.click();
            Thread.sleep(2000);

            System.out.println("=== Clicking Calculator Link ===");
            WebElement calculatorLink = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("a[href*='AvtCalcIndex']")
            ));
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                    calculatorLink
            );
            Thread.sleep(1000);
            calculatorLink.click();
            Thread.sleep(2000);

            System.out.println("URL after first click: " + driver.getCurrentUrl());

            WebElement specificCalcLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(@href, 'Avtalah') or contains(text(), 'חישוב') or contains(text(), 'סכום')]")
            ));
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                    specificCalcLink
            );
            Thread.sleep(500);
            specificCalcLink.click();
            Thread.sleep(3000);

            System.out.println("=== Current URL: " + driver.getCurrentUrl() + " ===");

            System.out.println("\n=== Searching for INPUT elements ===");
            List<WebElement> allInputs = driver.findElements(By.tagName("input"));
            System.out.println("Total input elements found: " + allInputs.size());

            for (int i = 0; i < Math.min(allInputs.size(), 30); i++) {
                WebElement input = allInputs.get(i);
                try {
                    String id = input.getAttribute("id");
                    String name = input.getAttribute("name");
                    String type = input.getAttribute("type");
                    String placeholder = input.getAttribute("placeholder");
                    boolean displayed = input.isDisplayed();

                    if (name != null && (name.contains("Date") || name.contains("Salary") ||
                            name.contains("Stop") || name.contains("Work"))) {
                        System.out.println("\n--- Input #" + i + " (RELEVANT) ---");
                        System.out.println("ID: " + id);
                        System.out.println("Name: " + name);
                        System.out.println("Type: " + type);
                        System.out.println("Placeholder: " + placeholder);
                        System.out.println("Displayed: " + displayed);
                    }
                } catch (Exception e) {
                }
            }

            System.out.println("\n=== Searching for BUTTON elements ===");
            List<WebElement> allButtons = driver.findElements(By.tagName("button"));
            System.out.println("Total button elements found: " + allButtons.size());

            for (int i = 0; i < allButtons.size(); i++) {
                WebElement button = allButtons.get(i);
                try {
                    String text = button.getText();
                    String id = button.getAttribute("id");
                    boolean displayed = button.isDisplayed();

                    System.out.println("\n--- Button #" + i + " ---");
                    System.out.println("Text: " + text);
                    System.out.println("ID: " + id);
                    System.out.println("Displayed: " + displayed);
                } catch (Exception e) {
                }
            }

            System.out.println("\n=== Searching for SUBMIT/BUTTON inputs ===");
            List<WebElement> submitInputs = driver.findElements(By.cssSelector("input[type='submit'], input[type='button']"));
            for (int i = 0; i < submitInputs.size(); i++) {
                WebElement input = submitInputs.get(i);
                try {
                    String value = input.getAttribute("value");
                    String id = input.getAttribute("id");
                    boolean displayed = input.isDisplayed();

                    System.out.println("\n--- Submit/Button Input #" + i + " ---");
                    System.out.println("Value: " + value);
                    System.out.println("ID: " + id);
                    System.out.println("Displayed: " + displayed);
                } catch (Exception e) {
                }
            }

            System.out.println("\n=== Waiting 30 seconds for manual inspection ===");
            Thread.sleep(30000);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}