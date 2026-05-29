package org.research.amazonbuybox.extraction;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SelectorConfigurationLoader {

    private static final String RESOURCE_NAME = "selectors.properties";
    private final Properties selectors = new Properties();

    public SelectorConfigurationLoader() {
        loadDefaults();
        loadOverridesFromClasspath();
    }

    public String[] getSelectors(String key) {
        String value = selectors.getProperty(key, "");
        if (value.trim().isEmpty()) {
            return new String[0];
        }
        return value.split("\\|\\|");
    }

    private void loadOverridesFromClasspath() {
        try (InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(RESOURCE_NAME)) {
            if (inputStream != null) {
                selectors.load(inputStream);
            }
        } catch (IOException ignored) {
        }
    }

    private void loadDefaults() {
        selectors.setProperty("product.title", "#productTitle");
        selectors.setProperty("buybox.container", "#desktop_buybox||#buybox||#apex_desktop||body");
        selectors.setProperty("buybox.price", "#corePrice_feature_div .a-offscreen||#apex_desktop .a-price .a-offscreen||.a-price .a-offscreen");
        selectors.setProperty("buybox.seller", "#sellerProfileTriggerId||#merchant-info||#tabular-buybox .tabular-buybox-text");
        selectors.setProperty("buybox.fulfillment", "#tabular-buybox .tabular-buybox-text||#merchant-info");
        selectors.setProperty("buybox.condition", "#newAccordionRow .a-text-bold||#usedAccordionRow .a-text-bold||#availability");
        selectors.setProperty("buybox.delivery", "#unified-delivery-message-||#deliveryBlockMessage||#mir-layout-DELIVERY_BLOCK-slot-PRIMARY_DELIVERY_MESSAGE_LARGE||[id*=delivery]");
        selectors.setProperty("buybox.rating", "#acrPopover||#acrCustomerReviewText||i.a-icon-star span.a-icon-alt");
        selectors.setProperty("buybox.reviewCount", "#acrCustomerReviewText");

        selectors.setProperty("offer.container", "#aod-offer||.aod-offer||[id=aod-offer]");
        selectors.setProperty("offer.price", "#aod-price-INDEX .a-offscreen||.a-price .a-offscreen||.a-price-whole");
        selectors.setProperty("offer.seller", "#aod-offer-soldBy a||#aod-offer-soldBy .a-col-right||[id*=soldBy] a||[id*=soldBy] .a-col-right");
        selectors.setProperty("offer.fulfillment", "#aod-offer-shipsFrom .a-col-right||[id*=shipsFrom] .a-col-right||[id*=shipsFrom]");
        selectors.setProperty("offer.condition", "#aod-offer-heading||.aod-offer-heading||h5");
        selectors.setProperty("offer.delivery", "#mir-layout-DELIVERY_BLOCK||[id*=delivery]||[class*=delivery]");
        selectors.setProperty("offer.rating", "#aod-offer-seller-rating||[id*=seller-rating]||i.a-icon-star span.a-icon-alt");
        selectors.setProperty("offer.reviewCount", "#aod-offer-seller-rating||[id*=seller-rating]");
        selectors.setProperty("offer.minimumQuantity", "[id*=quantity]||[class*=quantity]");
    }
}
