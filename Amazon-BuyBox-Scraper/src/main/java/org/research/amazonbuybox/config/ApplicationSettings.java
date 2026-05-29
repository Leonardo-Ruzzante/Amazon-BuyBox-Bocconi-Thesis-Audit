package org.research.amazonbuybox.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class ApplicationSettings {

    public static final String PRODUCT_CATALOG_TOPIC = envOrDefault("PRODUCT_CATALOG_TOPIC", "product-catalog");
    public static final String CRAWLING_TOPIC = envOrDefault("CRAWLING_TOPIC", "product-crawling");
    public static final String EXTRACTION_TOPIC = envOrDefault("EXTRACTION_TOPIC", "product-extraction");

    public static final String DEFAULT_PRODUCT_CATALOG_FILE = "data/input/products.csv";
    public static final String DEFAULT_OFFER_OUTPUT_FILE = "data/output/product_offers.csv";

    public static final Path PRODUCT_CATALOG_FILE = Paths.get(envOrDefault("PRODUCT_CATALOG_FILE", DEFAULT_PRODUCT_CATALOG_FILE));
    public static final Path OFFER_OUTPUT_FILE = Paths.get(envOrDefault("OFFER_OUTPUT_FILE", DEFAULT_OFFER_OUTPUT_FILE));
    public static final Path HTML_OUTPUT_DIRECTORY = Paths.get(envOrDefault("HTML_OUTPUT_DIRECTORY", "data/html"));
    public static final Path SCREENSHOT_OUTPUT_DIRECTORY = Paths.get(envOrDefault("SCREENSHOT_OUTPUT_DIRECTORY", "data/screenshots"));

    private ApplicationSettings() {
    }

    public static String envOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return value.trim();
    }
}
