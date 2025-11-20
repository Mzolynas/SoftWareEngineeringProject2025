import java.util.List;
import java.util.ArrayList;

public class InventoryService {
    // 模拟数据库/库存数据
    private static List<Shoe> inventory = new ArrayList<>();

    static {
        // 初始化一些模拟库存数据
        inventory.add(new Shoe("Air Max 90", 50, 120.00));
        inventory.add(new Shoe("Ultraboost 22", 30, 150.00));
        inventory.add(new Shoe("Jordan 1", 10, 180.00));
    }

    /**
     * 获取所有库存鞋子的列表
     */
    public static List<Shoe> getAllShoes() {
        return inventory;
    }
    
    /**
     * 新增方法：添加库存 (对应 EP2-S3)
     */
    public static void addItem(Shoe newShoe) {
        // 在实际应用中，这里应该有业务逻辑来检查重复、更新数据库等
        inventory.add(newShoe);
    }
}