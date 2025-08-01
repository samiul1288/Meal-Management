import java.sql.*;
import java.util.Scanner;

public class Bazar{

    public static void addBazar(Scanner sc,int userId) {
        System.out.print("Bazar Date (YYYY-MM-DD): ");
        String date=sc.next();
        System.out.print("Amount: ");
        double amount=sc.nextDouble();
        System.out.print("How many meals this bazar supports: ");
        int mealLimit=sc.nextInt();

        try (Connection con =Database.getConnection()) {
            String sql="INSERT INTO bazar (user_id, amount, bazar_date, meal_limit) VALUES (?, ?, ?, ?)";
            PreparedStatement pst =con.prepareStatement(sql);
            pst.setInt(1,userId);
            pst.setDouble(2,amount);
            pst.setString(3,date);
            pst.setInt(4,mealLimit);
            pst.executeUpdate();
            System.out.println("Bazar added with meal limit.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static double getTotalBazar() {
        double total=0;
        try (Connection con=Database.getConnection()) {
            String sql="SELECT SUM(amount) AS total FROM bazar";
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery(sql);
            if (rs.next()) {
                total=rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }


    public static int getTotalMealLimit() {
        int total = 0;
        try (Connection con = Database.getConnection()) {
            String sql = "SELECT SUM(meal_limit) AS total FROM bazar";
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
}
