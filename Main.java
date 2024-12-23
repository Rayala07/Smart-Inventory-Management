import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ProductManager manager = new ProductManager();
        Scanner scanner = new Scanner(System.in);

        // 1. Ask user if they want to add a product or not
        System.out.println("Do you want to add a new product? (Yes/No): ");
        String choice = scanner.nextLine();

        // If user chooses Yes, prompt them to enter product details
        if (choice.equalsIgnoreCase("Yes")) {
            System.out.print("Enter product name: ");
            String name = scanner.nextLine(); // Get product name
            System.out.print("Enter product category: ");
            String category = scanner.nextLine(); // Get product category
            System.out.print("Enter product quantity: ");
            int quantity = scanner.nextInt(); // Get product quantity
            scanner.nextLine(); // Consume the leftover newline character
            System.out.print("Enter product price: ");
            double price = scanner.nextDouble(); // Get product price
            scanner.nextLine(); // Consume the leftover newline character

            // Add the product
            manager.addProduct(name, category, quantity, price);
        } else {
            System.out.println("No product added.");
        }

        // 2. List all products
        System.out.println("\nListing all products :");
        manager.listProducts();

        // 3. Ask user if they want to update the product quantity or not
        System.out.println("\nDo you want to update the quantity of a product? (Yes/No): ");
        choice = scanner.nextLine();

        if (choice.equalsIgnoreCase("Yes")) {
            System.out.print("Enter the product ID to update: ");
            int id = scanner.nextInt(); // Get product ID
            scanner.nextLine(); // Consume the leftover newline character
            System.out.print("Enter the new quantity: ");
            int newQuantity = scanner.nextInt(); // Get new quantity
            scanner.nextLine(); // Consume the leftover newline character
            manager.updateProductQuantity(id, newQuantity); // Update the product
        } else {
            System.out.println("No update made...");
        }

        // 4. List products after update (if any)
        System.out.println("\nAfter update (if any):");
        manager.listProducts();

        // 5. Ask user if they want to delete a product or not
        System.out.println("\nDo you want to delete a product? (Yes/No): ");
        choice = scanner.nextLine();

        if (choice.equalsIgnoreCase("Yes")) {
            System.out.print("Enter the product ID to delete: ");
            int deleteId = scanner.nextInt(); // Get product ID to delete
            scanner.nextLine(); // Consume the leftover newline character
            manager.deleteProduct(deleteId); // Delete the product

            // 6. List products after deletion
            System.out.println("\nAfter deletion (if any):");
            manager.listProducts();
        } else {
            System.out.println("No product deleted...");
        }

         // Check for low stock products
         System.out.println("\nChecking for low stock products :");
         manager.checkLowStock();

         // Predict Demand at last day of month
         if (DateUtils.isLastDayOfMonth()) {
            System.out.println("\nToday is the last day of the month!");
            System.out.println("Fetching Predicted Demand for the next 30 days...");
            manager.displayPredictedDemand();
        } else {
            System.out.println("\nNot the last day of the month.. \nSkipping demand prediction..\n");
        }
        

          // Ask user if they want to see low sales products
          System.out.println("Do you want to view low sales products in the last 30 days? (Yes/No): ");
          String userChoice = scanner.nextLine();
  
          if (userChoice.equalsIgnoreCase("Yes")) {
              // Call low sales alert
              manager.lowSalesAlert();
              System.out.println("These are the Low Sales. \n Thank You !");
          } else {
              System.out.println("EXITING... \n... Thank You ! ...");
          }


        scanner.close(); // Close the scanner to prevent resource leak
    }
}
