package pro.quicksense.entity.payment;

import com.alibaba.fastjson.JSONObject;

/**
 * The instance of this object will be parsed to a json object,
 * and then be passed to the request for creating an Airwallex payment link.
 */
public class AirwallexPaymentLinkRequest {
    /**
     * A set of key-value pairs that you can attach to the PaymentLink for storing additional information.
     */
    private JSONObject metadata;

    /**
     * The payment amount. For Fixed pricing only.
     */
    private double amount;

    /**
     * The payment currency. For Fixed pricing only.
     */
    private String currency;

    /**
     * The title of the payment link that is displayed in the payment checkout page.
     */
    private String description;

    /**
     * Specifies whether the payment link can be used once or multiple times
     */
    private boolean reusable = false;

    /**
     * The title of the payment link that is displayed in the payment checkout page.
     */
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isReusable() {
        return reusable;
    }

    public void setReusable(boolean reusable) {
        this.reusable = reusable;
    }

    public JSONObject getMetadata() {
        return metadata;
    }

    public void setMetadata(JSONObject metadata) {
        this.metadata = metadata;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AirwallexPaymentLinkRequest(JSONObject metadata, double amount, String currency, String description, boolean reusable, String title) {
        this.metadata = metadata;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.reusable = reusable;
        this.title = title;
    }

    public AirwallexPaymentLinkRequest() {
    }
}
