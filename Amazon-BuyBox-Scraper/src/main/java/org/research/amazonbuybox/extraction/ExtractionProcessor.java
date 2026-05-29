package org.research.amazonbuybox.extraction;

import java.io.IOException;
import java.util.LinkedList;

import org.research.amazonbuybox.config.ApplicationSettings;
import org.research.amazonbuybox.io.ProductOfferCsvWriter;
import org.research.amazonbuybox.messaging.ExtractionRequest;
import org.research.amazonbuybox.model.ProductOffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

@Configuration
public class ExtractionProcessor {

    private final Logger logger = LoggerFactory.getLogger(ExtractionProcessor.class);

    @KafkaListener(id = "product-extraction", topics = "#{T(org.research.amazonbuybox.config.ApplicationSettings).EXTRACTION_TOPIC}", concurrency = "1")
    public void listen(ExtractionRequest request, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) throws IOException {
        logger.info("Received extraction request from topic {}", topic);

        ProductDataExtractor productDataExtractor = new ProductDataExtractor();
        LinkedList<ProductOffer> productOffers = productDataExtractor.extractProductOffers(
                request.getExperiment(), request.getExtractionTimestamp());

        logger.info("Extracted {} product offers", productOffers.size());

        ProductOfferCsvWriter csvWriter = new ProductOfferCsvWriter();
        csvWriter.appendOffers(
                ApplicationSettings.OFFER_OUTPUT_FILE,
                request.getExperiment(),
                request.getExtractionTimestamp(),
                productOffers);

        logger.info("Appended rows to {}", ApplicationSettings.OFFER_OUTPUT_FILE);
    }
}
