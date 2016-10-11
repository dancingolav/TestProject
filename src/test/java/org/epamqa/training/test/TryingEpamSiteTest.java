package org.epamqa.training.test;


import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;


import static org.testng.Assert.assertTrue;


/**
 * Created by AlexSh on 08.10.2016.
 */
public class TryingEpamSiteTest {

    private WebDriver myPersonalDriver;

    /*
    in testng.xml
    f.e. browser: "firefox","chrome","ie", "opera"
    f.e. pathToDriver: "D:\\PersonalDrivers\\geckodriver.exe",
     */

    @BeforeTest
    @Parameters({ "browser", "pathToDriver"})
    public void beforeSuite(@Optional("chrome") String browser, @Optional("D:\\PersonalDrivers\\chromedriver.exe") String pathToDriver ) {

        ArrayList<String> browsersHerd= new ArrayList<String> (Arrays.asList(new String[]{"firefox", "chrome", "ie", "opera"}));

        //System's properties we have to set to use drivers
        String[] sysProperty = new String[]{
                "webdriver.gecko.driver",
                "webdriver.opera.driver",
                "webdriver.ie.driver",
                "webdriver.chrome.driver"
        };
        //Checking whether file is exist
        File f = new File(pathToDriver);
        if(!f.exists() | f.isDirectory()) {
            System.out.println("Error! Check your browser's path in testng.xml!");
            Assert.fail("Error! Check your browser's path in testng.xml!");
        }
        //Checking  whether browser of correct type
        if (!browsersHerd.contains(browser)) {
            System.out.println("Error! Check your browser type in testng.xml!");
            System.out.println("firefox,chrome,ie,opera");
            Assert.fail("Error! Check your browser type testng.xml!");
        }



        //Since Java 7 we can use String
        switch (browser.toLowerCase()) {
            case "firefox":
                System.setProperty(sysProperty[0], pathToDriver);
                myPersonalDriver = new FirefoxDriver();
                break;
            case "opera":
                System.setProperty(sysProperty[1], pathToDriver);
                myPersonalDriver = new OperaDriver();
                break;
            case "ie":
                System.setProperty(sysProperty[2], pathToDriver);
                myPersonalDriver = new InternetExplorerDriver();
                break;
            case "chrome":
                System.setProperty(sysProperty[3], pathToDriver);
                myPersonalDriver = new ChromeDriver();
                break;
        }



        System.out.println(browser+" "+pathToDriver);

        myPersonalDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        myPersonalDriver.get("https://jdi-framework.github.io/tests/");
        myPersonalDriver.manage().window().maximize();

    }

    @DataProvider(name = "wrongdataforlogin")
    public Object[][] createWrongDataForLogin() {
        return new Object[][] {

                { "epam", ""},
                { "", ""},
                { "", "1234" },
                { StringUtils.repeat("qwerty0987", 10), StringUtils.repeat("QWERTY1234",10)},
                { "epam", "12345" },
        };
    }

    @Test (dataProvider="wrongdataforlogin")
    public void badLogin(String accountName, String accountPwd) {
        System.out.println("logging into the account");


        if (myPersonalDriver.findElements(By.xpath("//li[@class='dropdown uui-profile-menu open']")).size()==0)
            myPersonalDriver.findElement(By.xpath("//a[@class='dropdown-toggle'][@href='#']")).click();


        myPersonalDriver.findElement(By.xpath("//input[@id='Login']")).click();
        myPersonalDriver.findElement(By.xpath("//input[@id='Login']")).clear();
        myPersonalDriver.findElement(By.xpath("//input[@id='Login']")).sendKeys(accountName);


        myPersonalDriver.findElement(By.xpath("//input[@id='Password']")).click();
        myPersonalDriver.findElement(By.xpath("//input[@id='Password']")).clear();
        myPersonalDriver.findElement(By.xpath("//input[@id='Password']")).sendKeys(accountPwd);

        myPersonalDriver.findElement(By.xpath("//button[@class='uui-button dark-blue btn-login']/span[text()='Enter']")).click();

        //Is login failed? It has to be.
        assertTrue(myPersonalDriver.findElements(By.xpath("//span[@class='login-txt'][text()='* Login Faild']")).size() != 0);


    }

    @DataProvider(name = "rightdataforlogin")
    public Object[][] createRightDataForLogin() {
        return new Object[][] {

                { "epam", "1234"},
        };
    }

    @Test (dataProvider="rightdataforlogin")
    public void goodLogin(String accountName, String accountPwd) {
        System.out.println("logging into the account");

     if (myPersonalDriver.findElements(By.xpath("//li[@class='dropdown uui-profile-menu open']")).size()==0)
            myPersonalDriver.findElement(By.xpath("//a[@class='dropdown-toggle'][@href='#']")).click();


        myPersonalDriver.findElement(By.xpath("//input[@id='Login']")).click();
        myPersonalDriver.findElement(By.xpath("//input[@id='Login']")).clear();

        myPersonalDriver.findElement(By.xpath("//input[@id='Login']")).sendKeys(accountName);


        myPersonalDriver.findElement(By.xpath("//input[@id='Password']")).click();
        myPersonalDriver.findElement(By.xpath("//input[@id='Password']")).clear();

        myPersonalDriver.findElement(By.xpath("//input[@id='Password']")).sendKeys(accountPwd);
        myPersonalDriver.findElement(By.xpath("//button[@class='uui-button dark-blue btn-login']/span[text()='Enter']")).click();

        //Is login passed? It has to be.

        assertTrue(myPersonalDriver.findElements(By.xpath("//span[@class=''][text()='Piter Chailovskii']")).size() != 0);
    }

    @AfterTest
    public void afterTest () {
        //Close the driver
        myPersonalDriver.close();
        myPersonalDriver.quit();


      }

}