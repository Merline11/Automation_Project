package uk.axone.framework.base;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import uk.axone.extentreports.ExtentReportsManager;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

/**
 * BaseTest - to perform any common testing tasks across all the tests.
 */
public class BaseTest {

    /**
     * WebDriver instance.
     */
    private WebDriver driver;

    /**
     * instance of property.
     */
    private Properties prop;

    /**
     * wait time constant.
     */
    public static final int WAIT_TIME_IN_SECONDS = 10;

    protected ExtentReports report = ExtentReportsManager.getExtentReports();

    protected ExtentTest test;

    /**
     * no -arg constructor to load the application.properties.
     * @throws FileNotFoundException
     */
    public BaseTest() throws FileNotFoundException {
        String propPath = System.getProperty("user.dir")+"/src/test/resources/application.properties";
        //create an empty property list
        prop = new Properties();

        try {
            FileInputStream fis = new FileInputStream(propPath);
            prop.load(fis);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * getter method.
     * @return WebDriver
     */
    public WebDriver getDriver() {
        return driver;
    }

    /**
     * setter method
     * @param driver
     */
    public void setDriver(final WebDriver driver) {
        this.driver = driver;
    }

    public Properties getProp() {
        return prop;
    }

    public void setProp(final Properties prop) {
        this.prop = prop;
    }

    /**
     * to launch user's choice of browser
     */
    public void launchBrowser() {
        String browser = prop.getProperty("browserType");

        if(browser.equalsIgnoreCase("chrome")){
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("firefox")){
            driver = new FirefoxDriver();
        } else if (browser.equalsIgnoreCase("edge")){
            driver = new EdgeDriver();
        } else if (browser.equalsIgnoreCase("safari")){
            driver = new SafariDriver();
        } else {
            System.out.println("Please choose more appropriate valid browser");
        }

        //to maximize he window
        driver.manage().window().maximize();
        //to set the implicit wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(WAIT_TIME_IN_SECONDS));

    }

    /**
     * to navigate to the application url.
     */
    public void navigateToURL() {
        driver.navigate().to(prop.getProperty("url"));
    }

    public WebElement identifyElement(final String locatorKey) {
        WebElement element = null ;
        String propValue = prop.getProperty(locatorKey);

        if(locatorKey.endsWith("className")){
            element = driver.findElement(By.className(propValue));
        } else if (locatorKey.endsWith("xpath")) {
            element = driver.findElement(By.xpath(propValue));
        } else if (locatorKey.endsWith("id")) {
            element = driver.findElement(By.id(propValue));
        } else if (locatorKey.endsWith("name")) {
            element = driver.findElement(By.name(propValue));
        } else if (locatorKey.endsWith("css")) {
            element = driver.findElement(By.cssSelector(propValue));
        } else if (locatorKey.endsWith("linkText")) {
            element = driver.findElement(By.linkText(propValue));
        } else if (locatorKey.endsWith("partialLinkText")) {
            element = driver.findElement(By.partialLinkText(propValue));
        } else {
            System.out.println("Invalid locator specified");
        }

        return element;
    }

    /**
     * to click any WebElement
     * @param locatorKey
     */
    public void clickWebElement(final String locatorKey) {
        WebDriverWait wait = getWebDriverWait();
        wait.until(ExpectedConditions.elementToBeClickable(identifyElement(locatorKey))).click();
    }

    /**
     * to simulate sendkeys of WebElement.
     * @param locatorKey
     * @param valuesToEnter
     */
    public void typeValuesInTextBox(final String locatorKey,final String valuesToEnter) {
        WebDriverWait wait = getWebDriverWait();
        wait.until(ExpectedConditions.visibilityOf(identifyElement(locatorKey))).sendKeys(valuesToEnter);
    }

    /**
     * to select any WebElement
     * @param locatorKey
     */
    public void selectWebElement(final String locatorKey) {
        WebDriverWait wait = getWebDriverWait();
        wait.until(ExpectedConditions.elementToBeSelected(identifyElement(locatorKey)));
    }

    /**
     * WebDriver wait utility
     * @return
     */
    private WebDriverWait getWebDriverWait() {
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(WAIT_TIME_IN_SECONDS));
        return wait;
    }
    /**
     * to create an account by using your email address
     */
    public void createAccount() throws InterruptedException {

        clickWebElement("sign_in_lnk_className");
        Thread.sleep(2000);

        typeValuesInTextBox("email_address_txt_id","merline36@gmail.com");
        Thread.sleep(2000);

        clickWebElement("create_account_lnk_id");
        Thread.sleep(2000);

        driver.findElement(By.cssSelector("#id_gender2")).click();
        Thread.sleep(2000);

        typeValuesInTextBox("customer_firstname_txt_id","Merline");
        Thread.sleep(2000);

        typeValuesInTextBox("customer_lastname_txt_id","Jayaraj");
        Thread.sleep(2000);

        typeValuesInTextBox("password_txt_id","12345");
        Thread.sleep(2000);

        clickWebElement("submit_account_btn_id");
        Thread.sleep(2000);

        Assert.assertEquals(driver.findElement(By.xpath("//*[@id='header']/div[2]/div/div/nav/div[1]/a/span")).getText(), "Merline Jayaraj");
    }

    protected void logInfo(final String infoMessage) {
        test.log(LogStatus.INFO, infoMessage);
    }

    protected void logFail(final String failMessage) {
        test.log(LogStatus.FAIL,failMessage);
        takeScreenshot();
    }

    public void takeScreenshot() {

        Date d = new Date();
        String fileName = d.toString().replace(" ","-").replace(":","_")+"_png";

        String screenShotPath = SystemUtils.USER_DIR+"/target/screenshots/"+fileName;

        // 1. Take page screenshot
        Screenshot screenshot = new AShot()
                .shootingStrategy(ShootingStrategies.viewportPasting(100))
                .takeScreenshot(driver);

        // 2. to store it in different location
        try {
            ImageIO.write(screenshot.getImage(),"PNG", new File(screenShotPath));

            //3. add screenshot in html report
            test.log(LogStatus.ERROR, test.addScreenCapture(screenShotPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
