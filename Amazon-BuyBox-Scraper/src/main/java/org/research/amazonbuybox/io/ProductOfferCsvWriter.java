package org.research.amazonbuybox.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.research.amazonbuybox.model.Experiment;
import org.research.amazonbuybox.model.ProductOffer;

public class ProductOfferCsvWriter {

    private static final List<String> HEADER = List.of(
            "extraction_timestamp",
            "experiment_id",
            "experiment_name",
            "product_url",
            "product_name",
            "buy_box",
            "visibility_order",
            "condition",
            "sold_by",
            "shipped_by",
            "review_count",
            "positive_review_percentage",
            "unit_price",
            "minimum_quantity",
            "sold_product_price",
            "shipping_type",
            "shipping_price",
            "total_price",
            "unit_price_difference",
            "shipping_price_difference",
            "total_price_difference",
            "min_delivery_days",
            "max_delivery_days",
            "shipping_window_days",
            "min_fast_delivery_days",
            "max_fast_delivery_days",
            "fast_shipping_window_days",
            "delivery_text",
            "used_condition",
            "star_rating",
            "lowest_price_ratio",
            "fulfilled_by_amazon",
            "sold_by_amazon",
            "delivery_delta",
            "review_count_delta",
            "positive_review_delta",
            "html_file_path",
            "screenshot_file_path");

    public void appendOffers(Path outputFile, Experiment experiment, String extractionTimestamp,
            List<ProductOffer> offers) throws IOException {
        if (outputFile.getParent() != null) {
            Files.createDirectories(outputFile.getParent());
        }

        boolean writeHeader = !Files.exists(outputFile) || Files.size(outputFile) == 0;
        try (BufferedWriter writer = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            if (writeHeader) {
                writer.write(toCsvLine(HEADER));
                writer.newLine();
            }
            for (ProductOffer offer : offers) {
                writer.write(toCsvLine(row(experiment, extractionTimestamp, offer)));
                writer.newLine();
            }
        }
    }

    private List<String> row(Experiment experiment, String extractionTimestamp, ProductOffer offer) {
        return List.of(
                extractionTimestamp,
                String.valueOf(experiment.getExperimentId()),
                nullToEmpty(experiment.getName()),
                nullToEmpty(experiment.getUrl()),
                nullToEmpty(offer.getProductName()),
                String.valueOf(offer.getBuyBox()),
                String.valueOf(offer.getVisibilityOrder()),
                nullToEmpty(offer.getCondition()),
                nullToEmpty(offer.getSoldBy()),
                nullToEmpty(offer.getShippedBy()),
                String.valueOf(offer.getReviewCount()),
                String.valueOf(offer.getPositiveReviewPercentage()),
                String.valueOf(offer.getUnitPrice()),
                String.valueOf(offer.getMinimumQuantity()),
                String.valueOf(offer.getSoldProductPrice()),
                nullToEmpty(offer.getShippingType()),
                String.valueOf(offer.getShippingPrice()),
                String.valueOf(offer.getTotalPrice()),
                String.valueOf(offer.getUnitPriceDifference()),
                String.valueOf(offer.getShippingPriceDifference()),
                String.valueOf(offer.getTotalPriceDifference()),
                String.valueOf(offer.getMinDeliveryDays()),
                String.valueOf(offer.getMaxDeliveryDays()),
                String.valueOf(offer.getShippingWindowDays()),
                String.valueOf(offer.getMinFastDeliveryDays()),
                String.valueOf(offer.getMaxFastDeliveryDays()),
                String.valueOf(offer.getFastShippingWindowDays()),
                nullToEmpty(offer.getDeliveryText()),
                nullToEmpty(offer.getUsedCondition()),
                String.valueOf(offer.getStarRating()),
                String.valueOf(offer.getLowestPriceRatio()),
                String.valueOf(offer.isFulfilledByAmazon()),
                String.valueOf(offer.isSoldByAmazon()),
                String.valueOf(offer.getDeliveryDelta()),
                String.valueOf(offer.getReviewCountDelta()),
                String.valueOf(offer.getPositiveReviewDelta()),
                nullToEmpty(experiment.getHtmlFilePath()),
                nullToEmpty(experiment.getScreenshotFilePath()));
    }

    private String toCsvLine(List<String> values) {
        return values.stream().map(this::escape).collect(Collectors.joining(","));
    }

    private String escape(String value) {
        String normalizedValue = nullToEmpty(value);
        if (Stream.of(",", "\"", "\n", "\r").anyMatch(normalizedValue::contains)) {
            return "\"" + normalizedValue.replace("\"", "\"\"") + "\"";
        }
        return normalizedValue;
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
