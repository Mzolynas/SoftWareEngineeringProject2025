public class App {
    public static void main(String[] args) {
        // Use MySQLDatabase NOT shoeDataBase
    	shoeDataBase.initializeDatabase();
        
        // Start the login page
        loginPage.createLoginPage();
    }
}