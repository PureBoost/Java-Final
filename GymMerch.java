// Gym merchandise record for products sold at the gym.
public class GymMerch {
    private int merchId;
    private String merchName;
    private String merchType;
    private double merchPrice;
    private int quantityInStock;

    public GymMerch() {
    }

    public GymMerch(int merchId, String merchName, String merchType, double merchPrice, int quantityInStock) {
        this.merchId = merchId;
        this.merchName = merchName;
        this.merchType = merchType;
        this.merchPrice = merchPrice;
        this.quantityInStock = quantityInStock;
    }

    public int getMerchId() {
        return merchId;
    }

    public void setMerchId(int merchId) {
        this.merchId = merchId;
    }

    public String getMerchName() {
        return merchName;
    }

    public void setMerchName(String merchName) {
        this.merchName = merchName;
    }

    public String getMerchType() {
        return merchType;
    }

    public void setMerchType(String merchType) {
        this.merchType = merchType;
    }

    public double getMerchPrice() {
        return merchPrice;
    }

    public void setMerchPrice(double merchPrice) {
        this.merchPrice = merchPrice;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }
}
