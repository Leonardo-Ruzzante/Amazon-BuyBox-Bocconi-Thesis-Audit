package org.research.amazonbuybox.crawling;

import java.time.Duration;
import java.util.List;
import java.util.StringTokenizer;

import org.research.amazonbuybox.config.ApplicationSettings;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class PageBrowser {

    private WebDriver driver;

    public void expandOfferListing(String url) {
        initializeDriver();
        driver.get(url);
        acceptCookiesIfPresent();
        openOfferList();
        scrollThroughOffers();
        expandDeliveryDetails();
    }

    public String getHtml() {
        return driver.getPageSource();
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void close() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void initializeDriver() {
        String chromeDriverPath = ApplicationSettings.envOrDefault("CHROME_DRIVER_PATH", "");
        if (!chromeDriverPath.isEmpty()) {
            System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        }

        ChromeOptions options = new ChromeOptions();
        String chromeBinaryPath = ApplicationSettings.envOrDefault("CHROME_BINARY_PATH", "");
        if (!chromeBinaryPath.isEmpty()) {
            options.setBinary(chromeBinaryPath);
        }
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    private void acceptCookiesIfPresent() {
        try {
            driver.findElement(By.id("sp-cc-accept")).click();
        } catch (org.openqa.selenium.NoSuchElementException ignored) {
        }
    }

    private void openOfferList() {
        driver.findElement(By.cssSelector("#olpLinkWidget_feature_div a, #aod-ingress-link, #buybox-see-all-buying-choices a")).click();
    }

    private void scrollThroughOffers() {
        String offerCountText = driver.findElement(By.id("aod-filter-offer-count-string")).getText();
        StringTokenizer tokenizer = new StringTokenizer(offerCountText, " ");
        int sellerCount = Integer.parseInt(tokenizer.nextToken());

        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        for (int i = 0; i <= sellerCount; i++) {
            try {
                WebElement priceElement = driver.findElement(By.cssSelector("#aod-price-" + i + " .a-price, #aod-price-" + i + " .a-offscreen"));
                javascriptExecutor.executeScript("arguments[0].scrollIntoView(true)", priceElement);
            } catch (org.openqa.selenium.NoSuchElementException missingOffer) {
                try {
                    driver.findElement(By.id("aod-show-more-offers")).click();
                    WebElement priceElement = driver.findElement(By.cssSelector("#aod-price-" + i + " .a-price, #aod-price-" + i + " .a-offscreen"));
                    javascriptExecutor.executeScript("arguments[0].scrollIntoView(true)", priceElement);
                } catch (Exception ignored) {
                }
            }
        }
    }

    private void expandDeliveryDetails() {
        try {
            JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
            List<WebElement> elements = driver.findElements(By.id("aod-delivery-more-action"));
            for (WebElement element : elements) {
                javascriptExecutor.executeScript("arguments[0].scrollIntoView(true)", element);
                javascriptExecutor.executeScript("arguments[0].click()", element);
            }
        } catch (org.openqa.selenium.NoSuchElementException ignored) {
        }
    }
}
