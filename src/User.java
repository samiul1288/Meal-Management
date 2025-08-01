import java.sql.*;
import java.util.Scanner;

public class User {
    public static int login(Scanner sc) {
        System.out.print("Username: ");
        String username = sc.next();
        System.out.print("Password: ");
        String password = sc.next();

        try (Connection con = Database.getConnection()) {
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                System.out.println("Login successful.");
                return rs.getInt("id");
            } else {
                System.out.println("Login failed.");
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void register(Scanner sc) {
        System.out.print("New Username: ");
        String username = sc.next();
        System.out.print("Password: ");
        String password = sc.next();
        System.out.print("Is Admin? (true/false): ");
        boolean isAdmin = sc.nextBoolean();

        try (Connection con = Database.getConnection()) {
            String sql = "INSERT INTO users (username, password, isAdmin) VALUES (?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setBoolean(3, isAdmin);
            pst.executeUpdate();
            System.out.println("User registered.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static boolean isAdmin(int userId) {
        try (Connection con = Database.getConnection()) {
            String sql = "SELECT isAdmin FROM users WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("isAdmin") == 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
