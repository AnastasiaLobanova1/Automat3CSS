package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DebitCardTest {
    private WebDriver driver;

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    public void shouldSendForm() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванов-Петров Иван");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79001002002");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] > .checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(new By.ByCssSelector("[data-test-id='order-success']")).getText();
        Assertions.assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    public void shouldValidateNameForm() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Ivan&75!@");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79001002002");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] > .checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(new By.ByCssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        Assertions.assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    public void shouldNoNameForm() {
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79001002002");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] > .checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(new By.ByCssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        Assertions.assertEquals("Поле обязательно для заполнения", text.trim());
    }
    @Test
    public void shouldValidatePhoneForm() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванов-Петров Иван");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+7900100");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] > .checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(new By.ByCssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        Assertions.assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }
    @Test
    public void shouldNoPhoneForm() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванов-Петров Иван");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] > .checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(new By.ByCssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        Assertions.assertEquals("Поле обязательно для заполнения", text.trim());
    }
    @Test
    public void shouldInvalidCheckBox() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванов-Петров Иван");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79001002002");
        driver.findElement(By.className("button")).click();
        WebElement checkbox = driver.findElement(new By.ByCssSelector("[data-test-id='agreement'].input_invalid"));
        checkbox.isDisplayed();
    }

}
