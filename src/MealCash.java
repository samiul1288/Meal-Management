import java.sql.*;
import java.util.Scanner;

public class MealCash {
    public static void deposit(Scanner sc, int userId) {
        System.out.print("Deposit Amount: ");
        double amount = sc.nextDouble();
        try (Connection con = Database.getConnection()) {
            String sql = "INSERT INTO deposits (user_id, amount, deposit_date) VALUES (?, ?, NOW())";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, userId);
            pst.setDouble(2, amount);
            pst.executeUpdate();
            System.out.println("Deposit recorded.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static double getTotalDeposit(int userId) {
        double total = 0;
        try (Connection con = Database.getConnection()) {
            String sql = "SELECT SUM(amount) AS total FROM deposits WHERE user_id=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                total = rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
    public static void depositForUser(Scanner sc) {
        System.out.print("Enter user ID to deposit for: ");
        int userId = sc.nextInt();
        sc.nextLine(); // buffer clear

        deposit(sc, userId);
    }

    public static void showAllUserDeposits() {
        try (Connection con = Database.getConnection()) {
            String sql = "SELECT users.username, SUM(deposits.amount) AS total " +
                    "FROM deposits JOIN users ON deposits.user_id = users.id " +
                    "GROUP BY users.id";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            System.out.println("----- All User Deposits -----");
            while (rs.next()) {
                String name = rs.getString("username");
                double total = rs.getDouble("total");
                System.out.println(name + " → ৳" + total);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
