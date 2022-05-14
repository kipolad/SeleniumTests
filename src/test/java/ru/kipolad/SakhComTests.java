/**
 * Created by Yulya Telysheva
 */
package ru.kipolad;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SakhComTests {
    WebDriver driver;
    WebDriverWait webDriverWait;
    Actions actions;

    @BeforeAll
    static void registerDriver() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void initDriver() {
        driver = new ChromeDriver();
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        actions = new Actions(driver);
        driver.get("https://sakh.com/");
    }

    @Test
    void showMoscowWeather() {
        driver.findElement(By.xpath("//a[.='Погода' and @class='black']")).click();

        actions.moveToElement(driver.findElement(By.xpath("//span[contains(., 'Южно-Сахалинск')]")))
                .click()
                .build()
                .perform();

        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[.='Москва']")));
        driver.findElement(By.xpath("//a[.='Москва']")).click();

        Assertions.assertAll(
                () -> Assertions.assertTrue(driver.getCurrentUrl().contains("msk"), "Url does not contain msk"),
                () -> Assertions.assertTrue(driver.findElement(By.xpath("//span[contains(., 'Москва')]")).isDisplayed(),
                        "Moscow city is not displayed in the Weathers menu")
        );
    }

    @Test
    void rightFilterForAuto() {
        driver.findElement(By.xpath("//ul//a[.='Авто']")).click();
        actions.moveToElement(driver.findElement(By.xpath("//span[@data-select2-id='1']")))
                .click()
                .build()
                .perform();

        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[.='Subaru']")));
        driver.findElement(By.xpath("//li[.='Subaru']")).click();


        actions.moveToElement(driver.findElement(By.xpath("//span[@data-select2-id='3']")))
                .click()
                .build()
                .perform();

        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[contains(.,'XV')]")));
        driver.findElement(By.xpath("//li[contains(.,'XV')]")).click();


        actions.moveToElement(driver.findElement(By.xpath("//span[@data-select2-id='7']")))
                .click()
                .build()
                .perform();

        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[.='2015']")));
        driver.findElement(By.xpath("//li[.='2015']")).click();


        actions.moveToElement(driver.findElement(By.xpath("//span[@data-select2-id='9']")))
                .click()
                .build()
                .perform();

        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[.='2015']")));
        driver.findElement(By.xpath("//li[.='2015']")).click();

        driver.findElement(By.id("g-footer-notice-button")).click();

        driver.findElement(By.xpath("//button[@id='sale-filter-submit']")).click();

        List<WebElement> autoTitle = driver.findElements(By.xpath("//div[@class='sale-title desktop']//a[@class='sale-link']"));
        boolean isSubaru = true;
        for (WebElement autoElement : autoTitle) {
            if (!autoElement.getText().contains("Subaru XV, 2015")) isSubaru = false;
        }
        Assertions.assertTrue(isSubaru, "Filters return wrong list of cars");

    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
