package com.xiii.dynamic.server.utils;

import sun.net.ConnectionResetException;

import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTTPUtils {

    public static String getPaperDownload(final String version) {
        try {

            final String paperAPI = "https://api.papermc.io/v2/projects/paper/versions/" + version + "/builds";
            final URL url = new URL(paperAPI);
            final URLConnection conn = url.openConnection();
            conn.addRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            conn.connect();

            final BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            final Pattern p = Pattern.compile("(?<=\"build\":).[0-9]+");

            String inputLine, latestBuild = "0";
            while ((inputLine = br.readLine()) != null) {

                //If version doesn't exist
                if (inputLine.equalsIgnoreCase("{\"error\":\"Version not found.\"}")) {
                    br.close();
                    return "UNKNOWN_VERSION";
                }

                final Matcher m = p.matcher(inputLine);
                while (m.find()) {
                    latestBuild = m.group();
                }
            }
            br.close();

            return "https://api.papermc.io/v2/projects/paper/versions/" + version + "/builds/" + latestBuild + "/downloads/paper-" + version + "-" + latestBuild + ".jar";

        } catch (final IOException e) {
            e.printStackTrace();
        }
        return "UNKNOWN_VERSION";
    }

    public static String getSpigotDownload(final String version) {
        try {

            final String spigotDownloadURL = "https://download.getbukkit.org/spigot/spigot-" + version + ".jar";
            final URL url = new URL(spigotDownloadURL);
            final URLConnection conn = url.openConnection();
            conn.addRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            conn.connect();

            BufferedReader br;
            try {
                br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
            } catch (final FileNotFoundException e) {
                final String spigotLegacyDownloadURL = "https://cdn.getbukkit.org/spigot/spigot-" + VersionHelper.getVersion(version, "Spigot") + ".jar";
                final URL spigotLegacyURL = new URL(spigotLegacyDownloadURL);
                final URLConnection spigotLegacyConn = spigotLegacyURL.openConnection();
                spigotLegacyConn.addRequestProperty("User-Agent",
                        "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
                spigotLegacyConn.connect();

                final BufferedReader spigotLegacyBr = new BufferedReader(
                        new InputStreamReader(spigotLegacyConn.getInputStream()));

                String spigotLegacyInputLine;
                while ((spigotLegacyInputLine = spigotLegacyBr.readLine()) != null) {
                    if (spigotLegacyInputLine.contains("RESTRICTED!")  || spigotLegacyInputLine.contains("404 Not Found")) {
                        spigotLegacyBr.close();
                        return "UNKNOWN_VERSION";
                    }
                }

                return spigotLegacyDownloadURL;
            }

            String inputLine;
            while ((inputLine = br.readLine()) != null) {

                //If version doesn't exist
                if (inputLine.contains("RESTRICTED!") || inputLine.contains("404 Not Found")) {
                    br.close();

                    final String spigotLegacyDownloadURL = "https://cdn.getbukkit.org/spigot/spigot-" + VersionHelper.getVersion(version, "Spigot") + ".jar";
                    final URL spigotLegacyURL = new URL(spigotLegacyDownloadURL);
                    final URLConnection spigotLegacyConn = spigotLegacyURL.openConnection();
                    spigotLegacyConn.addRequestProperty("User-Agent",
                            "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
                    spigotLegacyConn.connect();

                    final BufferedReader spigotLegacyBr = new BufferedReader(
                            new InputStreamReader(spigotLegacyConn.getInputStream()));

                    String spigotLegacyInputLine;
                    while ((spigotLegacyInputLine = spigotLegacyBr.readLine()) != null) {
                        Logger.getGlobal().log(Level.WARNING, spigotLegacyInputLine);
                        if (spigotLegacyInputLine.contains("RESTRICTED!")  || spigotLegacyInputLine.contains("404 Not Found")) {
                            spigotLegacyBr.close();
                            return "UNKNOWN_VERSION";
                        }
                    }

                    return spigotLegacyDownloadURL;
                }
            }
            br.close();

            return spigotDownloadURL;

        } catch (final IOException e) {
            e.printStackTrace();
            return "UNKNOWN_VERSION";
        }
    }

    public static String getVanillaDownload(final String version) {
        try {

            //We first need to get the download link through the version_manifest...
            final String vanillaManifest = "https://launchermeta.mojang.com/mc/game/version_manifest.json";
            final URL url = new URL(vanillaManifest);
            final URLConnection conn = url.openConnection();
            conn.addRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            conn.connect();

            final BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            // Pattern for recognizing a URL, based off RFC 3986
            final Pattern urlPattern = Pattern.compile(
                    "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                            + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                            + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

            String inputLine, jsonManifestURL = null;
            while ((inputLine = br.readLine()) != null) {

                Matcher matcher = urlPattern.matcher(inputLine);
                while (matcher.find()) {
                    if (matcher.group().contains(version + ".json")) jsonManifestURL = matcher.group().substring(1);
                }
            }
            br.close();

            if (jsonManifestURL == null) {
                return "UNKNOWN_VERSION";
            }

            //We first need to get the download link through the version_manifest...
            final URL jsonURL = new URL(jsonManifestURL);
            final URLConnection jsonConn = jsonURL.openConnection();

            final BufferedReader jsonBr = new BufferedReader(
                    new InputStreamReader(jsonConn.getInputStream()));

            String jsonInputLine;
            while ((jsonInputLine = jsonBr.readLine()) != null) {

                Matcher matcher = urlPattern.matcher(jsonInputLine);
                while (matcher.find()) {
                    if (matcher.group().contains("server.jar")) {
                        jsonBr.close();
                        return matcher.group().substring(1);
                    }
                }
            }
            jsonBr.close();

        } catch (final IOException e) {
            e.printStackTrace();
        }
        return "UNKNOWN_VERSION";
    }

    public static String getBukkitDownload(final String version) {
        try {

            final String bukkitUrl = "https://download.getbukkit.org/craftbukkit/craftbukkit-" + version + ".jar";
            final URL url = new URL(bukkitUrl);
            final URLConnection conn = url.openConnection();
            conn.addRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            conn.connect();

            BufferedReader br;
            try {
                br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
            } catch (final FileNotFoundException e) {
                final String bukkitLegacyDownloadURL = "https://cdn.getbukkit.org/craftbukkit/craftbukkit-" + VersionHelper.getVersion(version, "Bukkit") + ".jar";
                final URL bukkitLegacyURL = new URL(bukkitLegacyDownloadURL);
                final URLConnection bukkitLegacyConn = bukkitLegacyURL.openConnection();
                bukkitLegacyConn.addRequestProperty("User-Agent",
                        "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
                bukkitLegacyConn.connect();

                final BufferedReader bukkitLegacyBr = new BufferedReader(
                        new InputStreamReader(bukkitLegacyConn.getInputStream()));

                String bukkitLegacyInputLine;
                while ((bukkitLegacyInputLine = bukkitLegacyBr.readLine()) != null) {
                    if (bukkitLegacyInputLine.contains("RESTRICTED!") || bukkitLegacyInputLine.contains("404 Not Found")) {
                        bukkitLegacyBr.close();
                        return "UNKNOWN_VERSION";
                    }
                }

                bukkitLegacyBr.close();

                return bukkitLegacyDownloadURL;
            }

            String inputLine;
            while ((inputLine = br.readLine()) != null) {

                //If version doesn't exist
                if (inputLine.contains("RESTRICTED!") || inputLine.contains("404 Not Found")) {
                    br.close();

                    final String bukkitLegacyDownloadURL = "https://cdn.getbukkit.org/craftbukkit/craftbukkit-" + VersionHelper.getVersion(version, "Bukkit") + ".jar";
                    final URL bukkitLegacyURL = new URL(bukkitLegacyDownloadURL);
                    final URLConnection bukkitLegacyConn = bukkitLegacyURL.openConnection();
                    bukkitLegacyConn.addRequestProperty("User-Agent",
                            "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
                    bukkitLegacyConn.connect();

                    final BufferedReader bukkitLegacyBr = new BufferedReader(
                            new InputStreamReader(bukkitLegacyConn.getInputStream()));

                    String bukkitLegacyInputLine;
                    while ((bukkitLegacyInputLine = bukkitLegacyBr.readLine()) != null) {
                        if (bukkitLegacyInputLine.contains("RESTRICTED!") || bukkitLegacyInputLine.contains("404 Not Found")) {
                            bukkitLegacyBr.close();
                            return "UNKNOWN_VERSION";
                        }
                    }

                    bukkitLegacyBr.close();

                    return bukkitLegacyDownloadURL;
                }
            }
            br.close();

            return bukkitUrl;

        } catch (final IOException e) {
            e.printStackTrace();
            return "UNKNOWN_VERSION";
        }
    }

    public static void downloadFileByURL(final String URL, final File downloadPath) throws ConnectionResetException, IOException {
        final URL url = new URL(URL);
        final URLConnection conn = url.openConnection();
        conn.addRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        conn.connect();

        final BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
        final FileOutputStream fileOutputStream = new FileOutputStream(downloadPath);
        final byte[] dataBuffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
            fileOutputStream.write(dataBuffer, 0, bytesRead);
        }
    }
}
