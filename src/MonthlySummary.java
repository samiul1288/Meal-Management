import java.sql.*;

public class MonthlySummary {
    public static void show(int userId) {
        int meals = MealCount.getTotalMeals(userId);
        double deposit = MealCash.getTotalDeposit(userId);
        double totalBazar = Bazar.getTotalBazar();
        int totalMealLimit = Bazar.getTotalMealLimit();

        double mealRate = (totalMealLimit > 0) ? totalBazar / totalMealLimit : 0;
        double cost = mealRate * meals;
        double balance = deposit - cost;

        System.out.println("Total Meals: " + meals);
        System.out.println("Total Deposit: " + deposit);
        System.out.println("Meal Rate: " + String.format("%.2f", mealRate));
        System.out.println("Meal Cost: " + String.format("%.2f", cost));
        System.out.println("Balance: " + String.format("%.2f", balance));

        if (totalMealLimit < getTotalMealsAllUsers()) {
            System.out.println("⚠️ Warning: Total meal limit (" + totalMealLimit +
                    ") is less than total meals consumed (" + getTotalMealsAllUsers() +
                    "). Please add new bazar.");
        }
    }

    public static int getTotalMealsAllUsers() {
        int total = 0;
        try (Connection con = Database.getConnection()) {
            String sql = "SELECT SUM(lunch + dinner) AS total FROM meals";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public static void showFinalSummaryForAllUsers() {
        try (Connection con = Database.getConnection()) {
            double totalBazar = Bazar.getTotalBazar();
            int totalMealLimit = Bazar.getTotalMealLimit();
            double mealRate = totalMealLimit > 0 ? totalBazar / totalMealLimit : 0;

            String sql = "SELECT users.username, users.id, " +
                    "COALESCE(SUM(meals.lunch + meals.dinner), 0) AS total_meals, " +
                    "COALESCE((SELECT SUM(amount) FROM deposits WHERE deposits.user_id = users.id), 0) AS total_deposit " +
                    "FROM users LEFT JOIN meals ON users.id = meals.user_id " +
                    "GROUP BY users.id";

            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            System.out.println("------- Monthly Summary -------");
            System.out.printf("%-15s %-10s %-10s %-10s %-10s%n", "Username", "Meals", "Deposit", "Cost", "Balance");

            while (rs.next()) {
                String name = rs.getString("username");
                int meals = rs.getInt("total_meals");
                double deposit = rs.getDouble("total_deposit");
                double cost = mealRate * meals;
                double balance = deposit - cost;

                System.out.printf("%-15s %-10d ৳%-9.2f ৳%-9.2f ৳%-9.2f%n", name, meals, deposit, cost, balance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
