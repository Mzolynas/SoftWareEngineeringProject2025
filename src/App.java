import javax.swing.UIManager;
import java.util.Locale;

public class App {
    public static void main(String[] args) {
        // 统一把对话框按钮改成英文
        Locale.setDefault(Locale.ENGLISH);
        UIManager.put("OptionPane.okButtonText", "OK");
        UIManager.put("OptionPane.cancelButtonText", "Cancel");

        loginPage.createLoginPage();
    }
}
