import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MonthlySummary{
    public static void show(int userId){
        int meals=MealCount.getTotalMeals(userId);
        double deposit=MealCash.getTotalDeposit(userId);
        double totalBazar=Bazar.getTotalBazar();

        int totalMealLimit=Bazar.getTotalMealLimit();

        double mealRate;
        if (totalMealLimit>0) {
            mealRate=totalBazar / totalMealLimit;
        } else {
            mealRate=0;
        }

        double cost=mealRate*meals;
        double balance=deposit-cost;

        System.out.println("Total Meals: "+meals);
        System.out.println("Total Deposit: "+deposit);
        System.out.println("Meal Rate: "+String.format("%.2f",mealRate));
        System.out.println("Meal Cost: "+String.format("%.2f",cost));
        System.out.println("Balance: "+String.format("%.2f",balance));


        if (totalMealLimit<getTotalMealsAllUsers()){
            System.out.println("⚠️ Warning: Total meal limit (" +totalMealLimit+") is less than total meals consumed (" + getTotalMealsAllUsers() + "). Please add new bazar.");
        }
    }
    public static int getTotalMealsAllUsers() {
        int total=0;
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
}
