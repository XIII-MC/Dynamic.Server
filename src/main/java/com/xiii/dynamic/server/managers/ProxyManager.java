package com.xiii.dynamic.server.managers;

import com.xiii.dynamic.server.DynamicServer;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

public class ProxyManager {

    private final DynamicServer instance;

    private final Charset charset = StandardCharsets.UTF_8;

    private final Path configYml = Paths.get("config.yml");

    public ProxyManager(final DynamicServer instance) {
        this.instance = instance;
    }

    public void configureProxy(final String defaultServer, final String ipForward, final String onlineMode, final String startPort) throws IOException {

        //Remove 'forced' BungeeCord settings
        clearForcedSettings();

        setDefaultServer(defaultServer);

        if (ipForward != null) toggleIPForward(Boolean.parseBoolean(ipForward));

        if (onlineMode != null) toggleOnlineMode(Boolean.parseBoolean(onlineMode));

        if (startPort != null) changeProxyPort(Integer.parseInt(startPort));

        instance.getLogger().log(Level.WARNING, "Server configured! You will have to restart your proxy to apply changes.");
    }

    public void clearForcedSettings() throws IOException {
        final String content = new String(Files.readAllBytes(configYml), charset);
        final String updatedContent = content.replace("md_5", "§").replace("- admin", "- §").replace("lobby", "§").replace("localhost:25565", "0:0").replace("restricted: false", "restricted: true").replace("&1Just another BungeeCord - Forced Host", "").replace("pvp.md-5.net: pvp", "§: §").replace("&1Another Bungee server", "A Minecraft Server");
        Files.write(configYml, updatedContent.getBytes(charset));
        instance.getLogger().log(Level.INFO, "Forced settings removed");
    }

    public void setDefaultServer(final String defaultServer) throws IOException {
        final String content = new String(Files.readAllBytes(configYml), charset);
        final String updatedContent = content.replaceAll("priorities:\\n.*", "priorities:" + System.lineSeparator() + "  - " + defaultServer);
        if (!content.contains(defaultServer)) {
            instance.getLogger().log(Level.SEVERE, defaultServer + " doesn't appear in the linked servers list! Canceling...");
            return;
        }
        Files.write(configYml, updatedContent.getBytes(charset));
        instance.getLogger().log(Level.INFO, "Set default server to " + defaultServer);
    }

    public void toggleIPForward(final boolean bool) throws IOException {
        final String content = new String(Files.readAllBytes(configYml), charset);
        final String updatedContent = content.replaceAll("ip_forward: " + !bool, "ip_forward: " + bool);
        Files.write(configYml, updatedContent.getBytes(charset));
        instance.getLogger().log(Level.INFO, "Set IP Forwarding to " + bool);
    }

    public void toggleOnlineMode(final boolean bool) throws IOException {
        final String content = new String(Files.readAllBytes(configYml), charset);
        final String updatedContent = content.replaceAll("online_mode: " + !bool, "online_mode: " + bool);
        Files.write(configYml, updatedContent.getBytes(charset));
        instance.getLogger().log(Level.INFO, "Set online mode to " + bool);
    }

    public void changeProxyPort(final int startPort) throws IOException {
        instance.getLogger().log(Level.INFO, "Searching for available ports starting from " + startPort);

        String content = new String(Files.readAllBytes(configYml), charset);

        //Find an available port
        boolean availablePort = false;
        int portNumber = startPort;
        for (int port = startPort; !availablePort; port++) {
            try (Socket ignored = new Socket("localhost", port)) {
            } catch (final ConnectException e) {
                if (!content.contains("localhost" + ":" + port)) {
                    availablePort = true;
                    portNumber = port;
                }
            }
        }

        if (!availablePort) {
            instance.getLogger().log(Level.WARNING, "Couldn't find any free port! The port was not changed.");
            return;
        }

        content = new String(Files.readAllBytes(configYml), charset);
        String updatedContent = content.replaceAll("query_port:.*", "query_port: " + portNumber);
        Files.write(configYml, updatedContent.getBytes(charset));
        content = new String(Files.readAllBytes(configYml), charset);

        updatedContent = content.replaceAll("((?<=host: ).*)(?<=:).*", "0.0.0.0:" + portNumber); ///(?<=host: ).*(?<=:)/g | Save IP
        Files.write(configYml, updatedContent.getBytes(charset));
        instance.getLogger().log(Level.INFO, "Query and host ports set to " + portNumber);
    }
}