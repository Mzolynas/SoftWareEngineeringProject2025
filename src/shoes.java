public class shoes {
    private int id;
    private String name;
    private String brand;
    private double size;
    private String color;
    private double price;
    private int quantity;
    private String category;
    private String description;
    private String dateAdded;
    
    // Constructor for existing shoes
    public shoes(int id, String name, String brand, double size, String color, 
                double price, int quantity, String category, String description, String dateAdded) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.size = size;
        this.color = color;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.description = description;
        this.dateAdded = dateAdded;
    }
    
    // Constructor for new shoes (without ID)
    public shoes(String name, String brand, double size, String color, 
                double price, int quantity, String category, String description) {
        this.name = name;
        this.brand = brand;
        this.size = size;
        this.color = color;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.description = description;
    }
    
    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public double getSize() { return size; }
    public String getColor() { return color; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getDateAdded() { return dateAdded; }
    
    // For table display
    public Object[] toTableRow() {
        return new Object[]{
            id, 
            name, 
            brand, 
            size, 
            color, 
            String.format("$%.2f", price), 
            quantity, 
            category,
            description.length() > 30 ? description.substring(0, 30) + "..." : description
        };
    }
}