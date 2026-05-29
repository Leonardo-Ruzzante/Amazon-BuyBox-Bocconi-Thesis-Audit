package org.research.amazonbuybox.catalog;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.research.amazonbuybox.config.ApplicationSettings;
import org.research.amazonbuybox.messaging.CrawlingRequest;
import org.research.amazonbuybox.model.Experiment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class ProductCatalogProcessor {

    private final Logger logger = LoggerFactory.getLogger(ProductCatalogProcessor.class);
    private final KafkaTemplate<Object, Object> kafkaTemplate;

    @Autowired
    public ProductCatalogProcessor(KafkaTemplate<Object, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(id = "product-catalog", topics = "#{T(org.research.amazonbuybox.config.ApplicationSettings).PRODUCT_CATALOG_TOPIC}", concurrency = "1")
    public void listen(String catalogPath, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) throws IOException {
        Path resolvedCatalogPath = resolveCatalogPath(catalogPath);
        logger.info("Received product catalog request from topic {} using file {}", topic, resolvedCatalogPath);

        ProductCatalogLoader productCatalogLoader = new ProductCatalogLoader();
        List<Experiment> experiments = productCatalogLoader.loadExperiments(resolvedCatalogPath);

        for (Experiment experiment : experiments) {
            logger.info("Submitting crawl request: {}", experiment);
            kafkaTemplate.send(ApplicationSettings.CRAWLING_TOPIC, new CrawlingRequest(experiment));
        }
    }

    private Path resolveCatalogPath(String catalogPath) {
        if (catalogPath == null || catalogPath.trim().isEmpty()) {
            return ApplicationSettings.PRODUCT_CATALOG_FILE;
        }
        return Paths.get(catalogPath.trim());
    }
}
