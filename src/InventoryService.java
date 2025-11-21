import java.util.ArrayList;
import java.util.List;

public class InventoryService {

    private static List<Shoe> inventory = new ArrayList<>();

    static {
        inventory.add(new Shoe("Air Max 90", 50, 120.0));
        inventory.add(new Shoe("Ultraboost 22", 30, 150.0));
        inventory.add(new Shoe("Jordan 1", 10, 180.0));
    }

    public static List<Shoe> getAllShoes() {
        return inventory;
    }

    public static void addItem(Shoe newShoe) {
        inventory.add(newShoe);
    }

    public static void updateItem(int index, String model, int quantity, double price) {
        if (index >= 0 && index < inventory.size()) {
            inventory.set(index, new Shoe(model, quantity, price));
        }
    }
}
