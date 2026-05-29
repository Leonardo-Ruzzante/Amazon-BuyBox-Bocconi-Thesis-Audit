package org.research.amazonbuybox.model;

public class ProductOffer {

    private String productName;
    private double unitPrice;
    private String shippedBy = " ";
    private String soldBy = " ";
    private int reviewCount;
    private int positiveReviewPercentage;
    private String condition = " ";
    private String deliveryText;
    private String usedCondition = " ";
    private int minimumQuantity = 1;
    private String shippingType;
    private double shippingPrice;
    private int minDeliveryDays;
    private int maxDeliveryDays;
    private int minFastDeliveryDays;
    private int maxFastDeliveryDays;
    private double unitPriceDifference;
    private double shippingPriceDifference;
    private double totalPriceDifference;
    private int buyBox;
    private int visibilityOrder;
    private double starRating;
    private double lowestPriceRatio;
    private boolean fulfilledByAmazon;
    private boolean soldByAmazon;
    private int deliveryDelta;
    private int reviewCountDelta;
    private int positiveReviewDelta;

    public double getSoldProductPrice() {
        return unitPrice * minimumQuantity;
    }

    public double getTotalPrice() {
        return roundDown(Double.sum(getSoldProductPrice(), shippingPrice));
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getShippedBy() {
        return shippedBy;
    }

    public void setShippedBy(String shippedBy) {
        this.shippedBy = shippedBy;
        this.fulfilledByAmazon = "amazon".equalsIgnoreCase(shippedBy);
    }

    public String getSoldBy() {
        return soldBy;
    }

    public void setSoldBy(String soldBy) {
        this.soldBy = soldBy;
        this.soldByAmazon = "amazon".equalsIgnoreCase(soldBy);
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public int getPositiveReviewPercentage() {
        return positiveReviewPercentage;
    }

    public void setPositiveReviewPercentage(int positiveReviewPercentage) {
        this.positiveReviewPercentage = positiveReviewPercentage;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDeliveryText() {
        return deliveryText;
    }

    public void setDeliveryText(String deliveryText) {
        this.deliveryText = deliveryText;
    }

    public String getUsedCondition() {
        return usedCondition;
    }

    public void setUsedCondition(String usedCondition) {
        this.usedCondition = usedCondition;
    }

    public int getMinimumQuantity() {
        return minimumQuantity;
    }

    public void setMinimumQuantity(int minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public double getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(double shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public int getMinDeliveryDays() {
        return minDeliveryDays;
    }

    public void setMinDeliveryDays(int minDeliveryDays) {
        this.minDeliveryDays = minDeliveryDays;
    }

    public int getMaxDeliveryDays() {
        return maxDeliveryDays;
    }

    public void setMaxDeliveryDays(int maxDeliveryDays) {
        this.maxDeliveryDays = maxDeliveryDays;
    }

    public int getMinFastDeliveryDays() {
        return minFastDeliveryDays;
    }

    public void setMinFastDeliveryDays(int minFastDeliveryDays) {
        this.minFastDeliveryDays = minFastDeliveryDays;
    }

    public int getMaxFastDeliveryDays() {
        return maxFastDeliveryDays;
    }

    public void setMaxFastDeliveryDays(int maxFastDeliveryDays) {
        this.maxFastDeliveryDays = maxFastDeliveryDays;
    }

    public double getUnitPriceDifference() {
        return roundDown(unitPriceDifference);
    }

    public void setUnitPriceDifference(double unitPriceDifference) {
        this.unitPriceDifference = unitPriceDifference;
    }

    public double getShippingPriceDifference() {
        return roundDown(shippingPriceDifference);
    }

    public void setShippingPriceDifference(double shippingPriceDifference) {
        this.shippingPriceDifference = shippingPriceDifference;
    }

    public double getTotalPriceDifference() {
        return roundDown(totalPriceDifference);
    }

    public void setTotalPriceDifference(double totalPriceDifference) {
        this.totalPriceDifference = totalPriceDifference;
    }

    public int getShippingWindowDays() {
        return maxDeliveryDays - minDeliveryDays;
    }

    public int getFastShippingWindowDays() {
        return maxFastDeliveryDays - minFastDeliveryDays;
    }

    public int getBuyBox() {
        return buyBox;
    }

    public void setBuyBox(int buyBox) {
        this.buyBox = buyBox;
    }

    public int getVisibilityOrder() {
        return visibilityOrder;
    }

    public void setVisibilityOrder(int visibilityOrder) {
        this.visibilityOrder = visibilityOrder;
    }

    public double getStarRating() {
        return starRating;
    }

    public void setStarRating(double starRating) {
        this.starRating = starRating;
    }

    public double getLowestPriceRatio() {
        return roundDown(lowestPriceRatio);
    }

    public void setLowestPriceRatio(double lowestPriceRatio) {
        this.lowestPriceRatio = lowestPriceRatio;
    }

    public boolean isFulfilledByAmazon() {
        return fulfilledByAmazon;
    }

    public boolean isSoldByAmazon() {
        return soldByAmazon;
    }

    public int getDeliveryDelta() {
        return deliveryDelta;
    }

    public void setDeliveryDelta(int deliveryDelta) {
        this.deliveryDelta = deliveryDelta;
    }

    public int getReviewCountDelta() {
        return reviewCountDelta;
    }

    public void setReviewCountDelta(int reviewCountDelta) {
        this.reviewCountDelta = reviewCountDelta;
    }

    public int getPositiveReviewDelta() {
        return positiveReviewDelta;
    }

    public void setPositiveReviewDelta(int positiveReviewDelta) {
        this.positiveReviewDelta = positiveReviewDelta;
    }

    @Override
    public String toString() {
        return buyBox + ";" + visibilityOrder + ";" + condition + ";" + soldBy + ";" + shippedBy + ";"
                + reviewCount + ";" + positiveReviewPercentage + ";" + unitPrice + ";" + minimumQuantity + ";"
                + getSoldProductPrice() + ";" + shippingType + ";" + shippingPrice + ";" + getTotalPrice() + ";"
                + getUnitPriceDifference() + ";" + getShippingPriceDifference() + ";" + getTotalPriceDifference()
                + ";" + minDeliveryDays + ";" + maxDeliveryDays + ";" + getShippingWindowDays() + ";"
                + minFastDeliveryDays + ";" + maxFastDeliveryDays + ";" + getFastShippingWindowDays() + ";"
                + deliveryText + ";" + usedCondition;
    }

    private double roundDown(double value) {
        return Math.floor(value * 100) / 100;
    }
}
