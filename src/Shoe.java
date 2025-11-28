public class Shoe {
    private int id;         // database primary key
    private String model;
    private int quantity;
    private double price;

    public Shoe(int id, String model, int quantity, double price) {
        this.id = id;
        this.model = model;
        this.quantity = quantity;
        this.price = price;
    }

    // constructor for new items (id not known yet)
    public Shoe(String model, int quantity, double price) {
        this(0, model, quantity, price);
    }

    public int getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setId(int id) {
        this.id = id;
    }
}
