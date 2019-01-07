import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;
import java.math.BigInteger;
import java.security.MessageDigest;


public class MainApp{
   public static void main(String[] args){
      try{
         Connection conn = Connect.getConnection();
         int temp = 0 ;//This is a value used to keep the while loop infinatly, until the user enters a 9 to exit.
         Scanner sc = new Scanner(System.in);
         System.out.println("Please enter your userID if you have one. Otherwise, please type 'new' to create your account. Please note, this is case sensitive");
         String userID = sc.next();
         if(userID.equals("new"))
         {
            userID = insertUser(conn);
         }
         else{
            System.out.println("Please enter in your password for the username you entered");
            String tempPassword = sc.next();
            String sql = "Select users.pass from users where userID = '"+ userID +"'";
         
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql);
            String actualPassword = "";
            while(result.next()){
               actualPassword = result.getString("pass");
            }
            
         
            if(getHash(tempPassword).equals(actualPassword)){
            
               while(temp != 9){
                  System.out.println();
                  System.out.println("Enter a '1' to be read back all of your information");
                  System.out.println("Enter a '2' to check your matches names and phone numbers");
                  System.out.println("Enter a '3' to change your phone number");
                  System.out.println("Enter a '4' to delete your account");
                  System.out.println("Enter a '9' to exit from this application.");
                  temp = sc.nextInt();
               
                  if(temp == 1)
                  {
                     selectUserInfo(conn,userID);
                  }
                  if(temp == 2)
                  {
                     selectUserMatches(conn,userID);
                  }
                  if(temp == 3)
                  {
                     updatePhoneNumber(conn,userID);
                  }
                  if(temp == 4)
                  {
                     deleteUser(conn,userID);
                     temp = 9;
                  }
                  if(temp == 9)
                  {
                     conn.close();
                  }
               }
            }
            else{
               System.out.print("Inncorrect password. Please re-run the application and try again");
            }
            
         }
        //updateExample(conn);
         conn.close();
      }catch (SQLException e){
         e.printStackTrace();
      }
   
   }
   
   public static String insertUser(Connection conn) throws SQLException {
      String sql = "INSERT INTO users(userID,pass,age,sex,phoneNumber,hobbies,lookingForRelationship)"
         + "VALUES (?,?,?,?,?,?,?)";
      Scanner sc = new Scanner(System.in);
   
      PreparedStatement statement = conn.prepareStatement(sql);
      System.out.println("Enter a userID");
      String valueOne = sc.next();
      statement.setString(1, valueOne);
      System.out.println("Enter a password");
      String valueTwo = sc.next();
      statement.setString(2, getHash(valueTwo));
      System.out.println("Enter your age");
      int valueThree = sc.nextInt();
      statement.setInt(3, valueThree);
      System.out.println("Enter your sex or enter a 0 if you choose not to");
      String valueFour = sc.next();
      statement.setString(4, valueFour);
      System.out.println("Enter your phone number. This will be given to matches.");
      String valueFive = sc.next();
      statement.setString(5, valueFive);
      System.out.println("Enter your hobby! This will help find you matches!");
      String valueSix = sc.next();
      statement.setString(6, valueSix);
      System.out.println("Lastly, are you looking for a relationship(1) or just for friends(0)? If you're looking for a relationship, enter a 1. Otherwise, enter a 0!");
      int valueSeven = sc.nextInt();
      statement.setInt(7, valueSeven);
      
      int rowsAffected = statement.executeUpdate();
      if (rowsAffected > 0){
         System.out.println("Your user has been created!!");
      }
      return valueOne;
      
   
   }
   public static void updatePhoneNumber(Connection conn, String userID) throws SQLException{
      String sql = "UPDATE users SET phoneNumber=? WHERE userID='" + userID + "'";
      PreparedStatement statement = conn.prepareStatement(sql);
      Scanner sc = new Scanner(System.in);
      System.out.println("Enter your new phone number here. Please format like this: 123-345-5678");
      String valueOne = sc.next();;
      statement.setString(1,valueOne);
   
   
      int rowsAffected = statement.executeUpdate();
      if (rowsAffected > 0){
         System.out.println("Your phone number has been updated!");
      }
   }
   
   //This will write all the users information for easy visability
   public static void selectUserInfo(Connection conn, String userID) throws SQLException{
      String sql = "SELECT * FROM users WHERE userID ='" + userID + "'";
      
      Statement statement = conn.createStatement();
      ResultSet result = statement.executeQuery(sql);
      String userName = "";
      String pass = "";
      int age= 0;
      String sex = "male";
      String phoneNumber = "000-000-0000";
      String hobbies = "";
      int lookingForRelationship= 2;
   
      while(result.next()){
         userName = result.getString("userID");
         pass = result.getString("pass");
         age = result.getInt("age");
         sex = result.getString("sex");
         phoneNumber = result.getString("phoneNumber");
         hobbies = result.getString("hobbies");
         lookingForRelationship = result.getInt("lookingForRelationship");
         
         
      }
      System.out.println("UserID: " + userID);
      System.out.println("Password: " + pass);
      System.out.println("Age: " + age);
      System.out.println("Sex: " + sex);
      System.out.println("Phone number: "+ phoneNumber);
      System.out.println("Hobbies: "+ hobbies);
      System.out.println("Looking for a relationship: "+ lookingForRelationship);
      
   }

   public static void deleteUser(Connection conn, String userID) throws SQLException{
   
      Scanner sc = new Scanner(System.in);
      System.out.println("Please confirm your password to delete your account");
      String passWord = sc.next();
      String sql = "DELETE from users WHERE userID = '" + userID +"' AND pass =?";
      PreparedStatement statement = conn.prepareStatement(sql);
      statement.setString(1,passWord);
   
   
      int rowsAffected = statement.executeUpdate();
      if (rowsAffected > 0){
         System.out.println("Your account has been deleted!");
      }
   }
   public static void selectUserMatches(Connection conn, String userID) throws SQLException{
      String sql = "Select users.userID, users.phoneNumber from users where users.userID = (Select matchID from matches where userID ='"+ userID +"')";
      
      Statement statement = conn.createStatement();
      ResultSet result = statement.executeQuery(sql);
      String matchUsername = "";
      String matchPhoneNumber= "";
   
   
      while(result.next()){
      
         matchUsername = result.getString("userID");
         matchPhoneNumber = result.getString("phoneNumber");
         System.out.println("Matches Username: " + matchUsername);
         System.out.println("Matches Phonenumber: " + matchPhoneNumber);
         
         
         
      }
      
      
   }
   
   
   public static String getHash(String pass) {
      String hashVal = null;
   
      try {
         MessageDigest digest = MessageDigest.getInstance("SHA-1");
      
         digest.reset();
      
         digest.update(pass.getBytes("utf8"));
      
         hashVal = String.format("%040x", new BigInteger(1, digest.digest()));
      
      } catch (Exception e) {
         e.printStackTrace();
      }
      return hashVal;
   }
}