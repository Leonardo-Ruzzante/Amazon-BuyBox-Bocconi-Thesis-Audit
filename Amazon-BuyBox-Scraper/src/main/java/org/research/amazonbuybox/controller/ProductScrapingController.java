package org.research.amazonbuybox.controller;

import org.research.amazonbuybox.config.ApplicationSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductScrapingController {

    private final KafkaTemplate<Object, Object> kafkaTemplate;

    @Autowired
    public ProductScrapingController(KafkaTemplate<Object, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping(path = "/scraping-jobs")
    public void submitScrapingJob(@RequestParam(name = "catalogPath", required = false) String catalogPath) {
        String resolvedCatalogPath = catalogPath;
        if (resolvedCatalogPath == null || resolvedCatalogPath.trim().isEmpty()) {
            resolvedCatalogPath = ApplicationSettings.PRODUCT_CATALOG_FILE.toString();
        }
        kafkaTemplate.send(ApplicationSettings.PRODUCT_CATALOG_TOPIC, resolvedCatalogPath);
    }
}
