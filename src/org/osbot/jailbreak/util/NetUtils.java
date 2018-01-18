package org.osbot.jailbreak.util;

import org.osbot.jailbreak.data.Constants;
import org.osbot.jailbreak.ui.logger.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ethan on 1/18/2018.
 */
public class NetUtils {

	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; …) Gecko/20100101 Firefox/57.0";

	public static boolean isVerified(String mac) throws IOException {
		URL obj = new URL(Constants.VERIFY_ACCESS_URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		String urlParameters = "search=" + mac + "&submit=Search";
		os.write(urlParameters.getBytes());
		os.flush();
		os.close();
		int responseCode = con.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			if (response.toString().trim().contains("true")) {
				return true;
			}
		} else {
			Logger.log("Error Verifying License!");
		}
		return false;
	}
}
