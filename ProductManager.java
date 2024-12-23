import java.sql.*;

public class ProductManager {

    // Create a new product
    public void addProduct(String name, String category, int quantity, double price) {
        String query = "INSERT INTO products (name, category, quantity, price) VALUES (?, ?, ?, ?)";
        DatabaseConnection.executeUpdate(query, name, category, quantity, price);
        System.out.println("Product added successfully!");
    }

    // Retrieve all products
    public void listProducts() {
        String query = "SELECT * FROM products";
        try (ResultSet rs = DatabaseConnection.executeQuery(query)) {
            while (rs != null && rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Name: " + rs.getString("name") +
                        ", Category: " + rs.getString("category") +
                        ", Quantity: " + rs.getInt("quantity") +
                        ", Price: " + rs.getDouble("price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update product quantity
    public void updateProductQuantity(int id, int newQuantity) {
        String query = "UPDATE products SET quantity = ? WHERE id = ?";
        DatabaseConnection.executeUpdate(query, newQuantity, id);
        System.out.println("Product quantity updated successfully!");
    }

    // Delete a product
    public void deleteProduct(int id) {
        String query = "DELETE FROM products WHERE id = ?";
        DatabaseConnection.executeUpdate(query, id);
        System.out.println("Product deleted successfully!");
    }

    // Check for products with low stock
public void checkLowStock() {
    String query = "SELECT * FROM products WHERE quantity <= low_stock_threshold";
    try (ResultSet rs = DatabaseConnection.executeQuery(query)) {
        System.out.println("Low Stock Products:");
        boolean hasLowStock = false; // Track if there are any low stock products
        while (rs != null && rs.next()) {
            hasLowStock = true;
            System.out.println("ID: " + rs.getInt("id") +
                    ", Name: " + rs.getString("name") +
                    ", Category: " + rs.getString("category") +
                    ", Quantity: " + rs.getInt("quantity") +
                    ", Price: " + rs.getDouble("price"));
        }
        if (!hasLowStock) {
            System.out.println("All products have sufficient stock!");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    //Predict the Demand of stock
    public void displayPredictedDemand() {
        String query = """
            SELECT 
                p.id AS product_id,
                p.name AS product_name,
                p.quantity AS current_stock,
                ROUND(SUM(s.quantity_sold) / COUNT(DISTINCT DATE(s.sale_date)) * 1.1, 2) AS predicted_demand_with_buffer
            FROM 
                products p
            JOIN 
                sales s ON p.id = s.product_id
            GROUP BY 
                p.id, p.name, p.quantity
        """;
    
        try (ResultSet rs = DatabaseConnection.executeQuery(query)) {
            System.out.println("Predicted Demand with Buffer:");
            while (rs != null && rs.next()) {
                int productId = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                int currentStock = rs.getInt("current_stock");
                double predictedDemand = rs.getDouble("predicted_demand_with_buffer");
    
                System.out.printf("Product: %s (ID: %d)%n", productName, productId);
                System.out.printf("Current Stock: %d, Predicted Demand (with Buffer): %.2f%n", currentStock, predictedDemand);
                System.out.println(currentStock < predictedDemand
                    ? "-> Action Required: Restock needed!"
                    : "-> Stock Level Sufficient.\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
        // Method to check low sales products based on average daily sales in the last 30 days
    public void lowSalesAlert() {
        String query = "SELECT p.id AS product_id, p.name AS product_name, p.category AS product_category, " +
                       "IFNULL(SUM(s.quantity_sold), 0) AS total_sales, " +
                       "(IFNULL(SUM(s.quantity_sold), 0) / COUNT(DISTINCT DATE(s.sale_date))) AS avg_daily_sales, " +
                       "CASE " +
                       "   WHEN (IFNULL(SUM(s.quantity_sold), 0) / COUNT(DISTINCT DATE(s.sale_date))) < 5 " +
                       "   THEN 'Low Sales' " +
                       "   ELSE 'Normal Sales' " +
                       "END AS sales_status " +
                       "FROM products p " +
                       "LEFT JOIN sales s " +
                       "   ON p.id = s.product_id " +
                       "   AND s.sale_date >= CURDATE() - INTERVAL 30 DAY " +
                       "GROUP BY p.id, p.name, p.category " +
                       "HAVING avg_daily_sales < 5";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("Low Sales Products in the Last 30 Days:");

            while (rs.next()) {
                int productId = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                String category = rs.getString("product_category");
                int totalSales = rs.getInt("total_sales");
                double avgDailySales = rs.getDouble("avg_daily_sales");
                String salesStatus = rs.getString("sales_status");

                System.out.println("ID: " + productId +
                                   ", Name: " + productName +
                                   ", Category: " + category +
                                   ", Total Sales: " + totalSales +
                                   ", Avg Daily Sales: " + avgDailySales +
                                   ", Sales Status: " + salesStatus);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

