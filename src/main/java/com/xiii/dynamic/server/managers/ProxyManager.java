package com.xiii.dynamic.server.managers;

import com.xiii.dynamic.server.DynamicServer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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

    final Charset charset = StandardCharsets.UTF_8;

    public ProxyManager(final DynamicServer instance) {
        this.instance = instance;
    }

    public void configureProxy(final String defaultServer, final String ipForward, final String onlineMode, final String startPort) throws IOException {

        instance.getLogger().log(Level.INFO, "Updating proxy's configuration! Please wait...");

        //Pre cache config file
        final Path path = Paths.get("config.yml"); //proxy config file
        String content = new String(Files.readAllBytes(Paths.get("config.yml")), charset);
        Files.write(path, content.getBytes());
        String updatedContent;

        if (!content.contains(defaultServer)) {
            instance.getLogger().log(Level.WARNING, defaultServer + " doesn't appear in the linked servers list! It was set anyway.");
        }
        content = new String(Files.readAllBytes(Paths.get("config.yml")), charset);
        updatedContent = new StringBuilder(content).insert(content.indexOf("priorities:"), "priorities:" + System.lineSeparator() + "  - " + defaultServer).toString().replace(defaultServer + "priorities:", defaultServer);
        Files.write(path, updatedContent.getBytes(charset));
        instance.getLogger().log(Level.INFO, "Set default server to " + defaultServer);

        if (ipForward != null) {
            content = new String(Files.readAllBytes(Paths.get("config.yml")), charset);
            updatedContent = content.replaceAll("ip_forward: " + !Boolean.parseBoolean(ipForward), "ip_forward: " + Boolean.parseBoolean(ipForward));
            Files.write(path, updatedContent.getBytes(charset));
            instance.getLogger().log(Level.INFO, "Set IP Forwarding to " + Boolean.parseBoolean(ipForward));
        }

        if (onlineMode != null) {
            content = new String(Files.readAllBytes(Paths.get("config.yml")), charset);
            updatedContent = content.replaceAll("online_mode: " + !Boolean.parseBoolean(onlineMode), "online_mode: " + Boolean.parseBoolean(onlineMode));
            Files.write(path, updatedContent.getBytes(charset));
            instance.getLogger().log(Level.INFO, "Set online mode to " + Boolean.parseBoolean(onlineMode));
        }

        if (startPort != null) {
            instance.getLogger().log(Level.INFO, "Searching for available ports starting from " + startPort);

            //Find an available port
            boolean availablePort = false;
            int portNumber = Integer.parseInt(startPort);
            for (int port = Integer.parseInt(startPort); !availablePort; port++) {
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
            }

            if (portNumber != Integer.parseInt(startPort)) {
                instance.getLogger().log(Level.WARNING, "The new port is not desired " + startPort + " port! Port did not change.");
            } else {
                content = new String(Files.readAllBytes(Paths.get("config.yml")), charset);
                updatedContent = content.replaceAll("query_port:.*", "query_port: " + portNumber);
                Files.write(path, updatedContent.getBytes(charset));
                //content = new String(Files.readAllBytes(Paths.get("config.yml")), charset);
                //updatedContent = content.replaceAll("/\\w+$/", String.valueOf(portNumber));
                //Files.write(path, updatedContent.getBytes(charset));
                //TODO: fix
                instance.getLogger().log(Level.INFO, "Query and host ports set to " + portNumber);
            }
        }
        instance.getLogger().log(Level.WARNING, "Server configured! You will have to restart your proxy to apply changes.");
    }
}