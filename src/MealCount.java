import java.sql.*;
import java.util.*;

public class MealCount {
    public static void addMeal(Scanner sc, int userId) {

        int totalMealLimit=Bazar.getTotalMealLimit();
        int totalMealsTaken=getAllUserMealCount();

        if (totalMealsTaken >= totalMealLimit) {
            System.out.println("Bazar meal limit exceeded! Please add new bazar before adding meals.");
            return;
        }


        System.out.print("Date (YYYY-MM-DD): ");
        String date=sc.next();
        System.out.print("Lunch (1/0): ");
        int lunch=sc.nextInt();
        System.out.print("Dinner (1/0): ");
        int dinner=sc.nextInt();

        try (Connection con=Database.getConnection()) {
            String sql = "INSERT INTO meals (user_id, meal_date, lunch, dinner) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, userId);
            pst.setString(2, date);
            pst.setInt(3, lunch);
            pst.setInt(4, dinner);
            pst.executeUpdate();
            System.out.println("✅ Meal added.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static int getTotalMeals(int userId) {
        int total=0;
        try (Connection con=Database.getConnection()) {
            String sql="SELECT SUM(lunch + dinner) AS total FROM meals WHERE user_id=?";
            PreparedStatement pst=con.prepareStatement(sql);
            pst.setInt(1, userId);
            ResultSet rs=pst.executeQuery();
            if (rs.next()) {
                total=rs.getInt("total");
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return total;
    }

    public static int getAllUserMealCount() {
        int total = 0;
        try (Connection con=Database.getConnection()) {
            String sql="SELECT SUM(lunch + dinner) AS total FROM meals";
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery(sql);
            if (rs.next()) {
                total=rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
    public static void addMealForUser(Scanner sc) {
        System.out.print("Enter user ID to add meal for: ");
        int userId = sc.nextInt();
        sc.nextLine(); // clear buffer

        addMeal(sc, userId);
    }

}
