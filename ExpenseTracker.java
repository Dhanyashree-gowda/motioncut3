import java.io.*;
import java.util.*;

public class ExpenseTracker {
    private static final String FILE_NAME = "expenses.txt";
    private static final Map<String, List<Double>> expenses = new HashMap<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadExpenses();
        while (true) {
            System.out.println("\nPersonal Expense Tracker");
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. Delete Expense");
            System.out.println("4. View Summary by Category");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> addExpense();
                case 2 -> viewExpenses();
                case 3 -> deleteExpense();
                case 4 -> viewSummary();
                case 5 -> {
                    saveExpenses();
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option, please try again.");
            }
        }
    }

    private static void addExpense() {
        System.out.print("Enter category (e.g., Food, Transport): ");
        String category = scanner.nextLine().trim();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        expenses.computeIfAbsent(category, k -> new ArrayList<>()).add(amount);
        System.out.println("Expense added successfully.");
    }

    private static void viewExpenses() {
        System.out.println("\nExpenses:");
        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }
        for (String category : expenses.keySet()) {
            System.out.println("\nCategory: " + category);
            List<Double> categoryExpenses = expenses.get(category);
            for (int i = 0; i < categoryExpenses.size(); i++) {
                System.out.println("Expense " + (i + 1) + ": " + categoryExpenses.get(i));
            }
        }
    }

    private static void deleteExpense() {
        System.out.print("Enter category of expense to delete: ");
        String category = scanner.nextLine().trim();
        List<Double> categoryExpenses = expenses.get(category);

        if (categoryExpenses == null || categoryExpenses.isEmpty()) {
            System.out.println("No expenses found for this category.");
            return;
        }

        System.out.println("Select the expense number to delete:");
        for (int i = 0; i < categoryExpenses.size(); i++) {
            System.out.println((i + 1) + ". " + categoryExpenses.get(i));
        }

        int index = scanner.nextInt() - 1;
        scanner.nextLine(); // consume newline

        if (index >= 0 && index < categoryExpenses.size()) {
            categoryExpenses.remove(index);
            if (categoryExpenses.isEmpty()) {
                expenses.remove(category);
            }
            System.out.println("Expense deleted successfully.");
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private static void viewSummary() {
        System.out.println("\nExpense Summary:");
        double totalExpenses = 0;
        for (String category : expenses.keySet()) {
            List<Double> categoryExpenses = expenses.get(category);
            double categoryTotal = categoryExpenses.stream().mapToDouble(Double::doubleValue).sum();
            System.out.printf("Category: %s, Total: %.2f%n", category, categoryTotal);
            totalExpenses += categoryTotal;
        }
        System.out.printf("Overall Total Expenses: %.2f%n", totalExpenses);
    }

    private static void saveExpenses() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (String category : expenses.keySet()) {
                for (Double amount : expenses.get(category)) {
                    writer.println(category + "," + amount);
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving expenses.");
        }
    }

    private static void loadExpenses() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String category = parts[0];
                double amount = Double.parseDouble(parts[1]);
                expenses.computeIfAbsent(category, k -> new ArrayList<>()).add(amount);
            }
        } catch (IOException e) {
            System.out.println("No previous data found. Starting fresh.");
        }
    }
}
