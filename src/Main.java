import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Meal Management ---");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("0. Exit");
            System.out.print("Choice: ");

            int choice;
            try {
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, please enter a number.");
                sc.nextLine(); // clear scanner buffer
                continue;
            }

            if (choice == 1) {
                User.register(sc);
            } else if (choice == 2) {
                int userId = User.login(sc);
                if (userId != -1) {
                    showMenu(sc, userId);
                }
            } else if (choice == 0) {
                System.out.println("Exiting... Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice, try again.");
            }
        }

        sc.close();
    }

    public static void showMenu(Scanner sc, int userId) {
        while (true) {
            System.out.println("\n--- User Menu ---");
            System.out.println("1. Add Meal");
            System.out.println("2. Add Deposit");
            System.out.println("3. Add Bazar");
            System.out.println("4. Show Monthly Summary");
            System.out.println("0. Logout");
            System.out.print("Choice: ");

            int c;
            try {
                c = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, please enter a number.");
                sc.nextLine(); // clear scanner buffer
                continue;
            }

            switch (c) {
                case 1:
                    MealCount.addMeal(sc, userId);
                    break;
                case 2:
                    MealCash.deposit(sc, userId);
                    break;
                case 3:
                    Bazar.addBazar(sc, userId);
                    break;
                case 4:
                    MonthlySummary.show(userId);
                    break;
                case 0:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice, try again.");
            }
        }
    }
}
