public class Shoe {
    private String model;
    private int quantity;
    private double price;

    public Shoe(String model, int quantity, double price) {
        this.model = model;
        this.quantity = quantity;
        this.price = price;
    }

    // Getter methods
    public String getModel() {
        return model;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}