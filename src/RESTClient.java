import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
 
import org.json.JSONObject;
 
/**
 * @author vikramgupta
 * 
 */
 
public class RESTClient {
	public static void main(String[] args) {
		String string = "";
		try {
 
			// Read file from fileSystem
			InputStream inputStream = new FileInputStream("jsonText");
			InputStreamReader reader = new InputStreamReader(inputStream);
			BufferedReader br = new BufferedReader(reader);
			String line;
			while ((line = br.readLine()) != null) {
				string += line + "\n";
			}
			
			JSONObject jsonObject = new JSONObject(string);
			
			System.out.println(jsonObject);
 
			// Pass JSON File Data to REST Service
			try {
				URL url = new URL("http://localhost:8080/SimpleWebServer/api/test");
				URLConnection connection = url.openConnection();
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setConnectTimeout(5000);
				connection.setReadTimeout(5000);
				OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
				out.write(jsonObject.toString());
				out.close();
 
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
 
				StringBuffer buffer = new StringBuffer();
				String inputLine;
	            while ((inputLine = in.readLine()) != null)
	                buffer.append(inputLine + "\n");
	            if (buffer.length() == 0) {
	                // Stream was empty. No point in parsing.
	                return;
	            }
	            System.out.println("\nBuffer: " + buffer.toString());
				System.out.println("CoWork REST Service Invoked Successfully..");
				in.close();
			} catch (Exception e) {
				System.out.println("\nError while calling CoWork REST Service");
				e.printStackTrace();
			}
 
			//br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}