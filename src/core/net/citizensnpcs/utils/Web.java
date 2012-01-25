package net.citizensnpcs.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.Settings;

import org.bukkit.Bukkit;

import com.google.common.collect.Maps;

public class Web {
    private static final Handler handler = new LogHandler();

    public static void initErrorReporting() {
        if (!Settings.getBoolean("ErrorReporting")) {
            return;
        }
        Bukkit.getLogger().addHandler(handler);
    }

    public static void disableErrorReporting() {
        if (!Settings.getBoolean("ErrorReporting")) {
            return;
        }
        Bukkit.getLogger().removeHandler(handler);
    }

    private static void report(String error) {
        try {
            Messaging.log("An error has occurred, please wait while it is sent to the developers...");
            // Construct data
            String data = URLEncoder.encode("Exception", "UTF-8") + "=" + URLEncoder.encode(error, "UTF-8");
            data += "&" + URLEncoder.encode("Version", "UTF-8") + "="
                    + URLEncoder.encode(Citizens.localVersion(), "UTF-8");
            data += "&" + URLEncoder.encode("Ident", "UTF-8") + "="
                    + URLEncoder.encode(Settings.getString("ErrorReportingIdent"), "UTF-8");
            // Send data
            URL url = new URL("http://errorreport.citizensnpcs.net");
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            while ((line = rd.readLine()) != null) {
                Messaging.log(line);
            }

            wr.close();
            rd.close();
        } catch (Exception e) {
            Messaging.log("An error occurred whilst sending your error report.");
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

    private static class LogHandler extends ConsoleHandler {
        @Override
        public void publish(final LogRecord record) {
            if (record.getMessage() == null || record.getThrown() == null || record.getLevel() != Level.SEVERE
                    || !record.getMessage().contains("Citizens"))
                return;
            for (String pattern : illegalStacks.keySet()) {
                if (record.getMessage().matches(pattern)) {
                    if (!illegalStacks.get(pattern).isEmpty())
                        Messaging.log(illegalStacks.get(pattern));
                    return;
                }
            }
            new Thread() {
                @Override
                public void run() {
                    report(stackToString(record.getThrown()));
                }
            }.start();
        }
    }

    private static Map<String, String> illegalStacks = Maps.newHashMap();
    static {
        illegalStacks.put("org.yaml.snakeyaml", "Something went wrong with config.");
        illegalStacks.put("NoSuchFieldError", "A bukkit update has changed obfuscation, wait for a Citizens update.");
        illegalStacks.put("OutOfMemoryError",
                "Uh oh - your server is out of memory - can you report this on the forums?");
        illegalStacks
                .put("NoClassDefFoundError",
                        "A required class is missing - usually, the type JARs are incompatible with the core Citizens JAR or there's an economy/permissions class missing.");
    }
}
