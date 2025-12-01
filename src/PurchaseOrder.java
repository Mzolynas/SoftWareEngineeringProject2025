public class PurchaseOrder {
    private int id;
    private String model;
    private int quantity;
    private String status;

    // constructor used when reading from DB or creating with known id & status
    public PurchaseOrder(int id, String model, int quantity, String status) {
        this.id = id;
        this.model = model;
        this.quantity = quantity;
        this.status = status;
    }

    // constructor used when creating a new order before insert (id unknown yet)
    public PurchaseOrder(String model, int quantity) {
        this(0, model, quantity, "Pending");
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}