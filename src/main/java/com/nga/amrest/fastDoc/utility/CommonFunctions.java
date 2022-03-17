package com.nga.amrest.fastDoc.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nga.amrest.connection.DestinationClient;
import com.nga.amrest.fastDoc.connection.ConnectionWithJWT;
import com.nga.amrest.fastDoc.controller.DocGen;
import com.sap.core.connectivity.api.configuration.ConnectivityConfiguration;
import com.sap.core.connectivity.api.configuration.DestinationConfiguration;

/*
 * CommonFunctions class 
 * Containing all the common functions required for the complete application
 * 
 * @author	:	Manish Gupta  
 * @email	:	manish.g@ngahr.com
 * @version	:	0.0.1
 */

public class CommonFunctions {
	static Logger logger = LoggerFactory.getLogger(DocGen.class);

	public static DestinationClient getDestinationCLient(String destinationName) throws NamingException {
		DestinationClient destClient = new DestinationClient();
		destClient.setDestName(destinationName);
		destClient.setHeaderProvider();
		destClient.setConfiguration();
		destClient.setDestConfiguration();
		destClient.setHeaders(destClient.getDestProperty("Authentication"));
		return destClient;
	}

	public static String callpostAPI(String url, JSONObject body) throws IOException {
		logger.debug("POST Body to send:" + body.toString());
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");

		// For POST only - START
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		os.write(body.toString().getBytes());
		os.flush();
		os.close();
		// For POST only - END

		int responseCode = con.getResponseCode();
		logger.debug("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			logger.debug("POST resposne:" + response.toString());
			return response.toString();
		} else {
			logger.debug("Error while retriving Templates from API, ResponseCode: " + responseCode);
			return new JSONObject().put("templates", new JSONArray()).toString();
		}
	}

	public String callpostAPIWithJWT(String path, JSONObject body, String destinationName)
			throws IOException, NamingException, URISyntaxException {
		logger.debug("POST Body to send: " + body.toString());
		ConnectivityConfiguration configuration;
		Context ctx = new InitialContext();
		configuration = (ConnectivityConfiguration) ctx.lookup("java:comp/env/connectivityConfiguration");
		DestinationConfiguration destination = configuration.getConfiguration(destinationName);
		ConnectionWithJWT connectionWithJWT = new ConnectionWithJWT();
		connectionWithJWT.setDestination(destination);
		HttpResponse response = connectionWithJWT.callDestinationPOST(path, body.toString());
		String responseJSONString = EntityUtils.toString(response.getEntity(), "UTF-8");
		logger.debug("Response from POST JWT API: " + responseJSONString);
		return responseJSONString;
	}

	public static Boolean checkIfAdmin(String loggedInUser)
			throws NamingException, ClientProtocolException, IOException, URISyntaxException {
		DestinationClient destClient = CommonFunctions.getDestinationCLient(CommonVariables.sfAdminPermission);
		try {
			HttpResponse response = destClient.callDestinationGET("/getDynamicGroupsByUser",
					"?userId='" + loggedInUser + "'&$format=json&groupSubType='permission'");
			String responseJsonString = EntityUtils.toString(response.getEntity(), "UTF-8");
			JSONObject responseObject = new JSONObject(responseJsonString);
			JSONArray responseObjectArray = responseObject.getJSONArray("d");

			for (int i = 0; i < responseObjectArray.length(); i++) {
				if (responseObjectArray.getJSONObject(i).getString("groupName").toLowerCase().contains("admin")) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
}
