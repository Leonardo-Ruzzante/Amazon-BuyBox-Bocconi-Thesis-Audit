package org.research.amazonbuybox.extraction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.research.amazonbuybox.config.ApplicationSettings;
import org.research.amazonbuybox.model.Experiment;
import org.research.amazonbuybox.model.ProductOffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductDataExtractor {

    private static final List<String> FREE_SHIPPING_MARKERS = Arrays.asList(
            "gratuita", "gratis", "free shipping", "spedizione gratuita");

    private static final Pattern EURO_PRICE_PATTERN = Pattern.compile(
            "(?:EUR|€)?\\s*([0-9]{1,3}(?:\\.[0-9]{3})*(?:,[0-9]{2})|[0-9]+(?:\\.[0-9]{2}))");

    private static final Pattern INTEGER_PATTERN = Pattern.compile("[0-9]+");
    private static final Pattern PERCENTAGE_PATTERN = Pattern.compile("([0-9]{1,3})\\s*%");
    private static final Pattern STAR_RATING_PATTERN = Pattern.compile("([0-5](?:[,.][0-9])?)\\s*(?:su|out of)?\\s*5");
    private static final Pattern DELIVERY_DATE_PATTERN = Pattern.compile("[0-9]+(?:\\s*-\\s*[0-9]+)?\\s+[a-z]{3}", Pattern.CASE_INSENSITIVE);

    private final Logger logger = LoggerFactory.getLogger(ProductDataExtractor.class);
    private final SelectorConfigurationLoader selectorConfigurationLoader;
    private Document document;

    public ProductDataExtractor() throws IOException {
        selectorConfigurationLoader = new SelectorConfigurationLoader();
    }

    public LinkedList<ProductOffer> extractProductOffers(Experiment experiment, String extractionTimestamp)
            throws IOException {

        Path htmlFile = resolveHtmlFile(experiment, extractionTimestamp);
        if (htmlFile == null || !Files.exists(htmlFile)) {
            logger.warn("No local HTML file found for timestamp {}", extractionTimestamp);
            return new LinkedList<>();
        }

        String html = Files.readString(htmlFile);
        document = Jsoup.parse(html);

        LinkedList<ProductOffer> offers = extractOffers(extractionTimestamp);
        enrichDerivedMetrics(offers);

        logger.info("Extracted {} offers from stored HTML file {}", offers.size(), htmlFile);
        return offers;
    }

    private Path resolveHtmlFile(Experiment experiment, String extractionTimestamp) throws IOException {
        if (!isBlank(experiment.getHtmlFilePath())) {
            Path explicitPath = Paths.get(experiment.getHtmlFilePath());
            if (Files.exists(explicitPath)) {
                return explicitPath;
            }
        }

        if (!Files.exists(ApplicationSettings.HTML_OUTPUT_DIRECTORY)) {
            return null;
        }

        try (Stream<Path> files = Files.list(ApplicationSettings.HTML_OUTPUT_DIRECTORY)) {
            return files
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().contains(extractionTimestamp))
                    .findFirst()
                    .orElse(null);
        }
    }

    private LinkedList<ProductOffer> extractOffers(String extractionTimestamp) {
        LinkedList<ProductOffer> offers = new LinkedList<>();

        ProductOffer buyBoxOffer = extractBuyBoxOffer(extractionTimestamp);
        if (buyBoxOffer.getUnitPrice() > 0 || !isBlank(buyBoxOffer.getSoldBy())) {
            offers.add(buyBoxOffer);
        }

        Elements offerElements = selectFromDocument("offer.container");
        int visibilityOrder = 1;
        for (Element offerElement : offerElements) {
            ProductOffer offer = extractSellerOffer(offerElement, visibilityOrder, extractionTimestamp);
            if (offer.getUnitPrice() > 0 || !isBlank(offer.getSoldBy()) || !isBlank(offer.getDeliveryText())) {
                offers.add(offer);
                visibilityOrder++;
            }
        }

        if (offers.isEmpty()) {
            logger.warn("No offer block could be extracted from the HTML page");
        }
        return offers;
    }

    private ProductOffer extractBuyBoxOffer(String extractionTimestamp) {
        Element container = firstFromDocument("buybox.container");
        if (container == null) {
            container = document.body();
        }

        ProductOffer offer = new ProductOffer();
        offer.setProductName(firstText(document, "product.title"));
        offer.setBuyBox(1);
        offer.setVisibilityOrder(0);
        offer.setCondition(defaultIfBlank(firstText(container, "buybox.condition"), "New"));
        offer.setSoldBy(normalizeSeller(defaultIfBlank(firstText(container, "buybox.seller"), inferSellerFromText(container.text()))));
        offer.setShippedBy(normalizeSeller(defaultIfBlank(firstText(container, "buybox.fulfillment"), inferFulfillmentFromText(container.text()))));
        offer.setDeliveryText(firstText(container, "buybox.delivery"));
        offer.setUnitPrice(parsePrice(defaultIfBlank(firstText(container, "buybox.price"), container.text())));
        offer.setShippingType(resolveShippingType(offer.getDeliveryText()));
        offer.setShippingPrice(resolveShippingPrice(offer.getShippingType(), offer.getDeliveryText()));
        offer.setMinimumQuantity(1);
        offer.setReviewCount(parseReviewCount(defaultIfBlank(firstText(container, "buybox.reviewCount"), container.text())));
        offer.setPositiveReviewPercentage(parsePositiveReviewPercentage(container.text()));
        offer.setStarRating(parseStarRating(defaultIfBlank(firstText(container, "buybox.rating"), container.html())));
        applyDeliveryDates(offer, extractionTimestamp);
        return offer;
    }

    private ProductOffer extractSellerOffer(Element container, int visibilityOrder, String extractionTimestamp) {
        ProductOffer offer = new ProductOffer();
        offer.setProductName(firstText(document, "product.title"));
        offer.setBuyBox(0);
        offer.setVisibilityOrder(visibilityOrder);
        offer.setCondition(defaultIfBlank(firstText(container, "offer.condition"), "New"));
        offer.setSoldBy(normalizeSeller(defaultIfBlank(firstText(container, "offer.seller"), inferSellerFromText(container.text()))));
        offer.setShippedBy(normalizeSeller(defaultIfBlank(firstText(container, "offer.fulfillment"), inferFulfillmentFromText(container.text()))));
        offer.setDeliveryText(firstText(container, "offer.delivery"));
        offer.setUnitPrice(parsePrice(defaultIfBlank(firstTextWithIndex(container, "offer.price", visibilityOrder), container.text())));
        offer.setShippingType(resolveShippingType(offer.getDeliveryText()));
        offer.setShippingPrice(resolveShippingPrice(offer.getShippingType(), offer.getDeliveryText()));
        offer.setMinimumQuantity(parseMinimumQuantity(defaultIfBlank(firstText(container, "offer.minimumQuantity"), container.text())));
        offer.setReviewCount(parseReviewCount(defaultIfBlank(firstText(container, "offer.reviewCount"), container.text())));
        offer.setPositiveReviewPercentage(parsePositiveReviewPercentage(container.text()));
        offer.setStarRating(parseStarRating(defaultIfBlank(firstText(container, "offer.rating"), container.html())));
        applyDeliveryDates(offer, extractionTimestamp);
        return offer;
    }

    private Elements selectFromDocument(String key) {
        Elements result = new Elements();
        for (String selector : selectorConfigurationLoader.getSelectors(key)) {
            result.addAll(document.select(selector));
        }
        return result;
    }

    private Element firstFromDocument(String key) {
        for (String selector : selectorConfigurationLoader.getSelectors(key)) {
            Element element = document.selectFirst(selector);
            if (element != null) {
                return element;
            }
        }
        return null;
    }

    private String firstText(Document source, String key) {
        for (String selector : selectorConfigurationLoader.getSelectors(key)) {
            Element element = source.selectFirst(selector);
            if (element != null && !isBlank(element.text())) {
                return element.text().trim();
            }
        }
        return "";
    }

    private String firstText(Element source, String key) {
        for (String selector : selectorConfigurationLoader.getSelectors(key)) {
            Element element = source.selectFirst(selector);
            if (element != null && !isBlank(element.text())) {
                return element.text().trim();
            }
        }
        return "";
    }

    private String firstTextWithIndex(Element source, String key, int index) {
        for (String selector : selectorConfigurationLoader.getSelectors(key)) {
            String indexedSelector = selector.replace("INDEX", String.valueOf(index));
            Element element = source.selectFirst(indexedSelector);
            if (element != null && !isBlank(element.text())) {
                return element.text().trim();
            }
        }
        return "";
    }

    private void enrichDerivedMetrics(LinkedList<ProductOffer> offers) {
        if (offers.isEmpty()) {
            return;
        }

        double minimumUnitPrice = minimumUnitPrice(offers);
        double minimumShippingPrice = minimumShippingPrice(offers);
        double minimumTotalPrice = minimumTotalPrice(offers);
        int fastestDeliveryDays = fastestDeliveryDays(offers);
        int maximumReviewCount = maximumReviewCount(offers);
        int maximumPositiveReviewPercentage = maximumPositiveReviewPercentage(offers);

        for (ProductOffer offer : offers) {
            offer.setUnitPriceDifference(offer.getUnitPrice() - minimumUnitPrice);
            offer.setShippingPriceDifference(offer.getShippingPrice() - minimumShippingPrice);
            offer.setTotalPriceDifference(offer.getTotalPrice() - minimumTotalPrice);
            offer.setReviewCountDelta(maximumReviewCount - offer.getReviewCount());
            offer.setPositiveReviewDelta(maximumPositiveReviewPercentage - offer.getPositiveReviewPercentage());

            int offerDeliveryDays = preferredDeliveryDays(offer);
            if (offerDeliveryDays > 0 && fastestDeliveryDays > 0) {
                offer.setDeliveryDelta(offerDeliveryDays - fastestDeliveryDays);
            }

            if (minimumTotalPrice > 0) {
                offer.setLowestPriceRatio(offer.getTotalPrice() / minimumTotalPrice);
            }
        }
    }

    private double minimumUnitPrice(LinkedList<ProductOffer> offers) {
        double minimum = Double.MAX_VALUE;
        for (ProductOffer offer : offers) {
            if (offer.getUnitPrice() > 0 && offer.getUnitPrice() < minimum) {
                minimum = offer.getUnitPrice();
            }
        }
        return minimum == Double.MAX_VALUE ? 0 : minimum;
    }

    private double minimumShippingPrice(LinkedList<ProductOffer> offers) {
        double minimum = Double.MAX_VALUE;
        for (ProductOffer offer : offers) {
            if (offer.getShippingPrice() >= 0 && offer.getShippingPrice() < minimum) {
                minimum = offer.getShippingPrice();
            }
        }
        return minimum == Double.MAX_VALUE ? 0 : minimum;
    }

    private double minimumTotalPrice(LinkedList<ProductOffer> offers) {
        double minimum = Double.MAX_VALUE;
        for (ProductOffer offer : offers) {
            if (offer.getTotalPrice() > 0 && offer.getTotalPrice() < minimum) {
                minimum = offer.getTotalPrice();
            }
        }
        return minimum == Double.MAX_VALUE ? 0 : minimum;
    }

    private int fastestDeliveryDays(LinkedList<ProductOffer> offers) {
        int minimum = Integer.MAX_VALUE;
        for (ProductOffer offer : offers) {
            int deliveryDays = preferredDeliveryDays(offer);
            if (deliveryDays > 0 && deliveryDays < minimum) {
                minimum = deliveryDays;
            }
        }
        return minimum == Integer.MAX_VALUE ? 0 : minimum;
    }

    private int preferredDeliveryDays(ProductOffer offer) {
        if (offer.getMinFastDeliveryDays() > 0) {
            return offer.getMinFastDeliveryDays();
        }
        return offer.getMinDeliveryDays();
    }

    private int maximumReviewCount(LinkedList<ProductOffer> offers) {
        int maximum = 0;
        for (ProductOffer offer : offers) {
            maximum = Math.max(maximum, offer.getReviewCount());
        }
        return maximum;
    }

    private int maximumPositiveReviewPercentage(LinkedList<ProductOffer> offers) {
        int maximum = 0;
        for (ProductOffer offer : offers) {
            maximum = Math.max(maximum, offer.getPositiveReviewPercentage());
        }
        return maximum;
    }

    private double parsePrice(String text) {
        if (isBlank(text)) {
            return 0.0;
        }
        Matcher matcher = EURO_PRICE_PATTERN.matcher(text.replace('\u00A0', ' '));
        if (!matcher.find()) {
            return 0.0;
        }
        String value = matcher.group(1).replace(".", "").replace(",", ".");
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ignored) {
            return 0.0;
        }
    }

    private int parseReviewCount(String text) {
        if (isBlank(text)) {
            return 0;
        }
        Matcher matcher = INTEGER_PATTERN.matcher(text.replace(".", ""));
        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group());
            } catch (NumberFormatException ignored) {
                return 0;
            }
        }
        return 0;
    }

    private int parsePositiveReviewPercentage(String text) {
        if (isBlank(text)) {
            return 0;
        }
        Matcher matcher = PERCENTAGE_PATTERN.matcher(text);
        if (matcher.find()) {
            try {
                int value = Integer.parseInt(matcher.group(1));
                return Math.min(value, 100);
            } catch (NumberFormatException ignored) {
                return 0;
            }
        }
        return 0;
    }

    private double parseStarRating(String text) {
        if (isBlank(text)) {
            return 0.0;
        }
        Matcher matcher = STAR_RATING_PATTERN.matcher(text.replace(',', '.'));
        if (matcher.find()) {
            try {
                return Double.parseDouble(matcher.group(1));
            } catch (NumberFormatException ignored) {
                return 0.0;
            }
        }
        return 0.0;
    }

    private int parseMinimumQuantity(String text) {
        if (isBlank(text)) {
            return 1;
        }
        String lowerText = text.toLowerCase();
        if (!lowerText.contains("minimum") && !lowerText.contains("minima") && !lowerText.contains("minimo")) {
            return 1;
        }
        Matcher matcher = INTEGER_PATTERN.matcher(text);
        if (matcher.find()) {
            try {
                return Math.max(1, Integer.parseInt(matcher.group()));
            } catch (NumberFormatException ignored) {
                return 1;
            }
        }
        return 1;
    }

    private String resolveShippingType(String shippingText) {
        if (isBlank(shippingText)) {
            return "Unknown";
        }
        String normalized = shippingText.toLowerCase();
        for (String marker : FREE_SHIPPING_MARKERS) {
            if (normalized.contains(marker)) {
                return "Free";
            }
        }
        if (parsePrice(shippingText) > 0) {
            return "Paid";
        }
        return "Unknown";
    }

    private double resolveShippingPrice(String shippingType, String shippingText) {
        if ("Free".equalsIgnoreCase(shippingType)) {
            return 0.0;
        }
        return parsePrice(shippingText);
    }

    private String inferSellerFromText(String text) {
        return textAfterLabel(text, Arrays.asList("venduto da", "sold by", "seller"));
    }

    private String inferFulfillmentFromText(String text) {
        return textAfterLabel(text, Arrays.asList("spedito da", "ships from", "dispatched from"));
    }

    private String textAfterLabel(String text, List<String> labels) {
        if (isBlank(text)) {
            return "";
        }
        String normalizedText = text.replace('\n', ' ').replaceAll("\\s+", " ").trim();
        String lowerText = normalizedText.toLowerCase();
        for (String label : labels) {
            int index = lowerText.indexOf(label);
            if (index >= 0) {
                int start = index + label.length();
                String fragment = normalizedText.substring(start).replaceFirst("^[:\\s]+", "");
                String[] stopLabels = {" spedito da ", " venduto da ", " ships from ", " sold by ", " seller "};
                int stopIndex = fragment.length();
                String lowerFragment = fragment.toLowerCase();
                for (String stopLabel : stopLabels) {
                    int candidateIndex = lowerFragment.indexOf(stopLabel);
                    if (candidateIndex > 0 && candidateIndex < stopIndex) {
                        stopIndex = candidateIndex;
                    }
                }
                return fragment.substring(0, stopIndex).trim();
            }
        }
        return "";
    }

    private String normalizeSeller(String seller) {
        if (isBlank(seller)) {
            return "";
        }
        String normalized = seller.replace('\n', ' ').replaceAll("\\s+", " ").trim();
        if (normalized.toLowerCase().contains("amazon")) {
            return "Amazon";
        }
        return normalized;
    }

    private void applyDeliveryDates(ProductOffer offer, String extractionTimestamp) {
        String deliveryText = offer.getDeliveryText();
        if (isBlank(deliveryText) || extractionTimestamp.length() < 10) {
            return;
        }

        String lowerText = deliveryText.toLowerCase();
        if (lowerText.contains("domani") || lowerText.contains("tomorrow")) {
            offer.setMinDeliveryDays(1);
            offer.setMaxDeliveryDays(1);
            return;
        }

        Calendar extractionDate = parseExtractionDate(extractionTimestamp);
        if (extractionDate == null) {
            return;
        }

        ArrayList<Integer> dayOffsets = new ArrayList<>();
        Matcher matcher = DELIVERY_DATE_PATTERN.matcher(lowerText);
        while (matcher.find()) {
            int offset = parseDeliveryDateOffset(matcher.group(), extractionDate);
            if (offset >= 0) {
                dayOffsets.add(offset);
            }
        }

        if (dayOffsets.isEmpty()) {
            return;
        }

        int minimum = Integer.MAX_VALUE;
        int maximum = 0;
        for (Integer offset : dayOffsets) {
            minimum = Math.min(minimum, offset);
            maximum = Math.max(maximum, offset);
        }
        offer.setMinDeliveryDays(minimum);
        offer.setMaxDeliveryDays(maximum);
    }

    private Calendar parseExtractionDate(String extractionTimestamp) {
        try {
            int year = Integer.parseInt(extractionTimestamp.substring(0, 4));
            int month = Integer.parseInt(extractionTimestamp.substring(5, 7)) - 1;
            int day = Integer.parseInt(extractionTimestamp.substring(8, 10));
            GregorianCalendar calendar = new GregorianCalendar(year, month, day);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar;
        } catch (RuntimeException ignored) {
            return null;
        }
    }

    private int parseDeliveryDateOffset(String dateText, Calendar extractionDate) {
        Matcher monthMatcher = Pattern.compile("[a-z]{3}", Pattern.CASE_INSENSITIVE).matcher(dateText);
        if (!monthMatcher.find()) {
            return -1;
        }

        int month = mapMonth(monthMatcher.group().toLowerCase());
        if (month < 0) {
            return -1;
        }

        Matcher dayMatcher = INTEGER_PATTERN.matcher(dateText);
        if (!dayMatcher.find()) {
            return -1;
        }
        int day = Integer.parseInt(dayMatcher.group());
        int year = extractionDate.get(Calendar.YEAR);

        Calendar deliveryDate = new GregorianCalendar(year, month, day);
        deliveryDate.set(Calendar.HOUR_OF_DAY, 0);
        deliveryDate.set(Calendar.MINUTE, 0);
        deliveryDate.set(Calendar.SECOND, 0);
        deliveryDate.set(Calendar.MILLISECOND, 0);

        if (deliveryDate.before(extractionDate)) {
            deliveryDate.add(Calendar.YEAR, 1);
        }

        long difference = deliveryDate.getTimeInMillis() - extractionDate.getTimeInMillis();
        return (int) (difference / (1000L * 60L * 60L * 24L));
    }

    private int mapMonth(String month) {
        switch (month) {
        case "gen":
        case "jan":
            return 0;
        case "feb":
            return 1;
        case "mar":
            return 2;
        case "apr":
            return 3;
        case "mag":
        case "may":
            return 4;
        case "giu":
        case "jun":
            return 5;
        case "lug":
        case "jul":
            return 6;
        case "ago":
        case "aug":
            return 7;
        case "set":
        case "sep":
            return 8;
        case "ott":
        case "oct":
            return 9;
        case "nov":
            return 10;
        case "dic":
        case "dec":
            return 11;
        default:
            return -1;
        }
    }

    private String defaultIfBlank(String value, String defaultValue) {
        return isBlank(value) ? defaultValue : value;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
