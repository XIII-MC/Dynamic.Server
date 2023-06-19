package com.xiii.dynamic.server.managers;

import com.xiii.dynamic.server.DynamicServer;
import com.xiii.dynamic.server.utils.HTTPUtils;

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
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;

public class ServerManager {

    private final DynamicServer instance;

    public ServerManager(final DynamicServer instance) {
        this.instance = instance;
    }

    public void createServer(final int xmxRamMB, final String serverName, final String serverSoftware, final String serverVersion, final boolean autoConfig) throws IOException {

        final Path serverFolder = Paths.get(serverName);
        final Charset charset = StandardCharsets.UTF_8;
        String downloadURL = "UNKNOWN_VERSION";

        //Check if serverSoftware exists and download it
        switch(serverSoftware) {

            case "Paper":
                downloadURL = HTTPUtils.getPaperDownload(serverVersion);

                break;

            case "Spigot":
                downloadURL = HTTPUtils.getSpigotDownload(serverVersion);

                break;

            case "Vanilla":
                downloadURL = HTTPUtils.getVanillaDownload(serverVersion);

                break;

            case "Bukkit":
                downloadURL = HTTPUtils.getBukkitDownload(serverVersion);

                break;

            case "Custom":
                downloadURL = serverVersion;

                break;
        }

        if (downloadURL.equalsIgnoreCase("UNKNOWN_VERSION")) {
            instance.getLogger().log(Level.INFO, serverVersion + " does not exist as a " + serverSoftware + " version! Exiting...");
            return;
        }

        //Create server's folder -> final String serverName
        try {
            Files.createDirectory(serverFolder);
        } catch (final IOException e) {
            instance.getLogger().log(Level.SEVERE, "A folder with that same name already exists! Exiting...");
            return;
        }

        instance.getLogger().log(Level.INFO, "File found! Downloading...");
        HTTPUtils.downloadFileByURL(downloadURL, new File(serverName + "/server.jar"));
        instance.getLogger().log(Level.INFO, "File downloaded! Installing...");

        //Pre setup EULA file to boot up on first start
        final BufferedWriter eula = new BufferedWriter(new FileWriter(serverName + "/eula.txt"));

        eula.write("eula=true");

        eula.close();

        //Setup run.bat/sh files
        final String startUpCommand = "@echo off" + "\n" + " java -jar -Xmx" + xmxRamMB + "M" + " server.jar";

        final BufferedWriter runBat = new BufferedWriter(new FileWriter(serverName + "/run.bat"));
        final BufferedWriter runSh = new BufferedWriter(new FileWriter(serverName + "/run.sh"));

        runBat.write(startUpCommand);
        runSh.write(startUpCommand);

        runBat.close();
        runSh.close();

        //We won't go further if autoConfig is false, meaning they chose not to configure the server with their proxy
        if (autoConfig) {

            //Pre cache config file
            final Path path = Paths.get("config.yml"); //proxy config file
            final String content = new String(Files.readAllBytes(path), charset);

            //Pre gen a server.properties file, only online mode and port are required to link it to the proxy
            instance.getLogger().log(Level.INFO, "Installation finished! Configuring server...");

            //Find an available port
            boolean availablePort = false;
            int portNumber = 25565;
            for (int port = 25565; !availablePort && !content.contains(":" + port); port++) {
                try (Socket ignored = new Socket("localhost", port)) {
                } catch (final ConnectException e) {
                    availablePort = true;
                    portNumber = port;
                }
            }

            final String serverProperties = "server-port:" + portNumber + System.lineSeparator() + "online-mode=false";

            final BufferedWriter serverPropertiesBw = new BufferedWriter(new FileWriter(serverName + "/server.properties"));

            serverPropertiesBw.write(serverProperties);

            serverPropertiesBw.close();

            instance.getLogger().log(Level.INFO, "Server configuration done! Linking server to the proxy...");

            final String addServer = new StringBuilder(content).insert(content.indexOf("servers:"), "servers:" + System.lineSeparator() + "  " + serverName + ":" + System.lineSeparator() + "    " + "motd: 'A Dynamic.Server!'" + System.lineSeparator() + "    " + "address: localhost:" + portNumber + System.lineSeparator() + "    " + "restricted: false").toString().replace("falseservers:", "false");

            Files.write(path, addServer.getBytes(charset));

            instance.getLogger().log(Level.INFO, "Proxy linked to the server!");
            instance.getLogger().log(Level.WARNING, "You will need to restart your Proxy in order to finish linking your server!");
        }

        instance.getLogger().log(Level.INFO, "Setup finished! Starting server...");

        startServer(serverName);
    }

    public void startServer(final String serverName) {
        try {
            Runtime.
                    getRuntime().
                    exec("cmd.exe /c start \"\" run.bat", null, new File(serverName));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
