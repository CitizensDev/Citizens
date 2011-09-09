package net.citizensnpcs.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.SettingsManager;

import org.bukkit.Bukkit;

public class ErrorReporting {
	public static final Logger log = Bukkit.getServer().getLogger();

	public static void init() {
		if (!SettingsManager.getBoolean("ErrorReporting")) {
			return;
		}
		log.setFilter(new Filter() {
			@Override
			public boolean isLoggable(LogRecord record) {
				if (record.getMessage() != null
						&& record.getLevel() == Level.SEVERE) {
					if (record.getMessage().contains("Citizens")) {
						report(stackToString(record.getThrown()));
					}
				}
				return true;
			}
		});
	}

	private static void report(String error) {
		try {
			Messaging
					.log("An error has occurred, please wait while it is sent to the developers...");
			// Construct data
			String data = URLEncoder.encode("Exception", "UTF-8") + "="
					+ URLEncoder.encode(error, "UTF-8");
			data += "&" + URLEncoder.encode("Version", "UTF-8") + "="
					+ URLEncoder.encode(Citizens.localVersion(), "UTF-8");
			data += "&"
					+ URLEncoder.encode("Ident", "UTF-8")
					+ "="
					+ URLEncoder.encode(
							SettingsManager.getString("ErrorReportingIdent"),
							"UTF-8");

			// Send data
			URL url = new URL("http://errorreport.citizensnpcs.net");
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(
					conn.getOutputStream());
			wr.write(data);
			wr.flush();

			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			/*
			 * String line; while ((line = rd.readLine()) != null) { //
			 * Possibility of suggestions for common errors in the future. }
			 */
			wr.close();
			rd.close();
		} catch (Exception e) {
			Messaging
					.log("An error occurred whilst sending your error report.");
		} finally {
			Messaging
					.log("Below is the stacktrace that has been transmitted to the developers, no other information has been included other than Citizens version information.");
		}
	}

	private static String stackToString(Throwable e) {
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return sw.toString();
		} catch (Exception e2) {
			return "Invalid stacktrace";
		}
	}
}