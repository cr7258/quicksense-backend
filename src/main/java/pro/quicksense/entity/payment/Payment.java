package pro.quicksense.entity.payment;

public class Payment {
    private String merchandiseId;
    private String currency;

    public Payment() {
    }

    public Payment(String merchandiseId, String currency) {
        this.merchandiseId = merchandiseId;
        this.currency = currency;
    }

    public String getMerchandiseId() {
        return merchandiseId;
    }

    public void setMerchandiseId(String merchandiseId) {
        this.merchandiseId = merchandiseId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
