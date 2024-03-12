import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.testng.Assert.assertEquals;

public class SausePage {
        private WebDriver driver;
        java.lang.String url="https://www.saucedemo.com/";
        By loginPageHeaderSelector = By.className("login_logo");
        WebElement loginPageHeader;
        By userName= By.id("user-name");
        String userInp="standard_user";
        By password= By.id("password");
        String passInp="secret_sauce";
        By loginBut= By.id("login-button");
        By inventoryItem= By.className("inventory_item");
        int itemCount;
        String itemX="//div[text()=\"%s\"]/parent::a/parent::div/following-sibling::div/button";
        String itemName=String.format(itemX,"Sauce Labs Backpack");
        String cartLogo="//div/div/div/div/div/div[@id='shopping_cart_container']/a";
        String cartItem="//div/div/div/div[2]/div/div[1]/div[3]/div[2]/a/div[@class=\"inventory_item_name\"]";
        String nameCart;
        @Test
        public void testDemo(){
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            driver = new ChromeDriver(options);
            driver.get(url);

            loginPageHeader = driver.findElement(loginPageHeaderSelector);
            System.out.println(loginPageHeader.getText());

            driver.findElement(userName).sendKeys(userInp);
            driver.findElement(password).sendKeys(passInp);
            driver.findElement(loginBut).click();

            // Count the number of items on the page
            itemCount = driver.findElements(inventoryItem).size();
            System.out.println("Number of items on the page: " + itemCount);
            assertEquals(itemCount, 6, "Number of items on the page doesn't match expected count");

            driver.findElement(By.xpath(itemName)).click();//add to cart
            driver.findElement(By.xpath(cartLogo)).click();//go to cart
            nameCart=driver.findElement(By.xpath(cartItem)).getText();
            assertEquals(nameCart, "Sauce Labs Backpack", "Not the same item");

            driver.findElement(By.id("checkout")).click();//go to checkout
            driver.findElement(By.id("first-name")).sendKeys("Muhammed");//Enter first name
            driver.findElement(By.id("last-name")).sendKeys("Magdy");//Enter last name
            driver.findElement(By.id("postal-code")).sendKeys("12345");//Enter postalcode

            driver.findElement(By.id("continue")).click();//continue
            driver.findElement(By.id("finish")).click();//finish
            driver.findElement(By.id("back-to-products")).click();//back to main menu
            //driver.close();
        }

        @AfterMethod
    public void takeScreenShot(ITestResult result)
        {
            if(ITestResult.FAILURE==result.getStatus())
            {
                var camera=(TakesScreenshot)driver;
                File screenshot=camera.getScreenshotAs(OutputType.FILE);
                try{
                    Files.move(screenshot.toPath(), new File("src/main/resources/screenshots"+result.getName()+".png").toPath());
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

        }

}
