import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.StringUtils;

public class DatabaseClass {

	public static Connection jdbcConn = null;
	
   public static Connection getConnection() {
	   try {
		   //STEP 2: Register JDBC driver
		   Class.forName(Constants.Database.JDBC_DRIVER);

		   //STEP 3: Open a connection
		   System.out.println("Connecting to a selected database...");	
		   jdbcConn = DriverManager.getConnection(Constants.Database.DB_URL, Constants.Database.USER, Constants.Database.PASS);
		   System.out.println("Connected database successfully...");
		   
	   } catch (Exception e) {
		   e.printStackTrace();
	   }
	   return jdbcConn;
   }
   
   public static int insertCowork(CoWork cowork) {
	   int coworkId = -1;
	   try {
		   String insertTableSQL = "INSERT INTO " + Constants.Database.TABLE_COWORKS
					+ "(creatorID, attendeesID, numAttendees, locationName, "
					+ "locationLat, locationLng, time, date, activityType, description, finished, canceled) VALUES"
					+ "(?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement preparedStatement = 
					(PreparedStatement) jdbcConn.prepareStatement(insertTableSQL, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, cowork.getCreatorID());
			preparedStatement.setString(2, cowork.getAttendeesID());
			preparedStatement.setInt(3, cowork.getNumAttendees());
			preparedStatement.setString(4, cowork.getLocationName());
			preparedStatement.setDouble(5, cowork.getLocationLat());
			preparedStatement.setDouble(6, cowork.getLocationLng());
			preparedStatement.setString(7, cowork.getTime());
			preparedStatement.setString(8, cowork.getDate());
			preparedStatement.setInt(9, cowork.getActivityType());
			preparedStatement.setString(10, cowork.getDescription());
			preparedStatement.setInt(11, cowork.getFinished());
			preparedStatement.setInt(12, cowork.getCanceled());
			
			// execute insert SQL statement
			preparedStatement .executeUpdate();
		   
			ResultSet rs = preparedStatement.getGeneratedKeys();
			rs.next();
			coworkId = rs.getInt(1);
		   System.out.println("Inserted data successfully into cowork table");
		   jdbcConn.close();
	   } catch (Exception e) {
		   System.out.println("Error inserting data into cowork table");
		   e.printStackTrace();
	   }
	   
	   return coworkId;
   }
   
   public static void getCowork(int coworkID) {
	   
   }
   
   /*
   public static void updateCowork(CoWork cowork) {
		
	   try {
		   String updateTableSQL = "UPDATE " + Constants.Database.TABLE_COWORKS
					+ " SET creatorID, attendeesID, numAttendees, locationName, "
					+ "locationLat, locationLng, time, date, activityType, description, finished, canceled) VALUES"
					+ "(?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement preparedStatement = (PreparedStatement) jdbcConn.prepareStatement(insertTableSQL);
			preparedStatement.setString(1, cowork.getCreatorID());
			preparedStatement.setString(2, cowork.getAttendeesID());
			preparedStatement.setInt(3, cowork.getNumAttendees());
			preparedStatement.setString(4, cowork.getLocationName());
			preparedStatement.setString(5, cowork.getLocationLat());
			preparedStatement.setString(6, cowork.getLocationLng());
			preparedStatement.setString(7, cowork.getTime());
			preparedStatement.setString(8, cowork.getDate());
			preparedStatement.setInt(9, cowork.getActivityType());
			preparedStatement.setString(10, cowork.getDescription());
			preparedStatement.setInt(11, cowork.getFinished());
			preparedStatement.setInt(12, cowork.getCanceled());
			
			// execute insert SQL statement
			preparedStatement .executeUpdate();
		   
		   jdbcConn.close();
	   } catch (Exception e) {
		   System.out.println("Error inserting data into cowork table");
		   e.printStackTrace();
	   }
   }
   */
   
   public static ArrayList<CoWork> getNearbyCoworks(LocationClass location) {
	   ArrayList<CoWork> coWorks = new ArrayList<>();
	   
	   double lat = Double.parseDouble(location.lat);
	   double lng = Double.parseDouble(location.lng);
	   
	   double lat1, lat2, lng1, lng2;
	   
	   lat1 = lat - 0.5;
	   lat2 = lat + 0.5;
	   
	   lng1 = lng - 0.5;
	   lng2 = lng + 0.5;
	   
	   System.out.println("lat1: " + lat1);
	   System.out.println("lat2: " + lat2);
	   System.out.println("lng1: " + lng1);
	   System.out.println("lng2: " + lng2);
	   
	   try {
		   String getNearbyCoworkQuery = "SELECT * FROM " + Constants.Database.TABLE_COWORKS
					+ " WHERE locationLat BETWEEN ? AND ? AND locationLng BETWEEN ? AND ?";
		   
			PreparedStatement preparedStatement = (PreparedStatement) jdbcConn.prepareStatement(getNearbyCoworkQuery);
			preparedStatement.setString(1, String.valueOf(lat1));
			preparedStatement.setString(2, String.valueOf(lat2));
			preparedStatement.setString(3, String.valueOf(lng1));
			preparedStatement.setString(4, String.valueOf(lng2));
			
			// execute SQL statement
			ResultSet rs = preparedStatement.executeQuery();
		   
			while(rs.next()) {
				
				System.out.println("coworkID: " + rs.getInt(Constants.MyDatabase.FIELD_COWORK_ID));
				
				CoWork cowork = new CoWork();
				
				cowork.setCoworkID(rs.getInt(Constants.MyDatabase.FIELD_COWORK_ID));
				cowork.setCreatorID(rs.getString(Constants.MyDatabase.FIELD_CREATOR_ID));
				cowork.setAttendeesID(rs.getString(Constants.MyDatabase.FIELD_ATTENDEES_ID));
				cowork.setNumAttendees(rs.getInt(Constants.MyDatabase.FIELD_NUM_ATTENDEES));
				cowork.setLocationName(rs.getString(Constants.MyDatabase.FIELD_lOCATION_NAME));
				cowork.setLocationLat(rs.getDouble(Constants.MyDatabase.FIELD_LOCATION_LATITUDE));
				cowork.setLocationLng(rs.getDouble(Constants.MyDatabase.FIELD_LOCATION_LONGITUDE));
				cowork.setTime(rs.getString(Constants.MyDatabase.FIELD_TIME));
				cowork.setDate(rs.getString(Constants.MyDatabase.FIELD_DATE));
				cowork.setActivityType(rs.getInt(Constants.MyDatabase.FIELD_ACTIVITY_TYPE));
				cowork.setDescription(rs.getString(Constants.MyDatabase.FIELD_DESCRIPTION));
				cowork.setFinished(rs.getInt(Constants.MyDatabase.FIELD_FINISHED));
				cowork.setCanceled(rs.getInt(Constants.MyDatabase.FIELD_CANCELED));
				
				coWorks.add(cowork);
			}
			
		   System.out.println("Selected nearby coworks successfully");
		   jdbcConn.close();
	   } catch (Exception e) {
		   System.out.println("Error selecting nearby coworks");
		   e.printStackTrace();
	   }
	   
	   return coWorks;
   }
   
   public static void insertUser(UserProfile user) {
	   try {
		   String insertTableSQL = "INSERT INTO " + Constants.Database.TABLE_USERS
					+ "(userID, password, name, gender, profession, email, birthday, photo, loginType)"
					+ "VALUES"
					+ "(?,?,?,?,?,?,?,?,?)";
			PreparedStatement preparedStatement = (PreparedStatement) jdbcConn.prepareStatement(insertTableSQL);
			preparedStatement.setString(1, user.getUserId());
			preparedStatement.setString(2, user.getPassword());
			preparedStatement.setString(3, user.getName());
			preparedStatement.setString(4, user.getGender());
			preparedStatement.setString(5, user.getProfession());
			preparedStatement.setString(6, user.getEmail());
			preparedStatement.setString(7, user.getBirthday());
			preparedStatement.setBlob(8, user.getPhoto());
			preparedStatement.setInt(9, user.getLoginType());
			
			// execute insert SQL statement
			preparedStatement.executeUpdate();
		   
		   System.out.println("Inserted data successfully into user profile table");
		   jdbcConn.close();
	   } catch (Exception e) {
		   System.out.println("Error inserting data into user profile table");
		   e.printStackTrace();
	   }
   }
   
   public static void updateUser(UserProfile user) {
		
	   try {
		   String updateUserSQL = "UPDATE " + Constants.Database.TABLE_USERS
					+ " SET name = ?, gender = ?, profession = ?, email = ?, birthday = ?, photo = ?"
					+ " WHERE userID = ?";
		   
			PreparedStatement preparedStatement = (PreparedStatement) jdbcConn.prepareStatement(updateUserSQL);
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getGender());
			preparedStatement.setString(3, user.getProfession());
			preparedStatement.setString(4, user.getEmail());
			preparedStatement.setString(5, user.getBirthday());
			preparedStatement.setBlob(6, user.getPhoto());
			preparedStatement.setString(7, user.getUserId());
			
			// execute insert SQL statement
			preparedStatement .executeUpdate();
		   
		   System.out.println("Updated user profile successfully");
		   jdbcConn.close();
	   } catch (Exception e) {
		   System.out.println("Error updating user profile");
		   e.printStackTrace();
	   }
   }

   public static ArrayList<UserProfile> getCorrespondingUserProfiles(UserProfileList[] userProfileArray) {
	   
	   ArrayList<UserProfile> userProfiles = new ArrayList<>();
	   
	   ArrayList<String> userIds = new ArrayList<>();
	   String parameters = "";
	   for (UserProfileList userProfile : userProfileArray) {
		   userIds.add(userProfile.getUserID());
		   if(userProfile.getUserID().equals("") == false) {
			   parameters = parameters + "\'" + userProfile.getUserID() + "\'" + ",";
		   }	   
	   }
	   
	   parameters = parameters.substring(0, parameters.length() - 1);
	   System.out.println("parameters: " + parameters);
	   
	   try {
		   String getNearbyCoworkQuery = "SELECT * FROM " + Constants.Database.TABLE_USERS
					+ " WHERE userID IN ('vikramgupta20.7@gmail.com','vikram@kamama.com')";
		   
			PreparedStatement preparedStatement = (PreparedStatement) jdbcConn.prepareStatement(getNearbyCoworkQuery);
			
			//Array array = jdbcConn.createArrayOf("VARCHAR", userIds);
			//preparedStatement.setString(1, parameters);
			
			// execute SQL statement
			ResultSet rs = preparedStatement.executeQuery();
		   
			while(rs.next()) {
				
				System.out.println("userID: " + rs.getString(Constants.MyDatabase.FIELD_USER_ID));
				
				UserProfile userProfile = new UserProfile();
				
				userProfile.setUserId(rs.getString(Constants.MyDatabase.FIELD_USER_ID));
				userProfile.setPassword(rs.getString(Constants.MyDatabase.FIELD_PASSWORD));
				userProfile.setName(rs.getString(Constants.MyDatabase.FIELD_NAME));
				userProfile.setGender(rs.getString(Constants.MyDatabase.FIELD_GENDER));
				userProfile.setProfession(rs.getString(Constants.MyDatabase.FIELD_PROFESSION));
				userProfile.setEmail(rs.getString(Constants.MyDatabase.FIELD_EMAIL));
				userProfile.setBirthday(rs.getString(Constants.MyDatabase.FIELD_BIRTHDAY));
				userProfile.setPhoto(rs.getBlob(Constants.MyDatabase.FIELD_PHOTO));
				userProfile.setLoginType(rs.getInt(Constants.MyDatabase.FIELD_LOGINTYPE));
				
				userProfiles.add(userProfile);
			}
			
		   System.out.println("Got corresponding users successfully");
		   jdbcConn.close();
	   } catch (Exception e) {
		   System.out.println("Error getting corresponding users");
		   e.printStackTrace();
	   }
	   
	   return userProfiles;
   }
   
   
}
