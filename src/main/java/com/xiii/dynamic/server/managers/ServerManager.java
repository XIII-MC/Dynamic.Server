package com.xiii.dynamic.server.managers;

import com.xiii.dynamic.server.DynamicServer;
import com.xiii.dynamic.server.utils.HTTPUtils;
import sun.net.ConnectionResetException;

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
import java.util.Objects;
import java.util.logging.Level;

public class ServerManager {

    private final DynamicServer instance;

    private boolean isVanillaProxy = false;

    public ServerManager(final DynamicServer instance) {
        this.instance = instance;
    }

    public void createServer(final int xmxRamMB, final String serverName, final String serverSoftware, final String serverVersion, final boolean autoConfig, String distantIP, int startPort) throws IOException {

        if (Objects.equals(distantIP, "DEFAULT")) {
            distantIP = "localhost";
            instance.getLogger().log(Level.WARNING, "No distant IP specified, defaulting to localhost.");
        }
        if (startPort == -1) {
            startPort = 25565;
            instance.getLogger().log(Level.WARNING, "No start port specified, defaulting to 25565");
        }
        final Path serverFolder = Paths.get(serverName);
        final Charset charset = StandardCharsets.UTF_8;
        String downloadURL = "UNKNOWN_VERSION";

        //Check if serverSoftware exists and download it
        switch (serverSoftware) {

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

        if (serverSoftware.equalsIgnoreCase("Vanilla") && autoConfig) {
            isVanillaProxy = true;
            downloadURL = "https://dev.me1312.net/jenkins/job/VanillaCord/lastSuccessfulBuild/artifact/artifacts/VanillaCord.jar";
            instance.getLogger().log(Level.WARNING, "Normal Vanilla cannot be linked to a proxy. Using VanillaCord.");
        }

        instance.getLogger().log(Level.INFO, "File found! Downloading...");
        try {
            HTTPUtils.downloadFileByURL(downloadURL, new File(serverName + "/server.jar"));
        } catch (final ConnectionResetException e) {
            endSetup(serverName, "Could not connect to the distant server (is the firewall blocking it?) (Connection Reset). Exiting...");

        }
        instance.getLogger().log(Level.INFO, "File downloaded! Installing...");

        //Create the out folder for VanillaCord
        if (isVanillaProxy) new File(serverName + "/out").mkdir();

        //Pre setup EULA file to boot up on first start
        final BufferedWriter eula = new BufferedWriter(new FileWriter(isVanillaProxy ? serverName + "/out/eula.txt" : serverName + "/eula.txt"));

        eula.write("eula=true");

        eula.flush();

        eula.close();

        //Setup run.bat/sh files
        String startUpCommand;

        //Vanilla needs a little special run.bat/sh
        if (isVanillaProxy) {
            startUpCommand = "@echo off" + System.lineSeparator() + "java -jar -Xmx" + xmxRamMB + "M" + " server.jar " + serverVersion;
        } else startUpCommand = "@echo off" + System.lineSeparator() + "java -jar -Xmx" + xmxRamMB + "M" + " server.jar";

        final BufferedWriter runBat = new BufferedWriter(new FileWriter(serverName + "/run.bat"));
        final BufferedWriter runSh = new BufferedWriter(new FileWriter(serverName + "/run.sh"));

        runBat.write(isVanillaProxy ? startUpCommand + System.lineSeparator() + "cd out" + System.lineSeparator() + "java -jar -Xmx" + xmxRamMB + "M " + serverVersion + "-bungee.jar" : startUpCommand);
        runSh.write(isVanillaProxy ? startUpCommand + System.lineSeparator() + "cd out" + System.lineSeparator() + "java -jar -Xmx" + xmxRamMB + "M " + serverVersion + "-bungee.jar" : startUpCommand);

        runBat.flush();
        runSh.flush();

        runBat.close();
        runSh.close();

        //Pre cache config file
        final Path path = Paths.get("config.yml"); //proxy config file
        final String content = new String(Files.readAllBytes(path), charset);

        //Pre gen a server.properties file, only online mode and port are required to link it to the proxy
        instance.getLogger().log(Level.INFO, "Installation finished! Configuring server...");

        //Find an available port
        boolean availablePort = false;
        int portNumber = startPort;
        for (int port = startPort; !availablePort; port++) {
            try (Socket ignored = new Socket(distantIP, port)) {
            } catch (final ConnectException e) {
                if (!content.contains(distantIP + ":" + port)) {
                    availablePort = true;
                    portNumber = port;
                }
            }
        }

        if (!availablePort) endSetup(serverName, "No available port found! Exiting...");

        final BufferedWriter serverPropertiesBw = new BufferedWriter(new FileWriter(isVanillaProxy ? serverName + "/out/server.properties" : serverName + "/server.properties"));

        serverPropertiesBw.write("server-port:" + portNumber + (autoConfig ? System.lineSeparator() + "online-mode=false" : ""));

        serverPropertiesBw.flush();

        serverPropertiesBw.close();

        //We won't go further if autoConfig is false, meaning they chose not to configure the server with their proxy
        if (autoConfig && (!serverSoftware.equalsIgnoreCase("Bukkit"))) {

            //Enable bungeecord in spigot.yml
            if (!serverSoftware.equalsIgnoreCase("Vanilla") && !serverSoftware.equalsIgnoreCase("Bukkit")) {
                final BufferedWriter spigotYml = new BufferedWriter(new FileWriter(serverName + "/spigot.yml"));

                spigotYml.write("settings:" + System.lineSeparator() + "  bungeecord: true");

                spigotYml.flush();

                spigotYml.close();
            }

            instance.getLogger().log(Level.INFO, "Server configuration done! Linking server to the proxy...");

            final String addServer = new StringBuilder(content).insert(content.indexOf("servers:"), "servers:" + System.lineSeparator() + "  " + serverName + ":" + System.lineSeparator() + "    " + "motd: 'A Dynamic.Server!'" + System.lineSeparator() + "    " + "address: localhost:" + portNumber + System.lineSeparator() + "    " + "restricted: false").toString().replace("falseservers:", "false");

            Files.write(path, addServer.getBytes(charset));

            instance.getLogger().log(Level.INFO, "Proxy linked to the server!");
            instance.getLogger().log(Level.WARNING, "You will need to restart your Proxy in order to finish linking your server!");
        }

        if (autoConfig && (serverSoftware.equalsIgnoreCase("Bukkit"))) instance.getLogger().log(Level.WARNING, serverSoftware + " does not support BungeeCord! Please use Spigot, Paper or Vanilla.");

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

    private void endSetup(final String serverName, final String errorMessage) {
        instance.getLogger().log(Level.SEVERE, errorMessage);
        new File(serverName).delete();
    }
}
