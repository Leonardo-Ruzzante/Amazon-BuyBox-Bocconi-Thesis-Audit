package org.research.amazonbuybox.crawling;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.research.amazonbuybox.config.ApplicationSettings;
import org.research.amazonbuybox.messaging.CrawlingRequest;
import org.research.amazonbuybox.messaging.ExtractionRequest;
import org.research.amazonbuybox.model.Experiment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

@Configuration
public class CrawlingProcessor {

    private final Logger logger = LoggerFactory.getLogger(CrawlingProcessor.class);
    private final KafkaTemplate<Object, Object> kafkaTemplate;

    @Autowired
    public CrawlingProcessor(KafkaTemplate<Object, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(id = "product-crawling", topics = "#{T(org.research.amazonbuybox.config.ApplicationSettings).CRAWLING_TOPIC}", concurrency = "1")
    public void listen(CrawlingRequest request, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) throws IOException {
        logger.info("Received crawl request from topic {}", topic);

        PageBrowser pageBrowser = new PageBrowser();
        try {
            pageBrowser.expandOfferListing(request.getExperiment().getUrl());

            String html = pageBrowser.getHtml();
            String timestamp = DateFormatUtils.format(new Date(), "yyyyMMdd'T'HHmmss");

            Experiment experiment = request.getExperiment();
            Path htmlFile = saveHtmlFile(experiment.getName(), html, timestamp);
            Path screenshotFile = saveScreenshot(experiment.getName(), timestamp, pageBrowser.getDriver());

            experiment.setHtmlFilePath(htmlFile.toString());
            experiment.setScreenshotFilePath(screenshotFile.toString());

            ExtractionRequest extractionRequest = new ExtractionRequest(experiment, timestamp);
            kafkaTemplate.send(ApplicationSettings.EXTRACTION_TOPIC, extractionRequest);
        } finally {
            pageBrowser.close();
        }
    }

    private Path saveScreenshot(String fileNamePrefix, String timestamp, WebDriver driver) throws IOException {
        Files.createDirectories(ApplicationSettings.SCREENSHOT_OUTPUT_DIRECTORY);
        String fileName = normalizeFileName(fileNamePrefix) + "_" + timestamp + ".png";
        Path outputPath = ApplicationSettings.SCREENSHOT_OUTPUT_DIRECTORY.resolve(fileName);

        TakesScreenshot screenshotDriver = (TakesScreenshot) driver;
        File screenshot = screenshotDriver.getScreenshotAs(OutputType.FILE);
        Files.move(screenshot.toPath(), outputPath, StandardCopyOption.REPLACE_EXISTING);

        logger.info("Saved screenshot to {}", outputPath);
        return outputPath;
    }

    private Path saveHtmlFile(String fileNamePrefix, String html, String timestamp) throws IOException {
        Files.createDirectories(ApplicationSettings.HTML_OUTPUT_DIRECTORY);
        String fileName = normalizeFileName(fileNamePrefix) + "_" + timestamp + ".html";
        Path outputPath = ApplicationSettings.HTML_OUTPUT_DIRECTORY.resolve(fileName);

        try (PrintWriter printWriter = new PrintWriter(outputPath.toFile())) {
            printWriter.append(html);
        }

        logger.info("Saved HTML to {}", outputPath);
        return outputPath;
    }

    private String normalizeFileName(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "product";
        }
        return value.trim().replaceAll("[^A-Za-z0-9._-]", "_").replaceAll("_+", "_");
    }
}
