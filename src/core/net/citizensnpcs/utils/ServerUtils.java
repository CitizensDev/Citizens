package net.citizensnpcs.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import net.citizensnpcs.Citizens;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ServerUtils {

	// Get a player object from the provided name
	public static Player matchPlayer(String name) {
		List<Player> players = Bukkit.getServer().matchPlayer(name);
		if (!players.isEmpty()) {
			if (players.get(0) != null) {
				return players.get(0);
			}
		}
		return null;
	}

	// Check the Citizens thread on the Bukkit forums if there is a new version
	// available
	public static void checkForUpdates(Player player) {
		try {
			URI baseURI = new URI("http://forums.bukkit.org/threads/7173/");
			HttpURLConnection con = (HttpURLConnection) baseURI.toURL()
					.openConnection();
			con.setInstanceFollowRedirects(false);
			if (con.getHeaderField("Location") == null) {
				Messaging
						.log("Couldn't connect to Citizens thread to check for updates.");
				return;
			}
			String url = new URI(con.getHeaderField("Location")).toString();
			if (!url.contains(Citizens.getReleaseVersion().replace(".", "-"))) {
				Messaging.send(player, null, ChatColor.YELLOW + "**ALERT** "
						+ ChatColor.GREEN
						+ "There is a new version of Citizens available!");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//Error Reporting code
	public static void ErrorReport(Exception error)
	{

	  try {
	      // Construct data
	      String data = URLEncoder.encode("Exception", "UTF-8") + "=" + URLEncoder.encode(StacktoString(error), "UTF-8");
	      data += "&" + URLEncoder.encode("Version", "UTF-8") + "=" + URLEncoder.encode(Citizens.getVersion(), "UTF-8");
	      data += "&" + URLEncoder.encode("Ident", "UTF-8") + "=" + URLEncoder.encode("Tester", "UTF-8");

	      // Send data
	      URL url = new URL("http://errorreport.citizensnpcs.net");
	      URLConnection conn = url.openConnection();
	      conn.setDoOutput(true);
	      OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
	      wr.write(data);
	      wr.flush();

	      // Get the response
	      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	      String line;
	      while ((line = rd.readLine()) != null) {
	          // Process line...
	      }
	      wr.close();
	      rd.close();
	  } catch (Exception e) {
	  //Well this'd be bad, likely server down I guess, no real reason to report anything I guess... Only effects us, not the user, but maybe worth putting something here?
	  }

	}
	 public static String StacktoString(Exception e) {
		  try {
		    StringWriter sw = new StringWriter();
		    PrintWriter pw = new PrintWriter(sw);
		    e.printStackTrace(pw);
		    return sw.toString();
		  }
		  catch(Exception e2) {
		    return "Bad Stacktrace... Hit Paul with something.";
		  }
	 }
}