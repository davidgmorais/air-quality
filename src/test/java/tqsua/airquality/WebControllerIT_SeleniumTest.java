package tqsua.airquality;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SeleniumJupiter.class)
class WebControllerIT_SeleniumTest {

    @Test
    void searchForACityInIndex(FirefoxDriver driver) {
        driver.get("http://localhost:8080/");
        driver.findElement(By.id("search")).click();
        driver.findElement(By.id("search")).sendKeys("lisbon");
        driver.findElement(By.id("search")).sendKeys(Keys.ENTER);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);     // wait 2 seconds for the result list
        driver.findElement(By.linkText("Olivais, Lisboa, Portugal")).click();
        assertEquals("Air Quality App: Olivais, Lisboa, Portugal", driver.getTitle());
        assertEquals("Olivais, Lisboa, Portugal", driver.findElement(By.id("city_name")).getText());
        assertEquals("City id: 10513", driver.findElement(By.id("city_idx")).getText());
    }

    @Test
    void getCacheStats(FirefoxDriver driver) {
        driver.get("http://localhost:8080/");
        driver.findElement(By.linkText("Cache info")).click();
        assertEquals("Air Quality App: Cache Stats", driver.getTitle());
    }

    @Test
    void searchForACityInAnotherCityInfoPage(FirefoxDriver driver) {
        driver.get("http://localhost:8080/city/8379");
        driver.findElement(By.id("search")).click();
        driver.findElement(By.id("search")).sendKeys("london");
        driver.findElement(By.id("search")).sendKeys(Keys.ENTER);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);     // wait 2 seconds for the result list
        driver.findElement(By.linkText("Camden - Euston Road, United Kingdom")).click();
        assertEquals("Air Quality App: Camden - Euston Road, United Kingdom", driver.getTitle());
        assertEquals("Camden - Euston Road, United Kingdom", driver.findElement(By.id("city_name")).getText());
        assertEquals("City id: 7945", driver.findElement(By.id("city_idx")).getText());
    }

}
