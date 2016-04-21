import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.print.attribute.standard.Media;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;

import com.google.gson.Gson;

@Path("/")
public class RESTService {
	@POST
	@Path("/insertcowork")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertCowork(InputStream incomingData) {
		StringBuilder jsonStr = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
				jsonStr.append(line);
			}
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
		}
		System.out.println("Data Received: " + jsonStr.toString());
 
		Gson gson = new Gson();
		CoWork cowork = gson.fromJson(jsonStr.toString(), CoWork.class);
		
		System.out.println("Activity: " + cowork.getActivityType());
		
		if(DatabaseClass.getConnection() != null) {
			int coworkId = DatabaseClass.insertCowork(cowork);
			// return HTTP response 200 in case of success
			return Response.status(200).entity(String.valueOf(coworkId)).build();
		}

		return Response.status(500).build();
	}
 
	@POST
	@Path("/insertuser")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertUser(InputStream incomingData) {
		StringBuilder jsonStr = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
				jsonStr.append(line);
			}
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
		}
		
		Gson gson = new Gson();
		UserProfile user = gson.fromJson(jsonStr.toString(), UserProfile.class);
		
		System.out.println("Activity: " + user.getName());
		
		if(DatabaseClass.getConnection() != null) {
			DatabaseClass.insertUser(user);
		}
		
		System.out.println("Data Received: " + jsonStr.toString());
 
		// return HTTP response 200 in case of success
		return Response.status(200).entity(jsonStr.toString()).build();
	}
	
	@POST
	@Path("/updateuser")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUser(InputStream incomingData) {
		StringBuilder jsonStr = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
				jsonStr.append(line);
			}
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
		}
		
		Gson gson = new Gson();
		UserProfile user = gson.fromJson(jsonStr.toString(), UserProfile.class);
		
		System.out.println("Activity: " + user.getName());
		
		if(DatabaseClass.getConnection() != null) {
			DatabaseClass.updateUser(user);
		}
		
		System.out.println("Data Received: " + jsonStr.toString());
 
		// return HTTP response 200 in case of success
		return Response.status(200).entity(jsonStr.toString()).build();
	}
	
	@GET
	@Path("/getcowork")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getCowork(InputStream incomingData) {
		String result = "CoWork Successfully started..";
 
		// return HTTP response 200 in case of success
		return Response.status(200).entity(result).build();
	}
	
	@POST
	@Path("/getnearbycoworks")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getNearbyCoworks(InputStream incomingData) {
		StringBuilder jsonStr = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
				jsonStr.append(line);
			}
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
		}
		System.out.println("Data Received: " + jsonStr.toString());
 
		Gson gson = new Gson();
		LocationClass location = gson.fromJson(jsonStr.toString(), LocationClass.class);
		
		System.out.println("Latitude: " + location.getLat());
		
		if(DatabaseClass.getConnection() != null) {
			ArrayList<CoWork> coworks = DatabaseClass.getNearbyCoworks(location);
			
			Gson gson1 = new Gson();
			String jsonNearbyCoworks = gson1.toJson(coworks);
			
			System.out.println("Json fetched from database: " + jsonNearbyCoworks);
			// return HTTP response 200 in case of success
			return Response.status(200).entity(String.valueOf(jsonNearbyCoworks)).build();
		}

		return Response.status(500).build();
	}	
	
	@POST
	@Path("/adduserasattendee")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addUserAsAttendee(InputStream incomingData) {
		StringBuilder jsonStr = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
				jsonStr.append(line);
			}
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
		}
		System.out.println("Data Received: " + jsonStr.toString());
 
		Gson gson = new Gson();
		AddUserClass addUserClass = gson.fromJson(jsonStr.toString(), AddUserClass.class);
		
		if(DatabaseClass.getConnection() != null) {
			CoWork coWork = DatabaseClass.addUserAsAttendee(addUserClass);
			
			String jsonCoWork = gson.toJson(coWork);
			System.out.println("Json fetched from database: " + jsonCoWork);
			
			// return HTTP response 200 in case of success
			return Response.status(200).entity(String.valueOf(jsonCoWork)).build();
		}

		return Response.status(500).build();
	}
	
	@POST
	@Path("/getcorrespondinguserprofilelist")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getCorrespondingUserProfileList(InputStream incomingData) {
		StringBuilder jsonStr = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
				jsonStr.append(line);
			}
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
		}
		System.out.println("Data Received: " + jsonStr.toString());
 
		Gson gson = new Gson();
		UserProfileList[] userProfileArray = gson.fromJson(jsonStr.toString(), UserProfileList[].class);

		if(DatabaseClass.getConnection() != null) {
			ArrayList<UserProfile> userProfiles = DatabaseClass.getCorrespondingUserProfiles(userProfileArray);
			
			Gson gson1 = new Gson();
			String jsonProfiles = gson1.toJson(userProfiles);
			
			System.out.println("Json fetched from database: " + jsonProfiles);
			// return HTTP response 200 in case of success
			return Response.status(200).entity(String.valueOf(jsonProfiles)).build();
		}

		return Response.status(500).build();
	}	
	
	@GET
	@Path("/getuser")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getUser(InputStream incomingData) {
		String result = "CoWork Successfully started..";
 
		// return HTTP response 200 in case of success
		return Response.status(200).entity(result).build();
	}
	
	@GET
	@Path("/verify")
	@Produces(MediaType.TEXT_PLAIN)
	public Response verifyRESTService(InputStream incomingData) {
		String result = "CoWork Successfully started..";
 
		// return HTTP response 200 in case of success
		return Response.status(200).entity(result).build();
	}
}
