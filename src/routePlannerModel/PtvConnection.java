package routePlannerModel;
import java.io.*;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/*
 * Source code from PTV api document.
 */

public class PtvConnection {
	private final int developerId = 1000231;
	private final String privateKey = "27e5b640-15f1-11e4-8bed-0263a9d0b8a0";
	
	public PtvConnection() {
		
	}
	
	private String generateSignature(String uri) {
		String encoding = "UTF-8";
		String HMAC_SHA1_ALGORITHM = "HmacSHA1";
		String signature;
		StringBuffer uriWithDeveloperID = new StringBuffer();
		uriWithDeveloperID
		    .append(uri)
		    .append(uri.contains("?") ? "&" : "?")
		    .append("devid="+developerId);
		try { 
		    byte[] keyBytes = privateKey.getBytes(encoding);
		    byte[] uriBytes = 
		    		uriWithDeveloperID.toString().getBytes(encoding);
		    Key signingKey = 
		    		new SecretKeySpec(keyBytes, HMAC_SHA1_ALGORITHM); 
		    Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM); 
		    mac.init(signingKey);
		    byte[] signatureBytes = mac.doFinal(uriBytes);
		    StringBuffer buf = new StringBuffer(signatureBytes.length * 2);
		    for (byte signatureByte : signatureBytes) {
			    int intVal = signatureByte & 0xff;
			    if (intVal < 0x10) {
				    buf.append("0");
			    }
		    buf.append(Integer.toHexString(intVal));
		    }
            signature = buf.toString(); 
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e); 
        }
        catch (InvalidKeyException e) {
            throw new RuntimeException(e); 
        }
		
        return signature.toString().toUpperCase();       
	}
	
	private String generateCompleteURLWithSignature(String uri) {
		String baseURL="http://timetableapi.ptv.vic.gov.au";
		StringBuffer url = new StringBuffer(baseURL)
		    .append(uri)
		    .append(uri.contains("?") ? "&" : "?")
		    .append("devid="+developerId)
		    .append("&signature=" + generateSignature(uri));
		
	    return url.toString();
	}
	
	public String getResponse(String uri) {
		URL url;
		HttpURLConnection con = null;
		String targetUrl = generateCompleteURLWithSignature(uri);
		
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
