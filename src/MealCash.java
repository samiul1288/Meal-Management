import java.sql.*;
import java.util.Scanner;

public class MealCash{
    public static void deposit(Scanner sc,int userId) {
        System.out.print("Deposit Amount: ");
        double amount=sc.nextDouble();
        try (Connection con=Database.getConnection()) {
            String sql="INSERT INTO deposits (user_id, amount, deposit_date) VALUES (?, ?, NOW())";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, userId);
            pst.setDouble(2, amount);
            pst.executeUpdate();
            System.out.println("Deposit recorded.");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    public static double getTotalDeposit(int userId){
        double total=0;
        try (Connection con=Database.getConnection()){
            String sql="SELECT SUM(amount) AS total FROM deposits WHERE user_id=?";
            PreparedStatement pst=con.prepareStatement(sql);
            pst.setInt(1, userId);
            ResultSet rs=pst.executeQuery();
            if (rs.next()){
                total=rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
}
