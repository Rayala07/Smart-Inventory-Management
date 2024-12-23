public class TestConnection {
    public static void main(String[] args) {
        if (DatabaseConnection.getConnection() != null) {
            System.out.println("Connected to the database!");
        } else {
            System.out.println("Failed to connect to the database.");
        }
    }
}
