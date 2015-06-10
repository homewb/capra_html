package routePlannerModel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*
 * Use Api Key to send HTTP request to Google server and get response 
 */

public class GoogleApiConnection {
	private final String API_KEY = 
			"AIzaSyBDleV5p3pgUnBASMqpcdp1wPDjoFcNbPY";
	
	public GoogleApiConnection() {
		
	}
	
	public String getResponse(String uri) {
		URL url;
		HttpURLConnection con = null;
		String targetUrl = uri + "&key=" + API_KEY;
		
		try {
			url = new URL(targetUrl);
			con = (HttpURLConnection) url.openConnection();
			BufferedReader in = new BufferedReader(
					new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
				response.append('\r');
			}
			in.close();
			return response.toString();
		}
		catch (Exception e) {
			e.getStackTrace();
			return null;
		}
		finally {
			if (con != null)
				con.disconnect();
		}
		
	}

}
